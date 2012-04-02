package com.dtosolutions

class Template{
	//static hasMany = [attributes:TemplateAttribute,nodes:Node]

	String templateName
	NodeType nodetype
	
    static constraints = {
        templateName(blank:false)
    }

    def String toString() {
        return templateName
    }
}
