
environments {
	development {
		dataSource {
			dbCreate = "create-drop"
			url = "jdbc:h2:file:db/devDb"
		}
	}
	test {
		dataSource {
			dbCreate = "create-drop"
			url = "jdbc:h2:file:db/testDb"
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:h2:file:db/prodDb"
		}
	}
}