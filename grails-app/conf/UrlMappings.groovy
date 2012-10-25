class UrlMappings {
	static mappings = {
		"/$controller/$action?/$id?"
		"/assets/$assetId**"(controller:'gspassets', action:'index')
	}
}
