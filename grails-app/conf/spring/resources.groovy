import org.springframework.security.acls.domain.DefaultPermissionFactory
import com.dtolabs.yana2.springacl.YanaPermission

import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider
import org.springframework.security.web.authentication.preauth.j2ee.J2eePreAuthenticatedProcessingFilter
import org.springframework.security.web.authentication.preauth.j2ee.J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource

import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService
import com.dtolabs.yana2.springsecurity.YanaAuthoritiesMapper
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint
import com.dtolabs.yana2.YanaConstants

// Place your Spring DSL code here
beans = {
    /**
     * define aclPermissionFactory used by spring-security-acl, to recognize the YanaPermission permission names.
     */
    aclPermissionFactory(DefaultPermissionFactory, YanaPermission)

    /*
     * Pre-authenticated bean setup defined below
     */

    preAuthenticatedGrantedAuthoritiesUserDetailsService(PreAuthenticatedGrantedAuthoritiesUserDetailsService)

    preAuthenticatedAuthenticationProvider(PreAuthenticatedAuthenticationProvider) {
        preAuthenticatedUserDetailsService = ref('preAuthenticatedGrantedAuthoritiesUserDetailsService')
    }

    //Default mapping uses same as internal role names
    roleAuthorityMapper(YanaAuthoritiesMapper){
        adminRole= YanaConstants.ROLE_ADMIN
        operatorRole= YanaConstants.ROLE_OPERATOR
        architectRole= YanaConstants.ROLE_ARCHITECT
        superuserRole= YanaConstants.ROLE_SUPERUSER
        userRole= YanaConstants.ROLE_USER
    }

    j2eeAuthDetailsSource(J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource){
        mappableRolesRetriever=ref('roleAuthorityMapper')
        userRoles2GrantedAuthoritiesMapper=ref('roleAuthorityMapper')

    }
    j2eePreAuthenticatedProcessingFilter(J2eePreAuthenticatedProcessingFilter) {
        authenticationManager = ref('authenticationManager')
        authenticationDetailsSource = ref('j2eeAuthDetailsSource')
    }
    if(application.config.grails?.plugins?.springsecurity?.providerNames && 'preAuthenticatedAuthenticationProvider' in application.config.grails.plugins.springsecurity.providerNames){
        authenticationEntryPoint(Http403ForbiddenEntryPoint)
    }

}
