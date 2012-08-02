package com.dtolabs

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(NodeService)
@Mock([Node,Project,NodeType,ChildNode])
class NodeServiceTests {

    void testCreateNode() {
        assertEquals(0, Node.list().size())
		
		Project testProject1 = new Project(name:'testProject1',
                                           description:'desc').save()
		NodeType testNodeType1 = new NodeType(project:testProject1,
											  name:"testNodeType1",
											  description:"test node type 1 description",
											  imate:"testNodeType.jpg").save()

        def testNode1 = service.createNode(testProject1,
                                           testNodeType1,
										   "testNode1",
										   "test node 1 description",
										   "test node 1 tags",
										   [], [], [])
		
        assertEquals('testNode1', testNode1.name)
        assertEquals("test node 1 description", testNode1.description)
        assertEquals("test node 1 tags", testNode1.tags)
        assertEquals(null, testNode1.parents)
        assertEquals(null, testNode1.children)
        assertEquals(null, testNode1.nodeValues)
    }
	
	void testUpdateNode() {
		assertEquals(0, Node.list().size())
		
		Project testProject1 = new Project(name:'testProject1',
										   description:'desc').save()
		NodeType testNodeType1 = new NodeType(project:testProject1,
											  name:"testNodeType1",
											  description:"test node type 1 description",
											  imate:"testNodeType.jpg").save()
		def testNode1 = service.createNode(testProject1,
										   testNodeType1,
										   "testNode1",
										   "test node 1 description",
										   "test node 1 tags",
										   [], [], [])
		service.updateNode(testProject1,
                           testNode1,
						   "testNode1Update",
						   "test node 1 description (update)",
						   "test node 1 tags (update)",
						   [], [], [])
		
		assertEquals('testNode1Update', testNode1.name)
		assertEquals("test node 1 description (update)", testNode1.description)
		assertEquals("test node 1 tags (update)", testNode1.tags)
		assertEquals(null, testNode1.parents)
		assertEquals(null, testNode1.children)
		assertEquals(null, testNode1.nodeValues)
	}
	
	void testDeleteNode() {
		assertEquals(0, Node.list().size())
		
		Project testProject1 = new Project(name:'testProject1',
										   description:'desc').save()
		NodeType testNodeType1 = new NodeType(project:testProject1,
											  name:"testNodeType1",
											  description:"test node type 1 description",
											  imate:"testNodeType.jpg").save()
		def testNode1 = service.createNode(testProject1,
										   testNodeType1,
										   "testNode1",
										   "test node 1 description",
										   "test node 1 tags",
										   [], [], [])

		assertEquals(1, Node.list().size())
		
		service.deleteNode(testNode1)
		
		assertEquals(0, Node.list().size())
	} 
}