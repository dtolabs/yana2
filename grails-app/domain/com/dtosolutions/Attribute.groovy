package com.dtosolutions

import java.util.Date;

class Attribute{
    static hasMany = [ TemplateAttribute ]

	String name
	Filter filter
	Date dateCreated
	Date dateModified = new Date()
	
    static constraints = {
        name(nullable:false, unique: true)
		filter(nullable:false)
    }

    def String toString() {
        return name
    }
}
