package com.dtolabs.yana2.springacl

import org.codehaus.groovy.grails.plugins.springsecurity.acl.AclClass
import org.codehaus.groovy.grails.plugins.springsecurity.acl.AclObjectIdentity
import org.codehaus.groovy.grails.plugins.springsecurity.acl.AclSid
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_SUPERUSER'])
class AclObjectIdentityController extends grails.plugins.springsecurity.ui.AclObjectIdentityController {

	protected String lookupClassName() { AclObjectIdentity.name }

	protected Class<?> lookupClass() { AclObjectIdentity }

	protected Class<?> lookupAclSidClass() { AclSid }

	protected Class<?> lookupAclClassClass() { AclClass }
}
