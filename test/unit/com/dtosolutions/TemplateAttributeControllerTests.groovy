package com.dtosolutions

import org.junit.*
import grails.test.mixin.*

@TestFor(TemplateAttributeController)
@Mock(TemplateAttribute)
class TemplateAttributeControllerTests {

    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
	  Date now = new Date()
	  mockDomain(Filter, [new Filter(id:1,version:1,dataType:'String',regex:'^.*\$',dateCreated:now)])
	  Filter fStr = Filter.get(1)
	  mockDomain(Attribute, [new Attribute(id:1,version:1,name:'Friendly_Name',filter:fStr,dateCreated:now)])
	  mockDomain(NodeType, [new NodeType(id:1,version:1,name:'Server',dateCreated:now)])
	  NodeType server = NodeType.get(1)
	  
	  params["id"] = 1
	  params["version"] = 1
      params["attribute"] = Attribute.get(1)
	  params["template"] = NodeType.get(1)
	  params["required"] = 'false'
    }

    void testIndex() {
        controller.index()
        assert "/templateAttribute/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.templateAttributeInstanceList.size() == 0
        assert model.templateAttributeInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.templateAttributeInstance != null
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/templateAttribute/list'


        populateValidParams(params)
        def templateAttribute = new TemplateAttribute(params)

        assert templateAttribute.save() != null

        params.id = templateAttribute.id

        def model = controller.show()

        assert model.templateAttributeInstance == templateAttribute
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/templateAttribute/list'


        populateValidParams(params)
        def templateAttribute = new TemplateAttribute(params)

        assert templateAttribute.save() != null

        params.id = templateAttribute.id

        def model = controller.edit()

        assert model.templateAttributeInstance == templateAttribute
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/templateAttribute/list'

        response.reset()


        populateValidParams(params)
        def templateAttribute = new TemplateAttribute(params)

		
		if(templateAttribute.save()){
			
			assert templateAttribute.save(flush:true) != null
			
			//FIX
			//controller.update()
			//params.id = templateAttribute.id
			//assert response.redirectedUrl == "/templateAttribute/show/$templateAttribute.id"
			//assert flash.message != null
		}else{
			// test invalid parameters in update
			//TODO: add invalid values to params object
			assert view == "/templateAttribute/edit"
			
		}

		//controller.update()
		//templateAttribute.clearErrors()
		//populateValidParams(params)
		
        //test outdated version number
        response.reset()
        templateAttribute.clearErrors()

        populateValidParams(params)
        params.id = templateAttribute.id
        params.version = -1
        //controller.update()

        //assert view == "/templateAttribute/edit"
        //assert model.templateAttributeInstance != null
        //assert model.templateAttributeInstance.errors.getFieldError('version')
       // assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/templateAttribute/list'

        response.reset()

        populateValidParams(params)
        def templateAttribute = new TemplateAttribute(params)

        assert templateAttribute.save() != null
        assert TemplateAttribute.count() == 1

        params.id = templateAttribute.id

        //controller.delete()

        //assert TemplateAttribute.count() == 0
        //assert TemplateAttribute.get(templateAttribute.id) == null
        //assert response.redirectedUrl == '/templateAttribute/list'
    }
}
