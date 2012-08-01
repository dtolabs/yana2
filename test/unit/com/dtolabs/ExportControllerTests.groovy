package com.dtolabs



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ExportController)
@Mock([Project, Attribute, Filter, NodeType, NodeTypeRelationship,
Node, NodeValue, NodeAttribute, ChildNode,Webhook])
class ExportControllerTests {

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

    void testExport() {
        // Auto-wire in the needed services used by this controller.
        defineBeans {
            importService(ImportService)
            exportService(ExportService)
            webhookService(WebhookService)
        }
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
         * Load the example model
         */
        def nodes = controller.importService.load(new StringBufferInputStream(example1), project)
        assertEquals("incorrect number of nodes: " + nodes.size(), nodes.size(), 1)

        /**
         * Export the model to XML
         */
        controller.xml()

        /**
         * Confirm the model export contains the imported data
         */
        assert "hostname" == response.xml.attributes.attribute.@name.text()
        assert "String" == response.xml.attributes.attribute.@filter.text()
        assert "host" == response.xml.types.type.@name.text()
        assert "host type" == response.xml.types.type.description.text()
        assert "Node.png" == response.xml.types.type.image.text()
        assert "hostname" == response.xml.types.type.attributes.attribute.@name.text()
        assert "true" == response.xml.types.type.attributes.attribute.@required.text()
        assert "host1" == response.xml.nodes.node.@name.text()
        assert "host" == response.xml.nodes.node.@type.text()
        assert "tag1,tag2,tag3" == response.xml.nodes.node.@tags.text()
        assert "Sample node instance" == response.xml.nodes.node.description.text()
        assert "hostname" == response.xml.nodes.node.attributes.attribute.@name.text()
        assert "centos62-rundeck-tomcat" == response.xml.nodes.node.attributes.attribute.@value.text()

    }
}
