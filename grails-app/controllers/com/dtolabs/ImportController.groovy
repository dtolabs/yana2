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
import com.dtolabs.Node
import com.dtolabs.NodeTypeRelationship
import com.dtolabs.NodeAttribute
import com.dtolabs.NodeValue
import grails.plugins.springsecurity.Secured
import org.xml.sax.SAXException

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_SUPERUSER'])
class ImportController {

	def springSecurityService
	def webhookService
	def projectService

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
	
	def importxml() {
        [projectList:projectService.listProjects()]
    }
	
	
	// all children are aspects of groovy.util.slurpersupport.NodeChild
	// this allows us to pass them as NodeChild objects
	def savexml() {
        def project = Project.findByName(params.project)
        if(!project){
            request.message = message(code: 'default.not.found.message', args: [params.project], default: "Project {0} was not found")
            return redirect(action: 'importxml')
        }
		if(!request.getFile("yanaimport").empty){
			def xml = new XmlSlurper().parse(request.getFile("yanaimport").inputStream)
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            def xsdIn= servletContext.getResourceAsStream("/xsd/yana.xsd")
            Schema schema = factory.newSchema(new StreamSource(xsdIn))
			Validator validator = schema.newValidator()
			
			Date now = new Date()

			try{
				// attempt to validate first
				validator.validate(new StreamSource(request.getFile("yanaimport").inputStream))

				// parse attributes
				xml.attributes.children().each{ attribute ->
					Attribute att = Attribute.findByProjectAndName(project,attribute.@id.toString())
					if(!att){
						//get dependencies first
						//println("attribute class:"+attribute.getClass())

						Filter filter = Filter.findByProjectAndDataType(project,attribute.@filter.toString())
						
						att = new Attribute()
                        att.project= project
						att.name = attribute.@id
						att.filter = filter
						att.dateCreated = new Date()
						att.dateModified = new Date()
						att.description=''
						
						att.save(flush: true,failOnError:true)
					}
				}

				// parse nodetypes and nodeattributes
				xml.nodetypes.children().each{ nodetype ->
                    //TODO: project in query
					NodeType ntype = NodeType.findByProjectAndName(project,nodetype.@id.toString())
					if(!ntype){
						//println("nodetype class name:"+nodetype.getClass())

						ntype = new NodeType()
                        ntype.project= project
						ntype.name = nodetype.@id
						ntype.description=nodetype.description.text()
						ntype.image=nodetype.image.text()
						ntype.dateCreated = new Date()
						ntype.dateModified = new Date()
						
						ntype.save(flush: true,failOnError:true)
					}
					def order = 1
					nodetype.nodeAttributes.children().each{ nodeAttribute ->
						Attribute attribute = Attribute.findByProjectAndName(project,nodeAttribute.@attribute.toString())
						NodeAttribute ta = NodeAttribute.findByNodetypeAndAttribute(ntype,attribute)
						if(!ta){
							ta = new NodeAttribute()
							ta.nodetype = ntype
							ta.attribute = attribute
							ta.required = nodeAttribute.@required.toString().toBoolean();
							//ta.order = order
							ta.save(flush: true,failOnError:true)
							order++
						}
					}
				}
				Node nd
				// parse nodes and attributevalues
				xml.nodes.children().each{ node ->
					nd = Node.findByProjectAndName(project,node.@id.toString())
					NodeType nodetype = NodeType.findByProjectAndName(project,node.@nodetype.toString())
					if(!nd){
						nd = new Node()
                        nd.project= project
						nd.name = node.@id
						nd.description = node.description.toString()
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
						def att = xml.nodetypes.nodetype.nodeAttributes.nodeAttribute.findAll { it.@id.text()==nodeAttribute }
						Attribute attribute = Attribute.findByProjectAndName(project,att.@attribute.toString())
						NodeAttribute ta = NodeAttribute.findByNodetypeAndAttribute(nodetype,attribute)
	
						NodeValue tv = new NodeValue()
						tv.node = nd
						tv.nodeattribute = ta
						tv.value = nodeValue.toString()
						tv.dateCreated = new Date()
						tv.dateModified = new Date()
						tv.save(flush: true,failOnError:true)
					}
				}

				// parse nodetype parent/child
				xml.nodetyperelationships.children().each{ nodetypechild ->
					// get dependencies
					NodeType parent = NodeType.findByProjectAndName(project,nodetypechild.@parent.toString())
					NodeType child = NodeType.findByProjectAndName(project,nodetypechild.@child.toString())

					NodeTypeRelationship childnodetype = NodeTypeRelationship.findByChildAndParent(child,parent)
					if(!childnodetype){
						childnodetype  = new NodeTypeRelationship()
						childnodetype.roleName = nodetypechild.@rolename.toString()
						childnodetype.child = child
						childnodetype.parent = parent
						childnodetype.save(flush: true,failOnError:true)
					}
				}

				// parse node parent/child
				xml.noderelationships.children().each{ nodechild ->
					// get dependencies
					Node parent = Node.findByProjectAndName(project,nodechild.@parent.toString())
					Node child = Node.findByProjectAndName(project,nodechild.@child.toString())

					Node[] childNodeTypes = Node.findAllByNodetype(child.nodetype)
					Node[] parentNodeTypes = Node.findAllByNodetype(parent.nodetype)
					
					NodeTypeRelationship childnodetype = NodeTypeRelationship.findByChildAndParent(child.nodetype,parent.nodetype)
					
					ChildNode childnode = ChildNode.findByChildAndParent(child,parent)
					if(!childnode && childnodetype){
						childnode  = new ChildNode()
						childnode.relationshipName = nodechild.@relationshipname.toString()
						childnode.child = child
						childnode.parent = parent
						childnode.save(flush: true,failOnError:true)
					}else{
						//throw new SAXException( "Nodechild relationship not within bounds as described by nodetypechild." )
					}
				}

				ArrayList nodes = [nd]
				webhookService.postToURL('node', nodes,'create')
				
	        } catch (SAXException e) {
		        flash.message = "Error in xml schema: " + e.message
		        redirect(action: "importxml")
	        }
		}else{
			flash.message = "Import file cannot be empty. Please try again. "
			redirect(action: "importxml")
		}




	}
}
