package com.dtosolutions

import java.util.Date;

class NodeType{

<<<<<<< HEAD
	//static hasMany = [nodes:Node,templates:Template]
=======
>>>>>>> owens_branch
	static hasMany = [nodes:Node,attributes:TemplateAttribute]

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
