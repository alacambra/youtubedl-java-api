#!/usr/bin/env bash
N=1.0
VERSION=1.0-SNAPSHOT
TS=$(curl https://jfrog.lacambra.tech/artifactory/list/lacambra.tech/tech/lacambra/downloader/downloader-server/${VERSION}/maven-metadata.xml | xmlstarlet sel -t -v '/metadata/versioning/snapshotVersions/snapshotVersion[1]/value')
URL="https://jfrog.lacambra.tech/artifactory/list/lacambra.tech/tech/lacambra/downloader/downloader-server/${VERSION}/downloader-server-${TS}.war"
echo ${URL}
curl  ${URL} -o docker/downloader.war