package com.dtosolutions



import org.junit.*
import grails.test.mixin.*

@TestFor(NodeTypeRelationshipController)
@Mock(NodeTypeRelationship)
class NodeTypeRelationshipControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...

	  Date now = new Date()
	  mockDomain(NodeType, [new NodeType(id:1,version:1,name:'Server',dateCreated:now)])
	  NodeType server = NodeType.get(1)
	  
	  mockDomain(NodeType, [new NodeType(id:2,version:1,name:'Software',dateCreated:now)])
	  NodeType software = NodeType.get(2)
	  
	  params["id"] = 1
	  params["version"] = 1
	  params["roleName"] = "softwareInstallation"
	  params["parent"] = server
	  params["child"] = software
	  
    }

    void testIndex() {
        controller.index()
        assert "/nodeTypeRelationship/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.nodeTypeRelationshipInstanceList.size() == 0
        assert model.nodeTypeRelationshipInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.nodeTypeRelationshipInstance != null
    }

    void testSave() {
        controller.save()

        assert model.nodeTypeRelationshipInstance != null
        assert view == '/nodeTypeRelationship/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/nodeTypeRelationship/show/1'
        assert controller.flash.message != null
        assert NodeTypeRelationship.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/nodeTypeRelationship/list'


        populateValidParams(params)
        def nodeTypeRelationship = new NodeTypeRelationship(params)

        assert nodeTypeRelationship.save() != null

        params.id = nodeTypeRelationship.id

        def model = controller.show()

        assert model.nodeTypeRelationshipInstance == nodeTypeRelationship
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/nodeTypeRelationship/list'


        populateValidParams(params)
        def nodeTypeRelationship = new NodeTypeRelationship(params)

        assert nodeTypeRelationship.save() != null

        params.id = nodeTypeRelationship.id

        def model = controller.edit()

        assert model.nodeTypeRelationshipInstance == nodeTypeRelationship
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/nodeTypeRelationship/list'

        response.reset()


        populateValidParams(params)
        def nodeTypeRelationship = new NodeTypeRelationship(params)

		
		
		if(nodeTypeRelationship.save()){
			assert nodeTypeRelationship.save(flush:true) != null
			
			//FIX
			controller.update()
			params.id = nodeTypeRelationship.id
			assert response.redirectedUrl == "/nodeTypeRelationship/show/${nodeTypeRelationship.id}"
			assert flash.message != null
		}else{
			// test invalid parameters in update
			//TODO: add invalid values to params object
			assert view == "/nodeTypeRelationship/edit"
		}

        controller.update()
        nodeTypeRelationship.clearErrors()
        populateValidParams(params)
		

        //test outdated version number
        response.reset()
        nodeTypeRelationship.clearErrors()

        populateValidParams(params)
        params.id = nodeTypeRelationship.id
        params.version = -1
        controller.update()

        assert view == "/nodeTypeRelationship/edit"
        assert model.nodeTypeRelationshipInstance != null
        assert model.nodeTypeRelationshipInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/nodeTypeRelationship/list'

        response.reset()

        populateValidParams(params)
        def nodeTypeRelationship = new NodeTypeRelationship(params)

        assert nodeTypeRelationship.save() != null
        assert NodeTypeRelationship.count() == 1

        params.id = nodeTypeRelationship.id

        controller.delete()

        assert NodeTypeRelationship.count() == 0
        assert NodeTypeRelationship.get(nodeTypeRelationship.id) == null
        assert response.redirectedUrl == '/nodeTypeRelationship/list'
    }
}
