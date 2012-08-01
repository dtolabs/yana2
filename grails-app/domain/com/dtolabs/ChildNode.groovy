package com.dtolabs

class ChildNode {
	static belongsTo = Node
	
	Node parent
	Node child

    static constraints = {
		parent(nullable:false)
		child(nullable:false)
    }

    Map toMap() {
        def map = [
                child: [name: child.name, type: child.nodetype.name, id: child.id],
                parent: [name: parent.name, type: parent.nodetype.name, id: parent.id]
        ]
        return map
    }
}

