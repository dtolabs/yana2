
import grails.plugins.springsecurity.SecurityConfigType

def deps = [
	"mysql-connector-java-5.1.6-bin.jar"
]

grails.war.dependencies = {
	fileset(dir: "libs") {
		deps.each { pattern -> include(name: pattern) }
	}
}

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
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
		
		// log4j configuration - log error and above to rolling log file
		log4j = {
			appenders {
				'null' name:'stacktrace'
				rollingFile name:'file',
				file:"/var/log/${appName}.log",
				maxFileSize:"1MB",
				layout: pattern(conversionPattern: '%d %-5p %c{2} %m%n')
			}
			root {
				error 'file'
				additivity = true
			}
		}
		// TODO: grails.serverURL = "http://www.changeme.com""
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
				file:"/var/log/${appName}.log",
				maxFileSize:"1MB",
				layout: pattern(conversionPattern: '%d %-5p %c{2} %m%n')
			}
			root {
				error 'file'
				additivity = true
			}
		}
		// TODO: grails.serverURL = "http://www.changeme.com"
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
				info 'stdout'
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
				info 'stdout'
				additivity = true
			}
		}
		// TODO: grails.serverURL = "http://www.changeme.com"
	}
}

// jquery
grails.views.javascript.library="jquery"

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'com.dtosolutions.SecUser'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.dtosolutions.SecUserSecRole'
grails.plugins.springsecurity.authority.className = 'com.dtosolutions.SecRole'

grails.plugins.springsecurity.secureChannel.definition = [
	'/common/**':				'ANY_CHANNEL',
	'/layouts/**':				'ANY_CHANNEL',
	'/login/**':				'REQUIRES_SECURE_CHANNEL',
	'/register/**':				'REQUIRES_SECURE_CHANNEL',
	'/test/**':					'ANY_CHANNEL',
	'/images/**':				'ANY_CHANNEL',
	'/css/**':					'ANY_CHANNEL',
	'/xml/**':					'ANY_CHANNEL',
	'/logout/**':				'ANY_CHANNEL',
	'/user/**':					'REQUIRES_SECURE_CHANNEL',
	'/role/**':					'REQUIRES_SECURE_CHANNEL',
	'/registrationCode/**':		'REQUIRES_SECURE_CHANNEL',
	'/securityInfo/**':			'REQUIRES_SECURE_CHANNEL',
	'/admin/**':				'ANY_CHANNEL',
 ]
