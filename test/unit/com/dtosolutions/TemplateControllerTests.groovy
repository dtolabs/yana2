package com.dtosolutions



import org.junit.*
import grails.test.mixin.*

@TestFor(TemplateController)
@Mock(Template)
class TemplateControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/template/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.templateInstanceList.size() == 0
        assert model.templateInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.templateInstance != null
    }

    void testSave() {
        controller.save()

        assert model.templateInstance != null
        assert view == '/template/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/template/show/1'
        assert controller.flash.message != null
        assert Template.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/template/list'


        populateValidParams(params)
        def template = new Template(params)

        assert template.save() != null

        params.id = template.id

        def model = controller.show()

        assert model.templateInstance == template
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/template/list'


        populateValidParams(params)
        def template = new Template(params)

        assert template.save() != null

        params.id = template.id

        def model = controller.edit()

        assert model.templateInstance == template
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/template/list'

        response.reset()


        populateValidParams(params)
        def template = new Template(params)

        assert template.save() != null

        // test invalid parameters in update
        params.id = template.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/template/edit"
        assert model.templateInstance != null

        template.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/template/show/$template.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        template.clearErrors()

        populateValidParams(params)
        params.id = template.id
        params.version = -1
        controller.update()

        assert view == "/template/edit"
        assert model.templateInstance != null
        assert model.templateInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/template/list'

        response.reset()

        populateValidParams(params)
        def template = new Template(params)

        assert template.save() != null
        assert Template.count() == 1

        params.id = template.id

        controller.delete()

        assert Template.count() == 0
        assert Template.get(template.id) == null
        assert response.redirectedUrl == '/template/list'
    }
}
