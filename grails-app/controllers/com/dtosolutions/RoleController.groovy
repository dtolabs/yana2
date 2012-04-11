package com.dtosolutions

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN','ROLE_SUPER_USER'])
class RoleController extends grails.plugins.springsecurity.ui.RoleController {

}
