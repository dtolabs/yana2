package com.dtolabs

import grails.test.mixin.TestFor
import grails.test.mixin.Mock


@TestFor(FilterController)
@Mock([Project,Filter])
class FilterControllerTests{


    void testIndex() {
        controller.index()
        assert "/filter/list" == response.redirectedUrl
    }

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

        assert model.filterInstanceList.size() == 0
        assert model.filterInstanceTotal == 0
    }




    void testShow() {
        defineBeans {
            iconService(IconService)
        }
        def project = new Project(name: 'test1', description: 'desc')
        def filter = new Filter(dataType:'String',regex:'^.*\$', project:project).save()

        def control = mockFor(ProjectService)
        control.demand.findProject{name->
            assert name == project.name
            project
        }
        controller.projectService=control.createMock()

        params.project = project.name
        params.id = filter.id

        def model = controller.show()

        assert model.filterInstance == filter
    }


}
