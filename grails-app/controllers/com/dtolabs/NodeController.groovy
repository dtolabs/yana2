package com.dtolabs

import com.dtolabs.ChildNode
import com.dtolabs.NodeType
import com.dtolabs.Node
import com.dtolabs.NodeAttribute
import com.dtolabs.NodeValue
import grails.converters.JSON
import java.util.Date
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_USER','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class NodeController {
	
	def iconService
	def springSecurityService
	def xmlService
	def jsonService
	def webhookService
	
    //static allowedMethods = [get: "POST", save: "POST", update: "POST", delete: "POST"]
   
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
				if(params.id){
			        def node = Node.get(params.id)
			        if(node){
			          node.delete()
					  
					  ArrayList nodes = [node]
					  webhookService.postToURL('node', nodes,'delete')
					  
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
		   case "GET":
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
		String path = iconService.getSmallIconPath()
		ArrayList nodes = []
		if(params.nodetype){
			List nodetypes = []
			params.nodetype.each{
				nodetypes += it.toLong()
			}
			def criteria = Node.createCriteria()
			nodes = criteria.list{
						not{'in'("nodetype.id",nodetypes)}
			}
		}else{
			nodes = Node.list(params)
		}
		if(params.format && params.format!='none'){
			switch(params.format.toLowerCase()){
				case 'xml':
					def xml = xmlService.formatNodes(nodes)
					render(text: xml, contentType: "text/xml")
					break
				case 'json':
					def json = jsonService.formatNodes(nodes)
					render(text:json, contentType: "text/json")
					break
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
			nodeInstance.nodeValues.each(){
				def tv = new NodeValue()
				tv.node = node
				tv.nodeattribute = it.nodeattribute
				tv.value = it.value
				tv.dateCreated = now
				tv.save(flush: true)
			}
		
			flash.message = message(code: 'default.created.message', args: [message(code: 'node.label', default: 'Node'), nodeInstance.id])
	        redirect(action: "show", id: node.id)
        }
	}
	
	boolean addNodeParent(String name,Node parent,Node nodeInstance){
		ChildNode parentNode = ChildNode.findByParentAndChild(parent,nodeInstance)
		if(!parentNode){
			parentNode = new ChildNode()
			parentNode.relationshipName = name
			parentNode.parent = parent
			parentNode.child = nodeInstance
			parentNode.save(flush: true)
			return true
		}else{
			return false
		}
	}
	
	boolean addNodeChild(String name,Node nodeInstance,Node child){
		ChildNode childNode = ChildNode.findByParentAndChild(nodeInstance,child)
		if(!childNode){
			childNode = new ChildNode()
			childNode.relationshipName = name
			childNode.parent = nodeInstance
			childNode.child = child
			childNode.save(flush: true)
			return true
		}else{
			return false
		}
	}
	
	String getRelationshipName(Node parent,Node child){
		String rolename = NodeTypeRelationship.findByParent(parent.nodetype).roleName
		String name = (rolename)?"${parent.name} [$rolename]":"${parent.name}"
		return name
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
			if(params.children){ children = Node.findAll("from Node as N where N.id IN (:ids)",[ids:kinder]) }
		}
		
		params.parents = null
		params.parents = null
		
		if((params.name && params.name!='null') && (params.status && params.status!='null') && (params.nodetype && params.nodetype!='null')){
			params.nodetype = NodeType.get(params.nodetype.toLong())
			Node nodeInstance  = new Node(params)
			nodeInstance.dateCreated = new Date()
						
	        if (!nodeInstance.save(flush: true)) {
				if(params.action=='api'){
					response.status = 400 //Bad Request
					render "Node Creation Failed"
				}else{
		            render(view: "create", model: [nodeInstance: nodeInstance])
		            return
				}
	        }else{
				Date now = new Date()
				params.each{ key, val ->
					if (key.contains('att') && !key.contains('_filter') && !key.contains('_require')) {
						NodeAttribute att = NodeAttribute.get(key[3..-1].toInteger())
					   new NodeValue(node:nodeInstance,nodeattribute:att,value:val,dateCreated:now,dateModified:now).save(failOnError:true)
					}
				}

				def parentList = getNodeParentsByCardinality(nodeInstance) 	
				if(parents){
					parents.each{ parent ->
						boolean goodParent = false
						parentList?.each() { pit ->
							if(pit.contains(parent)){ goodParent = true }
						}
						if(goodParent){
							//String name = (NodeTypeRelationship.findRoleNameByParent(parent.nodetype))?"${parent.name}_${nodeInstance.name} [${NodeTypeRelationship.findRoleNameByParent(parent.nodetype)}]":"${parent.name}_${nodeInstance.name}"
							String name = getRelationshipName(parent,nodeInstance)
							addNodeParent(name,parent,nodeInstance)
						}
					}
				}

				def childList = getNodeChildrenByCardinality(nodeInstance)
				if(children){
					children.each{ child ->
						boolean goodChild = false
						childList?.each() { cit ->
							if(cit.contains(child)){ goodChild = true }
						}
						if(goodChild){
							//String name = (NodeTypeRelationship.findRoleNameByParent(nodeInstance.nodetype))?"${nodeInstance.name}_${child.name} [${NodeTypeRelationship.findRoleNameByParent(nodeInstance.nodetype)}]":"${nodeInstance.name}_${child.name}"
							String name = getRelationshipName(nodeInstance,child)
							addNodeChild(name,nodeInstance,child)
						}
					}
				}

				ArrayList nodes = [nodeInstance]
				webhookService.postToURL('node', nodes,'create')
				
				if(params.action=='api'){
					response.status = 200
					if(params.format && params.format!='none'){
						switch(params.format.toLowerCase()){
							case 'xml':
								def xml = xmlService.formatNodes(nodes)
								render(text: xml, contentType: "text/xml")
								break
							case 'json':
								def jsn = jsonService.formatNodes(nodes)
								render(text:jsn, contentType: "text/json")
								break
						}
					}else{
						render "Successfully Created."
					}
				}else{
					flash.message = message(code: 'default.created.message', args: [message(code: 'node.label', default: 'Node'), nodeInstance.id])
					redirect(action: "show", id: nodeInstance.id)
				}
	        }
		}else{
			if(params.action=='api'){
				response.status = 404 //Not Found
				render "${params.id} not found."
			}else{
				Node nodeInstance  = new Node()
				flash.message = 'Required fields not filled out. Please try again'
				render(view: "create", model: [nodeInstance: nodeInstance,parents:parents,children:children,params: params])
			}
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
			if(nodeInstance){
				switch(params.format.toLowerCase()){
					case 'xml':
						def xml = xmlService.formatNodes(nodes)
						render(text: xml, contentType: "text/xml")
						break
					case 'json':
						def json = jsonService.formatNodes(nodes)
						render(text:json, contentType: "text/json")
						break
				}
			}else{
				response.status = 404 //Not Found
				render "${params.id} not found."
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
		Node nodeInstance = Node.get(params.id)
		
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
			Date now = new Date()
	        if (!nodeInstance) {
				if(params.format){
					response.status = 404 //Not Found
					render "${params.id} not found."
				}else{
	            	flash.message = message(code: 'default.not.found.message', args: [message(code: 'node.label', default: 'Node'), params.id])
					redirect(action: "list")
					return
				}
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
						NodeValue tval = NodeValue.get(key[3..-1].toInteger())
						tval.value = val
						tval.dateCreated = now
						tval.dateModified = now
						tval.save(flush: true)
					}
				}
				
				// delete all from childnode where node is child and reassign
				//def parentNodes = ChildNode.findByChild(nodeInstance)
				//parentNodes.each{
				//	it.delete()
				//}

				parents.each{
					def pNode = ChildNode.findByChildAndParent(nodeInstance,it)
					if(pNode){
						pNode.parent=it
						pNode.child=nodeInstance
						pNode.save(flush: true)
					}else{
						String pname = getRelationshipName(it,nodeInstance)
						pNode = new ChildNode()
						pNode.relationshipName = pname
						pNode.parent=it
						pNode.child=nodeInstance
						pNode.save(flush: true)
					}
				}
				
				// delete all from childnode where node is parent and reasign
				//def childNodes = ChildNode.findByParent(nodeInstance)
				//childNodes.each{
				//	it.delete()
				//}
				
				children.each{
					def cNode = ChildNode.findByParentAndChild(nodeInstance,it)
					if(cNode){
						cNode.parent=nodeInstance
						cNode.child=it
						cNode.save(flush: true)
					}else{
						String cname = getRelationshipName(nodeInstance,it)
						cNode = new ChildNode()
						cNode.relationshipName = cname
						cNode.parent=nodeInstance
						cNode.child=it
						cNode.save(flush: true)
					}
				}

				ArrayList nodes = [nodeInstance]
				webhookService.postToURL('node', nodes,'edit')
				
				flash.message = message(code: 'default.updated.message', args: [message(code: 'node.label', default: 'Node'), nodeInstance.id])
		        redirect(action: "show", id: nodeInstance.id)
	        }
			
			render(view: "edit", model: [nodeList: Node.list(),nodeInstance: nodeInstance])
		}else{
			flash.message = 'Required fields not filled out. Please try again'
			render(view: "edit", model: [parents:parents,children:children,nodeInstance: nodeInstance,params: params])
		}
    }

    def delete() {
		Node.withTransaction{ status ->
	        Node nodeInstance = Node.get(params.id)
	        if (!nodeInstance) {
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'node.label', default: 'Node'), params.id])
	            redirect(action: "list")
	            return
	        }
	
	        try {
	            nodeInstance.delete(flush: true)
				
				ArrayList nodes = [nodeInstance]
				webhookService.postToURL('node', nodes,'delete')
			
				flash.message = message(code: 'default.deleted.message', args: [message(code: 'node.label', default: 'Node'), params.id])
				redirect(action: "list")
	        }catch (Exception e) {
				status.setRollbackOnly()
				flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'node.label', default: 'Node'), params.id])
	            redirect(action: "show", id: params.id)
	        }
		}
    }
	
	def getNodeParentsByCardinality(Node node) {
		def cardinality = (node.parents?.size())?node.parents?.size():0
		def parents = Node.findAll("from Node as N left join N.nodetype as NT left join NT.parents as NTP where NTP.child=${node.nodetype.id} and (NTP.parentCardinality>=${cardinality} or NTP.parentCardinality is null)")
		return parents
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
				response += [id:it.id,name:it.name]
			}
			render response as JSON
		}
	}
	
	def getNodeChildrenByCardinality(Node node){
		def cardinality = (node.children?.size())?node.children?.size():0
		def children = Node.findAll("from Node as N left join N.nodetype as NT left join NT.children as NTP where NTP.parent=${node.nodetype.id} and (NTP.childCardinality>=${cardinality} or NTP.childCardinality is null)")
		return children
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
				response += [id:it.id,name:it.name]
			}
			render response as JSON
		}
	}
	
	def getNodeAttributes = {

			def response = []

			if(params.templateid){
				println("")
				List atts = []
				if(params.node){
					atts = NodeValue.executeQuery("select new map(TV.id as tid,TV.value as nodevalue,TA.required as required,A.name as attributename,A.id as id,F.dataType as datatype,F.regex as filter) from NodeValue as TV left join TV.nodeattribute as TA left join TA.attribute as A left join A.filter as F where TA.nodetype.id=${params.templateid} and TV.node.id=${params.node}")
				}else{
					atts = NodeAttribute.executeQuery("select new map(A.id as id,TA.required as required,A.name as attributename,F.dataType as datatype,F.regex as filter) from NodeAttribute as TA left join TA.attribute as A left join A.filter as F where TA.nodetype.id=${params.templateid}")
				}
				atts.each(){
					response += [tid:it.tid,id:it.id,required:it.required,key:it.nodevalue,val:it.attributename,datatype:it.datatype,filter:it.filter]
				}
			}

			render response as JSON
	}
}
