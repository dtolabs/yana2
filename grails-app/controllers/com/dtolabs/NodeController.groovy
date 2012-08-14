package com.dtolabs

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import com.dtolabs.yana2.springacl.DefaultProjectAccess
import com.dtolabs.yana2.springacl.ProjectAccess

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_USER','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
@DefaultProjectAccess(ProjectAccess.Level.operator)
class NodeController {
	def iconService
	def springSecurityService
	def xmlService
	def jsonService
	def webhookService
	def nodeService
	def projectService

    static defaultAction = "list"

    @ProjectAccess(ProjectAccess.Level.read)
	def api() {
		switch (request.method) {
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

				if (params.id) {
					def node = nodeService.readNode(params.id)
					if (node) {
						nodeService.deleteNode(node)

						ArrayList nodes = [node]
						webhookService.postToURL('node', nodes,'delete')

						response.status = 200
						render "Successfully Deleted."
					} else {
						response.status = 404 //Not Found
						render "${params.id} not found."
					}
				} else {
					response.status = 400 //Bad Request
					render """DELETE request must include the id"""
				}
				break
		}
		return
	}

    @ProjectAccess(ProjectAccess.Level.read)
	def listapi() {
		switch (request.method) {
			case "GET":
			case "POST":
				def json = request.JSON
				this.list()
				break
		}
	}

    @ProjectAccess(ProjectAccess.Level.read)
	def index() {
		redirect(action: "list", params: params)
	}

    @ProjectAccess(ProjectAccess.Level.read)
	def list() {
        def project = projectService.findProject(params.project)
		String path = iconService.getSmallIconPath()
        def result=nodeService.listNodes(project,params)
        int totCount = result.total
        ArrayList nodes = result.nodes
		if (params.format && params.format!='none') {
			switch (params.format.toLowerCase()) {
				case 'xml':
					def xml = xmlService.formatNodes(nodes)
					render(text: xml, contentType: "text/xml")
					break
				case 'json':
					def json = jsonService.formatNodes(nodes)
					render(text:json, contentType: "text/json")
					break
			}
		} else {
			params.max = Math.min(params.max ? params.int('max') : 10, 100)
			[nodeInstanceList: nodes, nodeInstanceTotal: totCount, path:path]
		}
	}

	def create() {
        def project = projectService.findProject(params.project)
        def result = nodeService.listNodes(project)

		[nodeList: result.nodes,
		 nodeTypeList: NodeType.findAllByProject(project),
		 params:params]
	}

	def clone(){
		Node node = nodeService.readNode(params.id)
		try {		
			Node nodeInstance = nodeService.cloneNode(node)
			flash.message = message(code: 'default.created.message', args: [
				message(code: 'node.label', default: 'Node'),
				nodeInstance.id
			])
			redirect(action: "show", id: nodeInstance.id)
		} catch (Throwable t) {
            log.error("could not create clone",t)
			flash.message = message(code: 'default.not.created.message', args: [
				message(code: 'node.label', default: 'Node'),
				t.message
			])
			redirect(action: "show", id: params.id)
		}
	}

	def save() {
        def project = projectService.findProject(params.project)
        params.project = null

		Node nodeInstance = new Node(params)
        nodeInstance.project = project

        if (!nodeInstance.validate()) {
            return render(view: "create", model:[
                    nodeTypeList: NodeType.findAllByProject(project),
                    nodeInstance: nodeInstance,
                    params: params
            ])
        }

		try {
            nodeInstance =
			  nodeService.createNode(project, nodeInstance.nodetype,
				  				     params.name, params.description, params.tags,
									 getNodesByIdStrings(params.parents),
                                     getNodesByIdStrings(params.children),
                                     params.attributevalues)

		} catch (Exception t) {
			if (params.action == 'api') {
				response.status = 400 //Bad Request
				render "node creation failed"
			} else {
				render(view: 'create',
					   model: [nodeInstance: nodeInstance])
				flash.message = message(code: 'default.not.created.message', args: [
					message(code: 'node.label', default: 'Node'),
					params.name
				])
				redirect(action: "create", name: params.name)
			}
			return
		}
		
		if (params.action == 'api') {
			response.status = 200
			if (params.format && params.format != 'none') {
				switch (params.format.toLowerCase()) {
					case 'xml':
						def xml = xmlService.formatNodes(nodes)
						render(text: xml, contentType: "text/xml")
						break
					case 'json':
						def jsn = jsonService.formatNodes(nodes)
						render(text:jsn, contentType: "text/json")
						break
				}
			} else {
				render "Successfully Created."
			}
		} else {
			flash.message = message(code: 'default.created.message', args: [
				message(code: 'node.label', default: 'Node'),
				nodeInstance.id
			])
            response.status = 201
			redirect(action: "show", id: nodeInstance.id)
		}
	}

    def update() {
        def project = projectService.findProject(params.project)
		Node nodeInstance = nodeService.readNode(params.id)
		if (!nodeInstance) {
			if (params.action == 'api') {
				response.status = 400 //Not Found
				render "node with id ${params.id} not found"
			} else {
				flash.message = message(code: 'default.not.found.message', args: [
					message(code: 'node.label', default: 'Node'),
					params.id
				])
				redirect(action: "list")
			}
			return
		}

		try {
            nodeService.updateNode(
			  nodeInstance,
			  params.name, params.description, params.tags,
              getNodesByIdStrings(params.parents),
              getNodesByIdStrings(params.children),
              params.attributevalues)
		} catch (Exception e) {

            if (params.action == 'api') {
				response.status = 400 //Bad Request
				render "node update failed"
			} else {
				render(view: "edit",
					   model: [nodeInstance: nodeInstance])
				flash.message = message(code: 'default.not.updated.message', args: [
					message(code: 'node.label', default: 'Node'),
					params.name
				])	
				redirect(action: params.action, id: params.id)
			}
			return
		}

		if (params.action == 'api') {
			response.status = 200
			if (params.format && params.format != 'none') {
				switch (params.format.toLowerCase()) {
					case 'xml':
						def xml = xmlService.formatNodes(nodes)
						render(text: xml, contentType: "text/xml")
						break
					case 'json':
						def jsn = jsonService.formatNodes(nodes)
						render(text:jsn, contentType: "text/json")
						break
				}
			} else {
				render "Successfully Updated."
			}
		} else {
			flash.message = message(code: 'default.updated.message', args: [
				message(code: 'node.label', default: 'Node'),
				nodeInstance.id
			])
			redirect(action: "show", id: nodeInstance.id)
		}
	}

    @ProjectAccess(ProjectAccess.Level.read)
	def show() {
		String path = iconService.getLargeIconPath()
		String smallpath = iconService.getSmallIconPath()

        Node nodeInstance=nodeService.readNode(params.id)
		List tagList = []

		if (params.format && params.format!='none') {
			ArrayList nodes = [nodeInstance]
			if (nodeInstance) {
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
			} else {
				response.status = 404 //Not Found
				render "${params.id} not found."
			}
		} else {
			ChildNode[] parents = ChildNode.createCriteria().list{
				eq("child", Node.get(params.id?.toLong()))
			}

			ChildNode[] children = ChildNode.createCriteria().list{
				eq("parent", Node.get(params.id?.toLong()))
			}

			if (nodeInstance?.tags) {
				tagList = nodeInstance.tags.split(',')
			}

			if (!nodeInstance) {
				flash.message = message(code: 'default.not.found.message', args: [
					message(code: 'node.label', default: 'Node'),
					params.id
				])
				redirect(action: "list")
				return
			}

			render(view:"show",
				   model:[parents:parents,
						  children:children,
						  nodeInstance:nodeInstance,
						  path:path,
						  smallpath:smallpath,
						  taglist:tagList])
		}
	}

	def edit() {
        def project = projectService.findProject(params.project)
		Node nodeInstance = nodeService.readNode(params.id)
		def criteria = Node.createCriteria()
		def nodes = criteria.list{
			ne ("id", params.id?.toLong())
            eq("project",project)
		}

		if (!nodeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'node.label', default: 'Node'),
				params.id
			])
			return redirect(action: "list")
		}
		
		def selectedParents = []
		def unselectedParents = []
		nodeInstance.nodetype.children.each {nodeTypeRelationship ->
			nodeTypeRelationship.parent.nodes.each {node ->
				def Node selectedParent = null
				nodeInstance.children.each {childNode ->
					if (childNode.parent.id == node.id) {
						selectedParent = node
					}
				}
				if (selectedParent != null) {
					selectedParents += [id:node.id,
										name:node.name,
										display:"${node.name} [${node.nodetype.name}]"]
				} else {
					unselectedParents += [id:node.id,
										  name:node.name,
										  display:"${node.name} [${node.nodetype.name}]"]
				}
			}
		}

		def selectedChildren = []
		def unselectedChildren = []
		nodeInstance.nodetype.parents.each {nodeTypeRelationship ->
			nodeTypeRelationship.child.nodes.each {node ->
				def Node selectedChild = null
				nodeInstance.parents.each {childNode ->
					if (childNode.child.id == node.id) {
						selectedChild = node
					}
				}
				if (selectedChild != null) {
					selectedChildren += [id:node.id,
										 name:node.name,
										 display:"${node.name} [${node.nodetype.name}]"]
				} else {
					unselectedChildren += [id:node.id,
										   name:node.name,
										   display:"${node.name} [${node.nodetype.name}]"]
				}
			}
		}

		[selectedParents:selectedParents,
		 selectedChildren:selectedChildren,
		 unselectedParents:unselectedParents,
		 unselectedChildren:unselectedChildren,
		 nodes:nodes,
		 nodeInstance:nodeInstance]
	}

	def delete() {
		Node nodeInstance = nodeService.readNode(params.id)
		if (!nodeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'node.label', default: 'Node'),
				params.id
			])
			redirect(action: "list")
			return
		}

        def nodeStr = nodeInstance.toString()
		try {
			nodeService.deleteNode(nodeInstance)

			ArrayList nodes = [nodeInstance]
			webhookService.postToURL('node', nodes,'delete')

			flash.message = message(code: 'default.deleted.message', args: [
				message(code: 'node.label', default: 'Node'),
				params.id
			])
			redirect(action: "list")
		} catch (Exception e) {
            log.error("could not delete node ${nodeStr}: ${e.message}",e)
			flash.message = message(code: 'default.not.deleted.message', args: [
				message(code: 'node.label', default: 'Node'),
				params.id
			])
			redirect(action: "show", id: params.id)
		}
	}

    @ProjectAccess(ProjectAccess.Level.read)
	def getNodeTypeParentNodes() {
		def unselectedParents = []
		if (params.id != 'null') {
			NodeType nodeType = NodeType.get(params.id)
            projectService.authorizedReadPermission(nodeType.project)
			nodeType.children.each {nodeTypeRelationship ->
				nodeTypeRelationship.parent.nodes.each {node ->
					unselectedParents += [id:node.id,
										  name:node.name,
										  nodeTypeName:node.nodetype.name]
				}
			}
		}
		render unselectedParents as JSON
	}

    @ProjectAccess(ProjectAccess.Level.read)
	def getNodeTypeChildNodes() {
		def unselectedChildren = []
		if (params.id != 'null') {
			NodeType nodeType = NodeType.get(params.id)
            projectService.authorizedReadPermission(nodeType.project)
			nodeType.parents.each {nodeTypeRelationship ->
				nodeTypeRelationship.child.nodes.each {node ->
					unselectedChildren += [id:node.id,
										   name:node.name,
										   nodeTypeName:node.nodetype.name]
				}
			}
		}
		render unselectedChildren as JSON
	}

    @ProjectAccess(ProjectAccess.Level.read)
	def getNodeAttributes () {
		def response = []

		if (params.templateid != 'null') {
			if (params.node) {
                def node = nodeService.readNode(params.node)
                NodeValue.findAllByNode(node).each {nodeValue ->
					response += [tid:nodeValue.id,
								 id:nodeValue.nodeattribute.attribute.id,
								 required:nodeValue.nodeattribute.required,
								 key:nodeValue.value,
								 val:nodeValue.nodeattribute.attribute.name,
								 datatype:nodeValue.nodeattribute.attribute.filter.dataType,
								 filter:nodeValue.nodeattribute.attribute.filter.regex]
				}
			} else {
                def ntype = NodeType.get(params.templateid)
                projectService.authorizedReadPermission(ntype.project)
                NodeAttribute.findAllByNodetype(ntype).each {nodeAttribute ->
					response += [id:nodeAttribute.id,
						         required:nodeAttribute.required,
							     val:nodeAttribute.attribute.name,
							     datatype:nodeAttribute.attribute.filter.dataType,
							     filter:nodeAttribute.attribute.filter.regex]
				}
			}
		}

		render response as JSON
	}
	

	private List<Node> getNodesByIdStrings(final List<String> input) {
        List<Long> nodeIDs = input.collect{Long.parseLong(it)}
		return (nodeIDs ? nodeService.listNodesById(nodeIDs) : [])
	}
}