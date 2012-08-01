package com.dtolabs.yana2.springacl

import org.codehaus.groovy.grails.plugins.springsecurity.acl.AclSid
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_SUPERUSER'])
class AclSidController extends grails.plugins.springsecurity.ui.AclSidController {

	protected String lookupClassName() { AclSid.name }

	protected Class<?> lookupClass() { AclSid }
}
