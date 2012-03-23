class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
		
        // rundeck api
        "/rundeckapi/$action"{
			constraints {
				controller = "rundeck"
				parseRequest = true
				switch(action){
					case 'getNodes':
						action = [ POST: "getNodesAsXML" ]
						break;
				}
			}
        }

        "/"(view: "/index")
        "500"(view:'/error')
    }
}
