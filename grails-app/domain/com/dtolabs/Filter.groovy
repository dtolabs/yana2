package com.dtolabs

import java.util.Date;

class Filter {

    //static belongsTo = [ Attribute ]

    String dataType
    String regex
    Project project
    Date dateCreated
    Date lastUpdated

    static constraints = {
        dataType(blank: false, unique: 'project')
        regex(blank: true, size: 0..500)
        project(nullable: false)
    }

    def String toString() {
        return dataType
    }

    def Boolean isValid(String dataType, String value) {
        // get filter based on name and test value
        return true
    }
}
