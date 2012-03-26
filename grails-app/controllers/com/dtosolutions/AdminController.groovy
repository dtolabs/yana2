package com.dtosolutions

import java.util.Date;

import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import org.xml.sax.SAXException

class AdminController {

	
    def index() { }
	
	def importxml() {
		
	}
	
	def savexml() {
		def xml = new XmlSlurper().parse(request.getFile("yanaimport").inputStream)
		
		def factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
		def schema = factory.newSchema(new StreamSource(getClass().classLoader.getResourceAsStream("docs/yana.xsd")))
		def validator = schema.newValidator()
		
		Date now = new Date()
		
		try{
			// attempt to validate first
			validator.validate(new StreamSource(request.getFile("yanaimport").inputStream))
				
			xml.locations.children().each{ location ->
				//get dependencies first
				Template locTemp = Template.findByTemplateName('Location_default')
				NodeType locType = NodeType.findByName('Location')
				Node loc = Node.findByName(location.@id.toString())
				if(!loc){
					Node n = new Node()
					n.name = location.@id
					n.template = locTemp
					n.status = Status.IMP
					n.importance = Importance.MED
					n.nodetype = locType
					n.dateCreated = new Date()
					n.dateModified = new Date()
					n.save(failOnError:true)

					//new Node(name:location.@id.toString(),template:locTemp,status:Status.IMP,importance:Importance.MED,nodetype:locType).save(failOnError:true)
				}

				if(!loc){
					TemplateAttribute locatt1 = TemplateAttribute.findByName('Provider Name')
					TemplateAttribute locatt2 = TemplateAttribute.findByName('City')
					TemplateAttribute locatt3 = TemplateAttribute.findByName('State/Province')
					TemplateAttribute locatt4 = TemplateAttribute.findByName('Country')
					TemplateAttribute locatt5 = TemplateAttribute.findByName('Postal Code')
					
					new TemplateValue(node:loc,templateattribute:locatt1,value:location.@provider.toString(),dateCreated:now,dateModified:now).save(failOnError:true)
					new TemplateValue(node:loc,templateattribute:locatt2,value:location.@city.toString(),dateCreated:now,dateModified:now).save(failOnError:true)
					new TemplateValue(node:loc,templateattribute:locatt3,value:location.@state.toString(),dateCreated:now,dateModified:now).save(failOnError:true)
					new TemplateValue(node:loc,templateattribute:locatt4,value:location.@country.toString(),dateCreated:now,dateModified:now).save(failOnError:true)
					new TemplateValue(node:loc,templateattribute:locatt5,value:location.@zip.toString(),dateCreated:now,dateModified:now).save(failOnError:true)
				}
			}
			
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
					att.save(failOnError:true)
				}
			}
			
			xml.nodetypes.children().each{ nodetype ->
				NodeType ntype = NodeType.findByName(nodetype.@id.toString())
				if(!ntype){
					NodeType nt = new NodeType()
					nt.name = nodetype.@id
					nt.dateCreated = new Date()
					nt.dateModified = new Date()
					nt.save(failOnError:true)
				}
			}

			
			xml.artifacts.children().each{ artifact ->
				//get dependencies first
				Template softTemp = Template.findByTemplateName('Software_default')
				NodeType softType = NodeType.findByName('Software')
				
				Node n1 = new Node()
				n1.name = artifact.@id
				n1.template = softTemp
				n1.status = Status.IMP
				n1.importance = Importance.MED
				n1.nodetype = softType
				n1.dateCreated = new Date()
				n1.dateModified = new Date()
				n1.save(failOnError:true)
				//Node soft = Node.findByName(artifact.@id.toString()) ?: new Node(name:artifact.@id,template:softTemp,status:Status.IMP,importance:Importance.MED,nodetype:softType).save(failOnError:true)
			}
			
			// initial creation of templates before creation of nodes
			xml.templates.children().each{ template ->
				Template tmp = Template.findByTemplateName(template.@id.toString())
				if(!tmp){
					NodeType nodetype = NodeType.findByName(template.@nodetype.toString())
					
					Template temp = new Template()
					temp.templateName = template.@id
					temp.nodetype = nodetype
					temp.save(failOnError:true)
				}
			}
			
			xml.nodes.children().each{ node ->
				Node nd = Node.findByName(node.@id.toString())
				if(!nd){
					// get dependencies
					Template template = Template.findByTemplateName(node.@template.toString())
					NodeType nodetype = NodeType.findByName(node.@nodetype.toString())
					
					nd = new Node()
					nd.name = node.@id
					nd.template = template
					nd.status = Status.IMP
					nd.importance = Importance.MED
					nd.nodetype = nodetype
					nd.dateCreated = new Date()
					nd.dateModified = new Date()
					nd.save(failOnError:true)
				}
			}
			
			xml.templates.children().each{ template ->
				//get dependencies
				Template temp = Template.findByTemplateName(template.@id.toString())

				def tav = [:]
				template.templateAttributes.children().each{ templateAttribute ->
					println("test: ${}")
					Attribute attribute = Attribute.findByName(templateAttribute.@attribute.toString())
					TemplateAttribute ta = new TemplateAttribute()
					ta.template = temp
					ta.attribute = attribute
					ta.save(failOnError:true)
					tav.putAt("${templateAttribute.@id.toString()[2..-1]}",ta)
				}
				
				template.templateValues.children().each{ templateValue ->
					Node node = Node.findByName(templateValue.@node.toString())
					TemplateValue tv = new TemplateValue()
					tv.node = node
					tv.templateattribute = tav.getAt("${templateValue.@id.toString()[2..-1]}")
					tv.value = templateValue.@value
					tv.dateCreated = new Date()
					tv.dateModified = new Date()
					tv.save(failOnError:true)
				}
			}
			
			xml.instances.children().each{ instance ->
				// get dependencies

				Node node = Node.findByName(instance.@artifact.toString())
				//Node node = Node.findByName(instance.@node.toString())
				
				Attribute vers = Attribute.findByName("Version")
				TemplateAttribute ta1 = new TemplateAttribute()
				ta1.template = node.template
				ta1.attribute = vers
				ta1.save(failOnError:true);
				
				new TemplateValue(node:node,templateattribute:ta1,value:instance.@softwareversion.toString(),dateCreated:now,dateModified:now).save(failOnError:true)
				
				Attribute license = Attribute.findByName("License")
				TemplateAttribute ta2 = new TemplateAttribute()
				ta2.template = node.template
				ta2.attribute = license
				ta2.save(failOnError:true);
				
				new TemplateValue(node:node,templateattribute:ta2,value:instance.@license.toString(),dateCreated:now,dateModified:now).save(failOnError:true)
			}

			
        } catch (SAXException e) {
	        flash.message = "Error in xml schema: " + e.message
	        redirect(action: "importxml")
        }

	}
}
