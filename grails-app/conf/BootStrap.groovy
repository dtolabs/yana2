import com.dtolabs.Filter
import com.dtolabs.Role
import com.dtolabs.User
import com.dtolabs.UserRole
import com.dtolabs.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import grails.util.Environment
import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class BootStrap {
    def projectService
    def sessionFactory

    def init = { servletContext ->
		
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
			user = new User(username:"${CH.config.root.login}",password:"${CH.config.root.password}",enabled:true,accountExpired:false,accountLocked:false,passwordExpired:false).save(failOnError:true)
		}
			
		if(!user?.authorities?.contains(rootRole)){
			UserRole.create user,rootRole
		}
		if(!user?.authorities?.contains(adminRole)){
			UserRole.create user,adminRole
		}


        if (Environment.current.name == 'development') {
            Project proj = Project.findByName('default')
            if (!proj) {
                //admin role required to create grants on the new project
                loginAsAdmin()
                projectService.createProject('default', 'Default project')
                sessionFactory.currentSession.flush()

                // logout
                SCH.clearContext()
            }
        }


    }

    private void loginAsAdmin() {
        // have to be authenticated as an admin to create ACLs
        SCH.context.authentication = new UsernamePasswordAuthenticationToken(
                CH.config.root.login, CH.config.root.password,
                AuthorityUtils.createAuthorityList('ROLE_YANA_ADMIN'))
    }


    def destroy = {}
}
