package com.dtolabs.yana2.springacl

import org.codehaus.groovy.grails.plugins.springsecurity.acl.AclClass
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_SUPERUSER'])
class AclClassController extends grails.plugins.springsecurity.ui.AclClassController {

	protected String lookupClassName() { AclClass.name }

	protected Class<?> lookupClass() { AclClass }
}
