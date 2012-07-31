
// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

/*
grails.config.locations = [ "classpath:${appName}-config.properties",
                            "classpath:${appName}-config.groovy",
                            "file:${userHome}/.grails/${appName}-config.properties",
                            "file:${userHome}/.grails/${appName}-config.groovy"]

if(System.properties["${appName}.config.location"]) {
   grails.config.locations << "file:" + System.properties["${appName}.config.location"]
}

*/


grails.config.locations = ["file:${userHome}/.yana/config.properties"]
if (System.properties["${appName}.config.location"]) {
    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
}
/*
def deps = [
	"httpcore-ab-4.2.jar"
]
grails.war.dependencies = {
	fileset(dir: "libs") {
		deps.each { pattern -> include(name: pattern) }
	}
}
*/


//grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
					  xml: ['text/xml', 'application/xml'],
					  text: 'text/plain',
					  js: 'text/javascript',
					  rss: 'application/rss+xml',
					  atom: 'application/atom+xml',
					  css: 'text/css',
					  csv: 'text/csv',
					  all: '*/*',
					  json: ['application/json','text/json'],
					  form: 'application/x-www-form-urlencoded',
					  multipartForm: 'multipart/form-data'
					]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = false
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// set per-environment serverURL stem for creating absolute links
environments {
	production {
		grails.plugins.springsecurity.portMapper.httpPort = 80
		grails.plugins.springsecurity.portMapper.httpsPort = 443
		
		grails.app.context = "/"
		
		// log4j configuration - log error and above to rolling log file
		log4j = {
			rootLogger="error,stdout"
			appenders {
				'null' name:'stacktrace'
				rollingFile name:'file',
				file: (System.getProperty('catalina.base') ?: 'target') + '/logs/yana.log',
				maxFileSize:"1MB",
				layout: pattern(conversionPattern: '%d %-5p %c{2} %m%n')
			}
			root {
				error 'file'
				additivity = true
				
			}
		}
	}
	staging {
		grails.plugins.springsecurity.portMapper.httpPort = 80
		grails.plugins.springsecurity.portMapper.httpsPort = 443
		
		grails.app.context = "/"
		
		// log4j configuration - log error and above to rolling log file
		log4j = {
			appenders {
				'null' name:'stacktrace'
				rollingFile name:'file',
				file: (System.getProperty('catalina.base') ?: 'target') + '/logs/yana.log',
				maxFileSize:"1MB",
				layout: pattern(conversionPattern: '%d %-5p %c{2} %m%n')
			}
			root {
				error 'file'
				additivity = true
			}
		}
		grails.serverURL = "http://localhost:8080"
	}
	development {
		grails.plugins.springsecurity.portMapper.httpPort = 8080
		grails.plugins.springsecurity.portMapper.httpsPort = 8443
		
		grails.resources.processing.enabled = false
		grails.app.context = "/"
		
		// log4j configuration - log info and above to console
		log4j = {
			appenders {
				'null' name:'stacktrace'
			}
			root {
				error 'stdout'
				additivity = true
			}
		}
		grails.serverURL = "http://localhost:8080"
	}
	test {
		grails.plugins.springsecurity.portMapper.httpPort = 80
		grails.plugins.springsecurity.portMapper.httpsPort = 443
		
		grails.resources.processing.enabled = false
		grails.app.context = "/"
		
		// log4j configuration - log info and above to console
		log4j = {
			appenders {
				'null' name:'stacktrace'
			}
			root {
				error 'stdout'
				additivity = true
			}
		}
		grails.serverURL = "http://localhost:8080"
	}
}

// jquery
grails.views.javascript.library="jquery"

grails.resources.modules = {
    core {
        dependsOn 'jquery-ui'
    }
    // Define reference to custom jQuery UI theme
    overrides {
        'jquery-theme' {
            resource id: 'theme', url: '/css/jquery-ui-1.8.19.custom.css'
        }
    }
}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = "com.dtolabs.User"
grails.plugins.springsecurity.userLookup.authorityJoinClassName = "com.dtolabs.UserRole"
grails.plugins.springsecurity.authority.className = "com.dtolabs.Role"
grails.plugins.springsecurity.rememberMe.persistent = true
grails.plugins.springsecurity.rememberMe.persistentToken.domainClassName = "com.dtolabs.PersistentLogin"


grails.plugins.springsecurity.secureChannel.definition = [
	'/login/**':				'REQUIRES_SECURE_CHANNEL',
	'/register/**':				'REQUIRES_SECURE_CHANNEL',
	'/node/api/**':				'ANY_CHANNEL',
	'/images/**':				'ANY_CHANNEL',
	'/css/**':					'ANY_CHANNEL',
	'/js/**':					'ANY_CHANNEL',
	'/logout/**':				'ANY_CHANNEL',
	'/user/**':					'REQUIRES_SECURE_CHANNEL',
	'/role/**':					'REQUIRES_SECURE_CHANNEL',
	'/registrationCode/**':		'REQUIRES_SECURE_CHANNEL',
	'/securityInfo/**':			'REQUIRES_SECURE_CHANNEL',
 ]

grails.rest.injectInto = ["Controller", "Service", "Routes"]

webhook.services = ["node"]

// spring-security-acl definitions
grails.plugins.springsecurity.acl.authority.modifyAuditingDetails = 'ROLE_YANA_ADMIN'
grails.plugins.springsecurity.acl.authority.changeOwnership = 'ROLE_YANA_ADMIN'
grails.plugins.springsecurity.acl.authority.changeAclDetails = 'ROLE_YANA_ADMIN'