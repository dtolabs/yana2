import com.dtosolutions.*

class BootStrap {

    def init = { servletContext ->
		
		Date now = new Date()
		/*
		SecRole rootRole = SecRole.findByAuthority('ROLE_ROOT')?: new SecRole(authority:'ROLE_ROOT').save(faileOnError:true)
		SecRole userRole = SecRole.findByAuthority('ROLE_USER')?: new SecRole(authority:'ROLE_USER').save(faileOnError:true)
		SecUser adminUser = SecUser.findByUsername('orubel')?: new SecUser(username:'owen',password:'th3r3dp1ll',enabled:'true',accountExpired:'false',accountLocked:'false',passwordExpired:'false').save(failOnError:true)
		if(!adminUser?.authorities?.contains(rootRole)){
			SecUserSecRole.create adminUser,rootRole
		}
		
		Filter fStr = new Filter(dataType:'String',regex:'^.*\$',dateCreated:now).save(failOnError:true)
		Filter fBool = new Filter(dataType:'Boolean',regex:"^([0-1]|true|false)\$",dateCreated:now).save(failOnError:true)
		Filter fInt = new Filter(dataType:'Integer',regex:"^[0-9]\$",dateCreated:now).save(failOnError:true)
		Filter fFloat = new Filter(dataType:'Float',regex:"^([+-]?(((\\d+(\\.)?)|(\\d*\\.\\d+))([eE][+-]?\\d+)?))",dateCreated:now).save(failOnError:true)
		Filter fURL = new Filter(dataType:'URL',regex:"^(http(?:s)?\\:\\/\\/[a-zA-Z0-9\\-]+(?:\\.[a-zA-Z0-9\\-]+)*\\.[a-zA-Z]{2,6}(?:\\/?|(?:\\/[\\w\\-]+)*)(?:\\/?|\\/\\w+\\.[a-zA-Z]{2,4}(?:\\?[\\w]+\\=[\\w\\-]+)?)?(?:\\&[\\w]+\\=[\\w\\-]+)*)\$",dateCreated:now).save(failOnError:true)
		Filter fEmail = new Filter(dataType:'Email',regex:"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\$",dateCreated:now).save(failOnError:true)
		Filter fDate = new Filter(dataType:'Date',regex:"^(0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])[-](19|20)\\d\\d\$ ",dateCreated:now).save(failOnError:true)
		Filter fTime = new Filter(dataType:'Time',regex:"^((([0]?[1-9]|1[0-2])(:|\\.)[0-5][0-9]((:|\\.)[0-5][0-9])?( )?(AM|am|aM|Am|PM|pm|pM|Pm))|(([0]?[0-9]|1[0-9]|2[0-3])(:|\\.)[0-5][0-9]((:|\\.)[0-5][0-9])?))\$",dateCreated:now).save(failOnError:true)
		Filter fDateTime = new Filter(dataType:'Datetime',regex:"^([0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30))))( (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])){0,1}|0000-00-00 00:00:00|0000-00-00\$",dateCreated:now).save(failOnError:true)
		Filter fIP = new Filter(dataType:'IP',regex:"^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\$",dateCreated:now).save(failOnError:true)
		Filter fOS = new Filter(dataType:'OS',regex:'^(Linux|linux|Unix|unix|Windows|windows|Mac|mac|Sun|sun|BSD|bsd)\$',dateCreated:now).save(failOnError:true)

		
		NodeType server = new NodeType(name:'Server',dateCreated:now).save(failOnError:true)
		
		Attribute att1 = new Attribute(name:'Friendly Name',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att2 = new Attribute(name:'Brand',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att3 = new Attribute(name:'Model',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att4 = new Attribute(name:'Serial Number',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att5 = new Attribute(name:'Management IP',filter:fIP,dateCreated:now).save(failOnError:true)
		Attribute att6 = new Attribute(name:'Gateway',filter:fIP,dateCreated:now).save(failOnError:true)
		Attribute att7 = new Attribute(name:'CPU #',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att8 = new Attribute(name:'RAM',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att9 = new Attribute(name:'HDD',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att10 = new Attribute(name:'Blackout Starttime',filter:fTime,dateCreated:now).save(failOnError:true)
		Attribute att11 = new Attribute(name:'Blackout Endtime',filter:fTime,dateCreated:now).save(failOnError:true)
		Attribute att12 = new Attribute(name:'OS Name',filter:fStr,dateCreated:now).save(failOnError:true)
		Attribute att13 = new Attribute(name:'OS Family',filter:fOS,dateCreated:now).save(failOnError:true)
		Attribute att14 = new Attribute(name:'HostName',filter:fURL,dateCreated:now).save(failOnError:true)

		Template template = new Template(templateName:'Server Template',nodetype:server).save(failOnError:true)
		
		TemplateAttribute tAtt1 = new TemplateAttribute(attribute:att1,template:template).save(failOnError:true)
		TemplateAttribute tAtt2 = new TemplateAttribute(attribute:att2,template:template).save(failOnError:true)
		TemplateAttribute tAtt3 = new TemplateAttribute(attribute:att3,template:template).save(failOnError:true)
		TemplateAttribute tAtt4 = new TemplateAttribute(attribute:att4,template:template).save(failOnError:true)
		TemplateAttribute tAtt5 = new TemplateAttribute(attribute:att5,template:template).save(failOnError:true)
		TemplateAttribute tAtt6 = new TemplateAttribute(attribute:att6,template:template).save(failOnError:true)
		TemplateAttribute tAtt7 = new TemplateAttribute(attribute:att7,template:template).save(failOnError:true)
		TemplateAttribute tAtt8 = new TemplateAttribute(attribute:att8,template:template).save(failOnError:true)
		TemplateAttribute tAtt9 = new TemplateAttribute(attribute:att9,template:template).save(failOnError:true)
		TemplateAttribute tAtt10 = new TemplateAttribute(attribute:att10,template:template).save(failOnError:true)
		TemplateAttribute tAtt11 = new TemplateAttribute(attribute:att11,template:template).save(failOnError:true)
		TemplateAttribute tAtt12 = new TemplateAttribute(attribute:att12,template:template).save(failOnError:true)
		TemplateAttribute tAtt13 = new TemplateAttribute(attribute:att13,template:template).save(failOnError:true)
		TemplateAttribute tAtt14 = new TemplateAttribute(attribute:att14,template:template).save(failOnError:true)
*/
    }
    def destroy = {}
}
