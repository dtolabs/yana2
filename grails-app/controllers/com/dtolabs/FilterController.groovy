package com.dtolabs

import org.springframework.dao.DataIntegrityViolationException
import com.dtolabs.Filter
import grails.plugins.springsecurity.Secured
import com.dtolabs.yana2.springacl.DefaultProjectAccess
import com.dtolabs.yana2.springacl.ProjectAccess

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER','ROLE_YANA_USER'])
@DefaultProjectAccess(ProjectAccess.Level.architect)
class FilterController {

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
				def filter = new Filter(params)
				if(filter){
					if (!filter.save(flush: true)) {
						response.status = 400 //Bad Request
						render "Filter Creation Failed"
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
			        Filter filter = Filter.get(params.id)
			        if(filter){
						try{
							filter.delete(flush:true)
							response.status = 200
							render "Successfully Deleted."
						}catch(org.springframework.dao.DataIntegrityViolationException e) {
					        Filter.withSession { session ->
					            session.clear()
					        }

							response.status = 400 //Bad Request
							render "Referential Integrity Violation: Please remove/reassign all Attribute relationships first."
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

    def index() {
        redirect(action: "list", params: params)
    }

    @ProjectAccess(ProjectAccess.Level.read)
    def list() {
        def project = projectService.findProject(params.project)
		if(params.format && params.format!='none'){
			def filters = Filter.findAllByProject(project)
			switch(params.format.toLowerCase()){
				case 'xml':
					def xml = xmlService.formatFilters(filters)
					render(text: xml, contentType: "text/xml")
					break;
				case 'json':
					def json = jsonService.formatFilters(filters)
					render(text:json, contentType: "text/json")
					break;
			}
		}else{
	        params.max = Math.min(params.max ? params.int('max') : 10, 100)
	        [filterInstanceList: Filter.findAllByProject(project,[max:params.max,offset:params.offset,sort:"dataType",order:"asc"]), filterInstanceTotal: Filter.countByProject(project)]
		}
    }

    def create() {
        def project = projectService.findProject(params.project)
        params.project=null
        def filter = new Filter(params)
        filter.project=project
        [filterInstance: filter]
    }

    def save() {

        def project = projectService.findProject(params.project)
        params.project = null
        def filterInstance = new Filter(params)
        filterInstance.project = project
        if (!filterInstance.save(flush: true)) {
            render(view: "create", model: [filterInstance: filterInstance])
            return
        }
		
		flash.message = message(code: 'default.created.message', args: [message(code: 'filter.label', default: 'Filter'), filterInstance.id])
        redirect(action: "show", id: filterInstance.id)
    }

    @ProjectAccess(ProjectAccess.Level.read)
    def show() {
		String path = iconService.getLargeIconPath()
        def filterInstance = Filter.get(params.id)
        if (!filterInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'filter.label', default: 'Filter'), params.id])
            redirect(action: "list")
            return
        }else{
			if(params.format && params.format!='none'){
				ArrayList filters = [filterInstance]
				if(filterInstance){
					switch(params.format.toLowerCase()){
						case 'xml':
							def xml = xmlService.formatFilters(filters)
							render(text: xml, contentType: "text/xml")
							break;
						case 'json':
							def json = jsonService.formatFilters(filters)
							render(text:json, contentType: "text/json")
							break;
					}
				}else{
			          response.status = 404 //Not Found
			          render "${params.id} not found."
				}
			}else{
				[filterInstance: filterInstance,path:path]
			}
        }
    }

    def edit() {
        def filterInstance = Filter.get(params.id)
        if (!filterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'filter.label', default: 'Filter'), params.id])
            redirect(action: "list")
            return
        }

        [filterInstance: filterInstance]
    }

    def update() {
        def filterInstance = Filter.get(params.id)
        if (!filterInstance) {
			if(params.format){
				response.status = 404 //Not Found
				render "${params.id} not found."
			}else{
            	flash.message = message(code: 'default.not.found.message', args: [message(code: 'filter.label', default: 'Filter'), params.id])
				redirect(action: "list")
				return
			}
        }

        if (params.version) {
            def version = params.version.toLong()
            if (filterInstance.version > version) {
                filterInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'filter.label', default: 'Filter')] as Object[],
                          "Another user has updated this Filter while you were editing")
                render(view: "edit", model: [filterInstance: filterInstance])
                return
            }
        }
        def project = projectService.findProject(params.project)
        params.project = null

        filterInstance.properties = params
        filterInstance.project = project

        if (!filterInstance.save(flush: true)) {
            render(view: "edit", model: [filterInstance: filterInstance])
            return
        }
		if(params.format){
			response.status = 200 //Not Found
			render "Successfully edited."
		}else{
			flash.message = message(code: 'default.updated.message', args: [message(code: 'filter.label', default: 'Filter'), filterInstance.id])
			redirect(action: "show", id: filterInstance.id)
		}
    }

    def delete() {
        def filterInstance = Filter.get(params.id)
        if (!filterInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'filter.label', default: 'Filter'), params.id])
            redirect(action: "list")
            return
        }

        try {
            filterInstance.delete(flush: true)
			
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'filter.label', default: 'Filter'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'filter.label', default: 'Filter'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
