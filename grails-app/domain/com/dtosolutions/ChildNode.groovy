package com.dtosolutions

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
}

