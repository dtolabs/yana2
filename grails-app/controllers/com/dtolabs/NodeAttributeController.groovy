package com.dtolabs

import org.springframework.dao.DataIntegrityViolationException
import com.dtolabs.NodeAttribute
import grails.plugins.springsecurity.Secured
import grails.converters.JSON

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class NodeAttributeController {

	def springSecurityService
	def xmlService
	def jsonService
	
    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def api(){
		switch(request.method){
			case "POST":
				def json = request.JSON
				if(!saveNodeAttribute()){
					response.status = 201 //Internal Server Error
					render "NodeAttribute created successfully\n"
				}else{
					response.status = 500 //Internal Server Error
					render "Could not create new nodeAttribute due to errors:\n ${nodeAttribute.errors}"
				}
				break
			case "GET":
				def json = request.JSON
				this.show()
				break
			case "PUT":
				def json = request.JSON
				def tatt = NodeAttribute.findById(params.id)
				tatt.template = NodeType.get(params.template.toLong())
				tatt.attribute = Attribute.get(params.attribute.toLong())
				if(tatt.save()){
					response.status = 200 // OK
			        render "Successfully updated"
				}else{
			        response.status = 500 //Internal Server Error
			        render "Could not update NodeAttribute due to errors:\n ${tatt.errors}"
				}
				break
			case "DELETE":
				def json = request.JSON
				if(params.id){
			        def tatt = NodeAttribute.get(params.id)
			        if(tatt){
						tatt.delete()
					  
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
			def tattributes = NodeAttribute.list()
			switch(params.format.toLowerCase()){
				case 'xml':
					def xml = xmlService.formatNodeAttributes(tattributes)
					render(text: xml, contentType: "text/xml")
					break;
				case 'json':
					def json = jsonService.formatNodeAttributes(tattributes)
					render(text:json, contentType: "text/json")
					break;
			}
		}else{
        	params.max = Math.min(params.max ? params.int('max') : 10, 100)
			[nodeAttributeInstanceList: NodeAttribute.list(params), nodeAttributeInstanceTotal: NodeAttribute.count()]
		}
    }

    def create() {
        [nodeAttributeInstance: new NodeAttribute(params)]
    }

    def save() {
        def nodeAttributeInstance = new NodeAttribute(params)
        if (!saveNodeAttribute()) {
            render(view: "create", model: [nodeAttributeInstance: nodeAttributeInstance])
            return
        }
		
		flash.message = message(code: 'default.created.message', args: [message(code: 'nodeAttribute.label', default: 'NodeAttribute'), nodeAttributeInstance.id])
        redirect(action: "show", id: nodeAttributeInstance.id)
    }

	/*
	 * AJAX method
	 */
	def saveNodeAttribute(){
		def json = request.JSON
		params.template=(json.template)?json.template:params.template
		params.attribute=(json.attribute)?json.attribute:params.attribute
        def temp = new NodeAttribute()
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
        def nodeAttributeInstance = NodeAttribute.get(params.id)
        if (!nodeAttributeInstance) {
			if(params.format){
				response.status = 404 //Not Found
				render "${params.id} not found."
			}else{
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeAttribute.label', default: 'NodeAttribute'), params.id])
	            redirect(action: "list")
	            return
			}
        }else{
			if(params.format && params.format!='none'){
				ArrayList nodeAttributes = [nodeAttributeInstance]
				switch(params.format.toLowerCase()){
					case 'xml':
						def xml = xmlService.formatNodeAttributes(nodeAttributes)
						render(text: xml, contentType: "text/xml")
						break;
					case 'json':
						def json = jsonService.formatNodeAttributes(nodeAttributes)
						render(text:json, contentType: "text/json")
						break;
				}
			}else{
	        	[nodeAttributeInstance: nodeAttributeInstance]
			}
        }
    }

    def edit() {
        def nodeAttributeInstance = NodeAttribute.get(params.id)
        if (!nodeAttributeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeAttribute.label', default: 'NodeAttribute'), params.id])
            redirect(action: "list")
            return
        }

        [nodeAttributeInstance: nodeAttributeInstance]
    }

    def update() {
        def nodeAttributeInstance = NodeAttribute.get(params.id)
        if (!nodeAttributeInstance) {
			if(params.format){
				response.status = 404 //Not Found
				render "${params.id} not found."
			}else{
            	flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeAttribute.label', default: 'NodeAttribute'), params.id])
				redirect(action: "list")
				return
			}
        }

        if (params.version) {
            def version = params.version.toLong()
            if (nodeAttributeInstance.version > version) {
                nodeAttributeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'nodeAttribute.label', default: 'NodeAttribute')] as Object[],
                          "Another user has updated this NodeAttribute while you were editing")
                render(view: "edit", model: [nodeAttributeInstance: nodeAttributeInstance])
                return
            }
        }

        nodeAttributeInstance.properties = params

        if (!nodeAttributeInstance.save(flush: true)) {
            render(view: "edit", model: [nodeAttributeInstance: nodeAttributeInstance])
            return
        }
		if(params.format){
			response.status = 200 //Not Found
			render "Successfully edited."
		}else{
			flash.message = message(code: 'default.updated.message', args: [message(code: 'nodeAttribute.label', default: 'NodeAttribute'), nodeAttributeInstance.id])
			redirect(action: "show", id: nodeAttributeInstance.id)
		}
    }
	

    def delete() {
        def nodeAttributeInstance = NodeAttribute.get(params.id)
        if (!nodeAttributeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeAttribute.label', default: 'NodeAttribute'), params.id])
			redirect(action: "list")
			return
        }

        if(deleteNodeAttribute()){
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'nodeAttribute.label', default: 'NodeAttribute'), params.id])
			redirect(action: "list")
		}else{
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'nodeAttribute.label', default: 'NodeAttribute'), params.id])
			redirect(action: "show", id: params.id)
        }
    }
	
	/*
	 * AJAX method
	 */
	def deleteNodeAttribute(){
		def json = request.JSON
		params.id=(json.id)?json.id:params.id
		def nodeAttributeInstance = NodeAttribute.get(params.id)
        try {
            nodeAttributeInstance.delete(flush: true)
			render "1"
        }catch (DataIntegrityViolationException e) {
            render "0"
        }
	}

	
	

}
