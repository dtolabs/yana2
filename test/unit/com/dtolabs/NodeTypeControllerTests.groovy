package com.dtolabs

import grails.test.mixin.TestFor
import grails.test.mixin.Mock


@TestFor(NodeTypeController)
@Mock([NodeType, Project, NodeTypeRelationship, Webhook, Node, Filter, Attribute, NodeAttribute])

class NodeTypeControllerTests {


    void testIndex() {
        controller.index()
        assert "/nodeType/list" == response.redirectedUrl
    }


    void testList() {
        defineBeans {
            iconService(IconService)
        }

        Project project = new Project(name: 'test1', description: 'desc').save()

        def typeA = new NodeType(project: project,
                name: "TypeA",
                description: "test node type A description",
                image: "Node.png").save()
        def typeB = new NodeType(project: project,
                name: "TypeB",
                description: "test node type B description",
                image: "Node.png").save()
        def typeC = new NodeType(project: project,
                name: "TypeC",
                description: "test node type C description",
                image: "Node.png").save()

        params.project = project.name

        /**
         * Run the controller action
         */
        def model = controller.list()

        //  model: [nodeTypeInstanceList: NodeType.list(params), nodeTypeInstanceTotal: NodeType.count(),path:path]
        assertNotNull("Model not returned", model)
        assertEquals(3, model.nodeTypeInstanceList.size())
        assertTrue(model.nodeTypeInstanceList.contains(typeA))
        assertTrue(model.nodeTypeInstanceList.contains(typeB))
        assertTrue(model.nodeTypeInstanceList.contains(typeC))

    }

    void testShow() {
        defineBeans {
            iconService(IconService)
        }

        Project project = new Project(name: 'test1', description: 'desc').save()

        def typeA = new NodeType(project: project,
                name: "TypeA",
                description: "test node type A description",
                image: "Node.png").save()

        params.project = project.name
        params.id = typeA.id

        /**
         * Run the controller action
         */
        def model = controller.show()

        // model:	[children:children,parents:parents,nodeTypeInstance: nodeTypeInstance,path:path]

        assertNotNull("Model not returned", model)
        assertEquals(typeA.name, model.nodeTypeInstance.name)
        assertEquals(0, model.children.size())
        assertEquals(0, model.parents.size())
        assertEquals("Incorrect number of node types", 1, NodeType.count)

    }

    void testDelete() {
        defineBeans {
            iconService(IconService)
            webhookService(WebhookService)
        }

        Project project = new Project(name: 'test1', description: 'desc').save()

        def typeA = new NodeType(project: project,
                name: "TypeA",
                description: "test node type A description",
                image: "Node.png").save()

        params.project = project.name
        params.id = typeA.id

        /**
         * Run the controller action
         */
        controller.delete()

        assertEquals("Incorrect number of node types", 0, NodeType.count)
        assert "/nodeType/list" == response.redirectedUrl
        assertFalse(NodeType.exists(params.id))

    }

    /**
     * A NodeType that has Nodes cannot be deleted
     */
    void testDelete_withNodes() {
        defineBeans {
            iconService(IconService)
            webhookService(WebhookService)
        }

        Project project = new Project(name: 'test1', description: 'desc').save()

        def typeA = new NodeType(project: project,
                name: "TypeA",
                description: "test node type A description",
                image: "Node.png").save()

        // create an instance of this NodeType
        def node1 = new Node(name: 'node1', description: 'desc', tags: 'tag1,tag2',
                project: project, nodetype: typeA).save()

        // Make sure the type sees it has one node
        assert 1 == typeA.nodes.size()

        // Declare the request parameters to delete the NodeType
        params.project = project.name
        params.id = typeA.id

        /**
         * Run the controller action
         */
        controller.delete()

        // Ensure the type still exists
        assertEquals("Incorrect number of node types", 1, NodeType.count)
        assertTrue("NodeType no longer exists.", NodeType.exists(params.id))

        // Should be redirected back to show page
        assertEquals("/nodeType/show/${params.id}", response.redirectedUrl)

    }

    /**
     * Tests AJAX method
     */
     void testGetNodeAttributes() {
         Project project = new Project(name: 'test1', description: 'desc').save()
         Filter filter = new Filter(project: project, dataType: "String", regex: ".*").save()
         Attribute arch = new Attribute(name:  "arch", project:  project, filter: filter).save()
         Attribute repo = new Attribute(name:  "repo", project:  project, filter: filter).save()
         NodeType nodeType = new NodeType(project: project,
                 name: "TypeA",
                 description: "test node type A description",
                 image: "Node.png").save()
         NodeAttribute attr1 = new NodeAttribute(attribute: arch, nodetype: nodeType, required: false).save()
         NodeAttribute attr2 = new NodeAttribute(attribute: repo, nodetype: nodeType, required: false).save()

         params.templateid = nodeType.id
         /**
          * Run the controller action
          */
         controller.getNodeAttributes()

         println("DEBUG: response.text=" + response.text)
         assertNotNull("No JSON returned", response.json)
         assertEquals("attList wrong size: " + response.json.attList, 2, response.json.attList[0].size())
         assertEquals("atts wrong size: " + response.json.atts, 2, response.json.atts[0].size())

         def hasAtt_arch = false
         def hasAtt_repo = false
         response.json.atts[0].each {
             assertEquals("NodeType id incorrect in atts.", nodeType.id, it?.tid.toLong())
             assertEquals("Filter incorrect in atts.", filter.dataType, it?.datatype)
             switch (it?.val) {
                 case "arch":
                     hasAtt_arch = true
                     break
                 case "repo":
                     hasAtt_repo = true

             }
         }
         assertTrue("attribute not found in JSON.", hasAtt_arch)
         assertTrue("attribute not found in JSON.", hasAtt_repo)
     }
}