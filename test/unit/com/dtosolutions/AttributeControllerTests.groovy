package com.dtosolutions



import org.junit.*
import grails.test.mixin.*

@TestFor(AttributeController)
@Mock(Attribute)
class AttributeControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/attribute/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.attributeInstanceList.size() == 0
        assert model.attributeInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.attributeInstance != null
    }

    void testSave() {
        controller.save()

        assert model.attributeInstance != null
        assert view == '/attribute/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/attribute/show/1'
        assert controller.flash.message != null
        assert Attribute.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/attribute/list'


        populateValidParams(params)
        def attribute = new Attribute(params)

        assert attribute.save() != null

        params.id = attribute.id

        def model = controller.show()

        assert model.attributeInstance == attribute
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/attribute/list'


        populateValidParams(params)
        def attribute = new Attribute(params)

        assert attribute.save() != null

        params.id = attribute.id

        def model = controller.edit()

        assert model.attributeInstance == attribute
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/attribute/list'

        response.reset()


        populateValidParams(params)
        def attribute = new Attribute(params)

        assert attribute.save() != null

        // test invalid parameters in update
        params.id = attribute.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/attribute/edit"
        assert model.attributeInstance != null

        attribute.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/attribute/show/$attribute.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        attribute.clearErrors()

        populateValidParams(params)
        params.id = attribute.id
        params.version = -1
        controller.update()

        assert view == "/attribute/edit"
        assert model.attributeInstance != null
        assert model.attributeInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/attribute/list'

        response.reset()

        populateValidParams(params)
        def attribute = new Attribute(params)

        assert attribute.save() != null
        assert Attribute.count() == 1

        params.id = attribute.id

        controller.delete()

        assert Attribute.count() == 0
        assert Attribute.get(attribute.id) == null
        assert response.redirectedUrl == '/attribute/list'
    }
}
