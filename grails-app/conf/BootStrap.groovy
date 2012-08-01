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
import com.dtolabs.yana2.springacl.YanaPermission

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
            //create dev users
            User archUser = User.findByUsername('arch1')
            if(!archUser){
                archUser = new User(username: 'arch1',password: 'arch1',enabled: true,accountExpired: false,accountLocked: false,passwordExpired: false).save(failOnError: true)
                UserRole.create archUser,archRole
            }

            User opUser = User.findByUsername('op1')
            if(!opUser){
                opUser = new User(username: 'op1',password: 'op1',enabled: true,accountExpired: false,accountLocked: false,passwordExpired: false).save(failOnError: true)
                UserRole.create opUser,userRole
            }



            Project proj = Project.findByName('default')
            if (!proj) {
                //admin role required to create grants on the new project
                loginAsAdmin()
                projectService.createProject('default', 'Default project')
                Project t1=projectService.createProject('test1', 'Default project')
                Project t2 =projectService.createProject('test2', 'Default project')

                //deny read to test1 by arch1
                projectService.denyPermission(t1,'arch1',YanaPermission.READ)

                //deny read to test2 by op1
                projectService.denyPermission(t2, 'op1', YanaPermission.READ)

                log.error("completed")

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
