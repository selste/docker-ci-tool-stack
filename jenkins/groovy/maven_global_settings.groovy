import jenkins.model.*
import org.jenkinsci.lib.configprovider.model.*
import org.jenkinsci.plugins.configfiles.GlobalConfigFiles
import org.jenkinsci.plugins.configfiles.maven.*

def configXml = """\
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
	<localRepository>/tmp/maven-cache</localRepository>
  <servers>
    <server>
      <id>maven-snapshot-internal</id>
      <username>admin</username>
      <password>admin123</password>
    </server>
    <server>
      <id>maven-release-internal</id>
      <username>admin</username>
      <password>admin123</password>
    </server>
  </servers>
  <mirrors>
    <mirror>
      <id>nexus</id>
      <mirrorOf>external:*</mirrorOf>
      <name>Maven Repository Proxy</name>
      <url>http://nexus:8081/repository/maven-public/</url>
    </mirror>
  </mirrors>
  <profiles>
    <profile>
      <id>nexus</id>
      <repositories>
        <repository>
          <id>central</id>
          <url>http://central</url>
          <releases><enabled>true</enabled></releases>
          <snapshots><enabled>true</enabled></snapshots>
        </repository>
				<repository>
					<id>archetypes</id>
					<url>http://nexus/content/groups/public/</url>
				</repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <id>central</id>
          <url>http://central</url>
          <releases><enabled>true</enabled></releases>
          <snapshots><enabled>true</enabled></snapshots>
        </pluginRepository>
      </pluginRepositories>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>nexus</activeProfile>
  </activeProfiles>
</settings>
"""

def jenkins = Jenkins.getInstance()

Collection<Config> collection = GlobalConfigFiles.get().getConfigs(GlobalMavenSettingsConfig.GlobalMavenSettingsConfigProvider.class)

if (collection.isEmpty()) {
  GlobalMavenSettingsConfig mavenConfig = new GlobalMavenSettingsConfig('mavenGlobalSettings', 'maven-global-settings.xml', 'Maven Global Settings', configXml)
  GlobalConfigFiles configFiles = jenkins.getExtensionList(GlobalConfigFiles.class).get(GlobalConfigFiles.class)
  configFiles.save(mavenConfig)

  println 'File created'
}
