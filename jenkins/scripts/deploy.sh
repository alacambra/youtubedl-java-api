#!/usr/bin/env bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
export VERSION=$1
echo "Download artrifacts for version ${VERSION}"
${DIR}/download.sh
echo
docker build -t lacambra/downloader:${VERSION} ${DIR}/docker/
docker stop downloader
docker rm downloader
docker run -d --name downloader -v ${DOWNLOADER_DOCKER_CONFIG_PATH}:/opt/downloder/config/ -p 28080:8080 -p 28443:8443 lacambra/downloader:${VERSION}
