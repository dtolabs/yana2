package com.dtosolutions

class Webhook {
	
	String url
	String format = 'XML'
	String service

    static constraints = {
		url(nullable:false)
		format(nullable:false)
		service(nullable:false)
    }
}

