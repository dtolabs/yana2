package com.dtosolutions

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_SUPER_USER'])
class RegistrationCodeController extends grails.plugins.springsecurity.ui.RegistrationCodeController {

}
