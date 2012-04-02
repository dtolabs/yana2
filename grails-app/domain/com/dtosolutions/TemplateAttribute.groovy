package com.dtosolutions

class TemplateAttribute{
    //static belongsTo = [ Attribute,Template ]
	static hasMany = [values:TemplateValue]

	Attribute attribute
	//Template template
	NodeType template
	Boolean required = false
	
    static constraints = {
		attribute(nullable:false)
        template(nullable:false)
		required(nullable:false)
    }
}
