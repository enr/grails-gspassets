package com.github.enr.gspassets

import grails.test.mixin.TestFor
import spock.lang.*

import org.codehaus.groovy.grails.web.mime.MimeUtility

@Unroll
@TestFor(GspassetsController)
class GspassetsControllerSpec extends Specification {

    def mimeTypes = [   html:'text/html', xml:'text/xml', text:'text/plain',
                        js:'text/javascript', css:'text/css', all:'*/*',
                        json:'application/json']

    def setup() {
        controller.grailsApplication.config.grails.mime.types = mimeTypes
    }

    def "should set the correct content type (#response_content_type) for extension #extension"() {
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
    
    def "should set the correct content type (#response_content_type) for known request format #request_format"() {
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

    def "#asset_id : should serve #response_content_type"() {
        given:
            request.format = request_format
            request.forwardURI = "${asset_id}${ext}"
            controller.params.assetId = asset_id
            controller.defaultResponse = default_response
            controller.skipRequestFormatAll = skip_request_all
        and:
            controller.serve()
        expect:
            response.contentType == "${response_content_type};charset=utf-8"
            view == "/gspassets/${asset_id}"
        where:
            request_format | skip_request_all | default_response   | response_content_type | asset_id | ext
            'all'          | true             | 'html'             | 'text/html'           | '01'     | ''
            'all'          | false            | 'html'             | '*/*'                 | '02'     | ''
            'all'          | true             | null               | 'text/plain'          | '03'     | ''
            'all'          | false            | null               | '*/*'                 | '04'     | ''
    }
}

