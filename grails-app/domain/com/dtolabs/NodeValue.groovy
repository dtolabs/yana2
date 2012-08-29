package com.dtolabs


class NodeValue{
    static belongsTo = [Node, NodeAttribute]

	Node node
	NodeAttribute nodeattribute
	String value

    static mapping = {
        value column: 'nodevalue', type: 'text'
    }

    static constraints = {
        node(nullable:false)
		nodeattribute(nullable:false)
		value(nullable:true,blank: true)
    }

    String getName() {
        return this.nodeattribute.attribute.name
    }

    Map toMap() {
        def map = [
                id: this.id,
                name: getName(),
                value: this.value?:'',
                required: this.nodeattribute.required
        ]
        return map
    }
}
