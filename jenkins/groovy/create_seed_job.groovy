import jenkins.model.*

def jobName = 'SeedJob'

def configXml = """\
<flow-definition plugin="workflow-job@2.11.1">
  <description>Build Pipeline Jobs</description>
  <keepDependencies>false</keepDependencies>
  <properties>
    <com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty plugin="gitlab-plugin@1.4.5">
      <gitLabConnection/>
    </com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty>
    <org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>
      <triggers/>
    </org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>
  </properties>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition" plugin="workflow-cps@2.36">
    <script>node { jobDsl targets: '*.groovy' }</script>
    <sandbox>true</sandbox>
  </definition>
  <triggers/>
  <customWorkspace>/usr/share/jenkins/ref/jobs/SeedJob/workspace</customWorkspace>
  <disabled>false</disabled>
</flow-definition>
""".stripIndent()

if (! Jenkins.instance.getItem(jobName)) {
    def xmlStream = new ByteArrayInputStream(configXml.getBytes('UTF-8'))

    try {
        def seedJob = Jenkins.instance.createProjectFromXML(jobName, xmlStream)
        seedJob.scheduleBuild(0, null)
    } catch (ex) {
        println "ERROR: ${ex}"
        println configXml.stripIndent()
    }
}
