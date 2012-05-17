package com.dtosolutions

import java.util.ArrayList;

import grails.converters.JSON

class JsonService {
	
	static transactional = false
	static scope = "prototype"
	
	String formatNodes(ArrayList data){
			ArrayList result = [:]
			data.each(){ val1 ->
				def attributequery = "select new map(TV.value as value,A.name as attribute,TA.required as required) from TemplateValue as TV left join TV.node as N left join TV.templateattribute as TA left join TA.attribute as A where N.id=${val1.id.toLong()} order by A.name desc"
				def values = TemplateValue.executeQuery(attributequery);
				
				def rents = ChildNode.findByChild(Node.get(val1.id.toLong()));
				ArrayList rent = [:]
				rents.each{ parent ->
					rent += [node:[id:parent.parent.id,name:parent.parent.name,type:parent.parent.nodetype.name,tags:parent.parent.tags]]
				}
				ArrayList kinder = [:]
				val1.children.each{ child ->
					kinder += [node:[id:child.child.id,name:child.child.name,type:child.child.nodetype.name,tags:child.child.tags]]
				}
				
				result += [node:[id:val1.id,name:val1.name,type:val1.nodetype.name,tags:val1.tags],
					description:val1.description,
					attributes:
						values.each{ val2 ->
							attribute:[name:val2.attribute,value:val2.value,required:val2.required]
						}
					,
					parents:rent,
					children:kinder
				]

			}

		return result as JSON
	}
	
	String formatNodeTypes(ArrayList data){
		ArrayList result = [:]
		data.each(){ val1 ->
			result += 	[nodetype:[id:val1.id,name:val1.name,description:val1.description,image:val1.image]]
		}
		return result as JSON
	}
	
	String formatHooks(ArrayList data){
		ArrayList result = [:]
		data.each(){ val1 ->
			result += 	[hook:[id:val1.id,name:val1.name,url:val1.url,format:val1.format,service:val1.service,fails:val1.attempts],user:val1.user.username]
		}
		return result as JSON
	}
	
}
