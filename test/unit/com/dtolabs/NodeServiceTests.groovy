package com.dtolabs

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(NodeService)
@Mock([Node,Project,NodeType,NodeTypeRelationship,ChildNode])
class NodeServiceTests {
	Project testProject
	NodeType testNodeType0, testNodeType1, testNodeType2, testNodeType3,
			 testNodeType4, testNodeType5, testNodeType6, testNodeType7

	void setUp() {
		testProject = new Project(name:'testProject1',
								   description:'desc').save()

		testNodeType0 = new NodeType(project:testProject,
									 name:"testNodeType0",
									 description:"test node type 0 description",
									 image:"testNodeType0.jpg").save()
		testNodeType1 = new NodeType(project:testProject,
									 name:"testNodeType1",
									 description:"test node type 1 description",
									 image:"testNodeType1.jpg").save()
		testNodeType2 = new NodeType(project:testProject,
									 name:"testNodeType2",
									 description:"test node type 2 description",
									 image:"testNodeType2.jpg").save()
		testNodeType3 = new NodeType(project:testProject,
									 name:"testNodeType3",
									 description:"test node type 3 description",
									 image:"testNodeType3.jpg").save()
		testNodeType4 = new NodeType(project:testProject,
									 name:"testNodeType4",
									 description:"test node type 4 description",
									 image:"testNodeType4.jpg").save()
		testNodeType5 = new NodeType(project:testProject,
									 name:"testNodeType5",
									 description:"test node type 5 description",
									 image:"testNodeType5.jpg").save()
		testNodeType6 = new NodeType(project:testProject,
									 name:"testNodeType6",
									 description:"test node type 6 description",
									 image:"testNodeType6.jpg").save()
		testNodeType7 = new NodeType(project:testProject,
									 name:"testNodeType7",
									 description:"test node type 7 description",
									 image:"testNodeType7.jpg").save()

		// Proposed node-type-relationship hierarchy
		// 0    1    2
		//     /  \	/ \
		//    3    4   5
		//            / \	
		//           6   7
		NodeTypeRelationship ntr_1_3, ntr_1_4, ntr_2_4, ntr_2_5, ntr_5_6, ntr_5_7
		ntr_1_3 = new NodeTypeRelationship(name:"ntp1-ntp3",
										   parent:testNodeType1,
										   child:testNodeType3).save()
		ntr_1_4 = new NodeTypeRelationship(name:"ntp1-ntp4",
										   parent:testNodeType1,
										   child:testNodeType4).save()
		ntr_2_4 = new NodeTypeRelationship(name:"ntp2-ntp4",
										   parent:testNodeType2,
										   child:testNodeType4).save()
		ntr_2_5 = new NodeTypeRelationship(name:"ntp2-ntp5",
										   parent:testNodeType2,
										   child:testNodeType5).save()
		ntr_5_6 = new NodeTypeRelationship(name:"ntp5-ntp6",
										   parent:testNodeType5,
										   child:testNodeType6).save()
		ntr_5_7 = new NodeTypeRelationship(name:"ntp5-ntp7",
										   parent:testNodeType5,
										   child:testNodeType7).save()
	}
	
	void createNodes() {
		// 0    1    2
		//     /  \	/ \
		//    3    4   5
		//            / \
		//           6   7
		def testNode0 = service.createNode(testProject,
										   testNodeType0,
										   "testNode0",
										   "test node 0 description",
										   "test node 0 tags",
										   [],[],[:])
		def testNode1 = service.createNode(testProject,
										   testNodeType1,

										   "testNode1",
										   "test node 1 description",
										   "test node 1 tags",
										   [],[],[:])
		def testNode2 = service.createNode(testProject,
										   testNodeType2,
										   "testNode2",
										   "test node 2 description",
										   "test node 2 tags",
										   [],[],[:])
		def testNode3 = service.createNode(testProject,
										   testNodeType3,
										   "testNode3",
										   "test node 3 description",
										   "test node 3 tags",
										   [testNode1], [], [:])
		def testNode4 = service.createNode(testProject,
										   testNodeType4,
										   "testNode4",
										   "test node 4 description",
										   "test node 4 tags",
										   [testNode1, testNode2], [], [:])
		def testNode6 = service.createNode(testProject,
										   testNodeType6,
										   "testNode6",
										   "test node 6 description",
										   "test node 6 tags",
										   [],[],[:])
		def testNode7 = service.createNode(testProject,
										   testNodeType7,
										   "testNode7",
										   "test node 7 description",
										   "test node 7 tags",
										   [],[],[:])
		def testNode5 = service.createNode(testProject,
										   testNodeType5,
										   "testNode5",
										   "test node 5 description",
										   "test node 5 tags",
										   [testNode2], [testNode6, testNode7], [:])
	}
	
	void tearDown() {
	}

	void testCreateNode() {
		assertEquals(0, Node.list().size())
        def mockControl = mockFor(ProjectService)
        mockControl.demand.authorizedOperatorPermission {project -> assert project == testProject }
        service.projectService = mockControl.createMock()

        service.createNode(testProject,
						   testNodeType1,
						   "testNode1",
						   "test node 1 description",
						   "test node 1 tags",
						   [], [], [:])
		
		def Node testNode1 = Node.findByProjectAndName(testProject, "testNode1")
		assertEquals('testNode1', testNode1.name)
		assertEquals("test node 1 description", testNode1.description)
		assertEquals("test node 1 tags", testNode1.tags)
		assertEquals(null, testNode1.parents)
		assertEquals(null, testNode1.children)
		assertEquals(null, testNode1.nodeValues)
	}
	
	void testUpdateNode() {
		assertEquals(0, Node.list().size())

        def mockControl = mockFor(ProjectService)
        mockControl.demand.authorizedOperatorPermission {project -> assert project == testProject }
        mockControl.demand.authorizedOperatorPermission {project -> assert project == testProject }
        service.projectService = mockControl.createMock()

		service.createNode(testProject,
						   testNodeType1,
						   "testNode1",
						   "test node 1 description",
						   "test node 1 tags",
						   [], [], [:])

		def Node testNode1 = Node.findByProjectAndName(testProject, "testNode1")


		service.updateNode(testProject,
						   testNode1,
						   "testNode1Update",
						   "test node 1 description (update)",
						   "test node 1 tags (update)",
						   [], [], [:])
		
		def Node testNode1Update = Node.findByProjectAndName(testProject, "testNode1Update")	
		assertEquals('testNode1Update', testNode1Update.name)
		assertEquals("test node 1 description (update)", testNode1Update.description)
		assertEquals("test node 1 tags (update)", testNode1Update.tags)
		assertEquals(null, testNode1Update.parents)
		assertEquals(null, testNode1Update.children)
		assertEquals(null, testNode1Update.nodeValues)
	}
	
	void testDeleteNode() {
		assertEquals(0, Node.list().size())

        def mockControl = mockFor(ProjectService)
        mockControl.demand.authorizedOperatorPermission {project -> assert project == testProject }
        mockControl.demand.authorizedOperatorPermission {project -> assert project == testProject }
        service.projectService = mockControl.createMock()

		service.createNode(testProject,
						   testNodeType1,
						   "testNode1",
						   "test node 1 description",
						   "test node 1 tags",
						   [], [], [:])

		assertEquals(1, Node.list().size())
		
		def Node testNode1 = Node.findByProjectAndName(testProject, "testNode1")	
		service.deleteNode(testNode1)
		
		assertEquals(0, Node.list().size())
	}

	void testCreateNodes() {
		assertEquals(0, Node.list().size())

        def mockControl = mockFor(ProjectService)
        8.times {
            mockControl.demand.authorizedOperatorPermission {project -> assert project == testProject }
        }
        service.projectService = mockControl.createMock()
		createNodes()

println("---------------------->")
NodeTypeRelationship.findAll().each {ntr ->
	println("-->")
	println("ntr.parent.name: ${ntr.parent.name}")
	println("ntr.child.name:  ${ntr.child.name}")
	println("<--")
}
Node.findAll().each {node ->
	println("-->")
	println("node.name: ${node.name}")
	node.parents.each {parent ->
		println("  parent-p: ${parent.parent.name}")
		println("  parent-c: ${parent.child.name}")
	}
	node.children.each {child ->
		println("   child-p: ${child.parent.name}")
		println("   child-c: ${child.child.name}")
	}
	println("<--")
}
ChildNode.findAll().each {cn ->
	println("-->")
	println("cn.parent.name: ${cn.parent.name}")
	println("cn.child.name:  ${cn.child.name}")
	println("<--")
}
println("<----------------------")

		// 0    1    2
		//     / \	/ \
		//    3    4   5
		//            / \
		//           6   7

		def List<ChildNode> childNodes

		def Node testNode0 = Node.findByProjectAndName(testProject, "testNode0")
		assertNull(testNode0.parents)
		assertNull(testNode0.children)

		def Node testNode1 = Node.findByProjectAndName(testProject, "testNode1")
		assertEquals(2, testNode1.parents.size())
		//assertEquals(2, testNode1.children.size())

		def Node testNode2 = Node.findByProjectAndName(testProject, "testNode2")
		assertEquals(2, testNode2.parents.size())
		//assertEquals(2, testNode2.children.size())

		def Node testNode3 = Node.findByProjectAndName(testProject, "testNode3")
		assertEquals(1, testNode3.parents.size())
		assertNull(testNode3.children)

		def Node testNode4 = Node.findByProjectAndName(testProject, "testNode4")
		assertEquals(2, testNode4.parents.size())
		assertNull(testNode4.children)

		def Node testNode5 = Node.findByProjectAndName(testProject, "testNode5")
		//assertEquals(1, testNode5.parents.size())
		//assertEquals(2, testNode5.children.size())

		def Node testNode6 = Node.findByProjectAndName(testProject, "testNode6")
		assertEquals(1, testNode6.parents.size())
		assertNull(testNode6.children)

		def Node testNode7 = Node.findByProjectAndName(testProject, "testNode7")
		assertEquals(1, testNode7.parents.size())
		assertNull(testNode7.children)

		childNodes = ChildNode.findAllByParent(testNode0)
		assertEquals([], childNodes)
		childNodes = ChildNode.findAllByChild(testNode0)
		assertEquals([], childNodes)

		childNodes = ChildNode.findAllByParent(testNode1)
		assertEquals(2, childNodes.size())
		childNodes = ChildNode.findAllByChild(testNode1)
		assertEquals([], childNodes)

		childNodes = ChildNode.findAllByParent(testNode2)
		assertEquals(2, childNodes.size())
		childNodes = ChildNode.findAllByChild(testNode2)
		assertEquals([], childNodes)

		childNodes = ChildNode.findAllByParent(testNode3)
		assertEquals([], childNodes)
		childNodes = ChildNode.findAllByChild(testNode3)
		assertEquals(1, childNodes.size())

		childNodes = ChildNode.findAllByParent(testNode4)
		//assertEquals(2, childNodes.size())
		childNodes = ChildNode.findAllByChild(testNode4)
		//assertEquals([], childNodes)

		childNodes = ChildNode.findAllByParent(testNode5)
		//assertEquals(1, childNodes.size())
		childNodes = ChildNode.findAllByChild(testNode5)
		//assertEquals(2, childNodes.size())

		childNodes = ChildNode.findAllByParent(testNode6)
		//assertEquals(1, childNodes.size())
		childNodes = ChildNode.findAllByChild(testNode6)
		assertEquals(1, childNodes.size())

		childNodes = ChildNode.findAllByParent(testNode7)
		//assertEquals(1, childNodes.size())
		childNodes = ChildNode.findAllByChild(testNode7)
		assertEquals(1, childNodes.size())
	}

}