package com.dtolabs

class NodeTypeRelationship {
	static belongsTo = [ NodeType ]
	
	String roleName
	NodeType parent
	NodeType child

    static constraints = {
		roleName(nullable:false)
		parent(nullable:false)
		child(nullable:false)
    }

    Map toMap() {
        def map = [
                id: this.id,
                name: this.roleName,
                parent: parent.name,
                child: child.name
        ]
        return map
    }

    String toString() {
        return roleName
    }
}

