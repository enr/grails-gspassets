package com.github.enr.gspassets

import org.apache.commons.io.FilenameUtils
// import org.codehaus.groovy.grails.web.mime.MimeType
// import org.codehaus.groovy.grails.web.mime.MimeUtility
import org.springframework.beans.factory.InitializingBean

class GspassetsController implements InitializingBean {
    
    private static final String MODEL_PARAMS_PREFIX = '_g_'
    private static final String DEFAULT_RESPONSE = 'text'
    private static final String DEFAULT_MIME_TYPE = 'text/plain'
    private static final Map<String, String> MIME_TYPES = [ html: 'text/html',
                                                            xml: 'text/xml',
                                                            text: 'text/plain',
                                                            js: 'text/javascript',
                                                            rss: 'application/rss+xml',
                                                            atom: 'application/atom+xml',
                                                            css: 'text/css',
                                                            csv: 'text/csv',
                                                            all: '*/*',   // */
                                                            json: 'application/json' ]

    //MimeUtility grailsMimeUtility
    def grailsApplication

    String defaultResponse

    boolean skipRequestFormatAll

    public void afterPropertiesSet() {
        defaultResponse = grailsApplication.config?.grails?.plugins?.gspassets?.defaultResponse ?: DEFAULT_RESPONSE
        skipRequestFormatAll = grailsApplication.config?.grails?.plugins?.gspassets?.skipRequestFormatAll ?: false
    }

    def serve() {
        def assetId = params.assetId
        log.debug "assetId=${assetId} defaultResponse=${defaultResponse} skipRequestFormatAll=${skipRequestFormatAll}"
        if (!assetId) {
            response.status = 404
            return
        }
        def requestFormat = (skipRequestFormatAll && request.format == 'all') ? '' : request.format
        def contentType = resolveContentType(request.forwardURI, requestFormat, defaultResponse)
        log.debug "contentType=${contentType}"
        //['assetId'].each { params.remove it }
        //params.keySet().asList().each { if (!it.startsWith(MODEL_PARAMS_PREFIX)) params.remove(it) }
        def model = [:]
        params.each { k, v ->
            if (k.startsWith(MODEL_PARAMS_PREFIX)) {
                model[(k - MODEL_PARAMS_PREFIX)] = v
            }
        }
        render view:assetId, contentType:contentType, model:model
    }

    private String resolveContentType(uri, requestFormat, defaultResponse) {
        def ct = MIME_TYPES.get(FilenameUtils.getExtension(uri))
        if (ct) {
            return ct
        }
        ct = MIME_TYPES.get(requestFormat)
        if (ct) {
            return ct
        }
        return (MIME_TYPES.get(defaultResponse) ?: DEFAULT_MIME_TYPE)
    }

    /*
    private String resolveContentType(uri, requestFormat, defaultResponse) {
        log.error "uri=${uri} requestFormat=${requestFormat}"
        def ct = contentTypeForUri(uri)
        log.error "1.ct=${ct}"
        if (ct != null) {
            log.error "2.ct=${ct}"
            return ct
        }
        //                                       grails.mime.types
        def mimeTypes = grailsApplication.config.grails.mime.types
        log.error "                          mimeTypes=${mimeTypes}"
        log.error "grailsMimeUtility.getKnownMimeTypes=${grailsMimeUtility.getKnownMimeTypes()}"
        if (requestFormat != null) {
            ct = mimeTypes[requestFormat]
            log.error "3.ct=${ct}"
            if (ct != null) {
                log.error "4.ct=${ct}"
                return toContentType(ct)
            }
        }
        return toContentType(mimeTypes[defaultResponse]) ?: DEFAULT_MIME_TYPE
    }
    
    private String contentTypeForUri(uri) {
        MimeType mimeType = grailsMimeUtility.getMimeTypeForExtension(FilenameUtils.getExtension(uri));
        log.error "mimeType=${mimeType}"
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
    */

}
