package com.dtosolutions

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_SUPERUSER'])
class UserController extends grails.plugins.springsecurity.ui.UserController {
	
}
