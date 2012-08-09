package yana2



import grails.test.mixin.*
import com.dtolabs.Project
import com.dtolabs.NodeController
import org.junit.Before
import org.springframework.context.MessageSource
import org.springframework.context.MessageSourceResolvable
import com.dtolabs.ProjectService
import com.dtolabs.ProjectController
import grails.artefact.Artefact
import com.dtolabs.yana2.springacl.DefaultProjectAccess
import com.dtolabs.yana2.springacl.ProjectAccess

@TestFor(TestController)
@Mock([Project,ProjectAccessFilters,ProjectFilters])
class ProjectAccessFiltersTests {
    TestController testcontroller

    @Before
    void setUp() {

        testcontroller = mockController(TestController)

    }

    void testProjectNotFound() {
        defineBeans {
            projectService(MockedProjectServiceNotFound)
            messageSource(MockedMessageSource)
        }
        session.project = 'something'
        withFilters(controller: 'test', action: "index") {
            controller.index()
        }
        assert response.redirectedUrl == '/errors/error404'
        assert flash.message == 'default.not.found.message'
    }


    void testProjectFound() {
        defineBeans {
            projectService(MockedProjectServiceFound)
            messageSource(MockedMessageSource)
        }

        session.project = "something"
        withFilters(controller: 'test', action: "index") {
            testcontroller.index()
        }
        assert response.redirectedUrl == null
        assert view == '/test/index.gsp'
        assert flash.message == null
    }
    void testProjectFoundNoAccess() {
        defineBeans {
            projectService(MockedProjectServiceFound)
            messageSource(MockedMessageSource)
        }

        session.project = "something"
        withFilters(controller: 'test', action: "architect") {
            testcontroller.architect()
        }
        assert flash.message == 'springSecurity.denied.message'
        assert response.redirectedUrl == '/login/denied'
    }
    void testProjectFoundHasAccess() {
        defineBeans {
            projectService(MockedProjectServiceFound)
            messageSource(MockedMessageSource)
        }

        session.project = "something"
        withFilters(controller: 'test', action: "operator") {
            testcontroller.operator()
        }
        assert flash.message == null
        assert response.redirectedUrl == null
        assert view == '/test/operator.gsp'
    }
}

@Artefact("Controller")
@DefaultProjectAccess(ProjectAccess.Level.read)
class TestController {

    def index() {}
    @ProjectAccess(ProjectAccess.Level.operator)
    def operator(){

    }
    @ProjectAccess(ProjectAccess.Level.architect)
    def architect(){

    }
}

class MockedMessageSource implements MessageSource {
    String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return code
    }

    String getMessage(String code, Object[] objects, Locale locale) {
        return code
    }

    String getMessage(MessageSourceResolvable resolvable, Locale locale) {
        return 'message'
    }
}
class MockedProjectServiceFound extends ProjectService {
    Project findProject(String name) {
        println('FindProject ' + name)
        assert name == 'something'
        def project = new Project(name: 'something', description: 'blah').save()
        return project
    }

    boolean authorizedArchitectPermission(Project p) {
        println('authorized architect')
        return false;
    }
    boolean authorizedOperatorPermission(Project p) {
        println('authorized operator')
        return true;
    }
}
class MockedProjectServiceNotFound extends ProjectService {
    Project findProject(String name) {
        println('MockedProjectServiceNotFound ' + name)
        assert name == 'something'
        null
    }
}