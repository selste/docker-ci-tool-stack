import jenkins.model.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.impl.*

def list = CredentialsProvider.lookupCredentials(StandardUsernameCredentials.class, Jenkins.instance, null, null)

if (null == list.findResult { it.id.equalsIgnoreCase('nexus') ? it:null }) {
  println 'Result list is empty'

  SystemCredentialsProvider.getInstance().getCredentials()
    .add(new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "nexus", "Nexus Admin User", "admin", "admin123"))

  println 'Credentials successfully created'
} else {
  println "Credentials for id 'nexus' already exist"
}

if (null == list.findResult { it.id.equalsIgnoreCase('dockerhub') ? it:null }) {
  println 'Result list is empty'

  SystemCredentialsProvider.getInstance().getCredentials()
    .add(new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "dockerhub", "Docker Hub access", "user", "password"))

  println 'Credentials successfully created'
} else {
  println "Credentials for id 'dockerhub' already exist"
}
