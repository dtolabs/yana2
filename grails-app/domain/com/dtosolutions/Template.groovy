package com.dtosolutions

class Template{
    static belongsTo = [NodeType,Node ]
	static hasMany = [attributes:TemplateAttribute]

	String templateName
	NodeType nodetype
	
    static constraints = {
        templateName(blank:false)
    }

    def String toString() {
        return templateName
    }
}
