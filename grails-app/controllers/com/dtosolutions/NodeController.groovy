package com.dtosolutions

import groovy.xml.MarkupBuilder
import org.custommonkey.xmlunit.*
import grails.converters.JSON
import grails.converters.XML
import java.util.Date;

class NodeController {
	
	def searchableService
	//def springSecurityService
	
    static allowedMethods = [get: "POST", save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }
	
    def list() {

		def nodes = Node.list(params)
		if(params.format){
			def writer = new StringWriter()
			def xml = new MarkupBuilder(writer)
			switch(params.format){
				case 'xml':
				case 'XML':
					xml.nodes() {
						nodes.each(){ val1 ->
							def attributequery = "select new map(TV.value as value,A.name as attribute,TA.required as required) from TemplateValue as TV left join TV.node as N left join TV.templateattribute as TA left join TA.attribute as A where N.id=${val1.id.toLong()}"
							def values = TemplateValue.executeQuery(attributequery);
							
							node(id:val1.id,name:val1.name,type:val1.nodetype.name,tags:val1.tags){
								description(val1.description)
									attributes(){
										values.each{ val2 ->
											attribute(name:val2.attribute,value:val2.value,required:val2.required)
										}
									}
							}
						}
					}
					break;
			}

			render(text: writer.toString(), contentType: "text/xml")
		}else{
        	params.max = Math.min(params.max ? params.int('max') : 10, 100)
			[nodeInstanceList: Node.list(params), nodeInstanceTotal: Node.count()]
		}
    }

    def create() {
        [nodeList: Node.list(),nodeInstance: new Node(params)]
    }

    def save() {
		def adults = ''
		def parents
		params.parents.each{ key ->
			adults += "${key},"
		}
		if(params.parents){ parents = Node.findAll("from Node as N where N.id IN (${adults[0..-2]})") }
		
		def kinder = ''
		def children
		params.children.each{ key ->
			kinder += "${key},"
		}
		if(params.children){ children = Node.findAll("from Node as N where N.id IN (${kinder[0..-2]})") }

		if((params.name && params.name!='null') && (params.status && params.status!='null') && (params.importance && params.importance!='null') && (params.nodetype && params.nodetype!='null')){
			Node nodeInstance  = new Node()
			nodeInstance.name = params.name
			nodeInstance.description = params.description
			nodeInstance.status = params.status
			nodeInstance.importance = params.importance
			nodeInstance.tags = params.tags
			nodeInstance.nodetype = NodeType.get(params.nodetype.toLong())
			nodeInstance.dateCreated = new Date()
			nodeInstance.dateModified = new Date()
						
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
							childNode.parent = nodeInstance
							childNode.child = child
							childNode.save(flush: true)
						}
					}
				}
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
        def nodeInstance = Node.get(params.id)
		
		def criteria = ChildNode.createCriteria()
		def parents = criteria.list{
			eq("child", Node.get(params.id?.toLong()))
		}
		
		def criteria2 = ChildNode.createCriteria()
		def children = criteria2.list{
			eq ("parent", Node.get(params.id?.toLong()))
		}
		
        if (!nodeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'node.label', default: 'Node'), params.id])
            redirect(action: "list")
            return
        }

        [children:children,parents:parents,nodeInstance: nodeInstance]
    }

    def edit() {
        def nodeInstance = Node.get(params.id)
		//def nodes = Node.findAll("from Node as N where N.id!=${params.id}") 
		def criteria = Node.createCriteria()
		def nodes = criteria.list{
			ne ("id", params.id?.toLong())
		}
		
        if (!nodeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'node.label', default: 'Node'), params.id])
            redirect(action: "list")
            return
        }

        [nodes:nodes,nodeInstance: nodeInstance]
    }

    def update() {
		def adults = ''
		def parents
		params.parents.each{ key ->
			adults += "${key},"
		}
		if(params.parents){ parents = Node.findAll("from Node as N where N.id IN (${adults[0..-2]}) and N.id!=${params.id}") }
		
		def kinder = ''
		def children
		params.children.each{ key ->
			kinder += "${key},"
		}
		if(params.children){ children = Node.findAll("from Node as N where N.id IN (${kinder[0..-2]}) and N.id!=${params.id}") }
		
		if((params.name && params.name!='null') && (params.status && params.status!='null') && (params.importance && params.importance!='null') && (params.nodetype && params.nodetype!='null')){
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
			nodeInstance.importance = params.importance
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
		def response = []
		if(params.id){
			List atts = Node.executeQuery("select new map(N.id as id,N.name as name) from Node as N");
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
