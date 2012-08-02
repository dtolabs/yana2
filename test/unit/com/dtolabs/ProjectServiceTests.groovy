package com.dtolabs



import grails.test.mixin.*
import org.junit.*
import org.grails.plugins.springsecurity.service.acl.AclUtilService

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ProjectService)
@Mock(Project)
class ProjectServiceTests {

    void testListProjects() {
        new Project(name:'test1',description:'desc').save()
        new Project(name:'test2',description:'desc').save()
        def list=service.listProjects()
        assertNotNull list
        assertEquals 2,list.size()
    }

    void testUserSelectProject(){
        def p1= new Project(name: 'test1', description: 'desc')
        p1.save()
        def session=[:]
        service.userSelectProject(session,p1)
        assertNotNull session.project
        assertEquals 'test1',session.project

        //change project
        def p2 = new Project(name: 'test2', description: 'desc')
        p2.save()

        service.userSelectProject(session, p2)
        assertNotNull session.project
        assertEquals 'test2', session.project
    }

    void testCreateProject(){
        assertEquals 0,Project.list().size()

        def mockControl = mockFor(AclUtilService)
        //NB: 6 addPermission calls for the roles
        6.times{
            mockControl.demand.addPermission{project,username,perm-> }
        }
        service.aclUtilService=mockControl.createMock()
        def project = service.createProject('blah1', 'descript')
        assertNotNull(project)
        assertFalse(project.hasErrors())
        assertEquals('blah1',project.name)

        assertEquals 1, Project.list().size()
    }

    void testCreateProjectInvalid(){
        def p1 = new Project(name: 'test1', description: 'desc')
        p1.save()
        assertEquals 1,Project.list().size()

        def mockControl = mockFor(AclUtilService)
        //NB: 6 addPermission calls for the roles
        6.times {
            mockControl.demand.addPermission {project, username, perm -> }
        }
        service.aclUtilService = mockControl.createMock()

        def project = service.createProject('test1', 'descript')
        assertNotNull(project)
        assertTrue(project.hasErrors())
        assertTrue(project.errors.hasFieldErrors('name'))


        assertEquals 1, Project.list().size()
    }
}
