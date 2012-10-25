Gsp assets plugin
=================

[![Build Status](https://secure.travis-ci.org/enr/grails-gspassets.png?branch=master)](http://travis-ci.org/enr/grails-gspassets)

Allows the usage of Grails GSP views to serve javascript, css and other types of file, so you can use GSP tags and Groovy in your resources.


Usage
-----

Map gspassets controller in your UrlMappings file:

    "/assets/$assetId**"(controller:'gspassets', action:'serve')

You can use the url you want (in this case "/assets"), but you have to register the assetId part.

Create your views in grails-app/views/gspassets, naming them with a 2 dots extension (eg .css.gsp).

A sample view, for a dynamic css file named dynamic.css.gsp:

    span.my-class {
        display:inline-block;
        background:url('${fam.icon(name: 'delete')}') center left no-repeat;
    }

Now you can include the file:

    <link rel="stylesheet" href="${createLink( uri:'/assets/dynamic.css' )}"/>

The response contentType is guessed from extension, otherwise will be set based on the request.format or will be text plain for unknown types.

That's all.