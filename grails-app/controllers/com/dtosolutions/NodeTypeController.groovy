package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN','ROLE_ARCHITECT','ROLE_SUPER_USER'])
class NodeTypeController {

	def springSecurityService
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [nodeTypeInstanceList: NodeType.list(params), nodeTypeInstanceTotal: NodeType.count()]
    }

    def create() {
        [nodeTypeInstance: new NodeType(params)]
    }

    def save() {
        def nodeTypeInstance = new NodeType(params)
        if (!nodeTypeInstance.save(flush: true)) {
            render(view: "create", model: [nodeTypeInstance: nodeTypeInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'nodeType.label', default: 'NodeType'), nodeTypeInstance.id])
        redirect(action: "show", id: nodeTypeInstance.id)
    }

    def show() {
        def nodeTypeInstance = NodeType.get(params.id)
        if (!nodeTypeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
            return
        }

        [nodeTypeInstance: nodeTypeInstance]
    }

    def edit() {
        def nodeTypeInstance = NodeType.get(params.id)
        if (!nodeTypeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
            return
        }

        [nodeTypeInstance: nodeTypeInstance]
    }

    def update() {
        def nodeTypeInstance = NodeType.get(params.id)
        if (!nodeTypeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (nodeTypeInstance.version > version) {
                nodeTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'nodeType.label', default: 'NodeType')] as Object[],
                          "Another user has updated this NodeType while you were editing")
                render(view: "edit", model: [nodeTypeInstance: nodeTypeInstance])
                return
            }
        }

        nodeTypeInstance.properties = params

        if (!nodeTypeInstance.save(flush: true)) {
            render(view: "edit", model: [nodeTypeInstance: nodeTypeInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'nodeType.label', default: 'NodeType'), nodeTypeInstance.id])
        redirect(action: "show", id: nodeTypeInstance.id)
    }

    def delete() {
        def nodeTypeInstance = NodeType.get(params.id)
        if (!nodeTypeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
            return
        }

        try {
            nodeTypeInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
