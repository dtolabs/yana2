package com.dtosolutions

import grails.converters.JSON
import java.util.Date;
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_USER','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class NodeController {
	
	def iconService
	def springSecurityService
	def xmlService
	def jsonService
	def webhookService
	
    //static allowedMethods = [get: "POST", save: "POST", update: "POST", delete: "POST"]
   
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
				this.save()
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
				this.delete()
				break
		  }
		return
	}
	
	def webhook(){
		switch(request.method){
			case "POST":
			   	def json = request.JSON
				params.service = params.controller
				Webhook webhookInstance = Webhook.findByUrlAndService(params.url,params.controller)
				if(!webhookInstance){
					def user = springSecurityService.isLoggedIn() ? User.get(springSecurityService.principal.id) : null
					params.user=user
					params.service='node'
					webhookInstance = new Webhook(params)
				}else{
			        println("URL EXISTS: PLEASE CHECK YOUR REGISTERED WEBHOOKS TO MAKE SURE THIS IS NOT A DUPLICATE.")
			        return
				}
			    if (!webhookInstance.save(flush: true)) {
			        println("INVALID/MALFORMED DATA: PLEASE SEE DOCS FOR 'JSON' FORMED STRING AND PLEASE TRY AGAIN.")
			        return
			    }
				flash.message = message(code: 'default.created.message', args: [message(code: 'webhook.label', default: 'Webhook'), webhookInstance.id])
		        redirect(controller:"webhook",action:"show", id: webhookInstance.id)
				break
			default:
				println("INCORRECT REQUEST METHOD: EXPECTING POST METHOD. PLEASE TRY AGAIN.")
				break;
	   }
   }
	
	def createWebhook(){}
	
    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
		String path = iconService.getSmallIconPath()
		def nodes = Node.list(params)
		if(params.format && params.format!='none'){
			switch(params.format.toLowerCase()){
				case 'xml':
					def xml = xmlService.formatNodes(nodes)
					render(text: xml, contentType: "text/xml")
					break;
				case 'json':
					def json = jsonService.formatNodes(nodes)
					render(text:json, contentType: "text/json")
					break;
			}
		}else{
        	params.max = Math.min(params.max ? params.int('max') : 10, 100)
			[nodeInstanceList: Node.list(params), nodeInstanceTotal: Node.count(),path:path]
		}
    }

    def create() {

        [nodeList: Node.list(),params:params]
    }

	def clone(){
		Node nodeInstance = Node.get(params.id)

		Date now = new Date()
		
		Node node = new Node()
		node.name = nodeInstance.name+"_clone"
		node.description = nodeInstance.description
		node.status = nodeInstance.status
		node.tags = nodeInstance.tags
		node.nodetype = nodeInstance.nodetype
		node.dateCreated =  now

        if (!node.save(flush: true)) {
			flash.message = message(code: 'Failed to clone node ${nodeInstance.id}')
            redirect(action: "show", id: nodeInstance.id)
        }else{
			nodeInstance.templateValues.each(){
				def tv = new TemplateValue()
				tv.node = node
				tv.templateattribute = it.templateattribute
				tv.value = it.value
				tv.dateCreated = now
				tv.save(flush: true)
			}
		
			flash.message = message(code: 'default.created.message', args: [message(code: 'node.label', default: 'Node'), nodeInstance.id])
	        redirect(action: "show", id: node.id)
        }
	}
	
    def save() {
		Node[] parents
		if(params.parents){
			Long[] adults = Eval.me("${params.parents}")
			if(params.parents){ parents = Node.findAll("from Node as N where N.id IN (:ids)",[ids:adults]) }
		}
		
		Node[] children
		if(params.children){
			Long[] kinder = Eval.me("${params.children}")
			if(params.children){ children = Node.findAll("from Node as N where N.id IN (:ids)",[ids:children]) }
		}

		if((params.name && params.name!='null') && (params.status && params.status!='null') && (params.nodetype && params.nodetype!='null')){
			params.nodetype = NodeType.get(params.nodetype.toLong())
			Node nodeInstance  = new Node(params)
			nodeInstance.dateCreated = new Date()
						
	        if (!nodeInstance.save(flush: true)) {
	            render(view: "create", model: [nodeInstance: nodeInstance])
	            return
	        }else{
				Date now = new Date()
				params.each{ key, val ->
					if (key.contains('att') && !key.contains('_filter') && !key.contains('_require')) {
						TemplateAttribute att = TemplateAttribute.get(key[3..-1].toInteger())
					   new TemplateValue(node:nodeInstance,templateattribute:att,value:val,dateCreated:now,dateModified:now).save(failOnError:true)
					}
				}
							
				
				if(parents){
					parents.each{ parent ->
						ChildNode parentNode = ChildNode.findByParentAndChild(parent,nodeInstance)
						if(!parentNode){
							parentNode = new ChildNode()
							parentNode.relationshipName = "${parent.name}_${nodeInstance.name} [${NodeTypeRelationship.findRoleNameByParent(parent.nodetype)}]"
							parentNode.parent = parent
							parentNode.child = nodeInstance
							parentNode.save(flush: true)
						}
					}
				}

				if(children){
					children.each{ child ->
						ChildNode childNode = ChildNode.findByParentAndChild(nodeInstance,child)
						if(!childNode){
							childNode = new ChildNode()
							childNode.relationshipName = "${nodeInstance.name}_${child.name} [${NodeTypeRelationship.findRoleNameByParent(nodeInstance.nodetype)}]"
							childNode.parent = nodeInstance
							childNode.child = child
							childNode.save(flush: true)
						}
					}
				}

				ArrayList nodes = [nodeInstance]
				
				webhookService.postToURL('node', nodes)
				
				flash.message = message(code: 'default.created.message', args: [message(code: 'node.label', default: 'Node'), nodeInstance.id])
		        redirect(action: "show", id: nodeInstance.id)
	        }
		}else{
			Node nodeInstance  = new Node()
			flash.message = 'Required fields not filled out. Please try again'
			render(view: "create", model: [nodeInstance: nodeInstance,parents:parents,children:children,params: params])
		}
    }

    def show() {
		String path = iconService.getLargeIconPath()
		String smallpath = iconService.getSmallIconPath()
		NodeType nodeTypeInstance = NodeType.get(params.id)

        Node nodeInstance = Node.get(params.id)
		List tagList=[]

		if(params.format && params.format!='none'){
			ArrayList nodes = [nodeInstance]
			switch(params.format.toLowerCase()){
				case 'xml':
					def xml = xmlService.formatNodes(nodes)
					render(text: xml, contentType: "text/xml")
					break;
				case 'json':
					def json = jsonService.formatNodes(nodes)
					render(text:json, contentType: "text/json")
					break;
			}
		}else{
			def criteria = ChildNode.createCriteria()
			def parents = criteria.list{
				eq("child", Node.get(params.id?.toLong()))
			}
			
			def criteria2 = ChildNode.createCriteria()
			def children = criteria2.list{
				eq ("parent", Node.get(params.id?.toLong()))
			}
			
			if(nodeInstance?.tags){
				tagList = nodeInstance.tags.split(',')
			}
			
	        if (!nodeInstance) {
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'node.label', default: 'Node'), params.id])
	            redirect(action: "list")
	            return
	        }

			render(view:"show",model:[children:children,parents:parents,nodeInstance: nodeInstance,path:path,smallpath:smallpath,taglist:tagList])
		}
    }

    def edit() {
        Node nodeInstance = Node.get(params.id)
		def criteria = Node.createCriteria()
		def nodes = criteria.list{
			ne ("id", params.id?.toLong())
		}
		
        if (!nodeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'node.label', default: 'Node'), params.id])
            redirect(action: "list")
            return
        }

		def pquery = """
select new map(N.id as id,N.name as name,NTP.parentCardinality as size) 
from Node as N 
left join N.nodetype as NT 
left join NT.parents as NTP 
where NTP.child=${nodeInstance.nodetype.id} 
and (NTP.parentCardinality>=${nodeInstance.parents.size()} or NTP.parentCardinality is null)
"""
		def parents = Node.executeQuery(pquery)

		def cquery = """
select new map(N.id as id,N.name as name) 
from Node as N 
left join N.nodetype as NT 
left join NT.children as NTP 
where NTP.parent=${nodeInstance.nodetype.id} 
and (NTP.childCardinality>=${nodeInstance.children.size()} or NTP.childCardinality is null)
"""
		def children = Node.executeQuery(cquery)
        [parents:parents,children:children,nodes:nodes,nodeInstance: nodeInstance]
    }

    def update() {
		Node[] parents
		if(params.parents){
			Long[] adults = Eval.me("${params.parents}")
			if(params.parents){ parents = Node.findAll("from Node as N where N.id IN (:ids) and N.id!=${params.id}",[ids:adults]) }
		}
		
		Node[] children
		if(params.children){
			Long[] kinder = Eval.me("${params.children}")
			if(params.children){ children = Node.findAll("from Node as N where N.id IN (:ids) and N.id!=${params.id}",[ids:kinder]) }
		}
		
		if((params.name && params.name!='null') && (params.status && params.status!='null') && (params.nodetype && params.nodetype!='null')){
	        def nodeInstance = Node.get(params.id)
			Date now = new Date()
	        if (!nodeInstance) {
	            flash.message = message(code: 'default.not.found.message', args: [message(code: 'node.label', default: 'Node'), params.id])
	            redirect(action: "list")
	            return
	        }
	
	        if (params.version) {
	            def version = params.version.toLong()
	            if (nodeInstance.version > version) {
	                nodeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
	                          [message(code: 'node.label', default: 'Node')] as Object[],
	                          "Another user has updated this Node while you were editing")
	                render(view: "edit", model: [nodeInstance: nodeInstance])
	                return
	            }
	        }
	
			nodeInstance.name = params.name
			nodeInstance.description = params.description
			nodeInstance.status = params.status
			nodeInstance.tags = params.tags
			nodeInstance.dateCreated = now
			nodeInstance.dateModified = now
			
	        if (!nodeInstance.save(flush: true)) {
	            render(view: "edit", model: [nodeInstance: nodeInstance])
	            return
	        }else{
				params.each{ key, val ->
					if (key.contains('att') && !key.contains('_filter') && !key.contains('_require')) {
						TemplateValue tval = TemplateValue.get(key[3..-1].toInteger())
						tval.value = val
						tval.dateCreated = now
						tval.dateModified = now
						tval.save(flush: true)
					}
				}
				
				// delete all from childnode where node is child and reassign
				def childNodes = ChildNode.findByChild(nodeInstance)
				childNodes.each{
					it.delete()
				}

				parents.each{
					def chnode = new ChildNode()
					chnode.relationshipName = "${it.name}_${nodeInstance.name} [${NodeTypeRelationship.findRoleNameByParent(it.nodetype)}]"
					chnode.parent=it
					chnode.child=nodeInstance
					chnode.save(flush: true)
				}
				// delete all from childnode where node is parent and reasign
				def parentNodes = ChildNode.findByParent(nodeInstance)
				parentNodes.each{
					it.delete()
				}
				children.each{
					def chnode = new ChildNode()
					chnode.relationshipName = "${nodeInstance.name}_${it.name} [${NodeTypeRelationship.findRoleNameByParent(nodeInstance.nodetype)}]"
					chnode.parent=nodeInstance
					chnode.child=it
					chnode.save(flush: true)
				}

				flash.message = message(code: 'default.created.message', args: [message(code: 'node.label', default: 'Node'), nodeInstance.id])
		        redirect(action: "show", id: nodeInstance.id)
	        }
			render(view: "edit", model: [nodeList: Node.list(),nodeInstance: nodeInstance])
		}else{
			def nodeInstance = Node.get(params.id)
			flash.message = 'Required fields not filled out. Please try again'
			render(view: "edit", model: [parents:parents,children:children,nodeInstance: nodeInstance,params: params])
		}
    }

    def delete() {
		Node.withTransaction{ status ->
	        def nodeInstance = Node.get(params.id)
	        if (!nodeInstance) {
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'node.label', default: 'Node'), params.id])
	            redirect(action: "list")
	            return
	        }
	
	        try {
	            nodeInstance.delete(flush: true)
				flash.message = message(code: 'default.deleted.message', args: [message(code: 'node.label', default: 'Node'), params.id])
				redirect(action: "list")
	        }catch (Exception e) {
				status.setRollbackOnly()
				flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'node.label', default: 'Node'), params.id])
	            redirect(action: "show", id: params.id)
	        }
		}
    }
	
	def getNodeParents = {
		Node nodeInstance = Node.get(params.id)
		def response = []
		if(params?.id.trim()){
			def pquery = """
select new map(N.id as id,N.name as name,NTP.parentCardinality as size) 
from Node as N 
left join N.nodetype as NT 
left join NT.parents as NTP 
where NTP.child=${nodeInstance.nodetype.id} 
and (NTP.parentCardinality>=${nodeInstance.parents.size()} or NTP.parentCardinality is null)
"""
			List atts = Node.executeQuery(pquery)
			atts.each(){
				response += [id:it.id,name:it.name];
			}
			render response as JSON
		}
	}
	

	def getNodeChildren = {
		Node nodeInstance = Node.get(params.id)
		def response = []
		if(params?.id.trim()){
			def cquery = """
select new map(N.id as id,N.name as name) 
from Node as N 
left join N.nodetype as NT 
left join NT.children as NTP 
where NTP.parent=${nodeInstance.nodetype.id} 
and (NTP.childCardinality>=${nodeInstance.children.size()} or NTP.childCardinality is null)
"""
			List atts = Node.executeQuery(cquery)
			atts.each(){
				response += [id:it.id,name:it.name];
			}
			render response as JSON
		}
	}
	
	def getTemplateAttributes = {

			def response = []

			if(params.templateid){
				println("")
				List atts = []
				if(params.node){
					atts = TemplateValue.executeQuery("select new map(TV.id as tid,TV.value as templatevalue,TA.required as required,A.name as attributename,A.id as id,F.dataType as datatype,F.regex as filter) from TemplateValue as TV left join TV.templateattribute as TA left join TA.attribute as A left join A.filter as F where TA.template.id=${params.templateid} and TV.node.id=${params.node}");
				}else{
					atts = TemplateAttribute.executeQuery("select new map(A.id as id,TA.required as required,A.name as attributename,F.dataType as datatype,F.regex as filter) from TemplateAttribute as TA left join TA.attribute as A left join A.filter as F where TA.template.id=${params.templateid}");
				}
				atts.each(){
					response += [tid:it.tid,id:it.id,required:it.required,key:it.templatevalue,val:it.attributename,datatype:it.datatype,filter:it.filter];
				}
			}

			render response as JSON
	}
}
