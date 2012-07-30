package com.dtolabs


class NodeValue{
    static belongsTo = [Node, NodeAttribute]

	Node node
	NodeAttribute nodeattribute
	String value

    static mapping = {
        value column: 'nodevalue'

    }
    static constraints = {
        node(nullable:false)
		nodeattribute(nullable:false)
		value(nullable:false)
    }
}
