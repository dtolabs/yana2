package com.dtolabs


@TestFor(NodeValueController)
@Mock(NodeValue)
class NodeValueControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
	  
	  Date now = new Date()
	  mockDomain(Filter, [new Filter(id:1,version:1,dataType:'String',regex:'^.*\$',dateCreated:now)])
	  Filter fStr = Filter.get(1)
	  mockDomain(Attribute, [new Attribute(id:1,version:1,name:'Friendly_Name',filter:fStr,dateCreated:now)])
	  Attribute att = Attribute.get(1)
	  mockDomain(NodeType, [new NodeType(id:1,version:1,name:'Server',dateCreated:now)])
	  NodeType server = NodeType.get(1)
	  mockDomain(NodeAttribute, [new NodeAttribute(id:1,version:1,attribute:att,template:server,required:'false',dateCreated:now)])
	  NodeAttribute tatt = NodeAttribute.get(1)
	  mockDomain(Node, [new Node(id:1,version:1,name:'test.server.com',description:'this is a description',status:com.dtolabs.Status.DEV,tags:"this,is,a,tag",nodetype:server,dateCreated:now)])
	  Node serverNode = Node.get(1)
	  
	  params["id"] = 1
	  params["version"] = 1
      params["node"] = serverNode
	  params["nodeattribute"] = tatt
	  params["value"] = 'testamundo'
    }

    void testIndex() {
        controller.index()
        assert "/nodeValue/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.nodeValueInstanceList.size() == 0
        assert model.nodeValueInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.nodeValueInstance != null
    }

    void testSave() {
        controller.save()

        assert model.nodeValueInstance != null
        assert view == '/nodeValue/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/nodeValue/show/1'
        assert controller.flash.message != null
        assert NodeValue.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/nodeValue/list'


        populateValidParams(params)
        def nodeValue = new NodeValue(params)

        assert nodeValue.save() != null

        params.id = nodeValue.id

        def model = controller.show()

        assert model.nodeValueInstance == nodeValue
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/nodeValue/list'


        populateValidParams(params)
        def nodeValue = new NodeValue(params)

        assert nodeValue.save() != null

        params.id = nodeValue.id

        def model = controller.edit()

        assert model.nodeValueInstance == nodeValue
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/nodeValue/list'

        response.reset()


        populateValidParams(params)
        def nodeValue = new NodeValue(params)

        assert nodeValue.save() != null

        // test invalid parameters in update
        params.id = nodeValue.id
        //TODO: add invalid values to params object

        //controller.update()

        //assert view == "/nodeValue/edit"
        //assert model.nodeValueInstance != null

        nodeValue.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/nodeValue/show/$nodeValue.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        nodeValue.clearErrors()

        populateValidParams(params)
        params.id = nodeValue.id
        params.version = -1
        controller.update()

        assert view == "/nodeValue/edit"
        assert model.nodeValueInstance != null
        assert model.nodeValueInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/nodeValue/list'

        response.reset()

        populateValidParams(params)
        def nodeValue = new NodeValue(params)

        assert nodeValue.save() != null
        assert NodeValue.count() == 1

        params.id = nodeValue.id

        controller.delete()

        assert NodeValue.count() == 0
        assert NodeValue.get(nodeValue.id) == null
        assert response.redirectedUrl == '/nodeValue/list'
    }
}
