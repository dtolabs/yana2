package com.dtosolutions

class Webhook {
	
	String url
	String format = 'XML'
	String service
	int attempts = 0

    static constraints = {
		url(nullable:false)
		format(nullable:false)
		service(nullable:false)
		attempts(nullable:false)
    }
}

