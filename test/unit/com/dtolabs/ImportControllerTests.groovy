package com.dtolabs

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import groovy.util.XmlSlurper

/**
 * Created with IntelliJ IDEA.
 * User: alexh
 * Date: 7/27/12
 * Time: 10:29 AM
 * To change this template use File | Settings | File Templates.
 */

@TestFor(ImportController)
@Mock([Project, Attribute, Filter, NodeType, NodeTypeRelationship,
Node, NodeValue, NodeAttribute, ChildNode, Webhook])

class ImportControllerTests {

    /**
     * Validate index redirects to import page
     */
    void testIndex() {
        controller.index()
        assert response.redirectedUrl == '/import/importxml'
    }

    def bad_xml1 = """<bad>
  <attributes>
    <attribute name="hostname" filter="String"/>
  </attributes>

  <types>
    <type name="host">
      <description>host type</description>
      <image>Node.png</image>
      <attributes>
        <attribute name="hostname" required="true"/>
      </attributes>
    </type>
  </types>

  <nodes>
    <node name="host1" type="host" tags="tag1,tag2,tag3">
      <description>Sample node instance</description>
      <attributes>
        <attribute name="hostname" value="centos62-rundeck-tomcat"/>
      </attributes>
    </node>
  </nodes>
</bad>
    """

    def bad_xml2 = """<yana>
  <attributes>
    <attribute blar="hostname" blewy="String"/>
  </attributes>

  <types>
    <type name="host">
      <description>host type</description>
      <image>Node.png</image>
      <junk/>
      <attributes>
        <attribute name="hostname" required="true"/>
      </attributes>
    </type>
  </types>

  <nodes>
    <node name="host1" type="host" tags="tag1,tag2,tag3">
      <description>Sample node instance</description>
      <attributes>
        <attribute name="hostname" value="centos62-rundeck-tomcat"/>
      </attributes>
    </node>
  </nodes>
</yana>
    """
    def ok_xml1 = """<yana>
  <attributes>
    <attribute name="hostname" filter="String"/>
  </attributes>

  <types>
    <type name="host">
      <description>host type</description>
      <image>Node.png</image>
      <attributes>
        <attribute name="hostname" required="true"/>
      </attributes>
    </type>
  </types>

  <nodes>
    <node name="host1" type="host" tags="tag1,tag2,tag3">
      <description>Sample node instance</description>
      <attributes>
        <attribute name="hostname" value="centos62-rundeck-tomcat"/>
      </attributes>
    </node>
  </nodes>
</yana>
    """

    /**
     * Check the XML upload and validation work
     */
    void testValidateXml1() {

        // Auto-wire in the ImportService and WebhookService used by this controller.
        defineBeans {
            importService(ImportService)
            webhookService(WebhookService)
        }
        assertNotNull("ImportService not auto-wired into the test", controller.importService)

        /**
         *
         * Create a test project to store a model
         */
        Project project = new Project(name: 'test1', description: 'desc').save()

        // Set the query string with the project parameter
        params.project = 'test1'
        /**
         * Declare supporting filter used in this example.
         */
        new Filter(project: project, dataType: "String", regex: ".*").save()

        /**
         * Read in the XML model data and add it to the request.
         */
        request.addFile(
                new MockMultipartFile('yanaimport', 'bad_xml1.xml', 'text/xml',
                        new StringBufferInputStream(bad_xml1)))

        /**
         * Run the controller action
         */
        controller.validatexml()

        /**
         * Confirm the format was invalid
         */

        //assert "<response><status valid='false'/></response>" == response.text
        def root = new XmlSlurper().parseText(response.text.toString())
        assert root.name() == 'response'
        assert root.status.@valid == 'false'
    }

/**
 * Check the XML upload and validation work
 */
    void testValidateXml2() {

        // Auto-wire in the ImportService and WebhookService used by this controller.
        defineBeans {
            importService(ImportService)
            webhookService(WebhookService)
        }
        assertNotNull("ImportService not auto-wired into the test", controller.importService)

        /**
         *
         * Create a test project to store a model
         */
        Project project = new Project(name: 'test1', description: 'desc').save()

        // Set the query string with the project parameter
        params.project = 'test1'
        /**
         * Declare supporting filter used in this example.
         */
        new Filter(project: project, dataType: "String", regex: ".*").save()

        request.addFile(
                new MockMultipartFile('yanaimport', 'bad_xml2.xml', 'text/xml',
                        new StringBufferInputStream(bad_xml2)))

        /**
         * Run the controller action
         */
        controller.validatexml()

        /**
         * Confirm the format was invalid
         */
        // <response><status valid='false'
        // message='Invalid XML content: cvc-complex-type.3.2.2: Attribute &apos;blar&apos; is not allowed to appear in element &apos;attribute&apos;.'
        // xsd='/import/yana.xsd'/>

        def root = new XmlSlurper().parseText(response.text.toString())
        assert root.name() == 'response'
        assert root.status.@valid == 'false'
        assert root.status.@xsd == '/import/yana.xsd'
        assert root.status.@message.toString().contains("Invalid XML content")
    }

/**
 * Check the XML upload and validation work
 */
    void testValidateXml3() {

        // Auto-wire in the ImportService and WebhookService used by this controller.
        defineBeans {
            importService(ImportService)
            webhookService(WebhookService)
        }
        assertNotNull("ImportService not auto-wired into the test", controller.importService)

        /**
         *
         * Create a test project to store a model
         */
        Project project = new Project(name: 'test1', description: 'desc').save()

        // Set the query string with the project parameter
        params.project = 'test1'
        /**
         * Declare supporting filter used in this example.
         */
        new Filter(project: project, dataType: "String", regex: ".*").save()

        /**
         * Read in the XML model data and add it to the request.
         */
        request.addFile(
                new MockMultipartFile('yanaimport', 'ok_xml1.xml', 'text/xml',
                        new StringBufferInputStream(ok_xml1)) )

        /**
         * Run the controller action
         */
        controller.validatexml()

        /**
         * Confirm the format was valid
         */

        def root = new XmlSlurper().parseText(response.text.toString())
        assert root.status.@valid == 'true'

    }

/**
 * Check the XML upload and import work
 */
    void testSavexml() {

        // Auto-wire in the ImportService and WebhookService used by this controller.
        defineBeans {
            importService(ImportService)
            webhookService(WebhookService)
        }
        assertNotNull("ImportService not auto-wired into the test", controller.importService)

        /**
         *
         * Create a test project to store a model
         */
        Project project = new Project(name: 'test1', description: 'desc').save()

        // Set the query string with the project parameter
        params.project = 'test1'
        /**
         * Declare supporting filter used in this example.
         */
        new Filter(project: project, dataType: "String", regex: ".*").save()

        /**
         * Read in the XML model data and add it to the request.
         */
        request.addFile(
                new MockMultipartFile('yanaimport', 'ok_xml1.xml', 'text/xml',
                        new StringBufferInputStream(ok_xml1)))

        /**
         * Run the controller action
         */
        controller.savexml()

        /**
         * Confirm the model was populated
         */
        def host = NodeType.findByProjectAndName(project, "host")
        assertNotNull("Imported NodeType not found in the model", host)
        assertEquals("NodeType name did not match", host.name, "host")
        def host1 = Node.findByProjectAndName(project, "host1")
        assertEquals("Node name did not match", host1.name, "host1")
        def hostname = Attribute.findByProjectAndName(project, "hostname")
        assertEquals("Attribute name did not match", hostname.name, "hostname")
    }

}
