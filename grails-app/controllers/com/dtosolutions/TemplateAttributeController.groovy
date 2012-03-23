package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException

class TemplateAttributeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [templateAttributeInstanceList: TemplateAttribute.list(params), templateAttributeInstanceTotal: TemplateAttribute.count()]
    }

    def create() {
        [templateAttributeInstance: new TemplateAttribute(params)]
    }

    def save() {
        def templateAttributeInstance = new TemplateAttribute(params)
        if (!templateAttributeInstance.save(flush: true)) {
            render(view: "create", model: [templateAttributeInstance: templateAttributeInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), templateAttributeInstance.id])
        redirect(action: "show", id: templateAttributeInstance.id)
    }

    def show() {
        def templateAttributeInstance = TemplateAttribute.get(params.id)
        if (!templateAttributeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), params.id])
            redirect(action: "list")
            return
        }

        [templateAttributeInstance: templateAttributeInstance]
    }

    def edit() {
        def templateAttributeInstance = TemplateAttribute.get(params.id)
        if (!templateAttributeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), params.id])
            redirect(action: "list")
            return
        }

        [templateAttributeInstance: templateAttributeInstance]
    }

    def update() {
        def templateAttributeInstance = TemplateAttribute.get(params.id)
        if (!templateAttributeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (templateAttributeInstance.version > version) {
                templateAttributeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'templateAttribute.label', default: 'TemplateAttribute')] as Object[],
                          "Another user has updated this TemplateAttribute while you were editing")
                render(view: "edit", model: [templateAttributeInstance: templateAttributeInstance])
                return
            }
        }

        templateAttributeInstance.properties = params

        if (!templateAttributeInstance.save(flush: true)) {
            render(view: "edit", model: [templateAttributeInstance: templateAttributeInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), templateAttributeInstance.id])
        redirect(action: "show", id: templateAttributeInstance.id)
    }

    def delete() {
        def templateAttributeInstance = TemplateAttribute.get(params.id)
        if (!templateAttributeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), params.id])
            redirect(action: "list")
            return
        }

        try {
            templateAttributeInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
