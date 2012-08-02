package com.dtolabs

import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator
import javax.xml.transform.stream.StreamSource
import org.xml.sax.SAXException
import grails.validation.ValidationException

/**
 * Provides a model import service that takes file uploads
 * of XML-based model content and stores it in a project.
 *
 * Import format must conform to the /import/yana.xsd file.
 */
class ImportService {

    public YANA_XSD = "/import/yana.xsd"

    /**
     * Validate the input XML conforms to the yana XSD.
     * @param xml The XML content containing yana model data
     */
    def validate(final InputStream xml) {
        if (null==xml) throw new ImportServiceException("XML content was empty")

        def InputStream xsd = getClass().getResourceAsStream(YANA_XSD)
        if (null==xsd) throw new ImportServiceException("Could not load ${YANA_XSD} from classpath.")

        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = factory.newSchema(new StreamSource(xsd))
        Validator validator = schema.newValidator()

        try {

            validator.validate(new StreamSource(xml))

        } catch (SAXException e) {

            throw new ImportServiceException("Invalid XML content: " + e.message, e);

        }
    }

    /**
     * Read an input stream for an XML import and load the model with its contents.
     * TODO: Refactor to use better separated collaboration. Encapsulate with domain class methods.
     * @param xmlInput InputStream containing yana XML data
     * @param project Project to store the model data
     */
    def load(InputStream xmlInput, Project project) {
        if (null == xmlInput) throw new IllegalArgumentException("XML content stream was null")
        if (null == project) throw new IllegalArgumentException("Project parameter was null")

        // Nodes list to return
        def results = []

        // Store XML GPath result from parse
        def xml

        try {

            // parse the XML content from the InputStream
            xml = new XmlSlurper().parse(xmlInput)

        } catch (SAXException e) {
            throw new ImportServiceException("Error parsing XML input", e)
        }

        try {
            // read Attributes
            xml.attributes.children().each { attribute ->
                Attribute att = Attribute.findByProjectAndName(project, attribute.@name.toString())
                if (!att) {
                    att = new Attribute(project: project) 
                }
                Filter filter = Filter.findByProjectAndDataType(project, attribute.@filter.toString())
                if (null == filter) {
                    throw new ImportServiceException("Filter not defined for data type: "
                            + attribute.@filter)
                }
                att.filter = filter
                att.name = attribute.@name.toString()
                att.description = attribute.@description.toString()

                att.save(flush: true, failOnError: true)
            }

            // read NodeTypes and NodeAttributes
            xml.types.children().each { type ->

                NodeType nodeType = NodeType.findByProjectAndName(project, type.@name.toString())
                if (!nodeType) {
                    nodeType = new NodeType(project: project)
                }
                nodeType.name = type.@name.toString()
                nodeType.description = type.description.text()
                nodeType.image = type.image.text()

                nodeType.save(flush: true, failOnError: true)

                type.attributes.children().each { attribute ->
                    Attribute attr = Attribute.findByProjectAndName(project, attribute.@name.toString())
                    NodeAttribute na = NodeAttribute.findByNodetypeAndAttribute(nodeType, attr)
                    if (!na) {
                        na = new NodeAttribute(nodetype: nodeType)
                    }
                    na.attribute = attr
                    na.required = attribute.@required.toBoolean();

                    na.save(flush: true, failOnError: true)
                }
            }

            // read NodeTypeRelationships
            xml.relationships.children().each { typerel ->

                NodeType parent = NodeType.findByProjectAndName(project, typerel.@parent.toString())
                NodeType child = NodeType.findByProjectAndName(project, typerel.@child.toString())

                NodeTypeRelationship relationship =
                    NodeTypeRelationship.findByParentAndChild(parent, child)
                if (!relationship) {
                    relationship = new NodeTypeRelationship(
                            name: typerel.@name.toString(),
                            parent: parent, child: child
                    )
                    relationship.save(flush: true, failOnError: true)
                }
            }


            Node nd
            // read Nodes and NodeValues
            xml.nodes.children().each { node ->
                nd = Node.findByProjectAndName(project, node.@name.toString())
                NodeType nodeType = NodeType.findByProjectAndName(project, node.@type.toString())
                if (!nd) {
                    nd = new Node(project: project)
                } else {
                    NodeValue.executeUpdate("delete NodeValue NV where NV.node = ?", [nd])
                }
                nd.name = node.@name.toString()
                nd.description = node.description.text()
                nd.tags = node.@tags.toString()
                nd.nodetype = nodeType
                nd.save(flush: true, failOnError: true)

                node.attributes.children().each { attribute ->
                    def attrName = attribute.@name.toString()

                    Attribute attr = Attribute.findByProjectAndName(project, attrName)
                    NodeAttribute na = NodeAttribute.findByNodetypeAndAttribute(nodeType, attr)
                    if (null == na) {
                        throw new ImportServiceException("attribute not found for that node type: "
                                + att.@name.toString())
                    }
                    NodeValue value = new NodeValue(node: nd,
                            nodeattribute: na, value: attribute.@value.toString()
                    )

                    value.save(flush: true, failOnError: true)
                }
                results += nd
            }

            // read ChildNodes
            xml."children".children().each { nodechild ->
                Node parent = Node.findByProjectAndNameAndNodetype(project, nodechild.parent.@name.toString(),
                        NodeType.findByNameAndProject(nodechild.parent.@type.toString(),project))
                Node child = Node.findByProjectAndNameAndNodetype(project, nodechild.@name.toString(),
                        NodeType.findByNameAndProject(nodechild.@type.toString(),project))

                // Lookup the relationship to see if these two nodes are allowed to relate.
                NodeTypeRelationship relationship =
                    NodeTypeRelationship.findByParentAndChild(parent.nodetype, child.nodetype)

                // See if there already is a child Node
                ChildNode childNode = ChildNode.findByChildAndParent(child, parent)

                if (!childNode && relationship) {
                    childNode = new ChildNode(child: child, parent: parent)

                    childNode.save(flush: true, failOnError: true)
                }
            }

        } catch (ValidationException ve) {

            throw new ImportServiceException("Error storing model", ve)

        } catch (SAXException e) {

            throw new ImportServiceException("Error processing input", e)
        }

        return results
    }
}

/**
 * Exception thrown by the ImportService
 */
class ImportServiceException extends RuntimeException {

    def ImportServiceException() {
    }

    def ImportServiceException(e) {
        super(e);
    }

    def ImportServiceException(e, throwable) {
        super(e, throwable);
    }
}
