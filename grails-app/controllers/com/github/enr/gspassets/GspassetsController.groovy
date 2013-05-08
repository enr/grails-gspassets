package com.github.enr.gspassets

import org.apache.commons.io.FilenameUtils
import org.codehaus.groovy.grails.web.mime.MimeType
import org.codehaus.groovy.grails.web.mime.MimeUtility

class GspassetsController {
    
    private static final String DEFAULT_RESPONSE = 'text'
    //private static final String DEFAULT_MIME_TYPE = 'text/plain'
    
    MimeUtility grailsMimeUtility
    def grailsApplication

    def serve() {
        def assetId = params.assetId
        if (!assetId) {
            response.status = 404
            return
        }
        def defaultResponse = grailsApplication.config?.grails?.plugins?.gspassets?.defaultResponse ?: DEFAULT_RESPONSE
        def uri = request.forwardURI
        def contentType = resolveContentType(uri, request.format, defaultResponse)
        render view:assetId, contentType:contentType // NOT YET IMPLEMENTED, model:params
    }

    private String resolveContentType(uri, requestFormat, defaultResponse) {
        def ct = contentTypeForUri(uri)
        if (ct != null) {
            return ct
        }
        def mimeTypes = grailsApplication.config.grails.mime.types
        if (requestFormat != null) {
            ct = mimeTypes[requestFormat]
            if (ct != null) {
                return toContentType(ct)
            }
        }
        return toContentType(mimeTypes[defaultResponse])
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
