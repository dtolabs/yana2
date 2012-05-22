package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class AttributeController {

	def springSecurityService
	def iconService
	def xmlService
	def jsonService
	def webhookService
	
    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def api(){
		switch(request.method){
			case "POST":
				def json = request.JSON
				def attribute = new Attribute(params)
				println(params)
				println(attribute)
				if(attribute){
					if (!attribute.save(flush: true)) {
						response.status = 400 //Bad Request
						render "Attribute Creation Failed"
					}else{
						ArrayList attributes = [attribute]
						webhookService.postToURL('attribute', attributes,'create')
						
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
			        Attribute attribute = Attribute.get(params.id)
			        if(attribute){
						try{
							attribute.delete(flush:true)
							response.status = 200
							render "Successfully Deleted."
						}catch(org.springframework.dao.DataIntegrityViolationException e) {
					        Attribute.withSession { session ->
					            session.clear()
					        }

							response.status = 400 //Bad Request
							render "Referential Integrity Violation: Please remove/reassign all Filter/Nodetype relationships first."
						}
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
			ArrayList atts = Attribute.list()
			switch(params.format.toLowerCase()){
				case 'xml':
					def xml = xmlService.formatAttributes(atts)
					render(text: xml, contentType: "text/xml")
					break;
				case 'json':
					def json = jsonService.formatAttributes(atts)
					render(text:json, contentType: "text/json")
					break;
			}
		}else{
        	params.max = Math.min(params.max ? params.int('max') : 10, 100)
			[attributeInstanceList: Attribute.list(max:params.max,offset:params.offset,sort:"name",order:"asc"), attributeInstanceTotal: Attribute.count()]
		}
    }

    def create() {
        [attributeInstance: new Attribute(params)]
    }

    def save() {
        def attributeInstance = new Attribute(params)
        if (!attributeInstance.save(flush: true)) {
            render(view: "create", model: [attributeInstance: attributeInstance])
            return
        }

		ArrayList attributes = [attributeInstance]
		webhookService.postToURL('attribute', attributes,'create')
		
		flash.message = message(code: 'default.created.message', args: [message(code: 'attribute.label', default: 'Attribute'), attributeInstance.id])
        redirect(action: "show", id: attributeInstance.id)
    }

    def show() {
		String path = iconService.getLargeIconPath()
        def attributeInstance = Attribute.get(params.id)
        if (!attributeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'attribute.label', default: 'Attribute'), params.id])
            redirect(action: "list")
            return
        }else{
			if(params.format && params.format!='none'){
				ArrayList atts = [attributeInstance]
				switch(params.format.toLowerCase()){
					case 'xml':
						def xml = xmlService.formatAttributes(atts)
						render(text: xml, contentType: "text/xml")
						break;
					case 'json':
						def json = jsonService.formatAttributes(atts)
						render(text:json, contentType: "text/json")
						break;
				}
			}else{
				[attributeInstance: attributeInstance,path:path]
			}
        }
    }

    def edit() {
        def attributeInstance = Attribute.get(params.id)
        if (!attributeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'attribute.label', default: 'Attribute'), params.id])
            redirect(action: "list")
            return
        }

        [attributeInstance: attributeInstance]
    }

    def update() {
        def attributeInstance = Attribute.get(params.id)
        if (!attributeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'attribute.label', default: 'Attribute'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (attributeInstance.version > version) {
                attributeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'attribute.label', default: 'Attribute')] as Object[],
                          "Another user has updated this Attribute while you were editing")
                render(view: "edit", model: [attributeInstance: attributeInstance])
                return
            }
        }

        attributeInstance.properties = params

        if (!attributeInstance.save(flush: true)) {
            render(view: "edit", model: [attributeInstance: attributeInstance])
            return
        }

		ArrayList attributes = [attributeInstance]
		webhookService.postToURL('attribute', attributes,'edit')
		
		flash.message = message(code: 'default.updated.message', args: [message(code: 'attribute.label', default: 'Attribute'), attributeInstance.id])
        redirect(action: "show", id: attributeInstance.id)
    }

    def delete() {
        def attributeInstance = Attribute.get(params.id)
        if (!attributeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'attribute.label', default: 'Attribute'), params.id])
            redirect(action: "list")
            return
        }

        try {
            attributeInstance.delete(flush: true)
			
			ArrayList attributes = [attributeInstance]
			webhookService.postToURL('attribute', attributes,'delete')
			
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'attribute.label', default: 'Attribute'), params.id])
            redirect(action: "list")
        }catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'attribute.label', default: 'Attribute'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
