package com.dtolabs

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.validation.FieldError
import groovy.xml.MarkupBuilder

@TestFor(NodeController)
@Mock([Node, NodeType, Project, NodeValue, ChildNode, Webhook, Filter, Attribute, NodeAttribute])
class NodeControllerTests {


    void testIndex() {
        controller.index()
        assert "/node/list" == response.redirectedUrl
    }

    void testList() {
        defineBeans {
            webhookService(WebhookService)
            iconService(IconService)
        }

        Project project = new Project(name: 'test1', description: 'desc').save()

        NodeType nodeType = new NodeType(project: project,
                                         name: "TypeA",
                                         description: "test node type A description",
                                         image: "Node.png").save()
        Node node1 = new Node(name: 'node1', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()
        Node node2 = new Node(name: 'node2', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()
        Node node3 = new Node(name: 'node3', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()

        params.project = project.name

        // model:	[nodeInstanceList: nodes, nodeInstanceTotal: totCount, path:path]

        def control = mockFor(ProjectService)
        control.demand.findProject {name ->
            assert name == 'test1'
            project
        }
        controller.projectService = control.createMock()

        def control2 = mockFor(NodeService, false)
        control2.demand.listNodes { Project proj, params ->
            assert proj == project
            [total: 3, nodes: [node1, node2, node3]]
        }
        controller.nodeService = control2.createMock()
        /**
         * Run the controller action
         */
        def model = controller.list()

        assertEquals(3, model.nodeInstanceList.size())
        assertEquals(3, model.nodeInstanceTotal)
        assertTrue(model.nodeInstanceList.contains(node1))
        assertTrue(model.nodeInstanceList.contains(node2))
        assertTrue(model.nodeInstanceList.contains(node3))
    }


    void testList_asXml() {
        defineBeans {
            webhookService(WebhookService)
            iconService(IconService)
            xmlService(XmlService)
        }

        Project project = new Project(name: 'test1', description: 'desc').save()

        NodeType nodeType = new NodeType(project: project,
                                         name: "TypeA",
                                         description: "test node type A description",
                                         image: "Node.png").save()
        Node node1 = new Node(name: 'node1', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()
        Node node2 = new Node(name: 'node2', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()
        Node node3 = new Node(name: 'node3', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()

        params.project = project.name
        params.format = "xml"

        def control = mockFor(ProjectService)
        control.demand.findProject {name ->
            assert name == 'test1'
            project
        }
        controller.projectService = control.createMock()

        def control2 = mockFor(NodeService, false)
        control2.demand.listNodes { Project proj, params ->
            assert proj == project
            [total: 3, nodes: [node1, node2, node3]]
        }
        controller.nodeService = control2.createMock()
        def control3 = mockFor(XmlService, false)
        control3.demand.formatNodes { nodelist ->
            assert nodelist == [node1, node2, node3]
            '<xml><test>ok3</test></xml>'
        }
        controller.xmlService = control3.createMock()
        /**
         * Run the controller action
         */
        controller.list()

        assertNotNull("Response did not contain XML", response.xml)
        assertNotNull("Response did not contain XML", response.xml)
        assertEquals("Incorrect number of nodes:", 1, response.xml.test.size())
        assertEquals('ok3', response.xml.test[0].text())
    }

    void testCreate() {
        defineBeans {
            webhookService(WebhookService)
        }
        Project project = new Project(name: 'test1', description: 'desc').save()

        NodeType nodeType = new NodeType(project: project,
                                         name: "TypeA",
                                         description: "test node type A description",
                                         image: "Node.png").save()

        params.project = project.name

        def control = mockFor(ProjectService)
        control.demand.findProject {name ->
            assert name == 'test1'
            project
        }
        controller.projectService = control.createMock()

        def control2 = mockFor(NodeService, false)
        control2.demand.listNodes { Project proj ->
            assert proj == project
            [total: 0, nodes: []]
        }
        controller.nodeService = control2.createMock()
        /**
         * Run the controller action
         */

        def model = controller.create() // this action returns a map

        assertEquals("Incorrect response code.", 200, response.status)
        assertNotNull("nodeTypeList was not found in the model.", model.nodeTypeList)
        assertEquals(nodeType.name, model.nodeTypeList[0].name)
    }

    void testSave() {
        defineBeans {
            webhookService(WebhookService)
        }

        Project project = new Project(name: 'test1', description: 'desc').save()

        NodeType nodeType = new NodeType(project: project,
                                         name: "TypeA",
                                         description: "test node type A description",
                                         image: "Node.png").save()

        params.name = "node1"
        params.description = "desc"
        params.nodetype = [id: nodeType.id]
        params.tags = "tag1,tag2"
        params.project = project.name
        request.method = "POST"

        def control = mockFor(ProjectService)
        control.demand.findProject {name ->
            assert name == 'test1'
            project
        }
        controller.projectService = control.createMock()

        def control2 = mockFor(NodeService, false)
        control2.demand.createNode { Project proj, NodeType nodetype, name, description, tags, parentNodes, childNodes, nodeValues ->
            assert name == 'node1'
            assert proj == project
            assert description == 'desc'
            assert tags == 'tag1,tag2'
            assert parentNodes == []
            assert childNodes == []
            assert nodeValues == null
            def node = new Node(name: name, description: description, tags: tags, project: proj, nodetype: nodetype)
            assert null != node.save()
            node
        }
        controller.nodeService = control2.createMock()
        /**
         * Run the controller action
         */
        controller.save()
        assert 'default.created.message' == flash.message
        assert 0 == model.size()

        assertEquals("Incorrect response code.", 302, response.status)
        assertEquals("/node/show/1", response.redirectedUrl)
        assertEquals("Incorrect node count: " + Node.count, 1, Node.count)

        def node1 = Node.findByNameAndProject("node1", project)
        assertNotNull("The node1 instance not found after save operation.", node1)

    }

    /**
     * FIXME: this test should be a NodeService unit test
     */
    void testSave_NotUnique() {
        defineBeans {
            webhookService(WebhookService)
        }

        Project project = new Project(name: 'test1', description: 'desc').save()

        NodeType nodeType = new NodeType(project: project,
                                         name: "TypeA",
                                         description: "test node type A description",
                                         image: "Node.png").save()

        Node node1 = new Node(name: 'node1', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()

        params.name = "node1"
        params.description = "desc"
        params.nodetype = [id: nodeType.id]
        params.tags = "tag1,tag2"
        params.project = project.name
        request.method = "POST"

        def control = mockFor(ProjectService)
        control.demand.findProject {name ->
            assert name == 'test1'
            project
        }
        controller.projectService = control.createMock()

        def control2 = mockFor(NodeService, false)
        control2.demand.createNode { Project proj, NodeType nodetype, name, description, tags, parentNodes, childNodes, nodeValues ->
            assert name == 'node1'
            assert proj == project
            assert description == 'desc'
            assert tags == 'tag1,tag2'
            assert parentNodes == []
            assert childNodes == []
            assert nodeValues == null
            def node = new Node(name: name, description: description, tags: tags, project: proj, nodetype: nodetype)
            assert null == node.save()
            node
        }
        controller.nodeService = control2.createMock()
        /**
         * Run the controller action
         */
        controller.save()

        assertEquals("Incorrect node count: " + Node.count, 1, Node.count)
        assertEquals("Incorrect response code.", 200, response.status)
        assertEquals("Incorrect view.", "/node/create", view)
        assertNotNull("nodeInstance not found in model.", model.nodeInstance)
        /**
         * This is the critical test. Check if name has a field error
         */
        assert model.nodeInstance.errors.hasFieldErrors("name")
        FieldError err = model.nodeInstance.errors.getFieldError("name")
        def value = err.getRejectedValue()
        assertEquals("Incorrect rejected field value:", node1.name, value)
    }


    void testShow() {
        defineBeans {
            webhookService(WebhookService)
            projectService(ProjectService)
            iconService(IconService)
        }

        Project project = new Project(name: 'test1', description: 'desc').save()

        NodeType nodeType = new NodeType(project: project,
                                         name: "TypeA",
                                         description: "test node type A description",
                                         image: "Node.png").save()

        Node node1 = new Node(name: 'node1', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()

        params.id = 1
        params.project = project.name

        def control = mockFor(ProjectService)
        control.demand.findProject {name ->
            assert name == 'test1'
            project
        }
        controller.projectService = control.createMock()
        def control2 = mockFor(NodeService, false)
        control2.demand.readNode { id ->
            assert id == 1L
            node1
        }
        controller.nodeService = control2.createMock()

        /**
         * Run the controller action
         */
        controller.show()

        assertEquals("Incorrect view.", "/node/show", view)

        assertEquals(node1, model.nodeInstance)
    }

    void testShow_asXml() {
        defineBeans {
            nodeService(NodeService)
            webhookService(WebhookService)
            iconService(IconService)
        }

        Project project = new Project(name: 'test1', description: 'desc').save()

        NodeType nodeType = new NodeType(project: project,
                                         name: "TypeA",
                                         description: "test node type A description",
                                         image: "Node.png").save()

        Node node1 = new Node(name: 'node1', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()

        params.id = 1
        params.project = project.name
        params.format = "xml"

        def control2 = mockFor(NodeService, false)
        control2.demand.readNode { id ->
            assert id == 1L
            node1
        }
        controller.nodeService = control2.createMock()
        def control3 = mockFor(XmlService, false)
        control3.demand.formatNodes { nodelist ->
            assert nodelist == [node1]
            '<xml><test>ok</test></xml>'
        }
        controller.xmlService = control3.createMock()
        /**
         * Run the controller action
         */
        controller.show()

        assertNotNull("Response did not contain XML", response.xml)
        assertEquals("Incorrect number of nodes:", 1, response.xml.test.size())
        assertEquals('ok', response.xml.test[0].text())
    }

    void testEdit() {

        Project project = new Project(name: 'test1', description: 'desc').save()

        NodeType nodeType = new NodeType(project: project,
                                         name: "TypeA",
                                         description: "test node type A description",
                                         image: "Node.png").save()

        Node node1 = new Node(name: 'node1', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()

        params.id = 1
        params.project = project.name

        def control = mockFor(ProjectService)
        control.demand.findProject {name ->
            assert name == 'test1'
            project
        }
        controller.projectService = control.createMock()

        def control2 = mockFor(NodeService, false)
        control2.demand.readNode { id ->
            assert id == 1L
            node1
        }
        controller.nodeService = control2.createMock()
        /**
         * Run the controller action
         */
        def model = controller.edit()

        /* model:
        [selectedParents:selectedParents,
         selectedChildren:selectedChildren,
         unselectedParents:unselectedParents,
         unselectedChildren:unselectedChildren,
         nodes:nodes,
         nodeInstance:nodeInstance]
          */

        assertNotNull("Node not found", model.nodeInstance)
        assertEquals("Incorrect node found", node1, model.nodeInstance)

    }


    void testUpdate() {
        Project project = new Project(name: 'test1', description: 'desc').save()

        NodeType nodeType = new NodeType(project: project,
                                         name: "TypeA",
                                         description: "test node type A description",
                                         image: "Node.png").save()

        Node node1 = new Node(name: 'node1', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()

        params.id = 1
        params.project = project.name
        /**
         * Change these properties
         */
        params.tags = "tag3"
        params.description = "new description"

        def control = mockFor(ProjectService)
        control.demand.findProject {name ->
            assert name == 'test1'
            project
        }
        controller.projectService = control.createMock()

        def control2 = mockFor(NodeService, false)
        control2.demand.readNode { id ->
            assert id == 1L
            node1
        }
        control2.demand.updateNode { Project proj, Node nodeInstance,
                                     String name, String description, String tags,
                                     parentNodes,
                                     childNodes,
                                     nodeValues ->
            assert proj == project
            assert nodeInstance == node1
            assert name == null
            assert description == 'new description'
            assert tags == 'tag3'
            assert parentNodes == []
            assert childNodes == []
            assert nodeValues == null
        }
        controller.nodeService = control2.createMock()
        /**
         * Run the controller action
         */
        controller.update()

        assertEquals("/node/show/1", response.redirectedUrl)
    }

    void testUpdate_withAttributes() {
        Project project = new Project(name: 'test1', description: 'desc').save()
        Filter filter = new Filter(project: project, dataType: "String", regex: ".*").save()
        Attribute arch = new Attribute(name: "arch", project: project, filter: filter).save()
        Attribute repo = new Attribute(name: "repo", project: project, filter: filter).save()
        NodeType nodeType = new NodeType(project: project,
                                         name: "TypeA",
                                         description: "test node type A description",
                                         image: "Node.png").save()
        NodeAttribute attr1 = new NodeAttribute(attribute: arch, nodetype: nodeType, required: false).save()
        NodeAttribute attr2 = new NodeAttribute(attribute: repo, nodetype: nodeType, required: false).save()
        Node node1 = new Node(name: 'node1', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()
        def nv1 = new NodeValue(node: node1, nodeattribute: attr1, value: "sparc").save()
        def nv2 = new NodeValue(node: node1, nodeattribute: attr2, value: "ftp://localhost/resource").save()

        params.id = node1.id
        params.project = project.name
        params.attributevalues = [arch: "noarch", repo: "http://localhost/resource"]

        /**
         * Change these properties
         */
        params.tags = "tag3"
        params.description = "new description"

        def control = mockFor(ProjectService)
        control.demand.findProject {name ->
            assert name == 'test1'
            project
        }
        controller.projectService = control.createMock()

        def control2 = mockFor(NodeService, false)
        control2.demand.readNode { id ->
            assert id == 1L
            node1
        }
        control2.demand.updateNode { Project proj, Node nodeInstance,
                                     String name, String description, String tags,
                                     parentNodes,
                                     childNodes,
                                     nodeValues ->
            assert proj == project
            assert nodeInstance == node1
            assert name == null
            assert description == 'new description'
            assert tags == 'tag3'
            assert parentNodes == []
            assert childNodes == []
            assert nodeValues == [arch: 'noarch', repo: 'http://localhost/resource']
        }
        controller.nodeService = control2.createMock()
        /**
         * Run the controller action
         */
        controller.update()

        assertEquals("/node/show/1", response.redirectedUrl)
    }


    void testDelete() {
        Project project = new Project(name: 'test1', description: 'desc').save()

        NodeType nodeType = new NodeType(project: project,
                                         name: "TypeA",
                                         description: "test node type A description",
                                         image: "Node.png").save()

        Node node1 = new Node(name: 'node1', description: 'desc', tags: 'tag1,tag2',
                              project: project, nodetype: nodeType).save()

        params.id = 1
        params.project = project.name

        def control2 = mockFor(NodeService, false)
        control2.demand.readNode { id ->
            assert id == 1L
            node1
        }
        control2.demand.deleteNode { Node node ->
            assert node == node1
            node1
        }
        controller.nodeService = control2.createMock()
        /**
         * Run the controller action
         */
        controller.delete()

        assertEquals("/node/list", response.redirectedUrl)
        assert 'default.deleted.message' == flash.message
    }
}
