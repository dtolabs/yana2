package com.dtolabs

import org.springframework.dao.DataIntegrityViolationException
import com.dtolabs.Attribute
import grails.plugins.springsecurity.Secured
import com.dtolabs.yana2.springacl.DefaultProjectAccess
import com.dtolabs.yana2.springacl.ProjectAccess

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER','ROLE_YANA_USER'])
@DefaultProjectAccess(ProjectAccess.Level.architect)
class AttributeController {

	def springSecurityService
	def iconService
	def xmlService
	def jsonService
	def projectService

    static defaultAction = "list"
    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def api(){
		switch(request.method){
			case "POST":
				def json = request.JSON
				def attribute = new Attribute(params)
				if(attribute){
					if (!attribute.save(flush: true)) {
						response.status = 400 //Bad Request
						render "Attribute Creation Failed"
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

    @ProjectAccess(ProjectAccess.Level.read)
   def listapi(){
	   switch(request.method){
		   case "POST":
			   def json = request.JSON
			   this.list()
			   break
		 }
	   return
   }

    @ProjectAccess(ProjectAccess.Level.read)
    def list() {
        def project=projectService.findProject(params.project)
		if(params.format && params.format!='none'){
            ArrayList atts = Attribute.findAllByProject(project)
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
			[attributeInstanceList: Attribute.findAllByProject(project,[max:params.max,offset:params.offset,sort:"name",order:"asc"]), attributeInstanceTotal: Attribute.countByProject(project)]
		}
    }

    def create() {
        def project = projectService.findProject(params.project)
        params.project = null
        def attribute = new Attribute(params)
        attribute.project = project
        [attributeInstance: attribute]
    }

    def save() {

        def project = projectService.findProject(params.project)
        params.project = null
        def attributeInstance = new Attribute(params)
        attributeInstance.project = project
        if (!attributeInstance.save(flush: true)) {
            render(view: "create", model: [attributeInstance: attributeInstance])
            return
        }
		
		flash.message = message(code: 'default.created.message', args: [message(code: 'attribute.label', default: 'Attribute'), attributeInstance.id])
        redirect(action: "show", id: attributeInstance.id)
    }

    @ProjectAccess(ProjectAccess.Level.read)
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
				if(attributeInstance){
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
			          response.status = 404 //Not Found
			          render "${params.id} not found."
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
			if(params.format){
				response.status = 404 //Not Found
				render "${params.id} not found."
			}else{
            	flash.message = message(code: 'default.not.found.message', args: [message(code: 'attribute.label', default: 'Attribute'), params.id])
				redirect(action: "list")
				return
			}
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
        def project = projectService.findProject(params.project)
        params.project = null
        attributeInstance.properties = params
        attributeInstance.project = project

        if (!attributeInstance.save(flush: true)) {
            render(view: "edit", model: [attributeInstance: attributeInstance])
            return
        }
		
		if(params.format){
			response.status = 200 //Not Found
			render "Successfully edited."
		}else{
			flash.message = message(code: 'default.updated.message', args: [message(code: 'attribute.label', default: 'Attribute'), attributeInstance.id])
			redirect(action: "show", id: attributeInstance.id)
		}
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
			
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'attribute.label', default: 'Attribute'), params.id])
            redirect(action: "list")
        }catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'attribute.label', default: 'Attribute'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
