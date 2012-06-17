package com.dtolabs

import java.util.Date;

class Filter {
	
	//static belongsTo = [ Attribute ]
	
    String dataType
    String regex
	Date dateCreated
	Date dateModified = new Date()

    static constraints = {
        dataType(blank:false, unique: true)
        regex(blank:true,size:0..500)
    }

    def String toString() {
        return dataType
    }

	def Boolean isValid(String dataType, String value){
		// get filter based on name and test value
		return true
	}
}
