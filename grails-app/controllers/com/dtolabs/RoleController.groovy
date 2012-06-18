package com.dtolabs

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_SUPERUSER'])
class RoleController extends grails.plugins.springsecurity.ui.RoleController {

}
