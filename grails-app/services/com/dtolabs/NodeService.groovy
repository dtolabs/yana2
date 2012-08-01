package com.dtolabs

import java.util.List;

class NodeService {

	def Node createNode(Project project,
						NodeType nodeType,
						String name,
						String description,
						String tags,
						List<Long> parentIds,
						List<Long> childIds,
						List<NodeValue> nodeValues) {
		return commitNode(false, project, new Node(),
			              nodeType, name, description, tags,
			  			  getParentNodesFromIDs(parentIds, nodeType),
						  getChildNodesFromIDs(childIds, nodeType),
						  nodeValues)
	}
	
	def void updateNode(Project project,
						Node nodeInstance,
						String name,
						String description,
						String tags,
						List<Long> parentIds,
						List<Long> childIds,
						List<NodeValue> nodeValues) {
		commitNode(true, project, nodeInstance,
				   nodeInstance.nodetype, name, description, tags,
	  			   getParentNodesFromIDs(parentIds, nodeInstance.nodetype),
				   getChildNodesFromIDs(childIds, nodeInstance.nodetype),
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
		nodeInstance.project = project
		nodeInstance.name = name
		nodeInstance.description = description
		nodeInstance.tags = tags
		if (!doUpdate) {
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
					addChildNode(getRelationshipName(parent, nodeInstance),
								 parent, nodeInstance)
				}

				// Next, assign all selected child nodes of this node.
				childList.each {child ->
					addChildNode(getRelationshipName(nodeInstance, child),
								 nodeInstance, child)
				}

				// Next, all the NodeValue objects for this node.
				nodeValues.each {nodeValue ->
					if (!doUpdate) {
						nodeValue.node = nodeInstance
					}
					nodeValue.save().save(failOnError:true)
				}
			} catch (Exception e) {
				status.setRollbackOnly()
				throw e;
			}
		}
		
		return nodeInstance
	}

    private deleteParentsAndChildren(Node nodeInstance) {
		["child", "parent"].each { kind ->
			ChildNode.createCriteria().list{
				eq(kind, nodeInstance)}.each {childNode ->
				childNode.delete()
			}
		}
	}

	private List<Node> getSelectedMembers(List<Node> selectedNodes,
										  List<Node> nodeCandidatesList) {
		List<Node> selectedMembers = []
		if (selectedNodes && nodeCandidatesList) {
			selectedNodes.each {selectedNode ->
				if (nodeCandidatesList.contains(selectedNode)) {
					selectedMembers += selectedNode
				}
			}
		}
		return selectedMembers
	}

	private List<Node> getParentNodesFromIDs(List<Long> nodeIDs, NodeType nodeType) {
		List<Node> nodes = []
		if (nodeIDs.size() != 0) {
		    nodes = getSelectedMembers(Node.findAll("from Node as N where N.id IN (:ids)",
													[ids:nodeIDs]),
						  			   getNodeParentCandidates(nodeType))
		}
		return nodes
	}
	
	private List<Node> getChildNodesFromIDs(List<Long> nodeIDs, NodeType nodeType) {
		List<Node> nodes = []
		if (nodeIDs.size() != 0) {
			nodes = getSelectedMembers(Node.findAll("from Node as N where N.id IN (:ids)",
													[ids:nodeIDs]),
									   getNodeChildrenCandidates(nodeType))
		}
		return nodes
	}

	private List<Node> getNodeParentCandidates(NodeType nodeType) {
		def parents = []
		nodeType.children.each {nodeTypeRelationship ->
			nodeTypeRelationship.parent.nodes.each {node ->
				parents += node
			}
		}
		return parents
	}

	private List<Node> getNodeChildrenCandidates(NodeType nodeType) {
		def children = []
		nodeType.parents.each {nodeTypeRelationship ->
			nodeTypeRelationship.child.nodes.each {node ->
				children += node
			}
		}
		return children
	}
	
	private boolean addChildNode(String name, Node parent, Node child) {
		ChildNode childNode = ChildNode.findByParentAndChild(parent, child)
		if (!childNode) {
			childNode = new ChildNode()
			childNode.parent = parent
			childNode.child = child
			childNode.save(flush: true)
			return true
		} else {
			return false
		}
	}
	
	private String getRelationshipName(Node parent,Node child) {
		String rolename = NodeTypeRelationship.findByParent(parent.nodetype).roleName
		String name = (rolename)?"${parent.name} [$rolename]":"${parent.name}"
		return name
	}

}
