#!/usr/bin/env bash
mvn clean
mvn package -Dmaven.test.skip=true
cd target || exit
jarname="$(ls locker-*.jar)"
echo "$jarname is syncing..."
rsync -e ssh --progress "$jarname" iot:/home/miot/opt/smart-locker
rsync -e ssh --progress ../src/main/resources/application-production.yaml iot:/home/miot/opt/smart-locker/application.yaml
# shellcheck disable=SC2029
ssh iot "ln -sf /home/miot/opt/smart-locker/$jarname /home/miot/opt/smart-locker/smart-locker.jar"