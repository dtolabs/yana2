package com.dtosolutions

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER','ROLE_ADMIN','ROLE_SUPER_USER'])
class UserController extends grails.plugins.springsecurity.ui.UserController {
	
}
