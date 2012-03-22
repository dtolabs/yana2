package com.dtosolutions



import org.junit.*
import grails.test.mixin.*

@TestFor(SolutionController)
@Mock(Solution)
class SolutionControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/solution/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.solutionInstanceList.size() == 0
        assert model.solutionInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.solutionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.solutionInstance != null
        assert view == '/solution/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/solution/show/1'
        assert controller.flash.message != null
        assert Solution.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/solution/list'


        populateValidParams(params)
        def solution = new Solution(params)

        assert solution.save() != null

        params.id = solution.id

        def model = controller.show()

        assert model.solutionInstance == solution
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/solution/list'


        populateValidParams(params)
        def solution = new Solution(params)

        assert solution.save() != null

        params.id = solution.id

        def model = controller.edit()

        assert model.solutionInstance == solution
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/solution/list'

        response.reset()


        populateValidParams(params)
        def solution = new Solution(params)

        assert solution.save() != null

        // test invalid parameters in update
        params.id = solution.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/solution/edit"
        assert model.solutionInstance != null

        solution.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/solution/show/$solution.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        solution.clearErrors()

        populateValidParams(params)
        params.id = solution.id
        params.version = -1
        controller.update()

        assert view == "/solution/edit"
        assert model.solutionInstance != null
        assert model.solutionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/solution/list'

        response.reset()

        populateValidParams(params)
        def solution = new Solution(params)

        assert solution.save() != null
        assert Solution.count() == 1

        params.id = solution.id

        controller.delete()

        assert Solution.count() == 0
        assert Solution.get(solution.id) == null
        assert response.redirectedUrl == '/solution/list'
    }
}
