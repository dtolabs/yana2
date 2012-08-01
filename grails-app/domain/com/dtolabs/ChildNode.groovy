package com.dtolabs

class ChildNode {
	static belongsTo = [ Node ]
	
	String relationshipName
	Node parent
	Node child

    static constraints = {
		relationshipName(nullable:false)
		parent(nullable:false)
		child(nullable:false)
    }

    Map toMap() {
        def map = [
                relationship: this.relationshipName,
                child: [name: child.name, type: child.nodetype.name, id: child.id,],
                parent: [name: parent.name, type: parent.nodetype.name, id: parent.id]
        ]
        return map
    }
}

