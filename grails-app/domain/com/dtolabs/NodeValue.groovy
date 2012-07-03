package com.dtolabs

import java.util.Date;

class NodeValue{
    static belongsTo = [Node,NodeAttribute]

	Node node
	NodeAttribute nodeattribute
	String value
	Date dateCreated
	Date dateModified = new Date()
	
    static constraints = {
        node(nullable:false)
		nodeattribute(nullable:false)
		value(nullable:false)
    }
}
