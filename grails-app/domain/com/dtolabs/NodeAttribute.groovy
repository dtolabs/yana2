package com.dtolabs

class NodeAttribute{

	static hasMany = [values:NodeValue]

	Attribute attribute
	NodeType nodetype
	Boolean required = false

    static constraints = {
		attribute(nullable:false)
        nodetype(nullable:false)
		required(nullable:false)
    }
}