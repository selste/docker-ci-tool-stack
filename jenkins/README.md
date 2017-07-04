## Jenkins Docker Container

Usage:
```
$ docker build -t jenkins .
$ docker run -d -p 8080:8080 --name jenkins jenkins
```

To use the Docker daemon of the host - e.g. to build Docker images as a step while executing a Pipeline job - instead use:
```
$ docker build --build-arg DOCKER_GROUP_ID=`getent group docker | cut -d: -f3` -t jenkins .
$ docker run -d -p 8080:8080 -v /var/run/docker.sock:/var/run/docker.sock --name jenkins jenkins
```

Once Jenkins is up and running go to http://localhost:8080

## Update Plugins

Install and update all plugins via the Jenkins Plugin Manager.
* http://<jenkins-url:port>/pluginManager/

After that use the Script Console to output all plugins including the version in the correct format for the **plugin.txt**.
* http://<jenkins-url:port>/script

```shell
Jenkins.instance.pluginManager.plugins.each {
  plugin ->
    println ("${plugin.getDisplayName()} (${plugin.getShortName()}): ${plugin.getVersion()}")
}
```

More example scripts can be found in the **groovy** folder.

### Links

- Job DSL API https://jenkinsci.github.io/job-dsl-plugin/
