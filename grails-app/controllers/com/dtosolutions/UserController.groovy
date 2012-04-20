package com.dtosolutions

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_SUPERUSER'])
class UserController extends grails.plugins.springsecurity.ui.UserController {
	
}
