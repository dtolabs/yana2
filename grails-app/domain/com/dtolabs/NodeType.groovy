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
	static hasMany = [nodes:Node, attributes:NodeAttribute,
            children:NodeTypeRelationship,parents:NodeTypeRelationship]

    String name
	String description
	String image
    Project project
    Date dateCreated
    Date lastUpdated
	
    static constraints = {
        name(blank:false, unique: 'project')
		description(blank:true, nullable:true)
		image(blank:true, nullable:true)
        project(nullable: false)
    }

    def String toString() {
        return name
    }

    Map toMap() {
        def map = [
                id: this.id,
                name: this.name,
                description: this.description,
                image: this.image
        ]
        map.attributes = []
        this.attributes.each { NodeAttribute attr ->
            map.attributes <<  attr.toMap()
        }
        map.children = []
        this.children.each {NodeTypeRelationship rel->
            map.children << rel.toMap()
        }
        map.parents = []
        this.parents.each {NodeTypeRelationship rel->
            map.parents << rel.toMap()
        }
        return map
    }
}
