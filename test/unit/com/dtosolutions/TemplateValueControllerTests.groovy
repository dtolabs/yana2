package com.dtosolutions



import org.junit.*
import grails.test.mixin.*

@TestFor(TemplateValueController)
@Mock(TemplateValue)
class TemplateValueControllerTests {


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
	  mockDomain(TemplateAttribute, [new TemplateAttribute(id:1,version:1,attribute:att,template:server,required:'false',dateCreated:now)])
	  TemplateAttribute tatt = TemplateAttribute.get(1)
	  mockDomain(Node, [new Node(id:1,version:1,name:'test.server.com',description:'this is a description',status:com.dtosolutions.Status.DEV,tags:"this,is,a,tag",nodetype:server,dateCreated:now)])
	  Node serverNode = Node.get(1)
	  
	  params["id"] = 1
	  params["version"] = 1
      params["node"] = serverNode
	  params["templateattribute"] = tatt
	  params["value"] = 'testamundo'
    }

    void testIndex() {
        controller.index()
        assert "/templateValue/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.templateValueInstanceList.size() == 0
        assert model.templateValueInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.templateValueInstance != null
    }

    void testSave() {
        controller.save()

        assert model.templateValueInstance != null
        assert view == '/templateValue/create'

        response.reset()

        populateValidParams(params)
        //controller.save()

        //assert response.redirectedUrl == '/templateValue/show/1'
        //assert controller.flash.message != null
        //assert TemplateValue.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/templateValue/list'


        populateValidParams(params)
        def templateValue = new TemplateValue(params)

        assert templateValue.save() != null

        params.id = templateValue.id

        def model = controller.show()

        assert model.templateValueInstance == templateValue
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/templateValue/list'


        populateValidParams(params)
        def templateValue = new TemplateValue(params)

        assert templateValue.save() != null

        params.id = templateValue.id

        def model = controller.edit()

        assert model.templateValueInstance == templateValue
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/templateValue/list'

        response.reset()


        populateValidParams(params)
        def templateValue = new TemplateValue(params)

        assert templateValue.save() != null

        // test invalid parameters in update
        params.id = templateValue.id
        //TODO: add invalid values to params object

        //controller.update()

        //assert view == "/templateValue/edit"
        //assert model.templateValueInstance != null

        templateValue.clearErrors()

        populateValidParams(params)
        //controller.update()

        //assert response.redirectedUrl == "/templateValue/show/$templateValue.id"
        //assert flash.message != null

        //test outdated version number
        response.reset()
        templateValue.clearErrors()

        populateValidParams(params)
        params.id = templateValue.id
        params.version = -1
        //controller.update()

        //assert view == "/templateValue/edit"
        //assert model.templateValueInstance != null
        //assert model.templateValueInstance.errors.getFieldError('version')
       // assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/templateValue/list'

        response.reset()

        populateValidParams(params)
        def templateValue = new TemplateValue(params)

        assert templateValue.save() != null
        assert TemplateValue.count() == 1

        params.id = templateValue.id

        //controller.delete()

        //assert TemplateValue.count() == 0
        //assert TemplateValue.get(templateValue.id) == null
        //assert response.redirectedUrl == '/templateValue/list'
    }
}
