package com.dtosolutions



import org.junit.*
import grails.test.mixin.*

@TestFor(FilterController)
@Mock(Filter)
class FilterControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/filter/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.filterInstanceList.size() == 0
        assert model.filterInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.filterInstance != null
    }

    void testSave() {
        controller.save()

        assert model.filterInstance != null
        assert view == '/filter/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/filter/show/1'
        assert controller.flash.message != null
        assert Filter.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/filter/list'


        populateValidParams(params)
        def filter = new Filter(params)

        assert filter.save() != null

        params.id = filter.id

        def model = controller.show()

        assert model.filterInstance == filter
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/filter/list'


        populateValidParams(params)
        def filter = new Filter(params)

        assert filter.save() != null

        params.id = filter.id

        def model = controller.edit()

        assert model.filterInstance == filter
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/filter/list'

        response.reset()


        populateValidParams(params)
        def filter = new Filter(params)

        assert filter.save() != null

        // test invalid parameters in update
        params.id = filter.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/filter/edit"
        assert model.filterInstance != null

        filter.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/filter/show/$filter.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        filter.clearErrors()

        populateValidParams(params)
        params.id = filter.id
        params.version = -1
        controller.update()

        assert view == "/filter/edit"
        assert model.filterInstance != null
        assert model.filterInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/filter/list'

        response.reset()

        populateValidParams(params)
        def filter = new Filter(params)

        assert filter.save() != null
        assert Filter.count() == 1

        params.id = filter.id

        controller.delete()

        assert Filter.count() == 0
        assert Filter.get(filter.id) == null
        assert response.redirectedUrl == '/filter/list'
    }
}
