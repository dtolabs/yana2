package com.dtosolutions



import org.junit.*
import grails.test.mixin.*

@TestFor(NodeController)
@Mock(Node)
class NodeControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
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
        controller.save()

        assert model.nodeInstance != null
        assert view == '/node/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/node/show/1'
        assert controller.flash.message != null
        assert Node.count() == 1
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

        assert node.save() != null

        // test invalid parameters in update
        params.id = node.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/node/edit"
        assert model.nodeInstance != null

        node.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/node/show/$node.id"
        assert flash.message != null

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
