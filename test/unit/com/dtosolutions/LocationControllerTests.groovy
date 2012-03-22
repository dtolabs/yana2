package com.dtosolutions



import org.junit.*
import grails.test.mixin.*

@TestFor(LocationController)
@Mock(Location)
class LocationControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/location/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.locationInstanceList.size() == 0
        assert model.locationInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.locationInstance != null
    }

    void testSave() {
        controller.save()

        assert model.locationInstance != null
        assert view == '/location/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/location/show/1'
        assert controller.flash.message != null
        assert Location.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/location/list'


        populateValidParams(params)
        def location = new Location(params)

        assert location.save() != null

        params.id = location.id

        def model = controller.show()

        assert model.locationInstance == location
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/location/list'


        populateValidParams(params)
        def location = new Location(params)

        assert location.save() != null

        params.id = location.id

        def model = controller.edit()

        assert model.locationInstance == location
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/location/list'

        response.reset()


        populateValidParams(params)
        def location = new Location(params)

        assert location.save() != null

        // test invalid parameters in update
        params.id = location.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/location/edit"
        assert model.locationInstance != null

        location.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/location/show/$location.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        location.clearErrors()

        populateValidParams(params)
        params.id = location.id
        params.version = -1
        controller.update()

        assert view == "/location/edit"
        assert model.locationInstance != null
        assert model.locationInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/location/list'

        response.reset()

        populateValidParams(params)
        def location = new Location(params)

        assert location.save() != null
        assert Location.count() == 1

        params.id = location.id

        controller.delete()

        assert Location.count() == 0
        assert Location.get(location.id) == null
        assert response.redirectedUrl == '/location/list'
    }
}
