package com.dtosolutions

import groovy.xml.MarkupBuilder
import org.custommonkey.xmlunit.*
import grails.converters.JSON
import grails.converters.XML
import java.util.Date;

class NodeApiController {
	
	def searchableService
	//def springSecurityService
	
    static allowedMethods = [get: "POST", save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(controller:"node",action: "list", params: params)
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

	        if (!params.id) {
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'node.label', default: 'Node'), params.id])
	            redirect(action: "list")
	            return
	        }
	
	        try {
				def nodelist = ''
				def nodes
				params.id.each{ key ->
					nodelist += "${key},"
				}
				if(params.id){ Node.executequery("delete from Node id IN (${nodelist[0..-2]})") }
	            nodeInstance.delete(flush: true)
				flash.message = message(code: 'default.deleted.message', args: [message(code: 'node.label', default: 'Node'), params.id])
				redirect(controller:"node",action: "list")
	        }catch (Exception e) {
				status.setRollbackOnly()
				flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'node.label', default: 'Node'), params.id])
	            redirect(controller:"node",action: "show", id: params.id)
	        }
		}
    }

}
