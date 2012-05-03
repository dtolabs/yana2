package com.dtosolutions

import grails.converters.JSON
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class NodeTypeController {

	def iconService;
	def springSecurityService
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [nodeTypeInstanceList: NodeType.list(params), nodeTypeInstanceTotal: NodeType.count()]
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
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
            return
        }

        [children:children,parents:parents,nodeTypeInstance: nodeTypeInstance,path:path]
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
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
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
