FROM jenkins:2.60.1

ARG DOCKER_GROUP_ID

USER root
RUN apt-get update \
      && apt-get install -y sudo curl \
      && rm -rf /var/lib/apt/lists/*
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers

# getting the docker-cli
# --- Attention: docker.sock needs to be mounted as volume in docker-compose.yml
# see: https://issues.jenkins-ci.org/browse/JENKINS-35025
# see: https://get.docker.com/builds/
# see: https://wiki.jenkins-ci.org/display/JENKINS/CloudBees+Docker+Custom+Build+Environment+Plugin#CloudBeesDockerCustomBuildEnvironmentPlugin-DockerinDocker
RUN curl -sSL -O https://get.docker.com/builds/Linux/x86_64/docker-latest.tgz && tar -xvzf docker-latest.tgz
RUN mv docker/* /usr/local/bin/

# create docker group and add jenkins to the group
# IMPORTANT: the ID of the group has to be identical to the one used on the host system, otherwise Jenkins will not be able to access the docker daemon
RUN groupadd -g $DOCKER_GROUP_ID docker \
    && gpasswd -a jenkins docker

# Apache Maven
RUN curl -sSL -O http://www-eu.apache.org/dist/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.tar.gz && tar -xvzf apache-maven-3.5.0-bin.tar.gz
RUN mv apache-maven-3.5.0 /opt

USER jenkins

# installing specific list of plugins. see: https://github.com/jenkinsci/docker#preinstalling-plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/ref/plugins.txt

# Groovy script to create the 'SeedJob', will be called automagically - once - on startup
COPY groovy/*.groovy /usr/share/jenkins/ref/init.groovy.d/

# SeedJob is set up with a custom workspace, which is populated with (a) Job DSL script(s) to be executed
RUN mkdir -p /usr/share/jenkins/ref/jobs/SeedJob/workspace
COPY jobs/*.groovy /usr/share/jenkins/ref/jobs/SeedJob/workspace

############################################
# Configure Jenkins
############################################
# Jenkins settings
COPY config/config.xml /usr/share/jenkins/ref/config.xml

# Jenkins Settings, i.e. Maven, Groovy, ...
COPY config/hudson.tasks.Maven.xml /usr/share/jenkins/ref/hudson.tasks.Maven.xml
COPY config/hudson.plugins.groovy.Groovy.xml /usr/share/jenkins/ref/hudson.plugins.groovy.Groovy.xml
# COPY config/com.dabsquared.gitlabjenkins.connection.GitLabConnectionConfig.xml /usr/share/jenkins/ref/com.dabsquared.gitlabjenkins.connection.GitLabConnectionConfig.xml
COPY config/org.jenkinsci.plugins.pipeline.modeldefinition.config.GlobalConfig.xml /usr/share/jenkins/ref/org.jenkinsci.plugins.pipeline.modeldefinition.config.GlobalConfig.xml

# SSH Keys & Credentials
COPY config/ssh-keys/cd-demo /usr/share/jenkins/ref/.ssh/id_rsa
COPY config/ssh-keys/cd-demo.pub /usr/share/jenkins/ref/.ssh/id_rsa.pub

# tell Jenkins that no banner prompt for pipeline plugins is needed
# see: https://github.com/jenkinsci/docker#preinstalling-plugins
RUN echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state
