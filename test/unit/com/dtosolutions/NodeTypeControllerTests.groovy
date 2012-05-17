package com.dtosolutions



import org.junit.*
import grails.test.mixin.*

@TestFor(NodeTypeController)
//@Mock(NodeType)
@Mock([NodeType,Webhook,WebhookService,IconService])
class NodeTypeControllerTests{

	def iconService;
	def webhookService

    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
	  params["id"] = 1
	  params["version"] = 1
      params["name"] = 'nodetype_test'
	  params["dateModified"] = new Date()
    }

    void testIndex() {
        controller.index()
        assert "/nodeType/list" == response.redirectedUrl
    }

	/*
	 * need to fix for included service call
    void testList() {

        def model = controller.list()

        assert model.nodeTypeInstanceList.size() == 0
        assert model.nodeTypeInstanceTotal == 0
    }
    */

    void testSave () {
        controller.save()

        assert model.nodeTypeInstance != null
        assert view == '/nodeType/create'

        response.reset()

        populateValidParams(params)
        //controller.save()
		
        //assert response.redirectedUrl == '/nodeType/show/1'
       // assert controller.flash.message != null
       // assert NodeType.count() == 1
    }

    void testEdit() {
        //controller.edit()

        //assert flash.message != null
        //assert response.redirectedUrl == '/nodeType/list'


        populateValidParams(params)
        def nodeType = new NodeType(params)

        assert nodeType.save() != null

        params.id = nodeType.id

        //def model = controller.edit()

        //assert model.nodeTypeInstance == nodeType
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/nodeType/list'

        response.reset()


        populateValidParams(params)
        def nodeType = new NodeType(params)

		if(nodeType.save()){
			
			assert nodeType.save(flush:true) != null
			
			//FIX
			//controller.update()
			//params.id = nodeType.id
			//assert response.redirectedUrl == "/nodeType/show/$nodeType.id"
			//assert flash.message != null
		}else{
			// test invalid parameters in update
			//TODO: add invalid values to params object
			assert view == "/nodeType/edit"
			
		}

        //controller.update()
        //nodeType.clearErrors()
        //populateValidParams(params)


        //test outdated version number
        response.reset()
        nodeType.clearErrors()

        populateValidParams(params)
        params.id = nodeType.id
        params.version = -1
        //controller.update()

        //assert view == "/nodeType/edit"
        //assert model.nodeTypeInstance != null
        //assert model.nodeTypeInstance.errors.getFieldError('version')
        //assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/nodeType/list'

        response.reset()

        populateValidParams(params)
        def nodeType = new NodeType(params)

        assert nodeType.save() != null
        assert NodeType.count() == 1

        params.id = nodeType.id

        //controller.delete()

        //assert NodeType.count() == 0
        //assert NodeType.get(nodeType.id) == null
        //assert response.redirectedUrl == '/nodeType/list'
    }
}
