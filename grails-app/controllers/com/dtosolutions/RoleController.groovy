package com.dtosolutions

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_SUPER_USER'])
class RoleController extends grails.plugins.springsecurity.ui.RoleController {

}
