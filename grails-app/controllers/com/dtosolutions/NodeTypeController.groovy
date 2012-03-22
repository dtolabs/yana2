package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException

class NodeTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [NodeTypeInstanceList: NodeType.list(params), NodeTypeInstanceTotal: NodeType.count()]
    }

    def create() {
        [NodeTypeInstance: new NodeType(params)]
    }

    def save() {
        def NodeTypeInstance = new NodeType(params)
        if (!NodeTypeInstance.save(flush: true)) {
            render(view: "create", model: [NodeTypeInstance: NodeTypeInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'NodeType.label', default: 'NodeType'), NodeTypeInstance.id])
        redirect(action: "show", id: NodeTypeInstance.id)
    }

    def show() {
        def NodeTypeInstance = NodeType.get(params.id)
        if (!NodeTypeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'NodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
            return
        }

        [NodeTypeInstance: NodeTypeInstance]
    }

    def edit() {
        def NodeTypeInstance = NodeType.get(params.id)
        if (!NodeTypeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'NodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
            return
        }

        [NodeTypeInstance: NodeTypeInstance]
    }

    def update() {
        def NodeTypeInstance = NodeType.get(params.id)
        if (!NodeTypeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'NodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (NodeTypeInstance.version > version) {
                NodeTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'NodeType.label', default: 'NodeType')] as Object[],
                          "Another user has updated this NodeType while you were editing")
                render(view: "edit", model: [NodeTypeInstance: NodeTypeInstance])
                return
            }
        }

        NodeTypeInstance.properties = params

        if (!NodeTypeInstance.save(flush: true)) {
            render(view: "edit", model: [NodeTypeInstance: NodeTypeInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'NodeType.label', default: 'NodeType'), NodeTypeInstance.id])
        redirect(action: "show", id: NodeTypeInstance.id)
    }

    def delete() {
        def NodeTypeInstance = NodeType.get(params.id)
        if (!NodeTypeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'NodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
            return
        }

        try {
            NodeTypeInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'NodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'NodeType.label', default: 'NodeType'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
