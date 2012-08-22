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
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.plugins.springsecurity.SecurityFilterPosition
import com.dtolabs.yana2.YanaConstants

class BootStrap {
    def projectService
    def sessionFactory

    def init = { servletContext ->
        if(CH.config.grails?.plugins?.springsecurity?.providerNames && 'preAuthenticatedAuthenticationProvider' in CH.config.grails.plugins.springsecurity.providerNames){
            SpringSecurityUtils.clientRegisterFilter('j2eePreAuthenticatedProcessingFilter', SecurityFilterPosition.PRE_AUTH_FILTER)
        }

        Role adminRole = Role.findByAuthority(YanaConstants.ROLE_ADMIN)?: new Role(authority: YanaConstants.ROLE_ADMIN).save(faileOnError:true)
		Role userRole = Role.findByAuthority(YanaConstants.ROLE_USER)?: new Role(authority: YanaConstants.ROLE_USER).save(faileOnError:true)
		Role operatorRole = Role.findByAuthority(YanaConstants.ROLE_OPERATOR)?: new Role(authority: YanaConstants.ROLE_OPERATOR).save(faileOnError:true)
		Role archRole = Role.findByAuthority(YanaConstants.ROLE_ARCHITECT)?: new Role(authority: YanaConstants.ROLE_ARCHITECT).save(faileOnError:true)
		Role rootRole = Role.findByAuthority(YanaConstants.ROLE_SUPERUSER)?: new Role(authority: YanaConstants.ROLE_SUPERUSER).save(faileOnError:true)

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
                UserRole.create opUser,operatorRole
            }
            User user1 = User.findByUsername('user1')
            if(!user1){
                user1 = new User(username: 'user1',password: 'user1',enabled: true,accountExpired: false,accountLocked: false,passwordExpired: false).save(failOnError: true)
                UserRole.create user1,userRole
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
                AuthorityUtils.createAuthorityList(YanaConstants.ROLE_ADMIN))
    }


    def destroy = {}
}
