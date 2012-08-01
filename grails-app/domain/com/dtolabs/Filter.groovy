package com.dtolabs

class Filter {

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

    Map toMap() {
        def map = [
                dataType: this.dataType,
                regex: this.regex
        ]
        return map
    }
}
