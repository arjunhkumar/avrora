#!/bin/bash
rm -r artifacts/
rm pom.xml
cp pom-21.xml pom.xml
mvn clean install -DskipTests=true

mkdir -p artifacts/jar

cp target/avrora-0.0.1-SNAPSHOT.jar artifacts/jar/avrora-cvs-20091224.jar

cp sanity/dacapo-9.12-MR1-bach.jar artifacts/dacapo-modified.jar

cd artifacts/

jar -uf dacapo-modified.jar jar/avrora-cvs-20091224.jar
