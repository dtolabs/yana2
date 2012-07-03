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
import com.dtolabs.Status
import com.dtolabs.NodeTypeRelationship
import com.dtolabs.NodeAttribute
import com.dtolabs.NodeValue
import grails.plugins.springsecurity.Secured
import org.xml.sax.SAXException

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_SUPERUSER'])
class ImportController {

	def springSecurityService
	def webhookService
	
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
	
	
	// all children are aspects of groovy.util.slurpersupport.NodeChild
	// this allows us to pass them as NodeChild objects
	def savexml() {
		if(!request.getFile("yanaimport").empty){
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
						//println("attribute class:"+attribute.getClass())
						Filter filter = Filter.findByDataType(attribute.@filter.toString())
						
						att = new Attribute()
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
					NodeType ntype = NodeType.findByName(nodetype.@id.toString())
					if(!ntype){
						//println("nodetype class name:"+nodetype.getClass())

						ntype = new NodeType()
						ntype.name = nodetype.@id
						ntype.description=nodetype.description.text()
						ntype.image=nodetype.image.text()
						ntype.dateCreated = new Date()
						ntype.dateModified = new Date()
						ntype.save(flush: true,failOnError:true)
					}
					def order = 1
					nodetype.nodeAttributes.children().each{ nodeAttribute ->
						Attribute attribute = Attribute.findByName(nodeAttribute.@attribute.toString())
						NodeAttribute ta = NodeAttribute.findByNodetypeAndAttribute(ntype,attribute)
						if(!ta){
							ta = new NodeAttribute()
							ta.nodetype = ntype
							ta.attribute = attribute
							//ta.order = order
							ta.save(flush: true,failOnError:true)
							order++
						}
					}
				}
				Node nd
				// parse nodes and attributevalues
				xml.nodes.children().each{ node ->
					nd = Node.findByName(node.@id.toString())
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
						def att = xml.nodetypes.nodetype.nodeAttributes.nodeAttribute.findAll { it.@id.text()==nodeAttribute }
						Attribute attribute = Attribute.findByName(att.@attribute.toString())
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
					NodeType parent = NodeType.findByName(nodetypechild.@parent.toString())
					NodeType child = NodeType.findByName(nodetypechild.@child.toString())
	
					NodeTypeRelationship childnodetype = NodeTypeRelationship.findByChildAndParent(child,parent)
					if(!childnodetype){
						childnodetype  = new NodeTypeRelationship()
						childnodetype.roleName = nodetypechild.@rolename.toString()

						childnodetype.parentCardinality = (nodetypechild.@parentCardinality.toString())?nodetypechild.@parentCardinality.toInteger():'999999999'.toInteger()

						childnodetype.childCardinality = (nodetypechild.@childCardinality.toString())?nodetypechild.@childCardinality.toInteger():'999999999'.toInteger()
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
					Node[] childNodeTypes = Node.findAllByNodetype(child.nodetype)
					Node[] parentNodeTypes = Node.findAllByNodetype(parent.nodetype)
					
					NodeTypeRelationship childnodetype = NodeTypeRelationship.findByChildAndParent(child.nodetype,parent.nodetype)
					
					ChildNode childnode = ChildNode.findByChildAndParent(child,parent)
					if(!childnode && childnodetype && (!childnodetype.childCardinality || childnodetype.childCardinality==0 || (childNodeTypes.size()+1<=childnodetype.childCardinality)) && (!childnodetype.parentCardinality || childnodetype.parentCardinality==0 || (parentNodeTypes.size()+1<=childnodetype.parentCardinality))){
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
