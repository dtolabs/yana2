package com.dtolabs.yana2.springsecurity

import org.springframework.security.core.authority.mapping.MapBasedAttributes2GrantedAuthoritiesMapper
import org.springframework.beans.factory.InitializingBean
import org.springframework.util.Assert
import com.dtolabs.yana2.YanaConstants
import org.apache.log4j.Logger

/*
 * Copyright 2012 DTO Labs, Inc. (http://dtolabs.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/*
* YanaAuthoritiesMapper.java
*
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 8/15/12 7:21 PM
*
*/
/**
 * Extends {@link MapBasedAttributes2GrantedAuthoritiesMapper} to provide a spring bean builder compatible map
 * property for directly setting the map values, and it also provides shortcuts for setting the known
 * Yana roles.
 */
class YanaAuthoritiesMapper extends MapBasedAttributes2GrantedAuthoritiesMapper implements InitializingBean {
    public static Logger log = Logger.getLogger(YanaAuthoritiesMapper.class)
    private static final Map<String, String> defaultMap = [
            (YanaConstants.ROLE_ADMIN): YanaConstants.ROLE_ADMIN,
            (YanaConstants.ROLE_ARCHITECT): YanaConstants.ROLE_ARCHITECT,
            (YanaConstants.ROLE_OPERATOR): YanaConstants.ROLE_OPERATOR,
            (YanaConstants.ROLE_SUPERUSER): YanaConstants.ROLE_SUPERUSER,
            (YanaConstants.ROLE_USER): YanaConstants.ROLE_USER,
    ]
    /**
     * A simple String,String map that can be configured via a groovy bean builder
     */
    Map<String, String> roleMap = new HashMap<String, String>()
    /**
     * Shortcut for setting the yana Admin role value, can be a string or list of strings, or string containing the
     * stringSeparator which will be split into a list of strings.
     */
    Object adminRole
    /**
     * Shortcut for setting the yana Operator role value, can be a string or list of strings, or string containing the
     * stringSeparator which will be split into a list of strings.
     */
    Object operatorRole
    /**
     * Shortcut for setting the yana Architect role value, can be a string or list of strings, or string containing the
     * stringSeparator which will be split into a list of strings.
     */
    Object architectRole
    /**
     * Shortcut for setting the yana Superuser role value, can be a string or list of strings, or string containing the
     * stringSeparator which will be split into a list of strings.
     */
    Object superuserRole
    /**
     * Shortcut for setting the yana User read-only role value, can be a string or list of strings, or string containing the
     * stringSeparator which will be split into a list of strings.
     */
    Object userRole

    /**
     * If true, map the default yana internal roles as-is by default
     */
    boolean useDefault

    /**
     * Separator for strings values in the adminRole/operatorRole/architectRole, default value is "," (comma)
     */
    String stringSeparator = ","

    public void afterPropertiesSet() throws Exception {
        if (adminRole) {
            setRoleForObject(YanaConstants.ROLE_ADMIN, adminRole)
        }
        if (operatorRole) {
            setRoleForObject(YanaConstants.ROLE_OPERATOR, operatorRole)
        }
        if (architectRole) {
            setRoleForObject(YanaConstants.ROLE_ARCHITECT, architectRole)
        }
        if (superuserRole) {
            setRoleForObject(YanaConstants.ROLE_SUPERUSER, superuserRole)
        }
        if (userRole) {
            setRoleForObject(YanaConstants.ROLE_USER, userRole)
        }
        Map<String, String> allRoles = (useDefault ? defaultMap : [:]) + roleMap
        if (!YanaConstants.ALL_ROLES.every {it in allRoles.values()}) {
            log.error("YanaAuthoritiesMapper configuration failed: not all Yana internal roles have a mapping. Set all role properties (adminRole, userRole, superuserRole, architectRole, operatorRole) or define the roleMap to include the Yana internal roles as values: ${YanaConstants.ALL_ROLES}")
        }
        Assert.notEmpty(allRoles, "roleMap or adminRole/operatorRole/architectRole must be set");
        Assert.isTrue(allRoles.containsValue(YanaConstants.ROLE_ADMIN), YanaConstants.ROLE_ADMIN + ' was not mapped by a container role')
        Assert.isTrue(allRoles.containsValue(YanaConstants.ROLE_OPERATOR), YanaConstants.ROLE_OPERATOR + ' was not mapped by a container role')
        Assert.isTrue(allRoles.containsValue(YanaConstants.ROLE_ARCHITECT), YanaConstants.ROLE_ARCHITECT + ' was not mapped by a container role')
        Assert.isTrue(allRoles.containsValue(YanaConstants.ROLE_SUPERUSER), YanaConstants.ROLE_SUPERUSER + ' was not mapped by a container role')
        Assert.isTrue(allRoles.containsValue(YanaConstants.ROLE_USER), YanaConstants.ROLE_USER + ' was not mapped by a container role')
        setAttributes2grantedAuthoritiesMap(allRoles)
    }

    private setRoleForObject(String role, Object input) {
        if (input instanceof Collection) {
            input.each {
                roleMap[it.toString()] = role
            }
        } else if (stringSeparator) {
            def parts = input.toString().split(stringSeparator)
            parts.findAll {it.trim()}.each {
                roleMap[it.trim()] = role
            }
        } else {
            roleMap[input.toString()] = role
        }
    }
}
