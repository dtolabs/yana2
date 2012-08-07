package com.dtolabs

import com.dtolabs.ChildNode
import com.dtolabs.Node
import com.dtolabs.NodeTypeRelationship
import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured
import com.dtolabs.yana2.springacl.ProjectAccess
import com.dtolabs.yana2.springacl.DefaultProjectAccess

@Secured(['ROLE_YANA_ADMIN', 'ROLE_YANA_ARCHITECT', 'ROLE_YANA_SUPERUSER'])
@DefaultProjectAccess(ProjectAccess.Level.operator)
class ChildNodeController {

    def iconService
    def xmlService
    def jsonService
    def projectService

    static defaultAction = "list"
    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @ProjectAccess(ProjectAccess.Level.read)
    def api() {
        switch (request.method) {
            case "POST":
                def project = projectService.findProject(params.project)
                if (!project) {
                    response.status = 404
                    break
                }
                if (!projectService.authorizedOperatorPermission(project)) {
                    break
                }
                def json = request.JSON
                this.save()
                break
            case "GET":
                def json = request.JSON
                this.show()
                break
            case "PUT":
                def json = request.JSON
                this.update()
                break
            case "DELETE":

                def project = projectService.findProject(params.project)
                if (!project) {
                    response.status = 404
                    break
                }
                if (!projectService.authorizedOperatorPermission(project)) {
                    break
                }
                def json = request.JSON
                if (params.id) {
                    def cnode = ChildNode.get(params.id)
                    if (cnode) {
                        try {
                            cnode.delete(flush: true)
                            response.status = 200
                            render "Successfully Deleted."
                        } catch (org.springframework.dao.DataIntegrityViolationException e) {
                            ChildNode.withSession { session ->
                                session.clear()
                            }

                            response.status = 400 //Bad Request
                            render "Referential Integrity Violation: Please remove/reassign all Node relationships first."
                        }
                    } else {
                        response.status = 404 //Not Found
                        render "${params.id} not found."
                    }
                } else {
                    response.status = 400 //Bad Request
                    render """DELETE request must include the id"""
                }
                break
        }
        return
    }

    @ProjectAccess(ProjectAccess.Level.read)
    def listapi() {
        switch (request.method) {
            case "POST":
                def json = request.JSON
                this.list()
                break
        }
        return
    }

    @ProjectAccess(ProjectAccess.Level.read)
    def list() {
        if (params.format && params.format != 'none') {
            ArrayList cnodes = ChildNode.list()
            switch (params.format.toLowerCase()) {
                case 'xml':
                    def xml = xmlService.formatChildNodes(cnodes)
                    render(text: xml, contentType: "text/xml")
                    break;
                case 'json':
                    def json = jsonService.formatChildNodes(cnodes)
                    render(text: json, contentType: "text/json")
                    break;
            }
        } else {
            params.max = Math.min(params.max ? params.int('max') : 10, 100)
            [childNodeInstanceList: ChildNode.list(params), childNodeInstanceTotal: ChildNode.count()]
        }
    }

    def create() {
        [childNodeInstance: new ChildNode(params)]
    }

    def save() {
        Node parent = Node.get(params.parent)
        Node child = Node.get(params.child)

        def ntparents = NodeTypeRelationship.findByChild(child.nodetype)
        def nparents = []
        ntparents.each {ntp ->
            nparents += ntp.parent
        }
        def ntchildren = NodeTypeRelationship.findByParent(parent.nodetype)
        def nchildren = []
        ntchildren.each {ntc ->
            nchildren += ntc.child
        }

        def exists = ChildNode.findByParentAndChild(parent, child)
        def childNodeInstance
        if (!exists) {
            if (nparents.contains(parent.nodetype) && nchildren.contains(child.nodetype)) {
                childNodeInstance = new ChildNode(parent: parent, child: child)
                if (!childNodeInstance.save(flush: true)) {
                    if (params.action == 'api') {
                        response.status = 400 //Bad Request
                        render "ChildNode Creation Failed"
                        return
                    } else {
                        render(view: "create", model: [childNodeInstance: childNodeInstance])
                        return
                    }
                } else {
                    if (params.action == 'api') {
                        if (params.format && params.format != 'none') {
                            ArrayList cnodes = [childNodeInstance]
                            switch (params.format.toLowerCase()) {
                                case 'xml':
                                    def xml = xmlService.formatChildNodes(cnodes)
                                    render(text: xml, contentType: "text/xml")
                                    break;
                                case 'json':
                                    def json = jsonService.formatChildNodes(cnodes)
                                    render(text: json, contentType: "text/json")
                                    break;
                            }
                        } else {
                            response.status = 200
                            render "Successfully created."
                            return
                        }
                    } else {
                        flash.message = message(code: 'default.created.message', args: [message(code: 'childNode.label', default: 'ChildNode'), childNodeInstance.id])
                        redirect(action: "show", id: childNodeInstance.id)
                    }
                }
            } else {
                if (params.action == 'api') {
                    response.status = 400
                    render "Parent/Child relationship is not available. Please Create in NodeTypeRelationship."
                    return
                } else {
                    flash.message = "Parent/Child relationship is not available. Please Create in NodeTypeRelationship"
                    render(view: "create", model: [childNodeInstance: childNodeInstance])
                }
            }
        } else {
            if (params.action == 'api') {
                response.status = 404 //Not Found
                render "Existing relationship for that Parent and child node already exists. Please try again."
                return
            } else {
                flash.message = "Existing relationship for that Parent and child node already exists. Please try again."
                render(view: "create", model: [childNodeInstance: childNodeInstance])
                return
            }
        }
    }

    @ProjectAccess(ProjectAccess.Level.read)
    def show() {
        String path = iconService.getSmallIconPath()
        def childNodeInstance = ChildNode.get(params.id)
        if (!childNodeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'childNode.label', default: 'ChildNode'), params.id])
            redirect(action: "list")
            return
        } else {
            if (params.format && params.format != 'none') {
                ArrayList cnodes = [childNodeInstance]
                if (childNodeInstance) {
                    switch (params.format.toLowerCase()) {
                        case 'xml':
                            def xml = xmlService.formatChildNodes(cnodes)
                            render(text: xml, contentType: "text/xml")
                            break;
                        case 'json':
                            def json = jsonService.formatChildNodes(cnodes)
                            render(text: json, contentType: "text/json")
                            break;
                    }
                } else {
                    response.status = 404 //Not Found
                    render "${params.id} not found."
                }
            } else {
                [childNodeInstance: childNodeInstance, path: path]
            }
        }
    }

    def edit() {
        def childNodeInstance = ChildNode.get(params.id)
        if (!childNodeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'childNode.label', default: 'ChildNode'), params.id])
            redirect(action: "list")
            return
        }

        [childNodeInstance: childNodeInstance]
    }

    def update() {
        def childNodeInstance = ChildNode.get(params.id)

        if (!childNodeInstance) {
            flash.message = message(code: 'default.not.found.message',
                    args: [message(code: 'childNode.label', default: 'ChildNode'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (childNodeInstance.version > version) {
                childNodeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'childNode.label', default: 'ChildNode')] as Object[],
                        "Another user has updated this ChildNode while you were editing")
                render(view: "edit", model: [childNodeInstance: childNodeInstance])
                return
            }
        }

        childNodeInstance.properties = params

        if (!childNodeInstance.save(flush: true)) {
            render(view: "edit", model: [childNodeInstance: childNodeInstance])
            return
        }
        if (params.format) {
            response.status = 200 //Not Found
            render "Successfully edited."
        } else {
            flash.message = message(code: 'default.updated.message',
                    args: [message(code: 'childNode.label', default: 'ChildNode'), childNodeInstance.id])
            redirect(action: "show", id: childNodeInstance.id)
        }

    }

    def delete() {
        def childNodeInstance = ChildNode.get(params.id)
        if (!childNodeInstance) {
            flash.message = message(code: 'default.not.found.message',
                    args: [message(code: 'childNode.label', default: 'ChildNode'), params.id])
            redirect(action: "list")
            return
        }

        try {
            childNodeInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message',
                    args: [message(code: 'childNode.label', default: 'ChildNode'), params.id])
            redirect(action: "list")
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message',
                    args: [message(code: 'childNode.label', default: 'ChildNode'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
