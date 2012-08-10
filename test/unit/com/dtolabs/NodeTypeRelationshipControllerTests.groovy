package com.dtolabs

import grails.test.mixin.Mock
import grails.test.mixin.TestFor


@TestFor(NodeTypeRelationshipController)
@Mock([NodeTypeRelationship,Project,NodeType])
class NodeTypeRelationshipControllerTests {


    void testList1() {
        def project = new Project(name: 'test1', description: 'desc').save()

        def control = mockFor(ProjectService)
        control.demand.findProject{name->
            assert name == project.name
            project
        }
        controller.projectService=control.createMock()

        params.project = project.name

        def model = controller.list()

        assert model.nodeTypeRelationshipInstanceList.size() == 0
        assert model.nodeTypeRelationshipInstanceTotal == 0
    }



    void testShow() {
        defineBeans {
            iconService(IconService)
        }

        def project = new Project(name: 'test1', description: 'desc').save()
        def parent =  new NodeType(name:  "typeA", description: "desc", project: project).save()
        def child =   new NodeType(name:  "typeB", description: "desc", project:  project).save()

        def relationship = new NodeTypeRelationship(name: "rel1", parent: parent, child: child )
        assert relationship.validate()
        assert null != relationship.save()


        def control = mockFor(ProjectService)
        control.demand.findProject{name->
            assert name == project.name
            project
        }
        controller.projectService=control.createMock()

        params.id = relationship.id
        params.project = project.id

        def model = controller.show()

        assert model.nodeTypeRelationshipInstance == relationship
    }



    void testDelete() {
        def project = new Project(name: 'test1', description: 'desc').save()
        def parent = new NodeType(name: "typeA", description: "desc", project:  project).save()
        def child = new NodeType(name:  "typeB", description: "desc", project:  project).save()

        def rel1 = new NodeTypeRelationship(name: "rel1", parent: parent, child: child ).save()

        def control = mockFor(ProjectService)
        control.demand.findProject{name->
            assert name == project.name
            project
        }
        controller.projectService=control.createMock()

        params.id = rel1.id
        params.project = project.name

        controller.delete()

        assert NodeTypeRelationship.count() == 0
        assert NodeTypeRelationship.get(rel1.id) == null
        assert response.redirectedUrl == '/nodeTypeRelationship/list'
    }
}
