package com.github.enr.gspassets

import grails.test.mixin.TestFor
import spock.lang.Specification

import org.codehaus.groovy.grails.web.mime.MimeUtility

@TestFor(GspassetsController)
class GspassetsControllerSpec extends Specification {

    def mimeTypes = [   html:'text/html', xml:'text/xml', text:'text/plain',
                        js:'text/javascript', css:'text/css', all:'*/*',
                        json:'application/json']

    def setup() {
        controller.grailsApplication.config.grails.mime.types = mimeTypes
    }
    
    def "should solve view from extension"() {
        given:
            controller.params.assetId = "test_01.${extension}"
        and:
            controller.serve()
        expect:
            view == "/gspassets/test_01.${extension}"
        where:
            extension         | response_content_type
            'html'            | 'text/html'
            'js'              | 'text/javascript'
            'css'             | 'text/css'
            'json'            | 'application/json'
    }
    
    def "should serve the correct content type for known request formats"() {
        given:
            request.format = request_format
            controller.params.assetId = 'test_02'
        and:
            controller.serve()
        expect:
            response.contentType == "${response_content_type};charset=utf-8"
            view == '/gspassets/test_02'
        where:
            request_format    | response_content_type
            'html'            | 'text/html'
            'js'              | 'text/javascript'
            'css'             | 'text/css'
            'json'            | 'application/json'
    }
    
    def "should serve the default content type for unknown request formats"() {
        given:
            request.format = request_format
            controller.params.assetId = 'test_03'
        and:
            controller.serve()
        expect:
            response.contentType == "${response_content_type};charset=utf-8"
            view == '/gspassets/test_03'
        where:
            request_format    | response_content_type
            'unknown'         | 'text/plain'
    }
    
    def "should serve html content type for empty request format"() {
        given:
            request.format = request_format
            controller.params.assetId = 'test_04'
        and:
            controller.serve()
        expect:
            response.contentType == "${response_content_type};charset=utf-8"
            view == '/gspassets/test_04'
        where:
            request_format    | response_content_type
            null              | 'text/html'
            ''                | 'text/html'
    }
}

