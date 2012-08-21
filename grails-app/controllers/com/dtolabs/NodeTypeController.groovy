package com.dtolabs

import com.dtolabs.Node
import com.dtolabs.NodeType
import com.dtolabs.NodeTypeRelationship
import grails.converters.JSON
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured
import com.dtolabs.yana2.springacl.DefaultProjectAccess
import com.dtolabs.yana2.springacl.ProjectAccess

@Secured(['ROLE_YANA_ADMIN', 'ROLE_YANA_USER', 'ROLE_YANA_ARCHITECT', 'ROLE_YANA_SUPERUSER'])
@DefaultProjectAccess(ProjectAccess.Level.architect)
class NodeTypeController {

	def iconService;
	def springSecurityService
	def xmlService
	def jsonService
	def webhookService
    def projectService

    static defaultAction = "list"

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @ProjectAccess(ProjectAccess.Level.read)
	def api(){
		switch(request.method){
			case "POST":
                def project = projectService.findProject(params.project)
                if(!project){
                    response.status=404
                    break
                }
                if(!projectService.authorizedArchitectPermission(project)){
                    break
                }

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
						if(params.format && params.format!='none'){
							switch(params.format.toLowerCase()){
								case 'xml':
									def xml = xmlService.formatNodeTypes(nodeTypes)
									render(text: xml, contentType: "text/xml")
									break;
								case 'json':
									def jsn = jsonService.formatNodeTypes(nodeTypes)
									render(text:jsn, contentType: "text/json")
									break;
							}
						}else{
							render "Successfully Created."
						}
						
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
                def project = projectService.findProject(params.project)
                if (!project) {
                    response.status = 404
                    break
                }
                if (!projectService.authorizedArchitectPermission(project)) {
                    break
                }

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
							render "Referential Integrity Violation: Please remove/reassign all Nodes/NodeAttributes first."
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
   }

    def index() {
        redirect(action: "list", params: params)
    }

    @ProjectAccess(ProjectAccess.Level.read)
    def list() {
        def project = projectService.findProject(params.project)
		def path = iconService.getSmallIconPath()
		if(params.format && params.format!='none'){
			def nodetypes = NodeType.findAllByProject(project)
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
            [nodeTypeInstanceList: NodeType.findAllByProject(project, params), nodeTypeInstanceTotal: NodeType.countByProject(project), path: path]
		}
    }

    def create() {
        def externalPath = servletContext.getRealPath("${grailsApplication.config.images.icons.large}")
		def defaultPath = servletContext.getRealPath("/images/icons/64")
		def images = iconService.listImages(defaultPath,externalPath)
        params.project=null // Nullify param because project.id is the expected
                            // value by save() not project's name

        [nodeTypeInstance: new NodeType(params),images:images]
    }

    def save() {
        def project = Project.findByName(params.project)
        if (!project) {
            response.status = 404
            return render(text: message(code: 'default.not.found.message',
                    args: ['Project', params.project],
                    default: "Project {0} was not found"))
        }

        params.project=null // Nullify param because project.id is the expected
                            // value by save() not project's name

        def nodeTypeInstance = new NodeType(params)
        nodeTypeInstance.project = project
        if (!nodeTypeInstance.save(flush: true)) {
            return render(view: "create", model: [nodeTypeInstance: nodeTypeInstance])
        }

		ArrayList nodetypes = [nodeTypeInstance]
		webhookService.postToURL('nodetype', nodetypes,'create')
		
		flash.message = message(code: 'default.created.message', args: [message(code: 'nodeType.label', default: 'NodeType'), nodeTypeInstance.id])
        redirect(action: "show", id: nodeTypeInstance.id)
    }

    @ProjectAccess(ProjectAccess.Level.read)
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
			if(params.format){
				response.status = 404 //Not Found
				render "${params.id} not found."
			}else{
	            flash.message = message(code: 'default.not.found.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
	            redirect(action: "list")
	            return
			}
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
		
		if(params.format){
			response.status = 200 //Not Found
			render "Successfully edited."
		}else{
			flash.message = message(code: 'default.updated.message', args: [message(code: 'nodeType.label', default: 'NodeType'), nodeTypeInstance.id])
			redirect(action: "show", id: nodeTypeInstance.id)
		}
    }

    def delete() {
        def nodeTypeInstance = NodeType.get(params.id)
        if (!nodeTypeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
                    message(code: 'nodeType.label', default: 'NodeType'), params.id])
            return redirect(action: "list")
        }
        if (nodeTypeInstance?.nodes?.size() > 0) {
            flash.message = message(code: 'nodeType.delete.nodesExist', args: [
                    message(code: 'nodeType.label', default: 'NodeType'), params.id])

            return redirect(action: "show", id: params.id)
        }

        try {
            nodeTypeInstance.delete(flush: true)
			log.info("Deleted nodeType: ${nodeTypeInstance.name}")

			ArrayList nodetypes = [nodeTypeInstance]
			webhookService.postToURL('nodetype', nodetypes,'delete')
			
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "list")

        } catch (DataIntegrityViolationException e) {

			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'nodeType.label', default: 'NodeType'), params.id])
            redirect(action: "show", id: params.id)
        }
    }


    /**
     * AJAX call used in the views, "/nodeType/{create,edit}.gsp"
     */
    @ProjectAccess(ProjectAccess.Level.read)
	def getNodeAttributes(){
        def response = []
        def attrs = []
        def NodeType nodeType = NodeType.get(params.templateid.toLong())
        if (nodeType) {

            def attributes = []
            Attribute.list().each() {
                attributes += [id:it.id, name:it.name]
            }

            NodeAttribute.findAllByNodetype(nodeType).each() { NodeAttribute na ->

                attrs += [tid:na.nodetype.id, id:na.id,
                        val:na.attribute.name, datatype:na.attribute.filter.dataType];
            }

            response += [attList:attributes, atts:attrs]
        }

        render response as JSON

    }
}
