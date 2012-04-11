package com.dtosolutions

import java.util.Date;

class NodeType{
	//static searchable = true
	
	static hasMany = [nodes:Node,attributes:TemplateAttribute]

	String name
	String description
	Date dateCreated
	Date dateModified = new Date()
	
    static constraints = {
        name(blank:false)
		description(blank:true, nullable:true)
    }

    def String toString() {
        return name
    }
}
