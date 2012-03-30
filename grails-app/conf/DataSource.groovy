
dataSource {
	driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
	pooled = false
}
environments {
	development {
		dataSource {
			dbCreate = "update"
			url = "jdbc:h2:file:./db/devDb"
		}
	}
	test {
		dataSource {
			dbCreate = "update"
			url = "jdbc:h2:file:./db/testDb"
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:h2:file:./db/prodDb"
		}
	}
}