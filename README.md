Gsp assets plugin
=================

[![Build Status](https://secure.travis-ci.org/enr/grails-gspassets.png?branch=master)](http://travis-ci.org/enr/grails-gspassets)

Allows the usage of Grails GSP views to serve javascript, css and other types of file, so you can use GSP tags and Groovy in your resources.


Usage
-----

Map gspassets controller in your UrlMappings file:

```groovy
    "/assets/$assetId**"(controller:'gspassets', action:'serve')
    // you can add multiple mappings
    "/staticpages/$assetId**"(controller:'gspassets', action:'serve')
```

You can use the base path you want (in this case `/assets` and `/staticpages`), but you have to register the `assetId` part.

Create your views in `grails-app/views/gspassets`.

You can use a 2 dots extension (eg `.css.gsp`) to have file names ending in the canonical manner.

A sample view, for a dynamic css file named dynamic.css.gsp using the "famfamfam" tag library:

```css
    span.my-class {
        display:inline-block;
        background:url('${fam.icon(name: 'delete')}') center left no-repeat;
    }
```

Now you can include the file:

```html
    <link rel="stylesheet" href="${request.contextPath}/assets/mydynamicstyle.css"/>
```

The response contentType resolved in this way:

- if the requested asset has a known extension the content type will be the default for that extension.
  Content types are taken from the Grails configuration `grails.mime.types`.
  So, the request for `/assets/mystyle.css` will be resolved as the view `gspassets/mystyle.css.gsp` with content type `text/css`
  
- if the asset has no extension (or an unknown one) the content type will be the type specified in `request.format` (if this is other than "all")

- if `request.format` is "all" and the configuration data `skipRequestFormatAll` is set to false, content type will be `"*/*"`

- if `grails.plugins.gspassets.defaultResponse` is set, content type will be the default for that key in the Grails configuration `grails.mime.types`.

- finally, if none of the above conditions are verified, content type will default to `"text/plain"`


Configuration
-------------

Available configuration keys:

- `grails.plugins.gspassets.defaultResponse` (string): one of: all, atom, css, csv, html, js, json, rss, text, xml

- `grails.plugins.gspassets.skipRequestFormatAll` (boolean): if true, requests with format "all" (if no extension is given to the assetId) will be served as "defaultResponse"
