import java.awt.RenderingHints.Key;
import java.util.Date
import java.util.Set
import java.util.LinkedHashMap
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator
import org.xml.sax.SAXException
import groovy.util.slurpersupport.GPathResult

import com.dtolabs.*


xmlFilePath = args[0]
xsdFilePath = args[1]
endOfTestMarker = args[2]
testNumber = 0

now = new Date()

filterMap = new HashMap<String,Filter>()
filterMap.put("String", new Filter())

attributeMap = new LinkedHashMap<String,Attribute>()
nodeTypeMap = new LinkedHashMap<String,NodeType>()
nodeAttributeMap = new LinkedHashMap<NodeType,NodeAttribute>()
nodeMap = new LinkedHashMap<String,Node>()
nodeValueMap = new LinkedHashMap<String,NodeValue>()
nodeTypeRelationshipMap = new LinkedHashMap<String,NodeTypeRelationship>()
childNodeMap = new LinkedHashMap<String,ChildNode>()

def Attribute createAttribute(String name, Filter filter) {
	Attribute attribute = new Attribute()
	attribute.name = name
	attribute.filter = filter
	attribute.description=''
	attributeMap.put(attribute.name, attribute)
	//println("attributeMap.put(${attribute.name}, ${attribute})")
	return attribute
}

def deleteAttribute(String name) {
	attributeMap.remove(name)
}

def NodeType createNodeType(String name, String description, String image) {
	NodeType nodeType = new NodeType()
	nodeType.name = name
	nodeType.description = description
	nodeType.image = image
	nodeTypeMap.put(nodeType.name, nodeType)
	//println("nodeTypeMap.put(${nodeType.name}, ${nodeType})")
	return nodeType
}

def deleteNodeType(String type) {
	nodeTypeMap.remove(type)
}

def NodeAttribute createNodeAttribute(NodeType nodeType, Attribute attribute) {	
	NodeAttribute nodeAttribute = new NodeAttribute()
	nodeAttribute.nodetype = nodeType
	nodeAttribute.attribute = attribute
	nodeAttributeMap.put(nodeType.name + "::" + attribute.name,
						 nodeAttribute)
	//println("nodeAttributeMap.put(${nodeType.name}::${attribute.name}, ${nodeAtribute})")
	return nodeAttribute
}

def deleteNodeAttribute(String key) {
	nodeTypeMap.remove(key)
}

def Node createNode(NodeType nodeType,
                    String name, String description, String tags) {
	Node node = new Node()
	node.name = name
	node.description = description 
	node.tags = tags
	node.nodetype = nodeType
	nodeMap.put(node.name, node)
	//println("nodeMap.put(${node.name}, ${node})")
	return node;
}
					
def findNodeByName(String name) {
	Node node = null;
	int nodeId = -1
	int i = 0
	nodeMap.each() {nodeMapKey, nodeMapValue ->
		++i
		if (nodeMapKey == name) {
			node = nodeMapValue;
			nodeId = i
		}
	}
	return [node, nodeId]
}

def findNodeIdByNodeName(String name) {
	Node node = null;
	int nodeId = -1
	int i = 0
	nodeMap.each() {nodeMapKey, nodeMapValue ->
		++i
		if (nodeMapKey == name) {
			node = nodeMapValue;
			nodeId = i
		}
	}
	return [node, nodeId]
}

def deleteNode(String name) {
	nodeMap.remove(name)
}

def createNodeValue(Node node, NodeAttribute nodeAttribute, String value) {
	NodeValue nodeValue = new NodeValue()
	nodeValue.node = node
	nodeValue.nodeattribute = nodeAttribute
	nodeValue.value = value
	nodeValueMap.put(nodeValue.node.name
		             + "::"
					 + nodeValue.nodeattribute.attribute.name
					 + "::"
					 + nodeValue.value,
					 nodeValue)
	//println("nodeValueMap.put(${nodeValue.node.name}::${nodeValue.nodeattribute.attribute.name}::${nodeValue.value}, ${nodeValue.value})")
	return nodeValue
}

def deleteNodeValue(String key) {
	nodeMap.remove(key)
}

def createNodeRelationship(String roleName,
						   NodeType parentNodeType, NodeType childNodeType) {
	NodeTypeRelationship nodeTypeRelationship = new NodeTypeRelationship()
	nodeTypeRelationship.name = roleName
	nodeTypeRelationship.child = childNodeType
	nodeTypeRelationship.parent = parentNodeType
	nodeTypeRelationshipMap.put(
	  childNodeType.name + "::" + parentNodeType.name, nodeTypeRelationship)
	//println("nodeTypeRelationshipMap.put(${childNodeType.name}::${parentNodeType.name}, ${nodeTypeRelationship})")
}

def deleteNodeRelationship(String key) {
   nodeTypeRelationshipMap.remove(key)
}

def createChildNode(Node parent, Node child) {
	ChildNode childNode  = new ChildNode()
	childNode.child = child
	childNode.parent = parent
	childNodeMap.put(child.name + "::" + parent.name, childNode)
	//println("childNodeMap.put(${child.name}::${parent.name}, ${childNode})")
	return childNode;
}

def deleteChildNode(String key) {
   childNodeMap.remove(key)
}

def parseXML() {
	File xmlFile = new File(xmlFilePath)

	// Attempt to validate the document:
	SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema")
				 .newSchema(new File(xsdFilePath))
				 .newValidator()
				 .validate(
				   new StreamSource(
					 new FileInputStream(xmlFile)))

	GPathResult xml =
	  new XmlSlurper().parse(
		new BufferedInputStream(
		  new FileInputStream(xmlFile)))
	
	// validate attributes
	xml.attributes.children().each {attributeXml ->
		createAttribute(attributeXml.@id.toString(),
						filterMap.get(attributeXml.@filter.toString()))
	}

	// validate nodetypes and nodeattributes
	xml.nodetypes.children().each {nodeTypeXml ->
		NodeType nodeType = nodeTypeMap.get(nodeTypeXml.@id.toString())
		if (!nodeType) {
			nodeType = createNodeType(nodeTypeXml.@id.toString(),
				                      nodeTypeXml.description.text(),
									  nodeTypeXml.image.text())
		}

		int order = 1
		nodeTypeXml.nodeAttributes.children().each {nodeAttributeXml ->
			Attribute attribute =
			  attributeMap.get(nodeAttributeXml.@attribute.toString())
			if (attribute) {
				NodeAttribute nodeAttribute =
				  nodeAttributeMap.get(nodeType.name + "::" + attribute.name)
				if (!nodeAttribute) {
					nodeAttribute =
					  createNodeAttribute(nodeType, attribute)
					order++
				}
			} else {
				//Error
			}
		}
	}
	
	// validate nodes and attributevalues
	xml.nodes.children().each {nodeXml ->
		NodeType nodeType = nodeTypeMap.get(nodeXml.@nodetype.toString())
		Node node = nodeMap.get(nodeXml.@id.toString())
		if (nodeType) {
			if (!node) {
				node = createNode(nodeType,
					              nodeXml.@id.toString(),
					              nodeXml.description.toString(),
								  nodeXml.@tags.toString())
			} else {
				//Error
			}

			nodeXml.values.children().each {nodeValueXml ->
				def nodeAttributeNew = nodeValueXml.@nodeAttribute.toString()
				def attributeXml =
				  xml.nodetypes.nodetype.nodeAttributes.nodeAttribute.findAll {
					it.@id.text() == nodeAttributeNew
				  }
				Attribute attribute =
				  attributeMap.get(attributeXml.@attribute.toString())
				NodeAttribute nodeAttribute =
				  nodeAttributeMap.get(nodeType.name + "::" + attribute.name)
	
				NodeValue nodeValue = createNodeValue(node,
                                                      nodeAttribute,
													  nodeValueXml.toString())
			}
		} else {
			//Error
		}
	}

	// validate nodetype parent/child
	xml.nodetyperelationships.children().each {nodeTypeRelationshipsXml ->
		// get dependencies
		NodeType parentNodeType = nodeTypeMap.get(nodeTypeRelationshipsXml.@parent.toString())
		NodeType childNodeType  = nodeTypeMap.get(nodeTypeRelationshipsXml.@child.toString())

		NodeTypeRelationship nodeTypeRelationship =
		  nodeTypeRelationshipMap.get(childNodeType.name + "::" + parentNodeType.name)
		if (!nodeTypeRelationship) {
			nodeTypeRelationship =
			  createNodeRelationship(nodeTypeRelationshipsXml.@rolename.toString(), 
									 parentNodeType, childNodeType)
		} else {
			//Error!
		}
	}
	
	// validate node parent/child
	xml.noderelationships.children().each {nodeRelationshipsXml ->
		// get dependencies
		Node parent = nodeMap.get(nodeRelationshipsXml.@parent.toString())
		Node child =  nodeMap.get(nodeRelationshipsXml.@child.toString())
		childNodeTypes = new ArrayList<Node>()
		parentNodeTypes = new ArrayList<Node>()
		nodeMap.each() {key, node ->
			if (child.nodetype.name == node.nodetype.name) {
				childNodeTypes.add(node)
			}
			if (parent.nodetype.name == node.nodetype.name) {
				parentNodeTypes.add(node)
			}
		}
		
		ChildNode childNode =
		  childNodeMap.get(child.name + "::" + parent.name)
		NodeTypeRelationship nodeTypeRelationship =
		  nodeTypeRelationshipMap.get(child.nodetype.name + "::" + parent.nodetype.name)
		if (!childNode && nodeTypeRelationship) {
			childNode = createChildNode(
                    parent, child)
		} else {
			//Error
		}
	}
}

def emitRerunZeroNodesTest() {
	println("TEST:RerunZeroNodesTest:" + ++testNumber)
	println("RERUN:yana:nodes")
	println(endOfTestMarker)
}

def emitRerunZeroTypesTest() {
	println("TEST:RerunZeroTypesTest:" + ++testNumber)
	println("RERUN:yana:types")
	println(endOfTestMarker)
}

def emitRerunImportXmlTest() {
	println("TEST:ImportXmlTest:" + ++testNumber)
	println("RERUN:yana:import -f ${xmlFilePath}")
	println(endOfTestMarker)
}

def emitRerunCreateTypeTest(String name, String description) {
	createNodeType(name, description, "")
	println("TEST:CreateTypeTest:" + ++testNumber)
	println("RERUN:yana:type -a create -t ${name} -d '${description}'")
	println(endOfTestMarker)
}

def emitRerunDeleteTypeTest(String type) {
	int nodeCount = 0
	nodeMap.each() {nodeMapKey, nodeMapValue ->
		if (nodeMapValue.nodetype.name == type) {
			++nodeCount
		}
	}

	boolean found = false
	int i = 0
	nodeTypeMap.each() {nodeTypeMapKey, nodeTypeMapValue ->
		++i
		if (nodeTypeMapKey == type) {
			println("TEST:DeleteTypeTest:" + ++testNumber)
			println("RERUN:yana:type -a delete -i ${i}")
			print(nodeCount == 0
				  ? ""
				  : "Deleting NodeType with associated nodes forbidden. (nodeCount=${nodeCount})\n")
			println(endOfTestMarker)
			found = true
		}
	}
	if (!found) {
		System.err.println("TEST ERROR: DeleteTypeTest!")
	} else if (nodeCount == 0) {
		deleteNodeType(type)
	}
}

def emitRerunCreateNodeTest(String type, String name, String description, String tags) {
	boolean found = false
	int i = 0
	nodeTypeMap.each() {nodeTypeMapKey, nodeTypeMapValue ->
		++i
		if (nodeTypeMapKey == type) {
			println("TEST:CreateNodeTest:" + ++testNumber)
			println("RERUN:yana:node "
				    + "-a create -t ${i} -n '${name}' "
				    + "-d '${description}' --tags '${tags}'")
			println(nodeMap.size() + 1
				    + ":" + type
					+ ":" + name)
			println(endOfTestMarker)
			found = true
		}
	}
	if (!found) {		
		System.err.println("TEST ERROR: CreateNodeTest!")
	} else {
		createNode(nodeTypeMap.get(type), name, description, tags)
	}
}

def emitRerunDeleteNodeTest(String name) {
	boolean found = false
	int i = 0
	nodeMap.each() {nodeMapKey, nodeMapValue ->
		++i
		if (nodeMapKey == name) {
			println("TEST:DeleteNodeTest:" + ++testNumber)
			println("RERUN:yana:node -a delete -i ${i}")
			println(endOfTestMarker)			
			found = true			
		}
	}
	if (!found) {		
		System.err.println("TEST ERROR: DeleteNodeTest!")
	} else {
		deleteNode(name)
	}
}

def emitRerunNodesTest() {
	println("TEST:NodesTest:" + ++testNumber)
	println("RERUN:yana:nodes")
	int i = 0
	nodeMap.each() {key, value ->
		println(++i
				+ ":" + value.name
			    + ":" + value.nodetype.name
				+ ":" + value.tags
				+ ":" + value.description)
	}
	println(endOfTestMarker)
}

def emitRerunNodesByAllTypeTest() {
	println("TEST:NodesByAllTypeTest:" + ++testNumber)
	println("RERUN:yana:nodes -t")
	int i = 0
	nodeMap.each() {key, value ->
		println(++i
				+ ":" + value.name
			    + ":" + value.nodetype.name
				+ ":" + value.tags
				+ ":" + value.description)
	}
	println(endOfTestMarker)
}

def emitRerunNodesByTypeTest() {		
	nodeTypeMap.each() {nodeTypeMapKey, nodeTypeMapValue ->
		println("TEST:NodesByTypeTest:" + ++testNumber)
		println("RERUN:yana:nodes -t " + nodeTypeMapKey)
		int i = 0
		nodeMap.each() {nodeMapKey, nodeMapValue ->
			++i
			if (nodeTypeMapKey == nodeMapValue.nodetype.name) {
				println(i
						+ ":" + nodeMapValue.name
					    + ":" + nodeMapValue.nodetype.name
						+ ":" + nodeMapValue.tags
						+ ":" + nodeMapValue.description)
			}
		}
		println(endOfTestMarker)
	}
}

def emitRerunNodeByIdTest() {
	int i = 0
	nodeMap.each() {nodeMapKey, nodeMapValue ->
		println("TEST:NodeByIdTest:" + ++testNumber)
		println("RERUN:yana:node -i " + ++i)
		println("name:" + nodeMapValue.name)
		println("type:" + nodeMapValue.nodetype.name)
		println("description:" + nodeMapValue.description)
		//println("status:" + ":" + value.status)
		println("tags:" + nodeMapValue.tags)
		
		Set<String> attributes = new TreeSet<String>()
		nodeValueMap.each() {nodeValueMapKey, nodeValueMapValue ->
			if (nodeMapValue.name == nodeValueMapValue.node.name) {
				attributes.add(nodeValueMapValue.nodeattribute.attribute.name
					           + ":" + nodeValueMapValue.value)
			}
		}
		attributes.each() {attribute ->
			println(attribute)
		}
	}
}

def emitRerunTypesTest() {
	println("TEST:TypesTest:" + ++testNumber)
	println("RERUN:yana:types")
	int i = 0
	nodeTypeMap.each() {key, value ->
		println(++i
			    + ":" + value.name
				+ ":" + value.description)
	}
	println(endOfTestMarker)
}

def emitRerunTypesByTypeTest() {
	int i = 0
	nodeTypeMap.each() {key, value ->
		println("TEST:TypesByTypeTest:" + ++testNumber)
		println("RERUN:yana:types -t " + value.name)
		println(++i + ":"
			    + value.name
				+ ":" + value.description)
		println(endOfTestMarker)
	}
}

def dumpDelimitedLine(String title, Set<String> entries) {
	String delim = title
	String eol = ""
	entries.each() {entry ->
		print(delim + entry)
		delim = ","; eol = "\n";
	}
	print(eol)
}

def dumpNodeType(NodeType nodeType, int i) {
	println("type:" + nodeType.name)
	println("id:" + i)
	println("description:" + nodeType.description)
	
	// Count the number of associated "nodes"
	int nodeCount = 0
    nodeMap.each() {nodeMapKey, nodeMapValue ->
		if (nodeType.name == nodeMapValue.nodetype.name) {
			++nodeCount
		}
	}
	println("nodeCount:" + nodeCount)

	Set<String> entries
	
	// List the associated "attributes"
	entries = new LinkedHashSet<String>()
	nodeAttributeMap.each() {nodeAttributeMapKey, nodeAttributeMapValue ->
		if (nodeType.name == nodeAttributeMapValue.nodetype.name) {
			entries.add(nodeAttributeMapValue.attribute.name)
		}
	}
	dumpDelimitedLine("attributes:", entries)

	// List the associated "relationships"
	entries = new TreeSet<String>()
	nodeTypeRelationshipMap.each() {nodeTypeRelationshipMapKey, nodeTypeRelationshipMapValue ->
		if ((nodeType.name == nodeTypeRelationshipMapValue.child.name)
			|| (nodeType.name == nodeTypeRelationshipMapValue.parent.name)) {
			entries.add(nodeTypeRelationshipMapValue.name)
		}
	}
	dumpDelimitedLine("relationships:", entries)

	println(endOfTestMarker)
}

def emitRerunTypeByTypeTest() {
	int i = 0
	nodeTypeMap.each() {nodeTypeMapKey, nodeTypeMapValue ->
		println("TEST:TypeByTypeTest:" + ++testNumber)
		println("RERUN:yana:type -t " + nodeTypeMapValue.name)
		dumpNodeType(nodeTypeMapValue, ++i)
	}
}

def emitRerunTypeByIdTest() {
	int i = 0
	nodeTypeMap.each() {nodeTypeMapKey, nodeTypeMapValue ->
		println("TEST:TypeByIdTest:" + ++testNumber)
		println("RERUN:yana:type -i " + ++i)
		dumpNodeType(nodeTypeMapValue, i)
	}
}

def emitRerunNodeChildRelationships() {
	int i = 0
	nodeMap.each() {nodeMapKey, nodeMapValue ->
		println("TEST:NodeChildRelationships:" + ++testNumber)
		println("RERUN:yana:relations --action children --node " + ++i)
		childNodeMap.each() {childNodeMapKey, childNodeMapValue ->
			if (nodeMapKey == childNodeMapValue.parent.name) {
				def (parentNode, parentNodeIndex) =
				  findNodeByName(childNodeMapValue.parent.name)
				def (childNode, childNodeIndex) =
				  findNodeByName(childNodeMapValue.child.name) 
				println(childNodeIndex
						+ ":" + parentNodeIndex
						+ ":" + childNodeMapValue.child.name
						+ ":" + childNodeMapValue.child.nodetype.name)
			}
		}
		println(endOfTestMarker)
	}
}

def emitRerunNodeParentRelationships() {
	int i = 0
	nodeMap.each() {nodeMapKey, nodeMapValue ->
		println("TEST:NodeParentRelationships:" + ++testNumber)
		println("RERUN:yana:relations --action parents --node " + ++i)
		childNodeMap.each() {childNodeMapKey, childNodeMapValue ->
			if (nodeMapKey == childNodeMapValue.child.name) {
				def (parentNode, parentNodeIndex) =
				  findNodeByName(childNodeMapValue.parent.name)
				def (childNode, childNodeIndex) =
				  findNodeByName(childNodeMapValue.child.name) 
				println(parentNodeIndex
						+ ":" + childNodeIndex
						+ ":" + childNodeMapValue.parent.name
						+ ":" + childNodeMapValue.parent.nodetype.name)
			}
		}
		println(endOfTestMarker)
	}
}

parseXML()

// Test that the database is empty:
//emitRerunZeroAttributesTest() 
emitRerunZeroNodesTest()
emitRerunZeroTypesTest()

emitRerunImportXmlTest()

emitRerunTypesTest()
emitRerunTypesByTypeTest()
emitRerunTypeByTypeTest()
emitRerunTypeByIdTest()

emitRerunNodesTest()
emitRerunNodesByAllTypeTest();
emitRerunNodesByTypeTest()
emitRerunNodeByIdTest()

emitRerunNodeChildRelationships()
emitRerunNodeParentRelationships()

emitRerunCreateTypeTest("City",    "City description")
emitRerunCreateTypeTest("State",   "State description")
emitRerunCreateTypeTest("Country", "Country description")

emitRerunTypesTest()
emitRerunTypesByTypeTest()
emitRerunTypeByTypeTest()
emitRerunTypeByIdTest()

emitRerunCreateNodeTest("City", "San Francisco", "City by the bay", "bay,hills,foggy,bridges")
emitRerunCreateNodeTest("City", "Berkeley", "Campus town", "Cal,hippies,liberal")
emitRerunCreateNodeTest("City", "Las Vegas", "What happens in Vegas, stays in vegas", "gambling,casino,entertainment")
emitRerunCreateNodeTest("State", "California", "The Golden Sate", "gold")
emitRerunCreateNodeTest("State", "Nevada", "The Silver Sate", "silver")
emitRerunCreateNodeTest("Country", "United States", "America, land of 50 states", "50,red,white,blue,freedom")

emitRerunNodesTest()
emitRerunNodesByAllTypeTest();
emitRerunNodesByTypeTest()
emitRerunNodeByIdTest()

emitRerunDeleteTypeTest("City")
emitRerunDeleteTypeTest("State")
emitRerunDeleteTypeTest("Country")

emitRerunDeleteNodeTest("United States")
emitRerunDeleteNodeTest("Nevada")
emitRerunDeleteNodeTest("California")
emitRerunDeleteNodeTest("Las Vegas")
emitRerunDeleteNodeTest("Berkeley")
emitRerunDeleteNodeTest("San Francisco")

emitRerunDeleteTypeTest("Country")
emitRerunDeleteTypeTest("State")
emitRerunDeleteTypeTest("City")
