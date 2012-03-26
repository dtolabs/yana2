import com.dtosolutions.*

class BootStrap {

    def init = { servletContext ->
		
		Date now = new Date()

		SecRole rootRole = SecRole.findByAuthority('ROLE_ROOT')?: new SecRole(authority:'ROLE_ROOT').save(faileOnError:true)
		SecRole userRole = SecRole.findByAuthority('ROLE_USER')?: new SecRole(authority:'ROLE_USER').save(faileOnError:true)
		SecUser adminUser = SecUser.findByUsername('admin')?: new SecUser(username:'admin',password:'admin',enabled:'true',accountExpired:'false',accountLocked:'false',passwordExpired:'false').save(failOnError:true)
		
		if(!adminUser?.authorities?.contains(rootRole)){
			SecUserSecRole.create adminUser,rootRole
		}
		
		Filter fStr = Filter.findByDataType('String') ?: new Filter(dataType:'String',regex:'^.*\$',dateCreated:now).save(failOnError:true)
		Filter fBool = Filter.findByDataType('Boolean') ?: new Filter(dataType:'Boolean',regex:"^([0-1]|true|false)\$",dateCreated:now).save(failOnError:true)
		Filter fInt = Filter.findByDataType('Integer') ?: new Filter(dataType:'Integer',regex:"^[0-9]\$",dateCreated:now).save(failOnError:true)
		Filter fFloat = Filter.findByDataType('Float') ?: new Filter(dataType:'Float',regex:"^([+-]?(((\\d+(\\.)?)|(\\d*\\.\\d+))([eE][+-]?\\d+)?))",dateCreated:now).save(failOnError:true)
		Filter fURL = Filter.findByDataType('URL') ?: new Filter(dataType:'URL',regex:"^(http(?:s)?\\:\\/\\/[a-zA-Z0-9\\-]+(?:\\.[a-zA-Z0-9\\-]+)*\\.[a-zA-Z]{2,6}(?:\\/?|(?:\\/[\\w\\-]+)*)(?:\\/?|\\/\\w+\\.[a-zA-Z]{2,4}(?:\\?[\\w]+\\=[\\w\\-]+)?)?(?:\\&[\\w]+\\=[\\w\\-]+)*)\$",dateCreated:now).save(failOnError:true)
		Filter fEmail = Filter.findByDataType('Email') ?: new Filter(dataType:'Email',regex:"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\$",dateCreated:now).save(failOnError:true)
		Filter fDate = Filter.findByDataType('Date') ?: new Filter(dataType:'Date',regex:"^(0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])[-](19|20)\\d\\d\$ ",dateCreated:now).save(failOnError:true)
		Filter fTime = Filter.findByDataType('Time') ?: new Filter(dataType:'Time',regex:"^((([0]?[1-9]|1[0-2])(:|\\.)[0-5][0-9]((:|\\.)[0-5][0-9])?( )?(AM|am|aM|Am|PM|pm|pM|Pm))|(([0]?[0-9]|1[0-9]|2[0-3])(:|\\.)[0-5][0-9]((:|\\.)[0-5][0-9])?))\$",dateCreated:now).save(failOnError:true)
		Filter fDateTime = Filter.findByDataType('DateTime') ?: new Filter(dataType:'Datetime',regex:"^([0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30))))( (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])){0,1}|0000-00-00 00:00:00|0000-00-00\$",dateCreated:now).save(failOnError:true)
		Filter fIP = Filter.findByDataType('IP') ?: new Filter(dataType:'IP',regex:"^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\$",dateCreated:now).save(failOnError:true)
		Filter fOS = Filter.findByDataType('OS') ?: new Filter(dataType:'OS',regex:'^(Linux|linux|Unix|unix|Windows|windows|Mac|mac|Sun|sun|BSD|bsd)\$',dateCreated:now).save(failOnError:true)

		// Server Attributes
		Attribute att1 = Attribute.findByName('Friendly Name') ?: new Attribute(name:'Friendly Name',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att2 = Attribute.findByName('Brand') ?: new Attribute(name:'Brand',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att3 = Attribute.findByName('Model') ?: new Attribute(name:'Model',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att4 = Attribute.findByName('Management IP') ?: new Attribute(name:'Management IP',filter:fIP,dateCreated:now).save(failOnError:true)
		Attribute att5 = Attribute.findByName('Gateway') ?: new Attribute(name:'Gateway',filter:fIP,dateCreated:now).save(failOnError:true)
		Attribute att6 = Attribute.findByName('Number of CPUs') ?: new Attribute(name:'Number of CPUs',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att7 = Attribute.findByName('RAM') ?: new Attribute(name:'RAM',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att8 = Attribute.findByName('HDD') ?: new Attribute(name:'HDD',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att9 = Attribute.findByName('Blackout Starttime') ?: new Attribute(name:'Blackout Starttime',filter:fTime,dateCreated:now).save(failOnError:true)
		Attribute att10 = Attribute.findByName('Blackout Endtime') ?: new Attribute(name:'Blackout Endtime',filter:fTime,dateCreated:now).save(failOnError:true)
		Attribute att11 = Attribute.findByName('OS Name') ?: new Attribute(name:'OS Name',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att12 = Attribute.findByName('OS Family') ?: new Attribute(name:'OS Family',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att13 = Attribute.findByName('Hostname') ?: new Attribute(name:'Hostname',filter:fURL,dateCreated:now).save(failOnError:true)
		
		NodeType server = NodeType.findByName('Server') ?: new NodeType(name:'Server',dateCreated:now).save(failOnError:true)
		NodeType soft = NodeType.findByName('Software') ?: new NodeType(name:'Software',dateCreated:now).save(failOnError:true)
		NodeType db = NodeType.findByName('Database') ?: new NodeType(name:'Database',dateCreated:now).save(failOnError:true)
		
		Template serverTemp = Template.findByTemplateName('Server') ?: new Template(templateName:'Server',nodetype:server,dateCreated:now).save(failOnError:true)
		Template softTemp = Template.findByTemplateName('Software') ?: new Template(templateName:'Software',nodetype:soft,dateCreated:now).save(failOnError:true)
		Template dbTemp = Template.findByTemplateName('Database') ?: new Template(templateName:'database',nodetype:db,dateCreated:now).save(failOnError:true)
		
		// server template attributes
		TemplateAttribute serverTA1 = TemplateAttribute.findByAttributeAndTemplate(att1,serverTemp) ?: new TemplateAttribute(attribute:att1,template:serverTemp).save(failOnError:true)
		TemplateAttribute serverTA2 = TemplateAttribute.findByAttributeAndTemplate(att2,serverTemp) ?: new TemplateAttribute(attribute:att2,template:serverTemp).save(failOnError:true)
		TemplateAttribute serverTA3 = TemplateAttribute.findByAttributeAndTemplate(att3,serverTemp) ?: new TemplateAttribute(attribute:att3,template:serverTemp).save(failOnError:true)
		TemplateAttribute serverTA4 = TemplateAttribute.findByAttributeAndTemplate(att4,serverTemp) ?: new TemplateAttribute(attribute:att4,template:serverTemp).save(failOnError:true)
		TemplateAttribute serverTA5 = TemplateAttribute.findByAttributeAndTemplate(att5,serverTemp) ?: new TemplateAttribute(attribute:att5,template:serverTemp).save(failOnError:true)
		TemplateAttribute serverTA6 = TemplateAttribute.findByAttributeAndTemplate(att6,serverTemp) ?: new TemplateAttribute(attribute:att6,template:serverTemp).save(failOnError:true)
		TemplateAttribute serverTA7 = TemplateAttribute.findByAttributeAndTemplate(att7,serverTemp) ?: new TemplateAttribute(attribute:att7,template:serverTemp).save(failOnError:true)
		TemplateAttribute serverTA8 = TemplateAttribute.findByAttributeAndTemplate(att8,serverTemp) ?: new TemplateAttribute(attribute:att8,template:serverTemp).save(failOnError:true)
		TemplateAttribute serverTA9 = TemplateAttribute.findByAttributeAndTemplate(att9,serverTemp) ?: new TemplateAttribute(attribute:att9,template:serverTemp).save(failOnError:true)
		TemplateAttribute serverTA10 = TemplateAttribute.findByAttributeAndTemplate(att10,serverTemp) ?: new TemplateAttribute(attribute:att10,template:serverTemp).save(failOnError:true)
		TemplateAttribute serverTA11 = TemplateAttribute.findByAttributeAndTemplate(att11,serverTemp) ?: new TemplateAttribute(attribute:att11,template:serverTemp).save(failOnError:true)
		TemplateAttribute serverTA12 = TemplateAttribute.findByAttributeAndTemplate(att12,serverTemp) ?: new TemplateAttribute(attribute:att12,template:serverTemp).save(failOnError:true)
		TemplateAttribute serverTA13 = TemplateAttribute.findByAttributeAndTemplate(att13,serverTemp) ?: new TemplateAttribute(attribute:att13,template:serverTemp).save(failOnError:true)

    }
    def destroy = {}
}
