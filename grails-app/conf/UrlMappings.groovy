class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/node/api/$id"(controller: "nodeApi", parseRequest: true) {
			action = [PUT: "update", DELETE: "delete"]
		}
		
        "/"(view: "/index")
        "500"(view:'/error')
    }
}
