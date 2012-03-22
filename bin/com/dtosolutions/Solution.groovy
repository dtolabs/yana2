package com.dtosolutions

import java.util.Date;

class Solution{
    static hasMany = [Template]
	static belongsTo = [Solution]

	String name
	Solution parent
	Date dateCreated
	Date dateModified = new Date()
	
    static constraints = {
        name(blank:false)
		parent(nullable:true)
    }

    def String toString() {
        return name
    }
}
