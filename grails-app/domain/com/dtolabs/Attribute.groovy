package com.dtolabs

class Attribute{
    static hasMany = [ NodeAttribute ]

	String name
	Filter filter
    Date dateCreated
    Date lastUpdated

	String description
    Project project
	
    static constraints = {
        name(nullable:false, unique: 'project')
		filter(nullable:false)
		description(nullable:true)
        project(nullable: false)
    }

    def String toString() {
        return name
    }

    Map toMap() {
        def map = [
                id: this.id,
                name: this.name,
                description: this.description,
                filter: this.filter.dataType
        ]
        return map
    }
}
