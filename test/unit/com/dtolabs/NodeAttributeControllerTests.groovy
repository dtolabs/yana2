package com.dtolabs


@TestFor(NodeAttributeController)
@Mock(NodeAttribute)
class NodeAttributeControllerTests {

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
        assert "/nodeAttribute/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.nodeAttributeInstanceList.size() == 0
        assert model.nodeAttributeInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.nodeAttributeInstance != null
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/nodeAttribute/list'


        populateValidParams(params)
        def nodeAttribute = new NodeAttribute(params)

        assert nodeAttribute.save() != null

        params.id = nodeAttribute.id

        def model = controller.show()

        assert model.nodeAttributeInstance == nodeAttribute
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/nodeAttribute/list'


        populateValidParams(params)
        def nodeAttribute = new NodeAttribute(params)

        assert nodeAttribute.save() != null

        params.id = nodeAttribute.id

        def model = controller.edit()

        assert model.nodeAttributeInstance == nodeAttribute
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/nodeAttribute/list'

        response.reset()


        populateValidParams(params)
        def nodeAttribute = new NodeAttribute(params)

		
		if(nodeAttribute.save()){
			
			assert nodeAttribute.save(flush:true) != null
			
			//FIX
			controller.update()
			params.id = nodeAttribute.id
			assert response.redirectedUrl == "/nodeAttribute/show/$nodeAttribute.id"
			assert flash.message != null
		}else{
			// test invalid parameters in update
			//TODO: add invalid values to params object
			assert view == "/nodeAttribute/edit"
			
		}

		//controller.update()
		//nodeAttribute.clearErrors()
		//populateValidParams(params)
		
        //test outdated version number
        response.reset()
        nodeAttribute.clearErrors()

        populateValidParams(params)
        params.id = nodeAttribute.id
        params.version = -1
        controller.update()

        assert view == "/nodeAttribute/edit"
        assert model.nodeAttributeInstance != null
        assert model.nodeAttributeInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/nodeAttribute/list'

        response.reset()

        populateValidParams(params)
        def nodeAttribute = new NodeAttribute(params)

        assert nodeAttribute.save() != null
        assert NodeAttribute.count() == 1

        params.id = nodeAttribute.id

        controller.delete()

        assert NodeAttribute.count() == 0
        assert NodeAttribute.get(nodeAttribute.id) == null
    }
}
