#!/bin/bash

# JSON payload
#   nuget.json to remove the predefined nuget repositories (proxy, hosted and group)
#   docker.json creates a hosted docker repository and a docker group
scripts="nuget.json docker.json"

# Script aliases as used by Nexus
aliases="nuget docker"

# Repository Proxy URL
url='http://localhost:18081/service/siesta/rest/v1/script'

# Uploading
printf "Processing scripts $scripts ...\n\n"

for script in $scripts
do
  printf "Creating Integration API Script from $script"
  status=$(curl -s -o /dev/null -w '%{http_code}' -u admin:admin123 --header 'Content-Type: application/json' $url -d @$script)

  if [ $status -eq 204 ]; then
    echo " ... done"
  else
    echo " ... got $status :-(\n"
    echo "Bailing out ...\n"
    exit 1
  fi
done

# Executing
printf "Running aliases $aliases ...\n\n"

for alias in $aliases
do
  printf "Running alias $alias"
  status=$(curl -s -o /dev/null -w '%{http_code}' -X POST -u admin:admin123 --header 'Content-Type: text/plain' "$url/$alias/run")

  if [ $status -eq 200 ]; then
    echo " ... done"
  else
    echo " ... got $status :-(\n"
    echo "Bailing out ...\n"
    exit 1
  fi
done
