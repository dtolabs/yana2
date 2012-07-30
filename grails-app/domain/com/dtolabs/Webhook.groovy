package com.dtolabs

import java.util.Date;

class Webhook {
	User user
	String name
	String url
	String format = 'XML'
	String service
	int attempts = 0
    Date dateCreated
    Date lastUpdated

    static constraints = {
		user(nullable:false)
		name(nullable:false)
		url(nullable:false)
		format(nullable:false)
		service(nullable:false)
		attempts(nullable:false)
    }
}

