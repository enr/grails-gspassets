package com.github.enr.gspassets

import grails.plugin.spock.IntegrationSpec

import spock.lang.*

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.plugins.web.mimes.MimeTypesFactoryBean
import org.codehaus.groovy.grails.web.mime.DefaultMimeUtility

class GspassetsControllerIntegrationSpec extends IntegrationSpec {
  
    GspassetsController controller

    /*
     * see https://github.com/grails/grails-core:
     * grails-plugin-mimetypes/src/test/groovy/org/codehaus/groovy/grails/web/mime/MimeUtilitySpec.groovy
     */
    def setup() {
        controller = new GspassetsController()
        def ga = new DefaultGrailsApplication()
        ga.config.grails.mime.types = [ html : ['text/html','application/xhtml+xml'],
                                        xml  : ['text/xml', 'application/xml'],
                                        text : 'text/plain',
                                        js   : 'text/javascript',
                                        rss  : 'application/rss+xml',
                                        atom : 'application/atom+xml',
                                        css  : 'text/css',
                                        csv  : 'text/csv',
                                        all  : '*/*',
                                        json : ['application/json','text/json']
                                      ]
        final factory = new MimeTypesFactoryBean(grailsApplication: ga)
        factory.afterPropertiesSet()
        def mimeTypes = factory.getObject()
        def mimeUtility = new DefaultMimeUtility(mimeTypes)
        controller.grailsApplication = ga
        controller.grailsMimeUtility = mimeUtility
    }
    
    def "should solve view and mime type from extension"() {
        given:
            controller.params.assetId = "test_01.${extension}"
            controller.request.forwardURI = "test_01.${extension}"
        and:
            controller.serve()
        expect:
            controller.response.contentType == "${response_content_type};charset=utf-8"
            controller.modelAndView.viewName == "/gspassets/test_01.${extension}"
        where:
            extension         | response_content_type
            'html'            | 'text/html'
            'js'              | 'text/javascript'
            'css'             | 'text/css'
            'json'            | 'application/json'
    }
    
    def "should solve view and mime type from request format"() {
        given:
            controller.request.format = "${request_format}"
            controller.params.assetId = 'test_02'
            controller.request.forwardURI = 'test_02'
        and:
            controller.serve()
        expect:
            controller.response.contentType == "${response_content_type};charset=utf-8"
            controller.modelAndView.viewName == "/gspassets/test_02"
        where:
            request_format    | response_content_type
            'html'            | 'text/html'
            'js'              | 'text/javascript'
            'css'             | 'text/css'
            'json'            | 'application/json'
    }
      
    def "should serve assets in subdirectories"() {
        given:
            controller.params.assetId = 'sub/test_03.css'
            controller.request.forwardURI = 'sub/test_03.css'
        when:
            controller.serve()
        then:
            controller.response.contentType == "text/css;charset=utf-8"
            controller.modelAndView.viewName == '/gspassets/sub/test_03.css'
    }
    
    def "should throw 404 if assetId is missing"() {
        given:
            controller.params.assetId = ''
        when:
            controller.serve()
        then:
            controller.response.status == 404
    }
}