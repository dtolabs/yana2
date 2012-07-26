package yana2



import grails.test.mixin.*
import com.dtolabs.ProjectController

@TestFor(ProjectController)
@Mock(ProjectFilters)
class ProjectFiltersTests {

    void testProjectNotRedirected() {
        withFilters(action: "list") {
            controller.create()
        }
        assert response.redirectedUrl == null
    }

    void testOtherIsRedirected() {
        session.project=null
        withFilters(controller:'node',action: "list") {
            controller.create()
        }
        assert response.redirectedUrl == '/project/list?mustChoose=1'
    }
    void testOtherIsNotRedirectedWithSession() {
        session.project="something"
        withFilters(controller:'node',action: "list") {
            controller.create()
        }
        assert response.redirectedUrl == null
    }
}
