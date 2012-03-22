class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/login/$action?"(controller: "login")
        "/logout/$action?"(controller: "logout")

        // Nodes
        "/api/nodes"(controller: "nodeRest",  parseRequest:true) {
            action = [ GET: "list" , POST: "add" ]
        }

        "/api/nodes/$id"(controller: "nodeRest",  parseRequest:true) {
            action = [ GET: "show" , POST: "save" , DELETE: "delete" ]
        }

        "/api/nodes/$nodeId/tags"(controller: "nodeRest",  parseRequest:true) {
            action = [ GET: "listTags" ]
        }

        // Apply and list tags on sets of Nodes
        "/api/nodes/tags/$name"(controller: "tagRest", parseRequest:true) {
            action = [ GET: "show", POST: "save", DELETE: "remove" ]
        }
		
        // Attributes
        "/api/attributes"(controller: "attributesRest") {
            action = [ GET: "list" ]
        }

        "/api/attributes/$id"(controller: "attributesRest") {
            action = [ GET: "show" ]
        }


        "/"(view:"/index")
        "500"(view:'/error')
    }
}
