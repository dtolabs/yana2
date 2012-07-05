package com.dtolabs

class Role {

	String authority

	static mapping = {
		//cache true
        table 'yana_role'
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
