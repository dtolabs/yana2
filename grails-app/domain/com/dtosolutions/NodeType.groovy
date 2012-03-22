package com.dtosolutions

import java.util.Date;

class NodeType{

	static hasMany = [nodes:Node,templates:Template]

	String name
	Date dateCreated
	Date dateModified = new Date()
	
    static constraints = {
        name(blank:false)
    }

    def String toString() {
        return name
    }
}
