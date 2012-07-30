package com.dtolabs



import grails.test.mixin.*

import grails.test.GrailsUnitTestCase
import org.springframework.core.io.Resource
import org.springframework.core.io.ClassPathResource

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ImportService)
@Mock([Project, Attribute, Filter, NodeType, NodeTypeRelationship,
Node, NodeValue, NodeAttribute, ChildNode])
class ImportServiceTests {

    /*
    * No yana root element
    */
    def bad_xml1 = """<yoda>
  <attributes>
    <attribute id="hostname" filter="String"/>
  </attributes>

  <nodetypes>
    <nodetype id="host">
      <description>host type</description>
      <image>Node.png</image>
      <nodeAttributes>
        <nodeAttribute id="node.hostname" attribute="hostname" required="true"/>
      </nodeAttributes>
    </nodetype>
  </nodetypes>

  <nodes>
    <node id="host1" nodetype="host" tags="tag1,tag2,tag3">
      <description>Sample node instance</description>
      <values>
        <attributeValue nodeAttribute="node.hostname">centos62-rundeck-tomcat</attributeValue>
      </values>
    </node>
  </nodes>
</yoda>
    """

    /**
     * Non-yana sub elements
     */
    def bad_xml2 = """<yana>
  <ooh>
    <attribute id="hostname" filter="String"/>
  </ooh>

  <aah>
    <nodetype id="host">
      <description>host type</description>
      <image>Node.png</image>
      <nodeAttributes>
        <nodeAttribute id="node.hostname" attribute="hostname" required="true"/>
      </nodeAttributes>
    </nodetype>
  </aah>

  <doh>
    <node id="host1" nodetype="host" tags="tag1,tag2,tag3">
      <description>Sample node instance</description>
      <values>
        <attributeValue nodeAttribute="node.hostname">centos62-rundeck-tomcat</attributeValue>
      </values>
    </doh>
  </nodes>
</yana>
    """

    /**
     * Basic model
     */
    def ok_xml1 = """<yana>
  <attributes>
    <attribute id="hostname" filter="String"/>
  </attributes>

  <nodetypes>
    <nodetype id="host">
      <description>host type</description>
      <image>Node.png</image>
      <nodeAttributes>
        <nodeAttribute id="node.hostname" attribute="hostname" required="true"/>
      </nodeAttributes>
    </nodetype>
  </nodetypes>

  <nodes>
    <node id="host1" nodetype="host" tags="tag1,tag2,tag3">
      <description>Sample node instance</description>
      <values>
        <attributeValue nodeAttribute="node.hostname">centos62-rundeck-tomcat</attributeValue>
      </values>
    </node>
  </nodes>
</yana>
    """
    /**
     * Basic model plus a new attribute
     */
    def ok_xml1v2 = """<yana>
 <attributes>
    <attribute id="arch" filter="String"/>
  </attributes>

  <nodetypes>
    <nodetype id="host">
      <description>host type</description>
      <image>Node.png</image>
      <nodeAttributes>
        <nodeAttribute id="node.arch" attribute="arch" required="true"/>
      </nodeAttributes>
    </nodetype>
  </nodetypes>

</yana>
    """
    /**
     * Validate a model import file
     */
    void testValidate() {

        /**
         * Check bad input XML
         */
        try {
            service.validate(new StringBufferInputStream(bad_xml1))
        } catch (ImportServiceException e) {
            assertNotNull("Invalid XML input did not cause an exception", e)
        }
        try {
            service.validate(new StringBufferInputStream(bad_xml2))
        } catch (ImportServiceException e) {
            assertNotNull("Invalid XML input did not cause an exception", e)
        }

        /**
         * Verify a trivial good example
         */
        service.validate(new StringBufferInputStream(ok_xml1))

        /**
         * Verify a more extensive model from a test example
         */
        service.validate(asInputStream("/import/example2.xml"))

    }

    /**
     * Test the population of a model
     */
    void testPopulate1() {

        /**
         * Create a project to store a model
         */
        Project project = new Project(name: 'test1', description: 'desc').save()
        /**
         * Input does not declare any filters yet so define one
         */
        new Filter(project: project, dataType: "String", regex: ".*").save()

        /*
         * Parse the XML input file and populate the model
         */
        def nodes = service.populate(new StringBufferInputStream(ok_xml1), project)
        assertEquals("incorrect number of nodes: " + nodes.size(), nodes.size(), 1)

        /**
         * Verify the objects have been populated in the model
         */

        // Attribute
        def hostname = Attribute.findByProjectAndName(project, "hostname")
        assertEquals("Attribute name did not match", hostname.name, "hostname")

        // NodeType
        def host = NodeType.findByProjectAndName(project, "host")
        assertEquals("NodeType name did not match", host.name, "host")

        // Node
        def host1 = Node.findByProjectAndName(project, "host1")
        assertEquals("Node name did not match", host1.name, "host1")

        /**
         * Declare another attribute for this type to ensure incremental update
         */
        service.populate(new StringBufferInputStream(ok_xml1v2), project)

        def arch = Attribute.findByProjectAndName(project, "arch")
        assertEquals("Attribute name did not match", arch.name, "arch")
        def att = NodeAttribute.findByNodetypeAndAttribute(host, arch)
        assertNotNull("Missing type attribute", att)
        assertEquals("Attribute was not required but should be", true, att.required)

    }

    /**
     * Test the population of a model
     */
    void testPopulate2() {

        /**
         * Create a project to store a model
         */
        Project project = new Project(name: 'test1', description: 'desc').save()
        /**
         * Input does not declare any filters yet so define a couple.
         */
        new Filter(project: project, dataType: "String", regex: ".*").save()
        new Filter(project: project, dataType: "URL", regex: ".*").save()

        /*
         * Parse the XML input file and populate the model
         */
        def nodes = service.populate(asInputStream("/import/example2.xml"), project)
        assertEquals("incorrect number of nodes: " + nodes.size(), nodes.size(), 5)

        /**
         * Verify the objects have been populated in the model
         */
        test: {       // Attribute
            def arch = Attribute.findByProjectAndName(project, "arch")
            assertEquals("Attribute name did not match", arch.name, "arch")
        }
        test: {       // NodeType
            def host = NodeType.findByProjectAndName(project, "Host")
            assertEquals("NodeType name did not match", host.name, "Host")
            def pkg = NodeType.findByProjectAndName(project, "Package")
            assertEquals("NodeType name did not match", pkg.name, "Package")
            def svc = NodeType.findByProjectAndName(project, "Service")
            assertEquals("NodeType name did not match", svc.name, "Service")
            def site = NodeType.findByProjectAndName(project, "Site")
            assertEquals("NodeType name did not match", site.name, "Site")
        }
        test: {        // Node
            def ubuntu = Node.findByProjectAndName(project, "ubuntu")
            assertEquals("Node name did not match", ubuntu.name, "ubuntu")
            def tomcat = Node.findByProjectAndName(project, "tomcat")
            assertEquals("Node name did not match", tomcat.name, "tomcat")
            def qa = Node.findByProjectAndName(project, "qa")
            assertEquals("Node name did not match", qa.name, "qa")
            def apachetom = Node.findByProjectAndName(project, "apache-tomcat-5.5.31")
            assertEquals("Node name did not match", apachetom.name, "apache-tomcat-5.5.31")
        }
        test: {        // NodeTypeRelationship
            def environment = NodeTypeRelationship.findByRoleName("environment")
            assertEquals("NodeTypeRelationship name did not match", environment.roleName, "environment")
            def service = NodeTypeRelationship.findByRoleName("service")
            assertEquals("NodeTypeRelationship name did not match", service.roleName, "service")
            def pack = NodeTypeRelationship.findByRoleName("package")
            assertEquals("NodeTypeRelationship name did not match", pack.roleName, "package")
        }
        test: {        // ChildNode
            def qa = ChildNode.findByRelationshipName("QA")
            assertEquals("ChildNode name did not match", qa.relationshipName, "QA")
            def server = ChildNode.findByRelationshipName("server")
            assertEquals("ChildNode name did not match", server.relationshipName, "server")
            def zip = ChildNode.findByRelationshipName("zip")
            assertEquals("ChildNode name did not match", zip.relationshipName, "zip")
        }


    }

    /**
     * Utility method to lookup a resource and return it as an InputStream
     * @param filename Filename to check in the classpath
     * @return InputStream
     */
    private InputStream asInputStream(String filename) {
        final Resource resource = new ClassPathResource(filename, getClass().classLoader)
        final File f = resource.getFile()
        return new FileInputStream(f)
    }

}
