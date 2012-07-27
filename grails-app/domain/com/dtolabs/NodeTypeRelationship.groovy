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
}

