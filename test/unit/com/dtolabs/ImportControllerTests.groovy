package com.dtolabs

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile

/**
 * Created with IntelliJ IDEA.
 * User: alexh
 * Date: 7/27/12
 * Time: 10:29 AM
 * To change this template use File | Settings | File Templates.
 */

@TestFor(ImportController)
@Mock([Project, Attribute, Filter, NodeType, NodeTypeRelationship,
Node, NodeValue, NodeAttribute, ChildNode,Webhook])

class ImportControllerTests {

    /**
     * Validate index redirects to import page
     */
    void testIndex() {
        controller.index()
        assert response.redirectedUrl == '/import/importxml'
    }

    def example1 = """<yana>
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
        def contentStream = new StringBufferInputStream(example1)
        request.addFile(
                new MockMultipartFile('yanaimport', 'example1.xml', 'text/xml', contentStream)
        )

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
