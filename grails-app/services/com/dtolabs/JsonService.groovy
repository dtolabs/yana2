package com.dtolabs

import java.util.ArrayList;

import grails.converters.JSON

class JsonService {
	
	static transactional = false
	static scope = "prototype"
	
	String formatNodes(ArrayList data){
			ArrayList result = [:]
			data.each(){ val1 ->
				def attributequery = "select new map(TV.value as value,A.name as attribute,A.id as id,TA.required as required) from NodeValue as TV left join TV.node as N left join TV.nodeattribute as TA left join TA.attribute as A where N.id=${val1.id.toLong()} order by A.name desc"
				def values = NodeValue.executeQuery(attributequery);
				
				def rents = ChildNode.findByChild(Node.get(val1.id.toLong()));
				ArrayList rent = [:]
				rents.each{ parent ->
					rent += [node:[id:parent.parent.id,name:parent.parent.name,nodetypeId:parent.parent.nodetype.id,type:parent.parent.nodetype.name,tags:parent.parent.tags]]
				}
				ArrayList kinder = [:]
				val1.children.each{ child ->
					kinder += [node:[id:child.child.id,name:child.child.name,nodetypeId:child.child.nodetype.id,type:child.child.nodetype.name,tags:child.child.tags]]
				}
				
				result += [node:[id:val1.id,name:val1.name,nodetypeId:val1.nodetype.id,type:val1.nodetype.name,tags:val1.tags],
					description:val1.description,
					attributes:
						values.each{ val2 ->
							attribute:[id:val2.id,name:val2.attribute,value:val2.value,required:val2.required]
						}
					,
					parents:rent,
					children:kinder
				]

			}

		return result as JSON
	}
	
	String formatChildNodes(ArrayList data){
		ArrayList result = [:]
		data.each(){ val1 ->
			result += 	[childNode:[id:val1.id,parentNodeId:val1.parent.id,parentName:val1.parent.name,parentNodeType:val1.parent.nodetype.name,childNodeId:val1.child.id,childName:val1.child.name,childNodeType:val1.child.nodetype.name,relationshipName:val1.relationshipName]]
		}
		return result as JSON
	}
	
	String formatNodeValues(ArrayList data){
		ArrayList result = [:]
		data.each(){ val1 ->
			result += 	[nodeValue:[id:val1.id,nodeId:val1.node.id,nodeAttributeId:val1.nodeattribute.id,value:val1.value]]
		}
		return result as JSON
	}

	
	String formatNodeAttributes(ArrayList data){
		ArrayList result = [:]
		data.each(){ val1 ->
			result += 	[nodeAttribute:[id:val1.id,attributeId:val1.attribute.id,nodetypeId:val1.template.id,required:val1.required]]
		}
		return result as JSON
	}
	
	String formatFilters(ArrayList data){
		ArrayList result = [:]
		data.each(){ val1 ->
			result += 	[attribute:[id:val1.id,dataType:val1.dataType,regex:val1.regex]]
		}
		return result as JSON
	}
	
	String formatAttributes(ArrayList data){
		ArrayList result = [:]
		data.each(){ val1 ->
			result += 	[attribute:[id:val1.id,name:val1.name,description:val1.description,filterId:val1.filter.id]]
		}
		return result as JSON
	}
	
	String formatNodeTypes(ArrayList data){
		ArrayList result = [:]
		data.each(){ val1 ->
			def nodecount = Node.findAllByNodetype(val1).size()
			def tatts = NodeAttribute.findAllByTemplate(NodeType.get(val1.id.toLong()))
			
			def criteria = NodeTypeRelationship.createCriteria()
			def parents = criteria.list{
				eq("child", NodeType.get(val1.id?.toLong()))
			}
			
			def criteria2 = NodeTypeRelationship.createCriteria()
			def children = criteria2.list{
				eq ("parent", NodeType.get(val1.id?.toLong()))
			}
			
			ArrayList attributes = [:]
			tatts.each(){ val2 ->
				attributes += [attribute:[id:val2.id,attributeName:val2.attribute.name,attributeId:val2.attribute.id,nodetypeId:val2.template.id,required:val2.required]]
			}
			ArrayList ntr = [:]
			parents.each(){ val3 ->
				ntr += [nodetypeRelationship:[id:val3.id,parentNodeId:val3.parent.id,parentName:val3.parent.name,childNodeId:val3.child.id,childName:val3.child.name,roleName:val3.roleName]]
			}
			children.each(){ val4 ->
				ntr += [nodetypeRelationship:[id:val4.id,parentNodeId:val4.parent.id,parentName:val4.parent.name,childNodeId:val4.child.id,childName:val4.child.name,roleName:val4.roleName]]
			}

			result += 	[nodetype:[id:val1.id,name:val1.name,description:val1.description,image:val1.image,nodeAttributes:attributes,nodetypeRelationships:ntr,nodeCount:nodecount]]
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
