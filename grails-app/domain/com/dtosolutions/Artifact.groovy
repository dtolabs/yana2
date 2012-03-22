package com.dtosolutions

import java.util.Date;

class Artifact {

	static hasMany = [ instances:Instance]
	
	String name
	String description
	Date dateCreated
	Date dateModified = new Date()
	
    static constraints = {
		name(nullable:false)
		description(nullable:true)
    }
}
