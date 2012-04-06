package com.dtosolutions

import java.util.Date;
import java.util.Set;

//import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator

import org.xml.sax.SAXException

class AdminController {

    def index() { }
	
	def importxml() {}
	
	def savexml() {
		def xml = new XmlSlurper().parse(request.getFile("yanaimport").inputStream)

		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		//Schema schema = factory.newSchema(new StreamSource(getClass().classLoader.getResourceAsStream("/docs/yana.xsd")));
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
				nodetype.children().each{ templateAttribute ->
					Attribute attribute = Attribute.findByName(templateAttribute.@attribute.toString())
					TemplateAttribute ta = TemplateAttribute.findByTemplateAndAttribute(ntype,attribute)
					if(!ta){
						ta = new TemplateAttribute()
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
					nd.status = Status.IMP
					nd.importance = Importance.MED
					nd.nodetype = nodetype
					nd.dateCreated = new Date()
					nd.dateModified = new Date()
					nd.save(flush: true,failOnError:true)
				}else{
					TemplateValue.executeUpdate("delete TemplateValue TV where TV.node = ?", [nd])
				}

				node.children().each{ templateValue ->
					def templateAttribute = templateValue.@templateAttribute.toString()
					def att = xml.nodetypes.nodetype.templateAttribute.findAll { it.@id.text()==templateAttribute }
					Attribute attribute = Attribute.findByName(att.@attribute.toString())
					TemplateAttribute ta = TemplateAttribute.findByTemplateAndAttribute(nodetype,attribute)

					TemplateValue tv = new TemplateValue()
					tv.node = nd
					tv.templateattribute = ta
					tv.value = templateValue.toString()
					tv.dateCreated = new Date()
					tv.dateModified = new Date()
					tv.save(flush: true,failOnError:true)
				}
			}
			
			// parse node parent/child
			xml.nodechildren.children().each{ nodechild ->
				// get dependencies
				Node parent = Node.findByName(nodechild.@parent.toString())
				Node child = Node.findByName(nodechild.@child.toString())

				ChildNode childnode = ChildNode.findByChildAndParent(child,parent)
				if(!childnode){
					childnode  = new ChildNode()
					childnode.child = child
					childnode.parent = parent
					childnode.save(flush: true,failOnError:true)
				}
			}

			
        } catch (SAXException e) {
	        flash.message = "Error in xml schema: " + e.message
	        redirect(action: "importxml")
        }

	}
}
