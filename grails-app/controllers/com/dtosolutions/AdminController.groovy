package com.dtosolutions

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
		
		try{
			// attempt to validate first
			validator.validate(new StreamSource(request.getFile("yanaimport").inputStream))
			
			
			xml.locations.children().each{ location ->
				Location loc = Location.findByName(location.@id.toString())
				if(!loc){
					loc = new Location()
					loc.name = location.@id
					loc.providerName = location.@provider
					loc.city = location.@city
					loc.province = location.@state
					loc.country = location.@country
					loc.postalCode = location.@zip
					loc.dateCreated = new Date()
					loc.dateModified = new Date()
					loc.save(failOnError:true)
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
				NodeType nt = new NodeType()
				nt.name = nodetype.@id
				nt.dateCreated = new Date()
				nt.dateModified = new Date()
				nt.save(failOnError:true)
			}
			
			xml.solutions.children().each{ solution ->
				Solution sol = new Solution()
				sol.name = solution.@id
				sol.dateCreated = new Date()
				sol.dateModified = new Date()
				sol.save(failOnError:true)
			}
			xml.solutions.children().each{ solution ->
				Solution sol = Solution.findByName(solution.@name.toString())
				Solution parent = Solution.findByName(solution.@parent.toString())
				if(parent){
					sol.parent=parent
					sol.save(failOnError:true)
				}
			}
			
			xml.artifacts.children().each{ artifact ->
				Artifact art = new Artifact()
				art.name = artifact.@id
				art.dateCreated = new Date()
				art.dateModified = new Date()
				art.save(failOnError:true)
			}
			
			// initial creation of templates before creation of nodes
			xml.templates.children().each{ template ->
				NodeType nodetype = NodeType.findByName(template.@nodetype.toString())
				
				Template temp = new Template()
				temp.templateName = template.@id
				temp.nodetype = nodetype
				temp.save(failOnError:true)
			}
			
			xml.nodes.children().each{ node ->
				Node nd = Node.findByName(node.@id.toString())
				if(!nd){
					// get dependencies
					Template template = Template.findByTemplateName(node.@template.toString())
					NodeType nodetype = NodeType.findByName(node.@nodetype.toString())
					Location location =  Location.findByName(node.@location.toString())
					Solution solution = Solution.findByName(node.@solution.toString())
					
					nd = new Node()
					nd.name = node.@id
					nd.template = template
					nd.status = Status.IMP
					nd.importance = Importance.MED
					nd.location = location
					if(solution){
						nd.solution = solution
					}
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
				Artifact artifact = Artifact.findByName(instance.@artifact.toString())
				Node node = Node.findByName(instance.@node.toString())
				
				Instance insta = new Instance()
				insta.artifact = artifact
				insta.node = node
				insta.license = instance.@license
				insta.softwareVersion = instance.@softwareversion
				insta.save(failOnError:true)
			}

			
        } catch (SAXException e) {
	        flash.message = "Error in xml schema: " + e.message
	        redirect(action: "importxml")
        }

	}
}
