package com.dtosolutions

import groovy.xml.MarkupBuilder
import org.custommonkey.xmlunit.*

class XmlService {
	
	static transactional = false
	static scope = "prototype"
	
	String formatNodes(ArrayList nodes){
		def writer = new StringWriter()
		def xml = new MarkupBuilder(writer)
			
		xml.nodes() {
			nodes.each(){ val1 ->
				def attributequery = "select new map(TV.value as value,A.name as attribute,TA.required as required) from TemplateValue as TV left join TV.node as N left join TV.templateattribute as TA left join TA.attribute as A where N.id=${val1.id.toLong()} order by A.name desc"
				def values = TemplateValue.executeQuery(attributequery);
				
				node(id:val1.id,name:val1.name,type:val1.nodetype.name,tags:val1.tags){
					description(val1.description)
					attributes(){
						values.each{ val2 ->
							attribute(name:val2.attribute,value:val2.value,required:val2.required)
						}
					}
					
					parents(){
						def rents = ChildNode.findByChild(Node.get(val1.id.toLong()));
						rents.each{ parent ->
							node(id:parent.parent.id,name:parent.parent.name,type:parent.parent.nodetype.name,tags:parent.parent.tags)
						}
					}
					children(){
						val1.children.each{ child ->
							node(id:child.child.id,name:child.child.name,type:child.child.nodetype.name,tags:child.child.tags)
						}
					}
				}
			}
		}
		return writer.toString()
	}
	
	String formatHooks(ArrayList hooks){
		def writer = new StringWriter()
		def xml = new MarkupBuilder(writer)
			
		xml.hooks() {
			hooks.each(){ val1 ->
				hook(id:val1.id,name:val1.name,url:val1.url,format:val1.format,service:val1.service,fails:val1.attempts){
					user(val1.user.username)
				}
			}
		}
		return writer.toString()
	}
}
