package com.dtosolutions

class TemplateAttribute{

	static hasMany = [values:TemplateValue]

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