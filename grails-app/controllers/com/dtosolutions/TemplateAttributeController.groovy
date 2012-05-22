package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured
import grails.converters.JSON

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class TemplateAttributeController {

	def springSecurityService
	def xmlService
	def jsonService
	def webhookService
	
    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def api(){
		switch(request.method){
			case "POST":
				def json = request.JSON
				if(!saveTemplateAttribute()){
					response.status = 201 //Internal Server Error
					render "TemplateAttribute created successfully\n"
				}else{
					response.status = 500 //Internal Server Error
					render "Could not create new templateAttribute due to errors:\n ${templateAttribute.errors}"
				}
				break
			case "GET":
				def json = request.JSON
				this.show()
				break
			case "PUT":
				def json = request.JSON
				def tatt = TemplateAttribute.findById(params.id)
				tatt.template = NodeType.get(params.template.toLong())
				tatt.attribute = Attribute.get(params.attribute.toLong())
				if(tatt.save()){
					response.status = 200 // OK
			        render "Successfully updated"
				}else{
			        response.status = 500 //Internal Server Error
			        render "Could not update TemplateAttribute due to errors:\n ${tatt.errors}"
				}
				break
			case "DELETE":
				def json = request.JSON
				if(params.id){
			        def tatt = TemplateAttribute.get(params.id)
			        if(tatt){
						tatt.delete()
					  
						ArrayList templateAttributes = [tatt]
						webhookService.postToURL('templateAttribute', templateAttributes,'delete')
					  
						response.status = 200
						render "Successfully Deleted."
			        }else{
			          response.status = 404 //Not Found
			          render "${params.id} not found."
			        }
				}else{
					response.status = 400 //Bad Request
					render """DELETE request must include the id"""
				}
				break
		  }
		return
	}
	
   def listapi(){
	   switch(request.method){
		   case "POST":
			   def json = request.JSON
			   this.list()
			   break
		 }
	   return
   }
	
    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
		if(params.format && params.format!='none'){
			def tattributes = TemplateAttribute.list()
			switch(params.format.toLowerCase()){
				case 'xml':
					def xml = xmlService.formatTemplateAttributes(tattributes)
					render(text: xml, contentType: "text/xml")
					break;
				case 'json':
					def json = jsonService.formatTemplateAttributes(tattributes)
					render(text:json, contentType: "text/json")
					break;
			}
		}else{
        	params.max = Math.min(params.max ? params.int('max') : 10, 100)
			[templateAttributeInstanceList: TemplateAttribute.list(params), templateAttributeInstanceTotal: TemplateAttribute.count()]
		}
    }

    def create() {
        [templateAttributeInstance: new TemplateAttribute(params)]
    }

    def save() {
        def templateAttributeInstance = new TemplateAttribute(params)
        if (!saveTemplateAttribute()) {
            render(view: "create", model: [templateAttributeInstance: templateAttributeInstance])
            return
        }
		
		ArrayList templateAttributes = [templateAttributeInstance]
		webhookService.postToURL('templateAttribute', templateAttributes,'create')
		
		flash.message = message(code: 'default.created.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), templateAttributeInstance.id])
        redirect(action: "show", id: templateAttributeInstance.id)
    }

	/*
	 * AJAX method
	 */
	def saveTemplateAttribute(){
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
        }else{
			if(params.format && params.format!='none'){
				ArrayList templateAttributes = [templateAttributeInstance]
				switch(params.format.toLowerCase()){
					case 'xml':
						def xml = xmlService.formatTemplateAttributes(templateAttributes)
						render(text: xml, contentType: "text/xml")
						break;
					case 'json':
						def json = jsonService.formatTemplateAttributes(templateAttributes)
						render(text:json, contentType: "text/json")
						break;
				}
			}else{
	        	[templateAttributeInstance: templateAttributeInstance]
			}
        }
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

		ArrayList templateAttributes = [templateAttributeInstance]
		webhookService.postToURL('templateAttribute', templateAttributes,'edit')
		
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

        if(deleteTemplateAttribute()){
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), params.id])
			redirect(action: "list")
		}else{
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute'), params.id])
			redirect(action: "show", id: params.id)
        }
    }
	
	/*
	 * AJAX method
	 */
	def deleteTemplateAttribute(){
		def templateAttributeInstance = TemplateAttribute.get(params.id)
        try {
            templateAttributeInstance.delete(flush: true)
			
			ArrayList templateAttributes = [templateAttributeInstance]
			webhookService.postToURL('templateAttribute', templateAttributes,'delete')
			
			render "1"
        }
        catch (DataIntegrityViolationException e) {
            render "0"
        }
	}

	
	

}
