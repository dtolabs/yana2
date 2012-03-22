package com.dtosolutions

class Instance {

	static belongsTo = [ Artifact,Node ]
	
	Artifact artifact
	Node node
	String softwareVersion
	String license
	//License license
	
    static constraints = {
		artifact(nullable:false)
		node(nullable:false)
    }
}
