package com.dtosolutions

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN','ROLE_SUPER_USER'])
class PersistentLoginController extends grails.plugins.springsecurity.ui.PersistentLoginController {

}
