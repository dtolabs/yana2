package com.dtolabs

import com.dtolabs.ChildNode
import com.dtolabs.NodeType
import com.dtolabs.Node
import com.dtolabs.NodeAttribute
import com.dtolabs.NodeValue
import grails.converters.JSON
import java.util.Date
import java.util.List;

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_USER','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class NodeController {
	def iconService
	def springSecurityService
	def xmlService
	def jsonService
	def webhookService
	def nodeService

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
					def node = Node.get(params.id)
					if (node) {
						node.delete()

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

	def listapi() {
		switch (request.method) {
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
        def project = Project.findByName(params.project)
        if (!project) {
            response.status=404
            render(text:message(code: 'default.not.found.message',
								args: ['Project', params.project],
								default: "Project {0} was not found"))
            return
        }
		String path = iconService.getSmallIconPath()
        int totCount = Node.countByProject(project)
		ArrayList nodes = []
		if (params.nodetype) {
			List nodetypes = []
			params.nodetype.each{
				nodetypes += it.toLong()
			}
			def criteria = Node.createCriteria()
			nodes = criteria.list{
				not{'in'("nodetype.id",nodetypes)}
			}
		} else {
			nodes = Node.list(params)
		}
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
		def project = Project.findByName(params.project)
		if (!project) {
			response.status = 404
			render(text: message(code: 'default.not.found.message',
				   args: ['Project', params.project],
				   default: "Project {0} was not found"))
			return
		}

		[nodeList: Node.findAllByProject(project),
		 nodeTypeList: NodeType.findAllByProject(project),
		 params:params]
	}

	def clone() {
        def project = Project.findByName(params.project)
        if (!project) {
            response.status = 404
            render(text: message(code: 'default.not.found.message',
								 args: ['Project', params.project],
								 default: "Project {0} was not found"))
            return
        }		

		Node node = Node.get(params.id)
		try {		
			Node nodeInstance = nodeService.cloneNode(project, node)
			flash.message = message(code: 'default.created.message', args: [
				message(code: 'node.label', default: 'Node'),
				nodeInstance.id
			])
			redirect(action: "show", id: nodeInstance.id)
		} catch (Throwable t) {
			render(view: params.action, model: [nodeInstance: node])
			flash.message = message(code: 'default.not.created.message', args: [
				message(code: 'node.label', default: 'Node'),
				params.name
			])
			redirect(action: "show", id: params.id)
		}
	}

	def save() {
        def project = Project.findByName(params.project)
        if (!project) {
            response.status = 404
            render(text: message(code: 'default.not.found.message',
								 args: ['Project', params.project],
								 default: "Project {0} was not found"))
            return
        }
        params.project = null

		if (! (params.nodetype && params.nodetype != 'null')) {
			if (params.action == 'api') {
				response.status = 400
				render "must specify node type"
			} else {
				flash.message = 'Please select a node type and try again.'
				render(view: "create", model:
					   [params:params])
			}
			return
		}

		NodeType nodeType = NodeType.get(params.nodetype.toLong())
		if (!nodeType) {
			if (params.action=='api') {
				response.status = 400 //Bad Request
				render "node type '${params.nodetype}' not found"
			} else {
				render(view: "create", model: [nodeType: nodeType])
			}
			return
		}

		Node nodeInstance
		try {
			List<NodeValue> nodeValues = []
			params.each {key, val ->
				if (key.contains('att')
					&& !key.contains('_filter')
					&& !key.contains('_require')) {
					NodeAttribute att = NodeAttribute.get(key[3..-1].toInteger())
					nodeValues +=
					  new NodeValue(node:nodeInstance,
								    nodeattribute:att,
								    value:val)
				}
			}

			nodeInstance =
			  nodeService.createNode(project, nodeType,
				  				     params.name, params.descripiton, params.tags,
									 getParentNodesFromParams(),
									 getChildNodesFromParams(),
									 nodeValues)
		} catch (Throwable t) {
			if (params.action == 'api') {
				response.status = 400 //Bad Request
				render "node creation failed"
			} else {
				render(view: params.action,
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
			redirect(action: "show", id: nodeInstance.id)
		}
	}
	
	def update() {		
		Node nodeInstance = Node.get(params.id)
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
			List<NodeValue> nodeValues = []
			params.each {key, val ->
				if (key.contains('att')
					&& !key.contains('_filter')
					&& !key.contains('_require')) {
					NodeValue nodeValue = NodeValue.get(key[3..-1].toInteger())
					nodeValue.value = val
				}
			}

			nodeService.updateNode(
			  nodeInstance.project, nodeInstance,
			  params.name, params.descripiton, params.tags,
			  getParentNodesFromParams(),
			  getChildNodesFromParams(),
			  nodeValues)
		} catch (Exception e) {
			if (params.action == 'api') {
				response.status = 400 //Bad Request
				render "node update failed"
			} else {
				render(view: params.action,
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

	def show() {
		String path = iconService.getLargeIconPath()
		String smallpath = iconService.getSmallIconPath()

		Node nodeInstance = Node.get(params.id)
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
        def project = Project.findByName(params.project)
        if (!project) {
            response.status = 404
            render(text: message(code: 'default.not.found.message',
								 args: ['Project', params.project],
								 default: "Project {0} was not found"))
            return
        }
		Node nodeInstance = Node.get(params.id)
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
			redirect(action: "list")
			return
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
        def project = Project.findByName(params.project)
        if (!project) {
            response.status = 404
            render(text: message(code: 'default.not.found.message',
								 args: ['Project', params.project],
								 default: "Project {0} was not found"))
            return
        }

		Node nodeInstance = Node.get(params.id)
		if (!nodeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'node.label', default: 'Node'),
				params.id
			])
			redirect(action: "list")
			return
		}

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
			status.setRollbackOnly()
			flash.message = message(code: 'default.not.deleted.message', args: [
				message(code: 'node.label', default: 'Node'),
				params.id
			])
			redirect(action: "show", id: params.id)
		}
	}

	def getNodeTypeParentNodes = {
		def unselectedParents = []
		if (params.id != 'null') {
			NodeType nodeType = NodeType.get(params.id)
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

	def getNodeTypeChildNodes = {
		def unselectedChildren = []
		if (params.id != 'null') {
			NodeType nodeType = NodeType.get(params.id)
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

	def getNodeAttributes = {
		def response = []

		if (params.templateid != 'null') {
			if (params.node) {
				NodeValue.findAllByNode(Node.get(params.node)).each {nodeValue ->
					response += [tid:nodeValue.id,
								 id:nodeValue.nodeattribute.attribute.id,
								 required:nodeValue.nodeattribute.required,
								 key:nodeValue.value,
								 val:nodeValue.nodeattribute.attribute.name,
								 datatype:nodeValue.nodeattribute.attribute.filter.dataType,
								 filter:nodeValue.nodeattribute.attribute.filter.regex]
				}
			} else {
			    NodeAttribute.findAllByNodetype(NodeType.get(params.templateid)).each {nodeAttribute ->
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
	
	private List<Node> getNodesFromParams(Long[] nodeIDs) {
		return Node.findAll("from Node as N where N.id IN (:ids)", [ids:nodeIDs])
	}

	private List<Node> getParentNodesFromParams() {
		Long[] nodeIDs = Eval.me("${params.parents}")		
		return (nodeIDs ? getNodesFromParams(nodeIDs) : [])
	}

	private List<Node> getChildNodesFromParams() {
		Long[] nodeIDs = Eval.me("${params.children}")		
		return (nodeIDs ? getNodesFromParams(nodeIDs) : [])
	}

}