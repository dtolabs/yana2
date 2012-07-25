package com.dtolabs

class Project {

    static constraints = {
        name unique: true, nullable: false
        description nullable: false
    }

    String name
    String description
}
