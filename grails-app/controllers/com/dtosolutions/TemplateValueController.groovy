package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured
import grails.converters.JSON

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class TemplateValueController {

	def springSecurityService
	def xmlService
	def jsonService
	
    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def api(){
		switch(request.method){
			case "POST":
				def json = request.JSON
				def templateValue = new TemplateValue(params)
				if(templateValue){
					if (!templateValue.save(flush: true)) {
						response.status = 400 //Bad Request
						render "TemplateValue Creation Failed"
					}else{

						response.status = 200
						render "Successfully Created."
					}
				}else{
			          response.status = 404 //Not Found
			          render "${params.id} not found."
				}
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
				def json = request.JSON
				if(params.id){
			        def tval = TemplateValue.get(params.id)
			        if(tval){
			          tval.delete()

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
			def tvals = TemplateValue.list()
			switch(params.format.toLowerCase()){
				case 'xml':
					def xml = xmlService.formatTemplateValues(tvals)
					render(text: xml, contentType: "text/xml")
					break;
				case 'json':
					def json = jsonService.formatTemplateValues(tvals)
					render(text:json, contentType: "text/json")
					break;
			}
		}else{
        	params.max = Math.min(params.max ? params.int('max') : 10, 100)
			[templateValueInstanceList: TemplateValue.list(params), templateValueInstanceTotal: TemplateValue.count()]
		}
    }

    def create() {
        [templateValueInstance: new TemplateValue(params)]
    }

    def save() {
        def templateValueInstance = new TemplateValue(params)
        if (!templateValueInstance.save(flush: true)) {
            render(view: "create", model: [templateValueInstance: templateValueInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'templateValue.label', default: 'TemplateValue'), templateValueInstance.id])
        redirect(action: "show", id: templateValueInstance.id)
    }

    def show() {
        def templateValueInstance = TemplateValue.get(params.id)
        if (!templateValueInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'templateValue.label', default: 'TemplateValue'), params.id])
            redirect(action: "list")
            return
        }else{
			if(params.format && params.format!='none'){
				ArrayList tvals = [templateValueInstance]
				switch(params.format.toLowerCase()){
					case 'xml':
						def xml = xmlService.formatTemplateValues(tvals)
						render(text: xml, contentType: "text/xml")
						break;
					case 'json':
						def json = jsonService.formatTemplateValues(tvals)
						render(text:json, contentType: "text/json")
						break;
				}
			}else{
	        	[templateValueInstance: templateValueInstance]
			}
        }
    }

    def edit() {
        def templateValueInstance = TemplateValue.get(params.id)
        if (!templateValueInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'templateValue.label', default: 'TemplateValue'), params.id])
            redirect(action: "list")
            return
        }

        [templateValueInstance: templateValueInstance]
    }

    def update() {
        def templateValueInstance = TemplateValue.get(params.id)
        if (!templateValueInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'templateValue.label', default: 'TemplateValue'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (templateValueInstance.version > version) {
                templateValueInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'templateValue.label', default: 'TemplateValue')] as Object[],
                          "Another user has updated this TemplateValue while you were editing")
                render(view: "edit", model: [templateValueInstance: templateValueInstance])
                return
            }
        }

        templateValueInstance.properties = params

        if (!templateValueInstance.save(flush: true)) {
            render(view: "edit", model: [templateValueInstance: templateValueInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'templateValue.label', default: 'TemplateValue'), templateValueInstance.id])
        redirect(action: "show", id: templateValueInstance.id)
    }

    def delete() {
        def templateValueInstance = TemplateValue.get(params.id)
        if (!templateValueInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'templateValue.label', default: 'TemplateValue'), params.id])
            redirect(action: "list")
            return
        }

        try {
            templateValueInstance.delete(flush: true)
			
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'templateValue.label', default: 'TemplateValue'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'templateValue.label', default: 'TemplateValue'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
