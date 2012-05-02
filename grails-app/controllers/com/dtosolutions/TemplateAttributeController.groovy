package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured
import grails.converters.JSON

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class TemplateAttributeController {

	def springSecurityService
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	/*
	 * Restful function to handle routing
	 * URLMapping wants to route everything to node or take over routing for node; needed to build
	 * routing function to handle REST handling to do custom routing for anything that doesn't 
	 * look like it is handled by controller
	 */
	def api(){
		switch(request.method){
			case "POST":
				def json = request.JSON
				this.saveTemplateAttribute()
				return
				break
			case "GET":
				this.show()
				return
				break
			case "PUT":
				this.update()
				return
				break
			case "DELETE":
				this.deleteTemplateAttribute()

				break
		  }
	}
	
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
		println(params)
        def templateAttributeInstance = new TemplateAttribute(params)
        if (!saveTemplateAttribute()) {
            render(view: "create", model: [templateAttributeInstance: templateAttributeInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), templateAttributeInstance.id])
        redirect(action: "show", id: templateAttributeInstance.id)
    }

	def saveTemplateAttribute(){
		println(params)
        def temp = new TemplateAttribute()
		temp.template=NodeType.get(params.template.toLong())
		temp.attribute = Attribute.get(params.attribute.toLong())
        if (!temp.save(flush: true,failOnError:true)) {
            render "0"
        }else{
			temp.save(flush: true,failOnError:true)
			render "1"
        }
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
		println("delete:"+params)
        def templateAttributeInstance = TemplateAttribute.get(params.id)
        if (!templateAttributeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), params.id])
			redirect(action: "list")
			return
        }

        if(deleteTemplateAttribute()){
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), params.id])
			redirect(action: "list")
		}else{
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), params.id])
			redirect(action: "show", id: params.id)
        }
    }
	
	def deleteTemplateAttribute(){
		println(params)
		def templateAttributeInstance = TemplateAttribute.get(params.id)
        try {
            templateAttributeInstance.delete(flush: true)
			render "1"
        }
        catch (DataIntegrityViolationException e) {
            render "0"
        }
	}

	
	

}
