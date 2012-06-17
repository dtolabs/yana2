import com.dtolabs.Filter
import com.dtolabs.Role
import com.dtolabs.User
import com.dtolabs.UserRole
import com.dtosolutions.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class BootStrap {

    def init = { servletContext ->
		
		Date now = new Date()

		Role adminRole = Role.findByAuthority('ROLE_YANA_ADMIN')?: new Role(authority:'ROLE_YANA_ADMIN').save(faileOnError:true)
		Role userRole = Role.findByAuthority('ROLE_YANA_USER')?: new Role(authority:'ROLE_YANA_USER').save(faileOnError:true)
		Role archRole = Role.findByAuthority('ROLE_YANA_ARCHITECT')?: new Role(authority:'ROLE_YANA_ARCHITECT').save(faileOnError:true)
		Role rootRole = Role.findByAuthority('ROLE_YANA_SUPERUSER')?: new Role(authority:'ROLE_YANA_SUPERUSER').save(faileOnError:true)
		
		User user = User.get(1)
		if(user?.id){
			user.username="${CH.config.root.login}"
			user.password="${CH.config.root.password}"
			user.save(faileOnError:true)
		}else{
			user = new User(username:"${CH.config.root.login}",password:"${CH.config.root.password}",enabled:'true',accountExpired:'false',accountLocked:'false',passwordExpired:'false').save(failOnError:true)
		}
			
		if(!user?.authorities?.contains(rootRole)){
			UserRole.create user,rootRole
		}

		Filter fStr = Filter.findByDataType('String') ?: new Filter(dataType:'String',regex:'^.*\$',dateCreated:now).save(failOnError:true)
		Filter fBool = Filter.findByDataType('Boolean') ?: new Filter(dataType:'Boolean',regex:"^([0-1]|true|false)\$",dateCreated:now).save(failOnError:true)
		Filter fInt = Filter.findByDataType('Integer') ?: new Filter(dataType:'Integer',regex:"^[0-9]\$",dateCreated:now).save(failOnError:true)
		Filter fFloat = Filter.findByDataType('Float') ?: new Filter(dataType:'Float',regex:"^([+-]?(((\\d+(\\.)?)|(\\d*\\.\\d+))([eE][+-]?\\d+)?))",dateCreated:now).save(failOnError:true)
		Filter fURL = Filter.findByDataType('URL') ?: new Filter(dataType:'URL',regex:"^(http(?:s)?\\:\\/\\/[a-zA-Z0-9\\-]+(?:\\.[a-zA-Z0-9\\-]+)*\\.[a-zA-Z]{2,6}(?:\\/?|(?:\\/[\\w\\-]+)*)(?:\\/?|\\/\\w+\\.[a-zA-Z]{2,4}(?:\\?[\\w]+\\=[\\w\\-]+)?)?(?:\\&[\\w]+\\=[\\w\\-]+)*)\$",dateCreated:now).save(failOnError:true)
		Filter fEmail = Filter.findByDataType('Email') ?: new Filter(dataType:'Email',regex:"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\$",dateCreated:now).save(failOnError:true)
		Filter fDate = Filter.findByDataType('Date') ?: new Filter(dataType:'Date',regex:"^(0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])[-](19|20)\\d\\d\$ ",dateCreated:now).save(failOnError:true)
		Filter fTime = Filter.findByDataType('Time') ?: new Filter(dataType:'Time',regex:"^((([0]?[1-9]|1[0-2])(:|\\.)[0-5][0-9]((:|\\.)[0-5][0-9])?( )?(AM|am|aM|Am|PM|pm|pM|Pm))|(([0]?[0-9]|1[0-9]|2[0-3])(:|\\.)[0-5][0-9]((:|\\.)[0-5][0-9])?))\$",dateCreated:now).save(failOnError:true)
		Filter fDateTime = Filter.findByDataType('DateTime') ?: new Filter(dataType:'DateTime',regex:"^([0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30))))( (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])){0,1}|0000-00-00 00:00:00|0000-00-00\$",dateCreated:now).save(failOnError:true)
		Filter fIP = Filter.findByDataType('IP') ?: new Filter(dataType:'IP',regex:"^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\$",dateCreated:now).save(failOnError:true)
		Filter fOS = Filter.findByDataType('OS') ?: new Filter(dataType:'OS',regex:'^(Linux|linux|Unix|unix|Windows|windows|Mac|mac|Sun|sun|BSD|bsd)\$',dateCreated:now).save(failOnError:true)
    }
    def destroy = {}
}
