package com.dtolabs.yana2.springsecurity

import org.springframework.security.core.authority.mapping.MapBasedAttributes2GrantedAuthoritiesMapper
import org.springframework.beans.factory.InitializingBean
import org.springframework.util.Assert

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
    public static final String ROLE_YANA_ADMIN = 'ROLE_YANA_ADMIN'
    public static final String ROLE_YANA_OPERATOR = 'ROLE_YANA_USER'
    public static final String ROLE_YANA_ARCHITECT = 'ROLE_YANA_ARCHITECT'
    public static final String ROLE_YANA_SUPERUSER = 'ROLE_YANA_SUPERUSER'
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
     * Separator for strings values in the adminRole/operatorRole/architectRole, default value is "," (comma)
     */
    String stringSeparator = ","

    public void afterPropertiesSet() throws Exception {
        if(adminRole){
            setRoleForObject(ROLE_YANA_ADMIN, adminRole)
        }
        if(operatorRole){
            setRoleForObject(ROLE_YANA_OPERATOR, operatorRole)
        }
        if(architectRole){
            setRoleForObject(ROLE_YANA_ARCHITECT, architectRole)
        }
        if(superuserRole){
            setRoleForObject(ROLE_YANA_SUPERUSER, superuserRole)
        }
        Assert.notEmpty(roleMap, "simpleMap or adminRole/operatorRole/architectRole must be set");
        Assert.isTrue(roleMap.containsValue(ROLE_YANA_ADMIN), ROLE_YANA_ADMIN + ' was not mapped by a container role')
        Assert.isTrue(roleMap.containsValue(ROLE_YANA_OPERATOR), ROLE_YANA_OPERATOR + ' was not mapped by a container role')
        Assert.isTrue(roleMap.containsValue(ROLE_YANA_ARCHITECT), ROLE_YANA_ARCHITECT + ' was not mapped by a container role')
        Assert.isTrue(roleMap.containsValue(ROLE_YANA_SUPERUSER), ROLE_YANA_SUPERUSER + ' was not mapped by a container role')
        setAttributes2grantedAuthoritiesMap(roleMap)
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
