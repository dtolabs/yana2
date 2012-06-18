package com.dtolabs

import grails.plugins.springsecurity.Secured
@Secured(['ROLE_YANA_SUPER_USER'])
class RegisterController extends grails.plugins.springsecurity.ui.RegisterController {

}
