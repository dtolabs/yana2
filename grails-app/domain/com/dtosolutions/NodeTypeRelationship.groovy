package com.dtosolutions

class NodeTypeRelationship {
	static belongsTo = [ NodeType ]
	
	String roleName
	Integer parentCardinality
	Integer childCardinality
	NodeType parent
	NodeType child

    static constraints = {
		roleName(nullable:false)
		parentCardinality(nullable:true)
		childCardinality(nullable:true)
		parent(nullable:false)
		child(nullable:false)
    }
}

