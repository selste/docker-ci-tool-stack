pipelineJob('jee7-demo-app') {
  description('Java EE 7 Pipeline Demo')

  logRotator {
    numToKeep(10)
    daysToKeep(5)
  }

  definition {
    cpsScm {
      scm {
        git {
          remote {
            url('http://gitlab/root/jee-7-demo-app.git')
          }
          branch('master')
        }
      }
      scriptPath('Jenkinsfile')
    }
  }

  configure {
    it /definition << lightweight(true)
  }
}
