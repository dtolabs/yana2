
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration
dataSource {
	configClass = GrailsAnnotationConfiguration.class
	   pooled = true
	   driverClassName = "com.mysql.jdbc.Driver"
	   dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
	   //    properties {
	   //        minEvictableIdleTimeMillis = 1000 * 60 * 30
	   //        timeBetweenEvictionRunsMillis = 1000 * 60 * 30
	   //        numTestsPerEvictionRun = 3
	   //        testOnBorrow = false
	   //        testWhileIdle = true
	   //        testOnReturn =  false
	   //        validationQuery = 'SELECT 1 FROM DUAL'
	   //    }
	   // logSql = true
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate="update"
            url = "jdbc:mysql://localhost/yana3"
            username = 'yana'
            password='1234'
        }
    }
    test {
        dataSource {
            url = "jdbc:mysql://localhost/yana3"
            username = 'yana'
            password='1234'
        }
    }
	staging {
		dataSource {
			url = "jdbc:mysql://localhost/yana3"
			username = 'yana'
			password='1234'
		}
	}
	production {
		dataSource {
			pooled = false
			dbCreate = "update"
			jndiName = "jdbc/mydatasource"
		}
	}
}


