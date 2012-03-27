
dataSource {
	pooled = false
	driverClassName = "org.h2.Driver"
}
environments {
	development {
		dataSource {
			dbCreate = "create-drop"
			url = "jdbc:h2:devDb;MVCC=TRUE"
			username = "sa"
			password = ""
		}
	}
	test {
		dataSource {
			dbCreate = "update"
			url = "jdbc:h2:testDb;MVCC=TRUE"
			username = "sa"
			password = ""
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:h2:prodDb;MVCC=TRUE"
			username = "sa"
			password = ""
		}
	}
}