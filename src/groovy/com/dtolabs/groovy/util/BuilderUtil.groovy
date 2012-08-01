package com.dtolabs.groovy.util
 
import groovy.xml.*

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
 * Copied with modifications from: https://github.com/dtolabs/rundeck/blob/development/rundeckapp/src/groovy/BuilderUtil.groovy
 */

/**
 * BuilderUtil assists in generating XML or any groovy builder structure
 * from a standard data structure consisting of Maps, Collections and Strings/other objects.
 *
 * Without any modifications, an XML builder would produce a document by:<br/>
 * <ul>
 * <li>converting each String map key into an element
 * <li>converting each String value into text content (*see note)
 * <li>converting each Collection value into a sequence of elements with the same key (*see note)
 * <li>converting each Map into a sequence of elements
 * </ul>
 * <p>
 * This process can be modified to support XML Attributes, and to support a slightly different mechanism
 * for collections.</p>
 * <p>
 * Attributes can be created by modifying the key to have a prefix of "@attr:".  This can be done with the
 * {@link #addAttribute(java.util.Map, java.lang.String, def)  } or {@link #asAttributeName(java.lang.String)  }
 * or {@link #makeAttribute(java.util.Map, java.lang.String)  } methods.
 * </p>
 * <p>
 * Collections can be wrapped in a plural/single form by using a certain suffix for a key.  If the key ends with "[s]"
 * then the collection is treated in this way:
 * </p>
 * <ul>
 * <li>A singular key name is created by removing the "[s]" suffix.</li>
 * <li>An element is created using the singular key name with "s" appended to it.</li>
 * <li>Each item in the collection is then processed under the enclosing renamed element, using the singular key
 * as the element name</li>
 * </ul>
 *
 * <p>
 * This allows naturally named data such as [myvalues:[1,2,3]] to produce:
 * </p>
 *
 * <pre>
 *    &lt;myvalues&gt;
 *       &lt;myvalue&gt;1&lt;myvalue&gt;
 *       &lt;myvalue&gt;2&lt;myvalue&gt;
 *       &lt;myvalue&gt;3&lt;myvalue&gt;
 *    &lt;/myvalues&gt;
 * </pre>
 * <p>
 * The original key can be configured this way using the {@link #makePlural(java.util.Map, java.lang.String)} method.
 * </p>
 * <p>
 * The contents of an element can be serialized as a CDATA by another mechanism.  If the key ends with "<cdata>"
 * then the "<cdata>" suffix is removed, and the string contents serialized in a CDATA section.  {@link #asCDATAName(java.lang.String) }
 * will return the correct cdata key name from the original key.
 * </p>
 * <p>
 *  Explicit text content for a map can be defined with a key of "_TEXT_".  This value will become the text
 *  content of the element, and can be used if there are other map entries as well such as attributes. The text
 *  content will be generated before any sub-elements.
 * </p>
 * <p>
 *  Examples:
 * </p>
 * <p>
 *  Basic usage to generate a XML string from a Map:
 * </p>
 * <pre>
 *  def map = [root:[element: ['@attr:myattr':'myvalue',subelement:'text']]]
 * def writer = new StringWriter()
 * def mp = new MarkupBuilder(writer)
 * def bu = new BuilderUtil()
 * bu.mapToDom(map, mp, nsprefixes)
 * writer.toString()
 * </pre>
 * <p>
 *     This will generate:
 * </p>
 * <pre>
 * &lt;root&gt;
 *   &lt;element myattr='myvalue'&gt;
 *     &lt;subelement&gt;text&lt;/subelement&gt;
 *   &lt;/element&gt;
 * &lt;/root&gt;
 * </pre>
 *
 */
class BuilderUtil{

    public static ATTR_PREFIX="@attr:"
    public static PLURAL_SUFFIX="[s]"
    public static PLURAL_REPL="s"
    public static CDATA_SUFFIX="<cdata>"
    ArrayList context
    Map namespaces=[:]
    public BuilderUtil(){
        context=new ArrayList()
    }
    /**
     * Utility method to generate an XML string directly
     * @param map
     * @param nsprefixes
     * @return
     */
    public static String mapToXmlString(Map map, nsprefixes=[:]){
        def writer = new StringWriter()
        def mp = new MarkupBuilder(writer)
        def bu = new BuilderUtil()
        bu.mapToDom(map, mp,nsprefixes)
        writer.toString()
    }
    
    public mapToDom( Map map, builder,nsprefixes=[:]){
        //generate a builder strucure using the map components
        for(Object o: map.keySet()){
            final Object val = map.get(o)
            this.objToDom(o,val,builder,nsprefixes)
        }
        builder
    }
    def tagName(name){
        if(name instanceof QName){
            def qp=name.prefix?name.prefix+':':''
            if(!qp && namespaces[name.namespaceURI]){
                qp=namespaces[name.namespaceURI]+':'
            }
            return "${qp}${name.localPart}"
        }else{
            return name
        }
    }
    public objToDom(key,obj,builder,nsprefixes=[:]){
        def nsattrs=[:]
        def seen=false
        if(key instanceof QName && !(obj instanceof Collection)){
            if(!nsprefixes[key.prefix]){
                nsprefixes[key.prefix]=key.namespaceURI
                def op=key.prefix?':'+key.prefix:''
                nsattrs["xmlns${op}"]=key.namespaceURI
                seen=true
            }
        }
        if(null==obj){
            builder."${tagName(key)}"(nsattrs)
        }else if (obj instanceof Collection){
            //iterate
            if(key instanceof String && ((String)key).length()>1 && ((String)key).endsWith(PLURAL_SUFFIX)){
                String keys=(String)key
                String name=keys.substring(0,keys.size()-PLURAL_SUFFIX.size());
                String rekey=name+PLURAL_REPL;
                builder."${rekey}"(nsattrs){
                    for(Object o: (Collection)obj){
                        this.objToDom(name,o,builder,nsprefixes.clone())
                    }
                }
            }else{
                for(Object o: (Collection)obj){
                    this.objToDom(key,o,builder,nsprefixes.clone())
                }
            }
        }else if(obj instanceof Map){
            //try to collect '@' prefixed keys to apply as attributes
            Map map = (Map)obj
            def attrs = map.keySet().findAll{it=~/^${ATTR_PREFIX}/}
            def attrmap=[:]+nsattrs
            if(attrs){
                attrs.each{String s->
                    def x =s.substring(ATTR_PREFIX.length())
                    attrmap[x]=map.remove(s)
                }
            }
            def str= map.remove('_TEXT_')
            builder."${tagName(key)}"(attrmap, str){
                this.mapToDom(map,delegate,nsprefixes.clone())
            }
        }else if(obj.metaClass.respondsTo(obj,'toMap')){
            def map = obj.toMap()
            builder."${tagName(key)}"(nsattrs){
                this.mapToDom(map,delegate,nsprefixes.clone())
            }
        }else {
            String os=obj.toString()
            if(key instanceof String && key.endsWith(CDATA_SUFFIX)){
                //FIXME: wrong way to remove string suffic
                builder."${key-CDATA_SUFFIX}"(nsattrs){
                    mkp.yieldUnescaped("<![CDATA["+os.replaceAll(']]>',']]]]><![CDATA[>')+"]]>")
                }
            }else{    
                builder."${tagName(key)}"(nsattrs,os)
            }
        }
        builder
    }


    /**
     * Add entry to the map for the given key, converting the key into an
     * attribute key identifier
     */
    public static addAttribute(Map map,String key,val){
        map[asAttributeName(key)]=val
        map
    }

    /**
     * Return the key as an attribute key identifier
     */
    protected static String asAttributeName(String key) {
        return ATTR_PREFIX + key
    }

    /**
     * Replace the key in the map with the attribute key identifier,
     * if the map entry exists and is not null
     */
    public static makeAttribute(Map map,String key){
        if(null!=map){
            final Object remove = map.remove(key)
            if(null!=remove){
                map[asAttributeName(key)]=remove
            }
        }
        map
    }


    /**
     * Return a Map with an attribute key identifier created from
     * the given key, and the given value 
     */
    public static Map toAttrMap(String key, val){
        def map=[:]
        if(null!=key){
            map[asAttributeName(key)]=val
        }
        return map
    }

    /**
     * Return the pluralized key form of the key
     */
    public static String pluralize(String key){
       if(key.endsWith(PLURAL_SUFFIX)){
           return key
       }else if(key.endsWith(PLURAL_REPL)){
           def k=key.substring(0,key.size()-PLURAL_REPL.size());
           return k+PLURAL_SUFFIX
       }
       return key+PLURAL_SUFFIX
    }

    /**
     * change the key for the map entry to the pluralized key form
     */
    public static Map makePlural(Map map, String key){
        map[pluralize(key)]=map.remove(key)
        return map
    }

    /**
     * Return the key name for use as a CDATA section
     */
    public static String asCDATAName(String key){
        return key+CDATA_SUFFIX
    }
}