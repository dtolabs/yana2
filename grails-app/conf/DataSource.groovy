

import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration

dataSource {
	configClass = GrailsAnnotationConfiguration.class
	pooled = true
	dbCreate = "update"
	url = "jdbc:mysql://localhost/yana2"
	driverClassName = "com.mysql.jdbc.Driver"
	username = "root"
	password = "Ch335eB0y"
	dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
	maxActive = 50
	maxIdle = 25
	minIdle = 5
	initialSize = 5
	minEvictableIdleTimeMillis = 60000
	timeBetweenEvictionRunsMillis = 60000
	maxWait = 10000
	validationQuery = "select 1"
	// logSql = true
}
hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = true
	cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
	development {
		dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:mysql://localhost/yana2"
		}
	}
	test {
		dataSource {
			dbCreate = "update"
			url = "jdbc:mysql://localhost/yana2"
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:mysql://localhost/yana2"
		}
	}
}


