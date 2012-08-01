package com.dtolabs.yana2.springacl

import org.codehaus.groovy.grails.plugins.springsecurity.acl.AclEntry
import org.codehaus.groovy.grails.plugins.springsecurity.acl.AclSid
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_SUPERUSER'])
class AclEntryController extends grails.plugins.springsecurity.ui.AclEntryController {

	protected String lookupClassName() { AclEntry.name }

	protected Class<?> lookupClass() { AclEntry }

	protected Class<?> lookupAclSidClass() { AclSid }
}
