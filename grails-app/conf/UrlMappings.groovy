class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/api/node/$id"(controller: "node", parseRequest: true) {
			action = [PUT: "update", DELETE: "delete"]
		}
		
        "/"(view: "/index")
        "500"(view:'/error')
    }
}
