package com.dtolabs

import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator
import javax.xml.transform.stream.StreamSource
import org.xml.sax.SAXException

/**
 * Provides a model import service that takes file uploads
 * of XML-based model content and stores it in a project.
 *
 * Import format must conform to the /import/yana.xsd file.
 */
class ImportService {


    /**
     * Validate the input XML conforms to the yana XSD.
     * @param xml The XML content containing yana model data
     */
    def validate(final InputStream xml) {
        if (null==xml) throw new ImportServiceException("XML content was empty")

        def InputStream xsd = getClass().getResourceAsStream("/import/yana.xsd")
        if (null==xsd) throw new ImportServiceException("Could not load yana.xsd from classpath")

        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = factory.newSchema(new StreamSource(xsd))
        Validator validator = schema.newValidator()

        try {

            validator.validate(new StreamSource(xml))

        } catch (SAXException e) {

            throw new ImportServiceException("Invalid XML content", e);

        }
    }

    /**
     * Read an input stream for an XML import and populate the model with its contents.
     * TODO: Refactor to use better separated collaboration. Encapsulate with domain class methods.
     * @param xmlInput InputStream containing yana XML data
     * @param project Project to store the model data
     */
    def populate(InputStream xmlInput, Project project) {
        if (null == xmlInput) throw new IllegalArgumentException("XML content stream was null")
        if (null == project) throw new IllegalArgumentException("Project parameter was null")

        // Nodes list to return
        def results = []

        // Store parsed XML GPath result
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
                Attribute att = Attribute.findByProjectAndName(project, attribute.@id.toString())
                if (!att) {
                    att = new Attribute(project: project) 
                }
                Filter filter = Filter.findByProjectAndDataType(project, attribute.@filter.toString())
                if (null == filter) {
                    throw new ImportServiceException("No Filter defined for data type: "
                            + attribute.@filter.toString())
                }
                att.filter = filter
                att.name = attribute.@id
                att.description = attribute.@description.toString()

                att.save(flush: true, failOnError: true)
            }

            // read NodeTypes and NodeAttributes
            xml.nodetypes.children().each { nodetype ->

                NodeType ntype = NodeType.findByProjectAndName(project, nodetype.@id.toString())
                if (!ntype) {
                    ntype = new NodeType(project: project)
                }
                ntype.name = nodetype.@id
                ntype.description = nodetype.description.text()
                ntype.image = nodetype.image.text()

                ntype.save(flush: true, failOnError: true)

                nodetype.nodeAttributes.children().each { nodeAttribute ->
                    Attribute attribute = Attribute.findByProjectAndName(project,
                            nodeAttribute.@attribute.toString())
                    NodeAttribute na = NodeAttribute.findByNodetypeAndAttribute(ntype, attribute)
                    if (!na) {
                        na = new NodeAttribute(nodetype: ntype)
                    }
                    na.attribute = attribute
                    na.required = nodeAttribute.@required.toString().toBoolean();

                    na.save(flush: true, failOnError: true)
                }
            }
            Node nd
            // read Nodes and NodeValues
            xml.nodes.children().each { node ->
                nd = Node.findByProjectAndName(project, node.@id.toString())
                NodeType nodetype = NodeType.findByProjectAndName(project, node.@nodetype.toString())
                if (!nd) {
                    nd = new Node(project: project)
                } else {
                    NodeValue.executeUpdate("delete NodeValue NV where NV.node = ?", [nd])
                }
                nd.name = node.@id
                nd.description = node.description.toString()
                nd.tags = node.@tags.toString()
                nd.nodetype = nodetype
                nd.save(flush: true, failOnError: true)

                node.values.children().each { nodeValue ->
                    def nodeAttribute = nodeValue.@nodeAttribute.toString()
                    def att = xml.nodetypes.nodetype.nodeAttributes.nodeAttribute.findAll {
                        it.@id.text() == nodeAttribute
                    }
                    Attribute attribute = Attribute.findByProjectAndName(project,
                            att.@attribute.toString())
                    if (null == attribute) {
                        throw new ImportServiceException("attribute not found " + att.@attribute)
                    }
                    NodeAttribute na = NodeAttribute.findByNodetypeAndAttribute(nodetype, attribute)

                    NodeValue value = new NodeValue(node: nd,
                            nodeattribute: na, value: nodeValue.toString()
                    )

                    value.save(flush: true, failOnError: true)
                }
                results += nd
            }

            // read NodeTypeRelationships
            xml.nodetyperelationships.children().each { typerel ->

                NodeType parent = NodeType.findByProjectAndName(project, typerel.@parent.toString())
                NodeType child = NodeType.findByProjectAndName(project, typerel.@child.toString())

                NodeTypeRelationship relationship = NodeTypeRelationship.findByChildAndParent(
                        child, parent)
                if (!relationship) {
                    relationship = new NodeTypeRelationship(
                            roleName: typerel.@rolename.toString(),
                            child: child, parent: parent
                    )

                    relationship.save(flush: true, failOnError: true)
                }
            }

            // read ChildNodes
            xml.noderelationships.children().each { nodechild ->

                Node parent = Node.findByProjectAndName(project, nodechild.@parent.toString())
                Node child = Node.findByProjectAndName(project, nodechild.@child.toString())

                NodeTypeRelationship relationship = NodeTypeRelationship.findByChildAndParent(
                        child.nodetype, parent.nodetype)

                ChildNode childnode = ChildNode.findByChildAndParent(child, parent)
                if (!childnode && relationship) {
                    childnode = new ChildNode(child: child, parent: parent)
                    childnode.relationshipName = nodechild.@relationshipname.toString()

                    childnode.save(flush: true, failOnError: true)
                }
            }

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
