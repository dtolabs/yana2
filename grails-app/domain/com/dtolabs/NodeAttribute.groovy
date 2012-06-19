package com.dtolabs

class NodeAttribute{

	static hasMany = [values:NodeValue]

	Attribute attribute
	NodeType template
	Boolean required = false
	//Integer order
	
    static constraints = {
		attribute(nullable:false)
        template(nullable:false)
		required(nullable:false)
		//order(nullable:false)
    }
}