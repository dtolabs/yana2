package com.dtolabs

import com.dtolabs.yana2.springacl.YanaPermission
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
class NodeServiceCRUDTests extends GroovyTestCase {

    def NodeService nodeService
    def ProjectService projectService


    private void loginAsAdmin() {
        // have to be authenticated as an admin to create ACLs
        SecurityContextHolder.context.authentication =
            new UsernamePasswordAuthenticationToken(
                    'admin', 'admin',
                    AuthorityUtils.createAuthorityList('ROLE_YANA_ADMIN'))
    }

    /**
     * Create
     */
    void testCreateNode() {
        assertEquals(0, Node.list().size())

        Project testProject = new Project(name: 'testProject1',
                description: 'testProject1 descr').save(flush: true)


        // login
        loginAsAdmin()

        // add some acls for the project
        projectService.addPermission(testProject, 'ROLE_YANA_USER', YanaPermission.READ)
        projectService.addPermission(testProject, 'ROLE_YANA_ADMIN', YanaPermission.ADMINISTRATION)


        NodeType testNodeType1 = new NodeType(project: testProject,
                name: "testNodeType1",
                description: "test node type 1 description",
                image: "Node.png").save(flush: true)

        nodeService.createNode(testProject,
                testNodeType1,
                "testNode1",
                "test node 1 description",
                "tagsA,tagsB",
                [], [], [:])

        Node testNode1 = Node.findByProjectAndName(testProject, "testNode1")
        assertEquals("testNode1", testNode1.name)
        assertEquals("test node 1 description", testNode1.description)
        assertEquals("tagsA,tagsB", testNode1.tags)
        assertEquals(null, testNode1.parents)
        assertEquals(null, testNode1.children)
        assertEquals(null, testNode1.nodeValues)
    }

    /**
     * Update
     */
    void testUpdateNode() {
        Project testProject = new Project(name: 'testProject1',
                description: 'testProject1 descr').save(flush: true)


        // login
        loginAsAdmin()

        // add some acls for the project
        projectService.addPermission(testProject, 'ROLE_YANA_USER', YanaPermission.READ)
        projectService.addPermission(testProject, 'ROLE_YANA_ADMIN', YanaPermission.ADMINISTRATION)

        NodeType testNodeType1 = new NodeType(project: testProject,
                name: "testNodeType1",
                description: "test node type 1 description",
                image: "Node.png").save(flush: true)

        assertEquals(0, Node.list().size())

        nodeService.createNode(testProject,
                testNodeType1,
                "testNode1",
                "test node 1 description",
                "tagsA,tagsB",
                [], [], [:])

        Node testNode1 = Node.findByProjectAndName(testProject, "testNode1")
        assert null != testNode1

        nodeService.updateNode(
                testNode1,
                "testNode1Update",
                "test node 1 description (update)",
                "tagsA,tagsB,tagUpdate",
                [], [], [:])

        Node testNode1Update = Node.findByProjectAndName(testProject, "testNode1Update")

        assertEquals('testNode1Update', testNode1Update.name)
        assertEquals("test node 1 description (update)", testNode1Update.description)
        assertEquals("tagsA,tagsB,tagUpdate", testNode1Update.tags)
        assertEquals(null, testNode1Update.parents)
        assertEquals(null, testNode1Update.children)
        assertEquals(null, testNode1Update.nodeValues)
    }

    /**
     * Delete
     */
    void testDeleteNode() {
        Project testProject = new Project(name: 'testProject1',
                description: 'testProject1 descr').save(flush: true)


        // login
        loginAsAdmin()

        // add some acls for the project
        projectService.addPermission(testProject, 'ROLE_YANA_USER', YanaPermission.READ)
        projectService.addPermission(testProject, 'ROLE_YANA_ADMIN', YanaPermission.ADMINISTRATION)

        NodeType testNodeType1 = new NodeType(project: testProject,
                name: "testNodeType1",
                description: "test node type 1 description",
                image: "Node.png").save(flush: true)

        assertEquals(0, Node.list().size())

        nodeService.createNode(testProject,
                testNodeType1,
                "testNode1",
                "test node 1 description",
                "tagsA,tagsB",
                [], [], [:])

        assertEquals(1, Node.list().size())

        Node testNode1 = Node.findByProjectAndName(testProject, "testNode1")
        nodeService.deleteNode(testNode1)

        assertEquals(0, Node.list().size())
    }


}