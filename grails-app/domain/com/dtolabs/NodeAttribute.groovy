package com.dtolabs

class NodeAttribute{

	static hasMany = [values:NodeValue]

	Attribute attribute
	NodeType nodetype
	Boolean required = false
	//Integer order
	
    static constraints = {
		attribute(nullable:false)
        nodetype(nullable:false)
		required(nullable:false)
		//order(nullable:false)
    }
}