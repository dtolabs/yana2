package com.dtolabs

import java.util.Date;

class TemplateValue{
    static belongsTo = [Node,TemplateAttribute]

	Node node
	TemplateAttribute templateattribute
	String value
	Date dateCreated
	Date dateModified = new Date()
	
    static constraints = {
        node(nullable:false)
		templateattribute(nullable:false)
		value(nullable:false)
    }
}
