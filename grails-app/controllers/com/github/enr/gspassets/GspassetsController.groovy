package com.github.enr.gspassets

import org.apache.commons.io.FilenameUtils
import org.codehaus.groovy.grails.web.mime.MimeType
import org.codehaus.groovy.grails.web.mime.MimeUtility

class GspassetsController {

    MimeUtility grailsMimeUtility
    
    private static final String DEFAULT_MIME_TYPE = 'text/plain'
    
    def grailsApplication

    def serve() {
        def assetId = params.assetId
        if (!assetId) {
            response.status = 404
            return
        }
        def uri = request.forwardURI
        //def mimeFileExtensions = grailsApplication.config.grails.mime.file.extensions
        def mimeTypes = grailsApplication.config.grails.mime.types
        def contentType = contentTypeForUri(uri) ?: toContentType(mimeTypes[request.format])
        contentType = contentType ?: DEFAULT_MIME_TYPE
        render view:assetId, contentType:contentType // NOT YET IMPLEMENTED, model:params
    }
    
    private String contentTypeForUri(uri) {
        MimeType mimeType = grailsMimeUtility.getMimeTypeForExtension(FilenameUtils.getExtension(uri));
        if (mimeType) {
            return mimeType.name
        }
        return null
    }
    
    private String toContentType(mime) {
        if (mime instanceof List && mime.size() > 0) {
            return mime[0]
        }
        return mime ? "${mime}" : ''
    }

}
