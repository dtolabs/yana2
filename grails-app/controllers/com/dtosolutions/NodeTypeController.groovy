package com.dtosolutions

import grails.converters.JSON
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class NodeTypeController {

	def iconService;
	def springSecurityService
	def xmlService
	def jsonService
	def webhookService
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def api(){
		switch(request.method){
			case "POST":
				def json = request.JSON
				def nodeType = new NodeType(params)
				if(nodeType){
					if (!nodeType.save(flush: true)) {
						response.status = 400 //Bad Request
						render "NodeType Creation Failed"
					}else{
						ArrayList nodeTypes = [nodeType]
						webhookService.postToURL('nodetype', nodeTypes,'create')
						
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
			        def nodetype = NodeType.get(params.id)
			        if(nodetype){
						try{
							nodetype.delete(flush:true)
							
							ArrayList nodetypes = [nodetype]
							webhookService.postToURL('nodetype', nodetypes,'delete')
							
							response.status = 200
							render "Successfully Deleted."
						}catch(org.springframework.dao.DataIntegrityViolationException e) {
					        NodeType.withSession { session ->
					            session.clear()
					        }
	
							response.status = 403 //Bad Request
							render "Referential Integrity Violation: Please remove/reassign all Nodes/TemplateAttributes first."
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
		def path = iconService.getSmallIconPath()
		if(params.format && params.format!='none'){
			def nodetypes = NodeType.list()
			switch(params.format.toLowerCase()){
				case 'xml':
					def xml = xmlService.formatNodeTypes(nodetypes)
					render(text: xml, contentType: "text/xml")
					break;
				case 'json':
					def json = jsonService.formatNodeTypes(nodetypes)
					render(text:json, contentType: "text/json")
					break;
			}
		}else{
			params.max = Math.min(params.max ? params.int('max') : 10, 100)
			[nodeTypeInstanceList: NodeType.list(params), nodeTypeInstanceTotal: NodeType.count(),path:path]
		}
    }

    def create() {
		def externalPath = servletContext.getRealPath("${grailsApplication.config.images.icons.large}")
		def defaultPath = servletContext.getRealPath("/images/icons/64")
		def images = iconService.listImages(defaultPath,externalPath)
        [nodeTypeInstance: new NodeType(params),images:images]
    }

    def save() {
        def nodeTypeInstance = new NodeType(params)
        if (!nodeTypeInstance.save(flush: true)) {
            render(view: "create", model: [nodeTypeInstance: nodeTypeInstance])
            return
        }

		ArrayList nodetypes = [nodeTypeInstance]
		webhookService.postToURL('nodetype', nodetypes,'create')
		
		flash.message = message(code: 'default.created.message', args: [message(code: 'nodeType.label', default: 'NodeType'), nodeTypeInstance.id])
        redirect(action: "show", id: nodeTypeInstance.id)
    }

    def show() {
		def path = iconService.getLargeIconPath()
        def nodeTypeInstance = NodeType.get(params.id)
		
		def criteria = NodeTypeRelationship.createCriteria()
		def parents = criteria.list{
			eq("child", NodeType.get(params.id?.toLong()))
		}
		
		def criteria2 = NodeTypeRelationship.createCriteria()
		def children = criteria2.list{
			eq ("parent", NodeType.get(params.id?.toLong()))
		}
		
        if (!nodeTypeInstance) {
			if(params.format){
				response.status = 404 //Not Found
				render "${params.id} not found."
			}else{
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
	            redirect(action: "list")
	            return
			}
        }else{
			if(params.format && params.format!='none'){
				ArrayList nodetypes = [nodeTypeInstance]
				switch(params.format.toLowerCase()){
					case 'xml':
						def xml = xmlService.formatNodeTypes(nodetypes)
						render(text: xml, contentType: "text/xml")
						break;
					case 'json':
						def json = jsonService.formatNodeTypes(nodetypes)
						render(text:json, contentType: "text/json")
						break;
				}

			}else{
				[children:children,parents:parents,nodeTypeInstance: nodeTypeInstance,path:path]
			}
        }
    }

    def edit() {
		def externalPath = servletContext.getRealPath("${grailsApplication.config.images.icons.large}")
		def defaultPath = servletContext.getRealPath("/images/icons/64")
		def images = iconService.listImages(defaultPath,externalPath)
        def nodeTypeInstance = NodeType.get(params.id)
        if (!nodeTypeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
            return
        }

        [nodeTypeInstance: nodeTypeInstance,images:images]
    }

    def update() {
        def nodeTypeInstance = NodeType.get(params.id)
        if (!nodeTypeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (nodeTypeInstance.version > version) {
                nodeTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'nodeType.label', default: 'NodeType')] as Object[],
                          "Another user has updated this NodeType while you were editing")
                render(view: "edit", model: [nodeTypeInstance: nodeTypeInstance])
                return
            }
        }

        nodeTypeInstance.properties = params

        if (!nodeTypeInstance.save(flush: true)) {
            render(view: "edit", model: [nodeTypeInstance: nodeTypeInstance])
            return
        }

		ArrayList nodetypes = [nodeTypeInstance]
		webhookService.postToURL('nodetype', nodetypes,'edit')
		
		flash.message = message(code: 'default.updated.message', args: [message(code: 'nodeType.label', default: 'NodeType'), nodeTypeInstance.id])
        redirect(action: "show", id: nodeTypeInstance.id)
    }

    def delete() {
        def nodeTypeInstance = NodeType.get(params.id)
        if (!nodeTypeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
            return
        }

        try {
            nodeTypeInstance.delete(flush: true)
			
			ArrayList nodetypes = [nodeTypeInstance]
			webhookService.postToURL('nodetype', nodetypes,'delete')
			
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
        }catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
	
	def getNodeTypes(){
		def response = []
		List origList = Topic.executeQuery( "select  new map(T.id as id, T.topicName as topicName) from Topic T");
		List delList = PostTopics.executeQuery( "select  new map(T.id as id, T.topicName as topicName) from Topic T left join T.posts P where P.post.id=?",[params.id.toLong()]);
		List addList = origList - delList

		//List origList = NodeType.findAll()

		response = [dellist:dellist,addlist:addlist];
		render response as JSON
	}
	
	def getTemplateAttributes = {
			def response = []
			def attrs = []
			if(params.templateid){
				println("")
				List attributes = Attribute.executeQuery("select new map(A.id as id,A.name as name) from Attribute as A order by A.name asc")
				List atts = TemplateAttribute.executeQuery("select new map(TA.id as tid,A.id as id,A.name as attributename,F.dataType as datatype) from TemplateAttribute as TA left join TA.attribute as A left join A.filter as F where TA.template.id=${params.templateid} order by A.name asc");
				atts.each(){
					attrs += [tid:it.tid,id:it.id,key:it.templatevalue,val:it.attributename,datatype:it.datatype];
				}
				
				response += [attList:attributes,atts:attrs]
			}

			render response as JSON
	}
}
