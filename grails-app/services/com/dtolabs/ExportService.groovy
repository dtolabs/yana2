package com.dtolabs

import groovy.xml.MarkupBuilder
import com.dtolabs.groovy.util.BuilderUtil

/**
 * Provides a model export service that takes a project
 * and dumps it out as XML-based model content.
 *
 * Export format must conform to the /import/yana.xsd file.
 */
class ExportService {

    /**
     * Dump the model
     * @param project The project model to export
     */

    def export(final Project project) {
        this.exportXml(project)
    }


    def exportXml(final Project project) {

        def writer = new StringWriter()
        def mb = new MarkupBuilder(writer)
        BuilderUtil bu = new BuilderUtil()

        /**
         * Top level document structure
         */
        def root = [yana:[
                attributes: [], types: [], nodes: [],
                relationships: []
        ]]

        /**
         * Attributes
         */
        Attribute.findAllByProject(project).each { Attribute attr ->
            Map attrMap = attr.toMap()
            ["name", "filter", "description"].each { key ->
                BuilderUtil.makeAttribute(attrMap, key)
            }
            attrMap.remove("id")
            root.yana.attributes << attrMap
        }
        BuilderUtil.makePlural(root.yana, "attributes")

        /**
         * NodeType
         */

        def nodeTypeRelationships = [] // NodeTypeRelationships are processed later

        NodeType.findAllByProject(project).each { NodeType type ->

            type.parents.each {
                if (! nodeTypeRelationships.contains(it))  nodeTypeRelationships << it
            }

            Map typeMap = type.toMap()
            typeMap.remove("id")
            BuilderUtil.makeAttribute(typeMap, "name")

            typeMap.remove("parents") // don't include relationship info
            typeMap.remove("children")//

            typeMap.attributes.each { Map m ->
                BuilderUtil.makeAttribute(m, "required")
                BuilderUtil.makeAttribute(m, "name")
                m.remove("id")  //  NodeAttributes are an internal object
            }
            BuilderUtil.makePlural(typeMap, "attributes")
            root.yana.types << typeMap

        }
        BuilderUtil.makePlural(root.yana, "types")

        /**
         * NodeTypeRelationships
         */
        nodeTypeRelationships.each { NodeTypeRelationship rel ->
            Map relMap = rel.toMap()
            relMap.remove("id")
            BuilderUtil.makeAttribute(relMap, "parent")
            BuilderUtil.makeAttribute(relMap, "child")
            BuilderUtil.makeAttribute(relMap, "name")

            root.yana.relationships << relMap
        }
        BuilderUtil.makePlural(root.yana, "relationships")

        /**
         * Node
         */
        def childNodes = []
        Node.findAllByProject(project).each { Node nd ->

            // ChildNodes are processed separately
            nd.parents.each {
                if (! childNodes.contains(it)) childNodes << it
            }

            Map ndMap = nd.toMap()
            ndMap.remove("id")
            ndMap.remove("parents") // ChildNodes are not included here
            ndMap.remove("children")

            ["name", "type", "tags"].each { key ->
                BuilderUtil.makeAttribute(ndMap, key)
            }
            ndMap.remove("typeId") // Don't include the type's ID
            ndMap.attributes.each { Map m ->
                BuilderUtil.makeAttribute(m, "name")
                BuilderUtil.makeAttribute(m, "value")
                m.remove("id")        // Don't include id and required properties
                m.remove("required")  //
            }
            BuilderUtil.makePlural(ndMap, "attributes")

            root.yana.nodes << ndMap
        }
        BuilderUtil.makePlural(root.yana, "nodes")


        /**
         * ChildNodes
         */

        // Initialize a child map for children.
        // Child nodes aren't "pluralized" because children/child don't
        // follow a simple pluralize convention.
        root.yana.children = [child: []]

        childNodes.each { ChildNode child ->
            Map childMap = child.toMap()
            if (childMap.relationship) {      // relationship is optional
                BuilderUtil.makeAttribute(childMap, "relationship")
            } else {
                childMap.remove("relationship")
            }
            childMap.remove("id") // We don't publicize ChildNode.id
            childMap.putAll(childMap.remove("child")) // Hoist the child info up a level
            BuilderUtil.makeAttribute(childMap, "name")
            BuilderUtil.makeAttribute(childMap, "type")
            BuilderUtil.makeAttribute(childMap, "id")
            BuilderUtil.makeAttribute(childMap.parent, "name")
            BuilderUtil.makeAttribute(childMap.parent, "type")
            BuilderUtil.makeAttribute(childMap.parent, "id")

            root.yana.children.child << childMap
        }

        /**
         * Dump and return the data
         */
        bu.mapToDom(root, mb)

        return writer.toString()
    }
}

/**
 * Exception thrown by the ExportService
 */
class ExportServiceException extends RuntimeException {

    def ExportServiceException() {
    }

    def ExportServiceException(e) {
        super(e);
    }

    def ExportServiceException(e, throwable) {
        super(e, throwable);
    }
}