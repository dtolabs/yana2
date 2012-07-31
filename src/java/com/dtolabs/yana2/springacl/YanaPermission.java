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
* YanaPermission.java
* 
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: 7/31/12 10:55 AM
* 
*/
package com.dtolabs.yana2.springacl;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

import java.util.*;


/**
 * YanaPermission is ...
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
public class YanaPermission extends BasePermission {
    /**
     * ARCHITECT permission: allow create/update/delete in type level domain classes
     */
    public static final Permission ARCHITECT = new YanaPermission(1 << 5, 'T'); // 32
    /**
     * OPERATOR permission: allow create/update/delete in object level domain classes
     */
    public static final Permission OPERATOR = new YanaPermission(1 << 6, 'O'); // 64

    YanaPermission(final int mask) {
        super(mask);
    }

    YanaPermission(final int mask, final char code) {
        super(mask, code);
    }
}