package com.dtolabs

import java.util.Date;

class NodeType{
	
    static searchable = {
        // only index name and description, and rename as nodetype and typedesc
        // NodeType will be added as a searchable component to the Node class, which will
        // effectively import these properties into the Node's search index
        only = ['name','description']
        name boost: 2.0, name: 'nodetype'
        description name: 'typedesc'
    }
	
	static mappedBy = [children: 'child', parents: 'parent']
	static hasMany = [nodes:Node,attributes:NodeAttribute,children:NodeTypeRelationship,parents:NodeTypeRelationship]

	String name
	String description
	String image
    Project project
	Date dateCreated
	Date dateModified = new Date()
	
    static constraints = {
        name(blank:false)
		description(blank:true, nullable:true)
		image(blank:true, nullable:true)
        project(nullable: false)
    }

    def String toString() {
        return name
    }
}
