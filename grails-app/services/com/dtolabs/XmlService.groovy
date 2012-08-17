package com.dtolabs

import groovy.xml.MarkupBuilder

class XmlService {

    static transactional = false
    static scope = "prototype"

    String formatNodes(ArrayList nodeList) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.yana() {
            nodes() {
                nodeList.each() { Node nd ->

                    def values = NodeValue.findAllByNode(nd)

                    node(id: nd.id, name: nd.name, type: nd.nodetype.name, tags: nd.tags) {
                        description(nd.description)

                        attributes() {
                            values.each { NodeValue val ->
                                'attribute'(
                                        name: val.nodeattribute.attribute.name,
                                        value: val.value)
                            }
                        }

                        parents() {
                            def rents = ChildNode.findAllByChild(Node.get(nd.id.toLong()));
                            rents.each { parent ->
                                node(id: parent.parent.id, name: parent.parent.name, type: parent.parent.nodetype.name)
                            }
                        }

                        children() {
                            def kinder = ChildNode.findAllByParent(Node.get(nd.id.toLong()));
                            kinder.each { child ->
                                node(id: child.child.id, name: child.child.name, type: child.child.nodetype.name)
                            }
                        }
                    }
                }
            }
        }

        return writer.toString()
    }



    String formatFilters(ArrayList filterList) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.yana() {
            filters() {
                filterList.each() {
                    filter(id: it.id, dataType: it.dataType, regex: it.regex)
                }
            }
        }
        return writer.toString()
    }

    String formatAttributes(ArrayList attributeList) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.yana() {
            attributes() {
                attributeList.each() {
                    attribute(id: it.id, name: it.name, filter: it.filter.dataType, description: it.description,)
                }
            }
        }
        return writer.toString()
    }

    String formatNodeTypes(ArrayList typeList) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.yana() {
            types() {
                typeList.each() { ntype ->
                    def nodecount = Node.findAllByNodetype(ntype).size()
                    def attrs = NodeAttribute.findAllByNodetype(NodeType.get(ntype.id.toLong()))

                    def criteria = NodeTypeRelationship.createCriteria()
                    def parents = criteria.list {
                        eq("child", NodeType.get(ntype.id?.toLong()))
                    }

                    def criteria2 = NodeTypeRelationship.createCriteria()
                    def children = criteria2.list {
                        eq("parent", NodeType.get(ntype.id?.toLong()))
                    }

                    type(id: ntype.id, name: ntype.name, image: ntype.image, nodeCount: nodecount) {
                        description(ntype.description)
                        attributes() {
                            attrs.each() { attr ->
                                attribute(name: attr.attribute.name, required: attr.required, filter: attr.attribute.filter.dataType)
                            }
                        }
                        relationships() {
                            parents.each() { p ->
                                relationship(parent: p.parent.name, child: p.child.name, name: p.name)
                            }
                            children.each() { c ->
                                relationship(parent: c.parent.name, child: c.child.name, name: c.name)
                            }
                        }
                    }
                }
            }
        }
        return writer.toString()
    }

    String formatNodeTypeRelationships(ArrayList nodeTypeRelationships) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.yana() {
            relationships() {
                nodeTypeRelationships.each {
                    relationship(id: it.id, name: it.name,
                            parent: it.parent.name,
                            child: it.child.name,
                    ) {
                    }
                }
            }
        }
        return writer.toString()
    }

    String formatHooks(ArrayList hooks) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.yana() {
            webhooks() {
                hooks.each() {
                    webhook(id: it.id, name: it.name, url: it.url, format: it.format, service: it.service, fails: it.attempts) {
                        user(it.user.username)
                    }
                }
            }
        }
        return writer.toString()
    }
}
