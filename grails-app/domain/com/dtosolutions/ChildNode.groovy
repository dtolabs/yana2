package com.dtosolutions

class ChildNode {
	static belongsTo = [ Node ]
	
	Node parent
	Node child

    static constraints = {
		parent(nullable:false)
		child(nullable:false)
    }
}

