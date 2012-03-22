package com.dtosolutions

class Location {

	static hasMany = [nodes:Node]
	
    String name
	String providerName
    String city
    String province
    String country
    String postalCode
    Date dateCreated
    Date dateModified = new Date()

    static constraints = {
        name(unique:true, nullable:false)
		providerName(nullable:true)
        city(nullable:true)
        province(nullable:true)
        country(nullable:true)
        postalCode(nullable:true)
    }

    def String toString() {
        return name
    }
}

