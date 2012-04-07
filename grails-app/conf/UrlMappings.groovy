class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/api/node/$id"(controller: "node", action:"api")
		
        "/"(view: "/index")
        "500"(view:'/error')
    }
}
