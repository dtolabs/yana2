class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/nodeapi/$id"(controller: "nodeApi", parseRequest: true) {
			action = [PUT: "update", DELETE: "delete"]
		}
		
        "/"(view: "/index")
        "500"(view:'/error')
    }
}
