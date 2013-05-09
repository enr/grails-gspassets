package com.github.enr.gspassets

import org.apache.commons.io.FilenameUtils
import org.codehaus.groovy.grails.web.mime.MimeType
import org.codehaus.groovy.grails.web.mime.MimeUtility
import org.springframework.beans.factory.InitializingBean

class GspassetsController implements InitializingBean {
    
    private static final String DEFAULT_RESPONSE = 'text'
    private static final String DEFAULT_MIME_TYPE = 'text/plain'
    
    MimeUtility grailsMimeUtility
    def grailsApplication

    String defaultResponse
    boolean skipRequestFormatAll

    public void afterPropertiesSet() {
        defaultResponse = grailsApplication.config?.grails?.plugins?.gspassets?.defaultResponse ?: DEFAULT_RESPONSE
        skipRequestFormatAll = grailsApplication.config?.grails?.plugins?.gspassets?.skipRequestFormatAll ?: false
    }

    def serve() {
        println ""
        def assetId = params.assetId
        if (!assetId) {
            response.status = 404
            return
        }
        def requestFormat = (skipRequestFormatAll && request.format == 'all') ? '' : request.format
        def contentType = resolveContentType(request.forwardURI, requestFormat, defaultResponse)
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
        return toContentType(mimeTypes[defaultResponse]) ?: DEFAULT_MIME_TYPE
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
