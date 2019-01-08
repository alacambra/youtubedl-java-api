#!/usr/bin/env bash
N=1.0


if [ -z ${VERSION} ];
then
VERSION=$1
fi
echo "Downloading version ${VERSION}";
TS=$(curl https://jfrog.lacambra.tech/artifactory/list/lacambra.tech/tech/lacambra/downloader/downloader-server/${VERSION}/maven-metadata.xml | xmlstarlet sel -t -v '/metadata/versioning/snapshotVersions/snapshotVersion[1]/value')
URL="https://jfrog.lacambra.tech/artifactory/list/lacambra.tech/tech/lacambra/downloader/downloader-server/${VERSION}/downloader-server-${TS}.war"
echo ${URL}
rm docker/*.war
curl  ${URL} -o docker/downloader.war