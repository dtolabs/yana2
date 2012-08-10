package com.dtolabs

import grails.test.mixin.TestFor
import grails.test.mixin.Mock


@TestFor(AttributeController)
@Mock([Attribute,WebhookService,Filter])
class AttributeControllerTests {

    void testList1() {
        def project = new Project(name: 'test1', description: 'desc')

        def control = mockFor(ProjectService)
        control.demand.findProject{name->
            assert name == project.name
            project
        }
        controller.projectService=control.createMock()

        params.project = project.name
        def model = controller.list()

        assert model.attributeInstanceList.size() == 0
    }

    void testList2() {
        def project = new Project(name: 'test1', description: 'desc')
        params.project = project.name

        def filter = new Filter(dataType:'String',regex:'^.*\$', project:project).save()
        def attr1 = new Attribute(name: "arch", filter: filter, project: project).save()


        def control = mockFor(ProjectService)
        control.demand.findProject{name->
            assert name == project.name
            project
        }
        controller.projectService=control.createMock()
        def model = controller.list()

        assert model.attributeInstanceList.size() == 1
        assert model.attributeInstanceList.get(0) == attr1

    }

    void testCreate() {
        def project = new Project(name: 'test1', description: 'desc')
        params.project = project.name

        def control = mockFor(ProjectService)
        control.demand.findProject{name->
            assert name == project.name
            project
        }
        controller.projectService=control.createMock()

       def model = controller.create()
       assert model.attributeInstance != null
    }




    void testShow1() {
        defineBeans {
            iconService(IconService)
        }

        def project = new Project(name: 'test1', description: 'desc')

        def control = mockFor(ProjectService)
        control.demand.findProject{name->
            assert name == project.name
            project
        }
        controller.projectService=control.createMock()

        params.project = project.name
        params.id = 123 // phony id

        def model = controller.show()


        assert null == model
    }


    void testShow2() {
        defineBeans {
            iconService(IconService)
        }

        def project = new Project(name: 'test1', description: 'desc')
        def filter = new Filter(dataType:'String',regex:'^.*\$', project: project).save()
        def attr1 = new Attribute(name: "arch", filter: filter,  project: project).save()


        def control = mockFor(ProjectService)
        control.demand.findProject{name->
            assert name == project.name
            project
        }
        controller.projectService=control.createMock()

        params.project = project.name
        params.id = attr1.id

        def model = controller.show()


        assert model.attributeInstance == attr1
    }

    void testDelete() {
        def project = new Project(name: 'test1', description: 'desc')
        def filter = new Filter(dataType:'String',regex:'^.*\$', project:project).save()
        def attr1 = new Attribute(name: "arch", filter: filter,  project: project).save()


        def control = mockFor(ProjectService)
        control.demand.findProject{name->
            assert name == project.name
            project
        }
        controller.projectService=control.createMock()

        params.project = project.name
        params.id = attr1.id

        controller.delete()

        assert 0 == Attribute.count()

        assert response.redirectedUrl == '/attribute/list'
    }
}
