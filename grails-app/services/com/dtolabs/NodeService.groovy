package com.dtolabs

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.access.prepost.PostFilter

class NodeService {

    def projectService
    def aclService
    def aclUtilService
    def springSecurityService
    static transactional = true // on by default but being explicit

    /**
     * Create a node, requires 'operator' or 'admin' permission.
     * @param project
     * @param nodeType
     * @param name
     * @param description
     * @param tags
     * @param parentIds
     * @param childIds
     * @param nodeValues
     * @return
     */
	Node createNode(Project project,
					NodeType nodeType,
					String name,
					String description,
					String tags,
					List<Node> selectedParents,
					List<Node> selectedChildren,
					Map<String,String> values) {
        projectService.authorizedOperatorPermission(nodeType.project)
		return commitNode(false, project, new Node(),
			              nodeType, name, description, tags,
			  			  selectedParents,
						  selectedChildren,
                          values)

	}

	void updateNode(Node nodeInstance,
					String name,
					String description,
					String tags,
					List<Node> selectedParents,
					List<Node> selectedChildren,
					Map<String,String> values) {
        projectService.authorizedOperatorPermission(nodeInstance.project)
		commitNode(true, nodeInstance.project, nodeInstance,
				   nodeInstance.nodetype, name, description, tags,
	  			   selectedParents,
				   selectedChildren,
				   values)
	}

    void deleteNode(Node nodeInstance) {
        projectService.authorizedOperatorPermission(nodeInstance.project)
        deleteParentsAndChildren(nodeInstance)
        nodeInstance.delete(flush: true)
	}

    private Node readNode(Node node) {
        projectService.authorizedReadPermission(node.project)
        return node
    }
	
    Node readNode(id) {
        def node = Node.get(id)
        if(!node){
            return null
        }
        return readNode(node)
    }

    /**
     * List nodes for the project with the given paging parameters, return a map with [total: Integer, nodes: List]
     * @param project
     * @param params
     * @return
     */
    Map listNodes(Project project, Map params=[:]){
        projectService.authorizedReadPermission(project)
        int totCount = Node.countByProject(project)
        def sort= params.sort
        def criteria= Node.createCriteria()
        def nodes = criteria.list{
            delegate.'project'{
                idEq(project.id)
            }
            if(params.max){
                maxResults(params.max.toInteger())
            }
            if(params.offset){
                firstResult(params.offset.toInteger())
            }
            if(sort){
                def x = sort.split(/\s*,\s*/)
                x[0..1].each{y->
                    if(y.contains('.')){
                        def z= y.split('\\.',2)
                        delegate."${z[0]}"{
                            order(z[1],params.order?:'asc')
                        }
                    }else{
                        order(y,params.order?:'asc')
                    }
                }
            }
        }
        [total:totCount,nodes:nodes]
    }

    @PostFilter("hasPermission(filterObject.project, read) or hasPermission(filterObject.project, admin)")
    List<Node> listNodesById(List<Long> ids){
        return Node.findAll("from Node as N where N.id IN (:ids)", [ids: ids])
    }

	Node cloneNode(Node nodeInstance) {
        projectService.authorizedOperatorPermission(nodeInstance.project)
		Map<String,String> nodeValues = [:]
		nodeInstance.nodeValues.each() {NodeValue nodeValue ->
			nodeValues[nodeValue.nodeattribute.attribute.name]= nodeValue.value
		}
		
		return commitNode(false, nodeInstance.project, new Node(),
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
                            Map<String, String> values) {
        nodeInstance.name = name
        nodeInstance.description = description
        nodeInstance.tags = tags
        if (!doUpdate) {
            nodeInstance.project = project
            nodeInstance.nodetype = nodeType;
        }

        if (!nodeInstance.validate()) {
            return nodeInstance
        }


        if (doUpdate) {
            deleteParentsAndChildren(nodeInstance)
        }

        HashSet toSave=[]
        // Next, assign all selected parent nodes of this node.
        parentList.each {parent ->
            if(addChildNode(parent, nodeInstance)){
                toSave<<parent
                toSave<<nodeInstance
            }
        }

        // Next, assign all selected child nodes of this node.
        childList.each {child ->
            if(addChildNode(nodeInstance, child)){
                toSave << child
                toSave << nodeInstance
            }
        }
        toSave.each{
            it.save()
        }
        nodeInstance.save()

        def nvmap = nodeInstance.nodeValues?.groupBy {it.nodeattribute.attribute.name}
        def atmap = nodeType.attributes?.groupBy {it.attribute.name}
        // Next, assign all the NodeValue objects for this node.
        values.each {nvname,nvalue ->
            if(!atmap || !atmap[nvname]){
                //ignore if no such attribute exists
                return
            }
            def nodeValue
            if(doUpdate && nvmap && nvmap[nvname]){
                //update existing nodevalue instance
                nodeValue = nvmap[nvname]?.getAt(0)
                nodeValue.node = nodeInstance
                nodeValue.value = nvalue
            }else{
                //create new nodevalue
                def att = atmap[nvname]?.getAt(0)
                nodeValue = new NodeValue(node: nodeInstance, nodeattribute: att, value: nvalue)
                nodeInstance.addToNodeValues(nodeValue)
            }
            nodeValue.save(failOnError: true) // TODO: Is this failOnError necessary?
        }
        nodeInstance.save(flush: true)

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

    /**
     * Create or return an existing ChildNode for the parent+child, return true if a new ChildNode was
     * created and added for both Nodes
     * @param parent
     * @param child
     * @return true if a new parent/child ChildNode was added to the Nodes
     */
    private boolean addChildNode(Node parent, Node child) {
        ChildNode childNode = ChildNode.findByParentAndChild(parent, child)
        if (!childNode) {
            // Is a relationship between parent & child allowed?
            NodeTypeRelationship nodeTypeRelationship =
                NodeTypeRelationship.findByParentAndChild(parent.nodetype, child.nodetype)
            if (nodeTypeRelationship) {                
                childNode = new ChildNode()
                parent.addToChildren(childNode)//sets the parent property
                child.addToParents(childNode)//sets the child property
                childNode.save(flush: true, failOnError: true)
                return true
            } else {                
            }
        } else {            
        }
        return false
    }
}

/**
 * Exception thrown by the NodeService
 */
class NodeServiceException extends RuntimeException {

    def NodeServiceException() {
    }

    def NodeServiceException(e) {
        super(e);
    }

    def NodeServiceException(e, throwable) {
        super(e, throwable);
    }
}
