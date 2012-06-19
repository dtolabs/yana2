package com.dtolabs

import java.util.Date;

class NodeValue{
    static belongsTo = [Node,NodeAttribute]

	Node node
	NodeAttribute templateattribute
	String value
	Date dateCreated
	Date dateModified = new Date()
	
    static constraints = {
        node(nullable:false)
		templateattribute(nullable:false)
		value(nullable:false)
    }
}
