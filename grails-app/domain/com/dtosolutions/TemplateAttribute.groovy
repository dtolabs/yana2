package com.dtosolutions

class TemplateAttribute{


	Attribute attribute
	NodeType template
	Boolean required = false
	
    static constraints = {
		attribute(nullable:false)
        template(nullable:false)
		required(nullable:false)
    }
}
