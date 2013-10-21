
grails.project.work.dir = 'target'

grails.project.target.level = 1.6

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {}
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    plugins {
        build(":release:3.0.1", ":rest-client-builder:2.0.0") {
            export = false
        }
    }
}
