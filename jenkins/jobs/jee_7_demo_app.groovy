pipelineJob('jee7-demo-app') {
  description('Java EE 7 Pipeline Demo')

  logRotator {
    numToKeep(10)
    daysToKeep(5)
  }

  definition {
    cpsScm {
      scm {
        git('http://gitlab/root/jee7-demo-app.git', 'master')
      }
      scriptPath('Jenkinsfile')
    }
  }

  configure {
    it /definition << lightweight(true)
  }
}
