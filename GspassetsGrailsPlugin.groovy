class GspassetsGrailsPlugin {
    def version = "0.3"
    def grailsVersion = "2.1 > *"
    def dependsOn = [:]
    def pluginExcludes = [ ]
    def title = "Gsp Assets Plugin"
    def author = "Enrico Comiti"
    def authorEmail = "enrico@comiti.name"
    def description = '''\
Allows the usage of Grails GSP views to serve javascript, css and other types of file.
'''
    def documentation = "https://github.com/enr/grails-gspassets"
    def license = 'APACHE'
    def scm = [ url: 'https://github.com/enr/grails-gspassets' ]
    def issueManagement = [system: 'GitHub', url: 'https://github.com/enr/grails-gspassets/issues']
}
