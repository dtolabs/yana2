package com.dtolabs

import org.springframework.dao.DataIntegrityViolationException
import com.dtolabs.NodeValue
import grails.plugins.springsecurity.Secured
import grails.converters.JSON

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class NodeValueController {

	def springSecurityService
	def xmlService
	def jsonService
	
    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def api(){
		switch(request.method){
			case "POST":
				def json = request.JSON
				def nodeValue = new NodeValue(params)
				if(nodeValue){
					if (!nodeValue.save(flush: true)) {
						response.status = 400 //Bad Request
						render "NodeValue Creation Failed"
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
			        def tval = NodeValue.get(params.id)
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
			def tvals = NodeValue.list()
			switch(params.format.toLowerCase()){
				case 'xml':
					def xml = xmlService.formatNodeValues(tvals)
					render(text: xml, contentType: "text/xml")
					break;
				case 'json':
					def json = jsonService.formatNodeValues(tvals)
					render(text:json, contentType: "text/json")
					break;
			}
		}else{
        	params.max = Math.min(params.max ? params.int('max') : 10, 100)
			[nodeValueInstanceList: NodeValue.list(params), nodeValueInstanceTotal: NodeValue.count()]
		}
    }

    def create() {
        [nodeValueInstance: new NodeValue(params)]
    }

    def save() {
        def nodeValueInstance = new NodeValue(params)
        if (!nodeValueInstance.save(flush: true)) {
            render(view: "create", model: [nodeValueInstance: nodeValueInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'nodeValue.label', default: 'NodeValue'), nodeValueInstance.id])
        redirect(action: "show", id: nodeValueInstance.id)
    }

    def show() {
        def nodeValueInstance = NodeValue.get(params.id)
        if (!nodeValueInstance) {
			if(params.format){
				response.status = 404 //Not Found
				render "${params.id} not found."
			}else{
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeValue.label', default: 'NodeValue'), params.id])
	            redirect(action: "list")
	            return
			}
        }else{
			if(params.format && params.format!='none'){
				ArrayList tvals = [nodeValueInstance]
				switch(params.format.toLowerCase()){
					case 'xml':
						def xml = xmlService.formatNodeValues(tvals)
						render(text: xml, contentType: "text/xml")
						break;
					case 'json':
						def json = jsonService.formatNodeValues(tvals)
						render(text:json, contentType: "text/json")
						break;
				}
			}else{
	        	[nodeValueInstance: nodeValueInstance]
			}
        }
    }

    def edit() {
        def nodeValueInstance = NodeValue.get(params.id)
        if (!nodeValueInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeValue.label', default: 'NodeValue'), params.id])
            redirect(action: "list")
            return
        }

        [nodeValueInstance: nodeValueInstance]
    }

    def update() {
        def nodeValueInstance = NodeValue.get(params.id)
        if (!nodeValueInstance) {
			if(params.format){
				response.status = 404 //Not Found
				render "${params.id} not found."
			}else{
            	flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeValue.label', default: 'NodeValue'), params.id])
				redirect(action: "list")
				return
			}
        }

        if (params.version) {
            def version = params.version.toLong()
            if (nodeValueInstance.version > version) {
                nodeValueInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'nodeValue.label', default: 'NodeValue')] as Object[],
                          "Another user has updated this NodeValue while you were editing")
                render(view: "edit", model: [nodeValueInstance: nodeValueInstance])
                return
            }
        }

        nodeValueInstance.properties = params

        if (!nodeValueInstance.save(flush: true)) {
			if(params.format){
				response.status = 400 //Not Found
				render "Could not save nodeValue."
			}else{
            	render(view: "edit", model: [nodeValueInstance: nodeValueInstance])
				return
			}
        }
		if(params.format){
			response.status = 200 //Not Found
			render "Successfully edited."
		}else{
			flash.message = message(code: 'default.updated.message', args: [message(code: 'nodeValue.label', default: 'NodeValue'), nodeValueInstance.id])
			redirect(action: "show", id: nodeValueInstance.id)
		}
    }

    def delete() {
        def nodeValueInstance = NodeValue.get(params.id)
        if (!nodeValueInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeValue.label', default: 'NodeValue'), params.id])
            redirect(action: "list")
            return
        }

        try {
            nodeValueInstance.delete(flush: true)
			
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'nodeValue.label', default: 'NodeValue'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'nodeValue.label', default: 'NodeValue'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
