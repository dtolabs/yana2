package com.dtosolutions

import java.util.Date;

import com.dtosolutions.*;

import org.springframework.web.multipart.MultipartFile;
import java.util.Date;



//import java.util.Map;
import org.springframework.web.context.request.RequestContextHolder

class TransferService {

    static transactional = false
    static scope = "prototype"
    
	// def file = request.getFile('import')
    def checkImport(MultipartFile file) {
		def message;
		
		def yana = new XmlSlurper().parse(file.inputStream)
		def locations = yana.locations
		def attributes = yana.attributes
		def nodetypes = yana.nodetypes
		def solutions = yana.solutions
		def artifacts = yana.artifacts
		def nodes = yana.nodes
		def instances = yana.instances
		
		/*
		 * for every nodeType.template.templateAttribute, check that attribute matches an existing attributes.@attribute
		 */
		//def matches = yana.attributes.attribute.findAll{ it.@id.text().contains(it.@attribute) }
		Boolean templateAttributeError = false;
		templates.template.templateAttributes.children().each{ templateAttribute ->
			def matches = yana.attributes.attribute.findAll{ it.@id.text()==templateAttribute.@attribute }
			if(matches==0 || !matches){ 
				message += "There is a mismatch in templateAttribute fields<br>"
				templateAttributeError = true; 
			}
			//if(nodeError){ return message }
		}
		
		Boolean solutionError = false;
		solutions.children().each{ solution ->
			matches = yana.solutions.solution.findAll{ it.@id.text()==solution.@parent }
			if(matches==0 || !matches){
				message += "There is a mismatch in location fields<br>"
				nodeError = true; 
			}
			//if(nodeError){ return message }
		}
		
		def locMatch;
		def solMatch;
		def typeMatch;
		def templateMatch;
		def tvMatch;
		
		Boolean nodeError = false
		nodes.children().each{ node ->
			locMatch = yana.locations.location.findAll{ it.@id.text()==node.@location }
			if(locMatch==0 || !locMatch){
				message += "There is a mismatch in location fields<br>"
				nodeError = true; 
			}
		
			solMatch = yana.solutions.solution.findAll{ it.@id.text()==node.@solution }
			if(solMatch==0 || !solMatch){ 
				message += "There is a mismatch in solution fields (parent)<br>"
				nodeError = true; 
			}
			
			typeMatch = yana.nodetypes.nodetype.findAll{ it.@id.text()==node.@nodetype }
			if(typeMatch==0 || !typeMatch){ 
				message += "There is a mismatch  nodetype fields<br>"
				nodeError = true; 
			}
			
			templateMatch = yana.nodetypes.nodetype.template.findAll{ it.@id.text()==node.@template }
			if(templateMatch==0 || !templateMatch){ 
				message += "There is a mismatch template fields"
				nodeError = true; 
			}
			
			// then do attributes
			node.children().each{ templateValue ->
					matches = yana.nodetypes.nodetype.template.templateAttribute.findAll{ it.@id.text()==templateValue.@templateAttribute }
					if(matches==0 || !matches){ 
						message += "There is a mismatch in templateValue fields<br>"
						nodeError = true; 
					}
			}
			
			//if(nodeError){ return message }
		}
	
		Boolean instanceError = false
		instances.children().each{ instance ->
			matches = yana.artifacts.artifact.findAll{ it.@id.text()==instance.@artifact }
			if(matches==0 || !matches){
				message += "There is a mismatch in instance fields<br>"
				instanceError = true;
			}
			
			matches = yana.nodes.node.findAll{ it.@id.text()==instance.@node }
			if(matches==0 || !matches){
				message += "There is a mismatch in instance fields<br>"
				instanceError = true;
			}
			
			//if(nodeError){ return message }
		}
		
		if(!message){
			return 0;
		}else{
			return message;
		}
    }
	
	def importXML(MultipartFile file) {
		def yana = new XmlSlurper().parse(file.inputStream)
	
		def locations = yana.locations
		def attributes = yana.attributes
		def nodetypes = yana.nodetypes
		def solutions = yana.solutions
		def artifacts = yana.artifacts
		def nodes = yana.nodes
		def instances = yana.instances
		def templates = yana.templates
		
		createLocations(locations)
		createAttributes(attributes)
		createNodeTypes(nodetypes)
		createSolutions(solutions)
		createArtifacts(artifacts)
		createNodes(nodes)
		createInstances(instances)
		createTemplateAttributesValues(templates)
	}

	def createLocations(Map locations){
		locations.children().each{ location ->
			Location loc = new Location()
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

	def createAttributes(Map attributes){
		attributes.children().each{ attribute ->
			//get dependencies first
			Filter filter = Filter.findByDataType(attribute.@filter)
			
			Attribute att = new Attribute()
			att.name = attribute.@id
			att.filter = filter
			att.dateCreated = new Date()
			att.dateModified = new Date()
			att.save(failOnError:true)
		}
	}
	
	def createNodeTypes(Map nodetypes){
		nodetypes.children().each{ nodetype ->
			NodeType nt = new NodeType()
			nt.name = nodetype.@id
			nt.dateCreated = new Date()
			nt.dateModified = new Date()
			nt.save(failOnError:true)
		}
	}
	
	def createSolution(Map solutions){
		solutions.children().each{ solution ->
			Solution sol = new Solution()
			sol.name = solution.@id
			sol.dateCreated = new Date()
			sol.dateModified = new Date()
			sol.save(failOnError:true)
		}
		solutions.children().each{ solution ->
			Solution sol = Solution.findByName(solution.@name)
			Solution parent = Solution.findByName(solution.@parent)
			if(parent){
				sol.parent=parent
				sol.save(failOnError:true)
			}
		}
	}

	def createArtifacts(Map artifacts){
		artifacts.children().each{ artifact ->
			Artifact art = new Artifact()
			art.name = artifact.@id
			art.dateCreated = new Date()
			art.dateModified = new Date()
			art.save(failOnError:true)
		}
	}
	

	def createNodes(Map nodes){
		nodes.children().each{ node ->
			// get dependencies
			Template template = Template.findByTemplateName(node.@template)
			NodeType nodetype = NodeType.findByName(node.@nodetype)
			Location location =  Location.findByName(node.@location)
			Solution solution = Solution.findByName(node.@solution)
			
			Node nd = new Node()
			nd.name = nodetype.@id
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
	
	def createInstance(Map instances){
		instances.children().each{ instance ->
			// get dependencies
			Artifact artifact = Artifact.findByName(node.@artifact)
			Node node = Node.findByName(instance.@node)
			
			Instance insta = new Instance()
			insta.artifact = artifact
			insta.node = node
			insta.license = instance.@license
			insta.save(failOnError:true)
		}
	}

	def createTemplateAttributesValues(Map templates){
		templates.children().each{ template ->
			NodeType nodetype = NodeType.findByName(template.@nodetype)
			
			Template temp = new Template()
			temp.templateName = template.@id
			temp.nodetype = nodetype
			temp.save(failOnError:true)

			def tav = [:]
			template.templateAttributes.children().each{ templateAttribute ->
				Attribute attribute = Attribute.findByName(templateAttribute.@attribute)
				TemplateAttribute ta = new TemplateAttribute()
				ta.template = temp
				ta.attribute = attribute
				ta.save(failOnError:true)
				tav[templateAttribute.@id[2..-1].toInteger()] = ta
			}
			
			template.templateValues.children().each{ templateValue ->
				Node node = Node.findByName(templateValue.@node)
				TemplateValue tv = new TemplateValue()
				tv.node = temp
				tv.templateattribute = tav[templateValue.@templateAttribute[2..-1].toInteger()]
				tv.value = templateValue.@value
				tv.dateCreated = new Date()
				tv.dateModified = new Date()
				tv.save(failOnError:true)
			}
		}
	}
	
	def exportXML(String structureType){
		switch(structureType){
			case 'yana':
			case 'rundeck':
			default:
				structureType='yana';
		}
	}
	
	/*
	 * Gives ability to modify exported data then reimport, replacing existing structure with NEW structure
	 * Far easier than trying to write SQL and changing database.
	 */
	def overwriteXML(MultipartFile file){
		
	}
	
}
