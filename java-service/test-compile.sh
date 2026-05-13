#!/bin/bash
cd /Users/deewakarkumar/Devops/sem6/java-service
cp pom.xml pom.xml.bak
# Remove the maven-compiler-plugin section using sed
sed -i '' '/<artifactId>maven-compiler-plugin<\/artifactId>/,/<\/plugin>/d' pom.xml
JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.19/libexec/openjdk.jdk/Contents/Home PATH="$JAVA_HOME/bin:$PATH" mvn clean compile
exit_code=$?
mv pom.xml.bak pom.xml
exit $exit_code
