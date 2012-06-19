package com.dtolabs

import java.util.Date;
import java.util.Set;

//import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator
import com.dtolabs.Attribute
import com.dtolabs.ChildNode
import com.dtolabs.Filter
import com.dtolabs.NodeType
import com.dtolabs.NodeTypeRelationship
import com.dtolabs.NodeAttribute
import com.dtolabs.NodeValue
import grails.plugins.springsecurity.Secured
import org.xml.sax.SAXException

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_SUPERUSER'])
class AdminController {

	def springSecurityService
	
	/*
	 * Restful function to handle routing
	 * URLMapping wants to route everything to node or take over routing for node; needed to build
	 * routing function to handle REST handling to do custom routing for anything that doesn't 
	 * look like it is handled by controller
	 */
	def api(){
		switch(request.method){
			case "POST":
				def json = request.JSON
				this.savexml()
				break
			case "GET":
				//this.show()
				break
			case "PUT":
				//this.update()
				break
			case "DELETE":
				//this.delete()
				break
		  }
	}
	
    def index() {}
	
	def importxml() {}
	
	def savexml() {
		def xml = new XmlSlurper().parse(request.getFile("yanaimport").inputStream)

		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = factory.newSchema(new File("docs/yana.xsd"));
		Validator validator = schema.newValidator()
		
		Date now = new Date()
		
		try{
			// attempt to validate first
			validator.validate(new StreamSource(request.getFile("yanaimport").inputStream))
			
			// parse attributes
			xml.attributes.children().each{ attribute ->
				Attribute att = Attribute.findByName(attribute.@id.toString())
				if(!att){
					//get dependencies first
					Filter filter = Filter.findByDataType(attribute.@filter.toString())
					
					att = new Attribute()
					att.name = attribute.@id
					att.filter = filter
					att.dateCreated = new Date()
					att.dateModified = new Date()
					att.save(flush: true,failOnError:true)
				}
			}
		
			// parse nodetypes and templateattributes
			xml.nodetypes.children().each{ nodetype ->
				NodeType ntype = NodeType.findByName(nodetype.@id.toString())
				if(!ntype){
					ntype = new NodeType()
					ntype.name = nodetype.@id
					ntype.dateCreated = new Date()
					ntype.dateModified = new Date()
					ntype.save(flush: true,failOnError:true)
				}
				def order = 1
				nodetype.children().each{ nodeAttribute ->
					Attribute attribute = Attribute.findByName(nodeAttribute.@attribute.toString())
					NodeAttribute ta = NodeAttribute.findByTemplateAndAttribute(ntype,attribute)
					if(!ta){
						ta = new NodeAttribute()
						ta.template = ntype
						ta.attribute = attribute
						//ta.order = order
						ta.save(flush: true,failOnError:true)
						order++
					}
				}
			}
			
			// parse nodes and attributevalues
			xml.nodes.children().each{ node ->
				Node nd = Node.findByName(node.@id.toString())
				NodeType nodetype = NodeType.findByName(node.@nodetype.toString())
				if(!nd){
					nd = new Node()
					nd.name = node.@id
					nd.description = node.description.toString()
					nd.status = Status.IMP
					nd.tags = node.@tags.toString()
					nd.nodetype = nodetype
					nd.dateCreated = new Date()
					nd.dateModified = new Date()
					nd.save(flush: true,failOnError:true)
				}else{
					NodeValue.executeUpdate("delete NodeValue TV where TV.node = ?", [nd])
				}

				node.values.children().each{ nodeValue ->
					def nodeAttribute = nodeValue.@nodeAttribute.toString()
					def att = xml.nodetypes.nodetype.nodeAttribute.findAll { it.@id.text()==nodeAttribute }
					Attribute attribute = Attribute.findByName(att.@attribute.toString())
					NodeAttribute ta = NodeAttribute.findByTemplateAndAttribute(nodetype,attribute)

					NodeValue tv = new NodeValue()
					tv.node = nd
					tv.templateattribute = ta
					tv.value = nodeValue.toString()
					tv.dateCreated = new Date()
					tv.dateModified = new Date()
					tv.save(flush: true,failOnError:true)
				}
			}
			
			// parse nodetype parent/child
			xml.nodetyperelationships.children().each{ nodetypechild ->
				// get dependencies
				NodeType parent = NodeType.findByName(nodetypechild.@parent.toString())
				NodeType child = NodeType.findByName(nodetypechild.@child.toString())

				NodeTypeRelationship childnodetype = NodeTypeRelationship.findByChildAndParent(child,parent)
				if(!childnodetype){
					childnodetype  = new NodeTypeRelationship()
					childnodetype.roleName = nodetypechild.@rolename.toString()
					if(nodetypechild.@parentCardinality.toString()){
						childnodetype.parentCardinality = nodetypechild.@parentCardinality.toInteger()
					}
					if(nodetypechild.@childCardinality.toString()){
						childnodetype.childCardinality = nodetypechild.@childCardinality.toInteger()
					}
					childnodetype.child = child
					childnodetype.parent = parent
					childnodetype.save(flush: true,failOnError:true)
				}
			}
			
			// parse node parent/child
			xml.noderelationships.children().each{ nodechild ->
				// get dependencies
				Node parent = Node.findByName(nodechild.@parent.toString())
				Node child = Node.findByName(nodechild.@child.toString())
				def childNodeTypes = Node.findByNodetype(child.nodetype)
				def parentNodeTypes = Node.findByNodetype(parent.nodetype)
				
				NodeTypeRelationship childnodetype = NodeTypeRelationship.findByChildAndParent(child.nodetype,parent.nodetype)
				
				ChildNode childnode = ChildNode.findByChildAndParent(child,parent)
				if(!childnode && childnodetype && (!childnodetype.childCardinality || childnodetype.childCardinality==0 || (childNodetypes.size()+1<=childnodetype.childCardinality)) && (!childnodetype.parentCardinality || childnodetype.parentCardinality==0 || (parentNodetypes.size()+1<=childnodetype.parentCardinality))){
					childnode  = new ChildNode()
					childnode.relationshipName = nodechild.@relationshipname.toString()
					childnode.child = child
					childnode.parent = parent
					childnode.save(flush: true,failOnError:true)
				}else{
					//throw new SAXException( "Nodechild relationship not within bounds as described by nodetypechild." )
				}
			}

			
        } catch (SAXException e) {
	        flash.message = "Error in xml schema: " + e.message
	        redirect(action: "importxml")
        }

	}
}
