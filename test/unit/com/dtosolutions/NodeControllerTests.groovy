package com.dtosolutions



import org.junit.*
import grails.test.mixin.*

@TestFor(NodeController)
@Mock(Node)
class NodeControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
	  Date now = new Date()
	  mockDomain(NodeType, [new NodeType(id:1,version:1,name:'Server',dateCreated:now)])
	  NodeType server = NodeType.get(1)
	  mockDomain(Template, [new Template(id:1,version:1,templateName:'Server_default',nodetype:server)])
	  
	  params["id"] = 1
	  params["version"] = 1
      params["name"] = 'node_name'
	  params["description"] = "some description"
	  params["template"] = Template.get(1)
	  params["status"] = Status.IMP
	  params["importance"] = Importance.MED
	  params["tags"] = "this,is,a,test"
	  params["nodetype"] = server
	  params["dateCreated"] = new Date()
	  params["dateModified"] = new Date()
	  params["parent"] = null
    }

    void testIndex() {
        controller.index()
        assert "/node/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.nodeInstanceList.size() == 0
        assert model.nodeInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.nodeInstance != null
    }

    void testSave() {
		populateValidParams(params)
        //controller.save()
		
		def node = new Node(params)
		if(node.save()){
			assert node.save(flush:true) != null
			
			controller.update()
			params.id = node.id
			assert response.redirectedUrl == "/node/show/${node.id}"
			assert controller.flash.message != null
			assert Node.count() == 1
		}else{
			assert view == '/node/create'
		}

        //response.reset()

        //populateValidParams(params)
        //controller.save()

        //assert response.redirectedUrl == '/node/show/1'
        //assert controller.flash.message != null
        //assert Node.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/node/list'


        populateValidParams(params)
        def node = new Node(params)

        assert node.save() != null

        params.id = node.id

        def model = controller.show()

        assert model.nodeInstance == node
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/node/list'


        populateValidParams(params)
        def node = new Node(params)

        assert node.save() != null

        params.id = node.id

        def model = controller.edit()

        assert model.nodeInstance == node
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/node/list'

        response.reset()


        populateValidParams(params)
        def node = new Node(params)

		if(node.save()){
			
			assert node.save(flush:true) != null
			
			//FIX
			controller.update()
			params.id = node.id
			assert response.redirectedUrl == "/node/show/${node.id}"
			assert flash.message != null
		}else{
			// test invalid parameters in update
			//TODO: add invalid values to params object
			assert view == "/node/edit"
			
		}

        controller.update()
        node.clearErrors()
        populateValidParams(params)
		

        //test outdated version number
        response.reset()
        node.clearErrors()

        populateValidParams(params)
        params.id = node.id
        params.version = -1
        controller.update()

        assert view == "/node/edit"
        assert model.nodeInstance != null
        assert model.nodeInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/node/list'

        response.reset()

        populateValidParams(params)
        def node = new Node(params)

        assert node.save() != null
        assert Node.count() == 1

        params.id = node.id

        controller.delete()

        assert Node.count() == 0
        assert Node.get(node.id) == null
        assert response.redirectedUrl == '/node/list'
    }
}
