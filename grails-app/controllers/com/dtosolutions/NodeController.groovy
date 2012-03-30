package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON
import grails.converters.XML
import java.util.Date;

class NodeController {

	def springSecurityService
	
    static allowedMethods = [get: "POST", save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
		
		if(params.format){
			def response = []
			switch(params.format){
				case 'xml':
				case 'XML':
					def nodequery = "select N.id,N.name,N.description,T.templateName,NT.name as nodetype,N.status,N.importance,N.tags from Node as N left join N.nodetype as NT left join N.template as T"
					
					def nodes = Node.executeQuery(nodequery);
					
					nodes.each(){
						def attributequery = "select new map(TV.value as value,A.name as attribute,TA.required as required) from TemplateValue as TV left join TV.node as N left join TV.templateattribute as TA left join TA.attribute as A where N.id=${it[0].toLong()}"
						def values = TemplateValue.executeQuery(attributequery);
						response += [attribute:[values.attribute,values.value],required:values.required];
					}
					break;
			}
			render response as XML
		}else{
        	params.max = Math.min(params.max ? params.int('max') : 10, 100)
			[nodeInstanceList: Node.list(params), nodeInstanceTotal: Node.count()]
		}
    }

    def create() {
        [nodeInstance: new Node(params)]
    }

    def save() {
		if((params.name.trim() && params.name!='null') && (params.template.id.trim() && params.template.id!='null') && (params.status.trim() && params.status!='null') && (params.importance.trim() && params.importance!='null') && (params.nodetype.id.trim() && params.nodetype.id!='null')){
			Node nodeInstance  = new Node()
			nodeInstance.name = params.name
			nodeInstance.description = params.description
			nodeInstance.template = Template.get(params.template.id.toLong())
			nodeInstance.status = params.status
			nodeInstance.importance = params.importance
			nodeInstance.nodetype = NodeType.get(params.nodetype.id.toLong())
			nodeInstance.dateCreated = new Date()
			nodeInstance.dateModified = new Date()
			//n.save(failOnError:true)
						
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
				flash.message = message(code: 'default.created.message', args: [message(code: 'node.label', default: 'Node'), nodeInstance.id])
		        redirect(action: "show", id: nodeInstance.id)
	        }
		}else{
			flash.message = 'Required fields not filled out. Please try again'
			render(view: "create", model: [params: params])
		}
    }

    def show() {
        def nodeInstance = Node.get(params.id)
        if (!nodeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'node.label', default: 'Node'), params.id])
            redirect(action: "list")
            return
        }

        [nodeInstance: nodeInstance]
    }

    def edit() {
        def nodeInstance = Node.get(params.id)
        if (!nodeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'node.label', default: 'Node'), params.id])
            redirect(action: "list")
            return
        }

        [nodeInstance: nodeInstance]
    }

    def update() {
		if((params.name.trim() && params.name!='null') && (params.template.id.trim() && params.template.id!='null') && (params.status.trim() && params.status!='null') && (params.importance.trim() && params.importance!='null') && (params.nodetype.id.trim() && params.nodetype.id!='null')){
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
			render(view: "edit", model: [nodeInstance: nodeInstance])
		}else{
		flash.message = 'Required fields not filled out. Please try again'
		render(view: "create", model: [params: params])
	}
    }

    def delete() {
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
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'node.label', default: 'Node'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
	
	def getNodeParents = {
		def response = []
		if(params.id){
			List atts = Node.executeQuery("select new map(N.id as id,N.name as name) from Node as N where N.nodetype.id=${params.id}");
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
	
	def getTemplates = {
			def response = []
			if(params.id){
				List temps = Template.executeQuery("select new map(T.id as id,T.templateName as name) from Template as T where T.nodetype.id=${params.id}");
				temps.each(){
					response += [id:it.id,name:it.name];
				}
			}

			render response as JSON
	}
}
