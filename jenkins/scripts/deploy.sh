#!/usr/bin/env bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
export VERSION=1.0-SNAPSHOT
${DIR}/download.sh
docker build -t lacambra/downloader:${VERSION} ${DIR}/docker/
