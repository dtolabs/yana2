package com.dtolabs

class NodeTypeRelationship {
	static belongsTo = [ NodeType ]
	
	String name
	NodeType parent
	NodeType child

    static constraints = {
		name(nullable:false)
		parent(nullable:false)
		child(nullable:false)
    }

    Map toMap() {
        def map = [
                id: this.id,
                name: this.name,
                parent: parent.name,
                child: child.name
        ]
        return map
    }

    String toString() {
        return name
    }
}

