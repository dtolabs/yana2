package com.dtolabs

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_SUPERUSER'])
class PersistentLoginController extends grails.plugins.springsecurity.ui.PersistentLoginController {

}
