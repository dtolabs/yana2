package com.dtolabs



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ProjectController)
@Mock(Project)
class ProjectControllerTests {

    void testIndex() {
        controller.index()
        assertEquals '/project/list',response.redirectedUrl
    }
    void testList(){
        def strictControl = mockFor(ProjectService)
        strictControl.demand.listProjects(1..1) { -> return [] }
        controller.projectService=strictControl.createMock()
        def model=controller.list()
        assert model!=null
        assert model.projects==[]
        strictControl.verify()
    }
    void testList2(){
        def strictControl = mockFor(ProjectService)
        def project = new Project(name: 'bob', description: 'x')
        strictControl.demand.listProjects(1..1) { -> return [project] }
        controller.projectService=strictControl.createMock()
        def model=controller.list()
        assert model!=null
        assert model.projects==[project]
        strictControl.verify()
    }
    void testSelectNoProjectParam(){
        //no project parameter

        messageSource.addMessage("parameter.missing", request.locale, "Missing: {0}")
        controller.select()
        assert '/project/list'==response.redirectedUrl
        assert 'Missing: project'==request.message
    }
    void testSelectMissingProject(){
        //project not found
        messageSource.addMessage("default.not.found.message", request.locale, "NotFound: {0}")

        def strictControl = mockFor(ProjectService)
        strictControl.demand.findProject(1..1) {proj-> return null }
        controller.projectService = strictControl.createMock()

        controller.params.project='test1'
        controller.select()
        assert 404==response.status
        assert 'NotFound: test1' == response.text
    }

    void testSelect(){
        def strictControl = mockFor(ProjectService)
        def project = new Project(name: 'bob', description: 'x').save()
        strictControl.demand.findProject(1..1) {proj -> return project }
        strictControl.demand.userSelectProject(1..1) {session, pr -> assert pr==project  }
        controller.projectService = strictControl.createMock()
        controller.params.project = 'bob'
        controller.select()
        assert null==request.message, "unexpected message: "+request.message
        assert '/node/index' == response.redirectedUrl
        strictControl.verify()
    }

    void testCreate(){
        controller.create()
        assert view == null
        assert model.size()==0
    }
    void testCancel(){

        messageSource.addMessage("project.action.cancel.message", request.locale, "Cancel")
        controller.cancel()
        assert response.redirectedUrl == "/project/list"
        assert flash.message=='Cancel'
        assert model.size() == 0
    }
    void testSaveMissingParameter(){
        //no project parameter
        messageSource.addMessage("parameter.missing", request.locale, "Missing: {0}")
        controller.save()
        assert '/project/create' == view
        assert 'Missing: name'==request.message
    }
    void testSaveInvalidDesc(){
        //no project parameter

        def strictControl = mockFor(ProjectService)
        strictControl.demand.createProject(1..1) {pname,pdesc ->
            assert pname=='bob'
            assert pdesc==''
            def p=new Project(name:pname,description:pdesc)
            p.validate()
            return p
        }
        controller.projectService = strictControl.createMock()

        controller.params.name='bob'
        controller.params.description=''
        controller.save()
        assert '/project/create' == view
        assert model.project!=null
        strictControl.verify()
    }
    void testSaveValid(){
        //no project parameter
        messageSource.addMessage("default.created.message", request.locale, "Create message: {0} {1}")

        def strictControl = mockFor(ProjectService)
        strictControl.demand.createProject(1..1) {pname,pdesc ->
            assert pname=='bob'
            assert pdesc=='desc'
            def p=new Project(name:pname,description:pdesc)
            p.validate()
            return p
        }
        strictControl.demand.userSelectProject(1..1) {session,Project pname ->
            assert 'bob'==pname.name
        }
        controller.projectService = strictControl.createMock()

        controller.params.name='bob'
        controller.params.description='desc'
        controller.save()
        assert '/project/list' == response.redirectedUrl
        assert flash.message=='Create message: Project bob'
        strictControl.verify()
    }
}
