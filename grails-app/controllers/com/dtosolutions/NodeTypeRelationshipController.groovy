package com.dtosolutions

import com.dtolabs.NodeType
import com.dtolabs.NodeTypeRelationship
import org.springframework.dao.DataIntegrityViolationException
import com.dtolabs.NodeTypeRelationship
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class NodeTypeRelationshipController {

	def iconService
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [nodeTypeRelationshipInstanceList: NodeTypeRelationship.list(params), nodeTypeRelationshipInstanceTotal: NodeTypeRelationship.count()]
    }

    def create() {
		def cardinality = ['0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10','999999999':'*']
        [nodeTypeRelationshipInstance: new NodeTypeRelationship(params),cardinality:cardinality]
    }

    def save() {
		def cardinality = ['0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10','999999999':'*']
		def parent = NodeType.get(params.parent.id.toLong())
		def child = NodeType.get(params.child.id.toLong())
		def exists= NodeTypeRelationship.findByParentAndChild(parent,child)
		
		if(!exists){
	        def nodeTypeRelationshipInstance = new NodeTypeRelationship(params)
	        if (!nodeTypeRelationshipInstance.save(flush: true)) {
	            render(view: "create", model: [nodeTypeRelationshipInstance: nodeTypeRelationshipInstance,cardinality:cardinality])
	            return
	        }
	
			flash.message = message(code: 'default.created.message', args: [message(code: 'nodeTypeRelationship.label', default: 'NodeTypeRelationship'), nodeTypeRelationshipInstance.id])
	        redirect(action: "show", id: nodeTypeRelationshipInstance.id)
		}else{
			flash.message = message("Existing relationship for that Parent and child NodeType already exists. Please try again.")
	        render(view: "create", model: [nodeTypeRelationshipInstance: nodeTypeRelationshipInstance,cardinality:cardinality])
			return
		}
    }

    def show() {
		String path = iconService.getMedIconPath()
        def nodeTypeRelationshipInstance = NodeTypeRelationship.get(params.id)
        if (!nodeTypeRelationshipInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeTypeRelationship.label', default: 'NodeTypeRelationship'), params.id])
            redirect(action: "list")
            return
        }

        [nodeTypeRelationshipInstance: nodeTypeRelationshipInstance,path:path]
    }

    def edit() {
        def nodeTypeRelationshipInstance = NodeTypeRelationship.get(params.id)
        if (!nodeTypeRelationshipInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeTypeRelationship.label', default: 'NodeTypeRelationship'), params.id])
            redirect(action: "list")
            return
        }
		def cardinality = ['0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10','999999999':'*']
        [nodeTypeRelationshipInstance: nodeTypeRelationshipInstance,cardinality:cardinality]
    }

    def update() {
		def cardinality = ['0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10','999999999':'*']
        def nodeTypeRelationshipInstance = NodeTypeRelationship.get(params.id)
		if(!params.roleName){
			flash.message = "Rolename is a required field"
			render(view: "edit", model: [nodeTypeRelationshipInstance: nodeTypeRelationshipInstance,cardinality:cardinality])
		}else{
	        if (!nodeTypeRelationshipInstance) {
	            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeTypeRelationship.label', default: 'NodeTypeRelationship'), params.id])
	            redirect(action: "list")
	            return
	        }
	
	        if (params.version) {
	            def version = params.version.toLong()
	            if (nodeTypeRelationshipInstance.version > version) {
	                nodeTypeRelationshipInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
	                          [message(code: 'nodeTypeRelationship.label', default: 'NodeTypeRelationship')] as Object[],
	                          "Another user has updated this NodeTypeRelationship while you were editing")
	                render(view: "edit", model: [nodeTypeRelationshipInstance: nodeTypeRelationshipInstance,cardinality:cardinality])
	                return
	            }
	        }
	
	        nodeTypeRelationshipInstance.properties = params
	
	        if (!nodeTypeRelationshipInstance.save(flush: true)) {
	            render(view: "edit", model: [nodeTypeRelationshipInstance: nodeTypeRelationshipInstance])
	            return
	        }
	
			flash.message = message(code: 'default.updated.message', args: [message(code: 'nodeTypeRelationship.label', default: 'NodeTypeRelationship'), nodeTypeRelationshipInstance.id])
	        redirect(action: "show", id: nodeTypeRelationshipInstance.id)
		}
    }

    def delete() {
        def nodeTypeRelationshipInstance = NodeTypeRelationship.get(params.id)
        if (!nodeTypeRelationshipInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeTypeRelationship.label', default: 'NodeTypeRelationship'), params.id])
            redirect(action: "list")
            return
        }

        try {
            nodeTypeRelationshipInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'nodeTypeRelationship.label', default: 'NodeTypeRelationship'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'nodeTypeRelationship.label', default: 'NodeTypeRelationship'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
