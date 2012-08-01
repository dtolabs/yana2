package com.dtolabs



import grails.test.mixin.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ExportService)
@Mock([Project, Attribute, Filter, NodeType, NodeTypeRelationship,
Node, NodeValue, NodeAttribute, ChildNode])

class ExportServiceTests {

    /**
     * Basic model
     */
    def ok_xml1 = """<yana>
  <attributes>
    <attribute name="hostname" filter="String" description="the host name"/>
    <attribute name="arch" filter="String" descripiton="the architecture"/>
  </attributes>

  <types>
    <type name="host">
      <description>host type</description>
      <image>Node.png</image>
      <attributes>
        <attribute name="hostname" required="true"/>
      </attributes>
    </type>
    <type name="package">
      <description>package type</description>
      <image>Package.png</image>
      <attributes>
        <attribute name="arch" required="true"/>
      </attributes>
    </type>
  </types>

  <relationships>
    <relationship name="software" parent="host" child="package"/>
  </relationships>

  <nodes>
    <node name="host1" type="host" tags="tag1,tag2,tag3">
      <description>Sample node instance</description>
      <attributes>
        <attribute name="hostname" value="centos62-rundeck-tomcat-1"/>
      </attributes>
    </node>
    <node name="pack1" type="package" tags="tag1,tag2,tag3">
      <description>Sample package instance</description>
      <attributes>
        <attribute name="arch" value="none"/>
      </attributes>
    </node>
  </nodes>

  <children>
      <child name="pack1" type="package">
         <parent name="host1" type="host"/>
      </child>
  </children>
</yana>
    """

    void testExport() {
        defineBeans {
            importService(ImportService)
            webhookService(WebhookService)
        }
        /**
         * Create a project to store a model
         */
        Project project = new Project(name: 'test1', description: 'desc').save()
        /**
         * Input does not declare any filters yet so define one
         */
        new Filter(project: project, dataType: "String", regex: ".*").save()
        /*
        * Load the XML input file to load the model
        */
        ImportService importService = new ImportService()
        def nodes = importService.load(new StringBufferInputStream(ok_xml1), project)
        assertEquals("incorrect number of nodes: " + nodes.size(), nodes.size(), 2)

        /**
         * Test an export operation
         */
        def output = service.export(project)
        assertNotNull(output)
        println("TEST: output="+output)
        importService.validate(new StringBufferInputStream(output))
    }
}
