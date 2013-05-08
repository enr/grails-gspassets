class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        "/assets/${assetId}**" {
            controller = 'gspassets'
            action = 'serve'
            plugin = 'gspassets'
        }
        "/page/${assetId}**" {
            controller = 'gspassets'
            action = 'serve'
            plugin = 'gspassets'
        }

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
