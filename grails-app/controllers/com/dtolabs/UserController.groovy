package com.dtolabs

import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import grails.converters.JSON

@Secured(['ROLE_YANA_SUPERUSER', 'ROLE_YANA_ADMIN'])
class UserController extends grails.plugins.springsecurity.ui.UserController {

    /**
     * Ajax call used by autocomplete textfield.
     */
    def ajaxUserRoleSearch = {

        def jsonData = []

        if (params.term?.length() > 2) {
            String username = params.term
            String usernameFieldName = SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName
            String roleFieldName = SpringSecurityUtils.securityConfig.authority.nameField

            setIfMissing 'max', 10, 100

            def results = lookupUserClass().executeQuery(
                                                   "SELECT DISTINCT u.$usernameFieldName " +
                                                   "FROM ${lookupUserClassName()} u " +
                                                   "WHERE LOWER(u.$usernameFieldName) LIKE :name " +
                                                   "ORDER BY u.$usernameFieldName",
                                                   [name: "${username.toLowerCase()}%"],
                                                   [max: params.max])

            for (result in results) {
                jsonData << [value: result]
            }
            def roleResults = lookupRoleClass().executeQuery(
                                               "SELECT DISTINCT u.$roleFieldName " +
                                               "FROM ${lookupRoleClassName()} u " +
                                               "WHERE LOWER(u.$roleFieldName) LIKE :name " +
                                               "ORDER BY u.$roleFieldName",
                                               [name: "${username.toLowerCase()}%"],
                                               [max: params.max])

            for (result in roleResults) {
                jsonData << [value: result]
            }
        }

        render text: jsonData as JSON, contentType: 'text/plain'
    }
}
