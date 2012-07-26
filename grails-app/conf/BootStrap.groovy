import com.dtolabs.Filter
import com.dtolabs.Role
import com.dtolabs.User
import com.dtolabs.UserRole
import com.dtolabs.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import grails.util.Environment

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


        if (Environment.current.name == 'development') {
            Project proj = Project.findByName('default')
            if (!proj) {
                proj=new Project(name: 'default', description: 'Default project')
                proj.save(failOnError: true,flush:true)
            }
            assert null!=proj
            Properties defaultFilters = new Properties()
            def instream=servletContext.getResourceAsStream("/properties/default-filters.properties")
            if(instream){
                defaultFilters.load(instream)
            }
            defaultFilters.each{String k,String v->
                if(!Filter.findByProjectAndDataType(proj,k)){
                    Filter test = new Filter(dataType: k, regex: v)
                    test.project=proj
                    test.save(failOnError: true,flush:true)
                }
            }
        }


    }
    def destroy = {}
}
