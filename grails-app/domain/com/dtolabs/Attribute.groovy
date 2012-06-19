package com.dtolabs

import java.util.Date;

class Attribute{
    static hasMany = [ NodeAttribute ]

	String name
	Filter filter
	Date dateCreated
	Date dateModified = new Date()
	String description
	
    static constraints = {
        name(nullable:false, unique: true)
		filter(nullable:false)
		description(nullable:true)
    }

    def String toString() {
        return name
    }
}
