package com.dtolabs

class NodeService {

	def Node createNode(Project project,
						NodeType nodeType,
						String name,
						String description,
						String tags,
						List<Node> selectedParents,
						List<Node> selectedChildren,
						List<NodeValue> nodeValues) {
		return commitNode(false, project, new Node(),
			              nodeType, name, description, tags,
			  			  selectedParents,
						  selectedChildren,
						  nodeValues)
	}
	
	def void updateNode(Project project,
						Node nodeInstance,
						String name,
						String description,
						String tags,
						List<Node> selectedParents,
						List<Node> selectedChildren,
						List<NodeValue> nodeValues) {
		commitNode(true, project, nodeInstance,
				   nodeInstance.nodetype, name, description, tags,
	  			   selectedParents,
				   selectedChildren,
				   nodeValues)
	}
						
	def void deleteNode(Node nodeInstance) {
		Node.withTransaction{ status ->
			try {
				deleteParentsAndChildren(nodeInstance)

				nodeInstance.delete(flush: true)
			} catch (Exception e) {
				status.setRollbackOnly()
				throw e
			}
		}
	}
	
	def listNodes() {
	}
	
	def showNode() {
	}
					
	def Node cloneNode(Project project, Node nodeInstance) {
		List<NodeValue> nodeValues = []
		nodeInstance.nodeValues.each() {NodeValue nodeValue ->
			def nodeValueClone = new NodeValue()
			nodeValueClone.nodeattribute = nodeValue.nodeattribute
			nodeValueClone.value = nodeValue.value
			nodeValues += nodeValueClone
		}
		
		return commitNode(false, project, new Node(),
						  nodeInstance.nodetype,
						  nodeInstance.name  + "_clone",
						  nodeInstance.description,
						  nodeInstance.tags,
						  [], [],
						  nodeValues)
	}
	
	private Node commitNode(boolean doUpdate,
							Project project,
							Node nodeInstance,
							NodeType nodeType,
							String name,
							String description,
							String tags,
							List<Node> parentList,
							List<Node> childList,
							List<NodeValue> nodeValues) {
		nodeInstance.name = name
		nodeInstance.description = description
		nodeInstance.tags = tags
		if (!doUpdate) {
			nodeInstance.project = project
			nodeInstance.nodetype = nodeType;
		}

		Node.withTransaction() {status ->
			try {
				if (!nodeInstance.save(flush: true)) {
					throw new Exception()
				}

				if (doUpdate) {
					deleteParentsAndChildren(nodeInstance)
				}

				// Next, assign all selected parent nodes of this node.
				parentList.each {parent ->
					addChildNode(parent, nodeInstance)
				}

				// Next, assign all selected child nodes of this node.
				childList.each {child ->
					addChildNode(nodeInstance, child)
				}

				// Next, assign all the NodeValue objects for this node.
				nodeValues.each {nodeValue ->
					if (!doUpdate) {
						nodeValue.node = nodeInstance
					}
					nodeValue.save(failOnError:true)
				}
			} catch (Exception e) {
				status.setRollbackOnly()
				throw e;
			}
		}
println("-->")
println("nodeInstance.name: ${nodeInstance.name}")
nodeInstance.parents.each {parent ->
	println("  parent-p: ${parent.parent.name}")
	println("  parent-c: ${parent.child.name}")
}
nodeInstance.children.each {child ->
	println("   child-p: ${child.parent.name}")
	println("   child-c: ${child.child.name}")
}
println("<--")

		return nodeInstance
	}

    private deleteParentsAndChildren(Node nodeInstance) {
		ChildNode.findAllByParent(nodeInstance).each() { it.delete() }
		ChildNode.findAllByChild(nodeInstance).each() { it.delete() }
	}
	
	private deleteNodeValues(Node nodeInstance) {
		nodeInstance.nodeValues.each { nodeValue ->
			nodeValue.delete()	
		}
	}
	
	private boolean addChildNode(Node parent, Node child) {
		ChildNode childNode = ChildNode.findByParentAndChild(parent, child)
		if (!childNode) {
            // Is a relationship between parent & child allowed?
            NodeTypeRelationship nodeTypeRelationship =
              NodeTypeRelationship.findByParentAndChild(parent.nodetype, child.nodetype)
			if (nodeTypeRelationship) {
println("=== INFO: node-type-relationship allowed between ${parent.name} & ${child.name}")			
				childNode = new ChildNode(parent:parent, child:child)
				childNode.save(flush: true, failOnError: true)
				return true
			} else {
println("=== ERROR: node-type-relationship disallowed between ${parent.name} & ${child.name}")			
			}
		} else {
println("=== ERROR: child-node already exists for ${parent.name} & ${child.name}")			
		}
		return false
	}

}
