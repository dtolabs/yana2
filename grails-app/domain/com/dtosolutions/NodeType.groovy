package com.dtosolutions

import java.util.Date;

class NodeType{
	
    static searchable = { 
        // don't add id and version to index
        except = ['id', 'version','description','image']
        name boost: 2.0
    }
	
	static mappedBy = [children: 'child', parents: 'parent']
	static hasMany = [nodes:Node,attributes:TemplateAttribute,children:NodeTypeRelationship,parents:NodeTypeRelationship]

	String name
	String description
	String image
	Date dateCreated
	Date dateModified = new Date()
	
    static constraints = {
        name(blank:false)
		description(blank:true, nullable:true)
		image(blank:true, nullable:true)
    }

    def String toString() {
        return name
    }
}
