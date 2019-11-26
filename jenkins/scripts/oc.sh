#!/usr/bin/env bash

oc new-app downloader-base:1~/. --name downloader
oc start-build --from-dir target/war/ downloader

oc create configmap downloader-transfer-params --from-file /Users/albertlacambra/git/lacambra.tech/downloader/jenkins/scripts/docker/data/config/transfer.properties

oc set env dc/downloader "DOWNLOADER_SSH_PROPERTIES=/opt/downloder/config/transfer.properties"
oc set env dc/downloader "DOWNLOADER_TRANSFER_PROPERTIES=/opt/downloder/config/transfer.properties"
oc set volume dc/downloader --add --configmap-name downloader-transfer-params  --mount-path=/opt/downloder/config/
oc set volume dc/downloader --add --secret-name downloader-private --mount-path=/opt/downloder/.ssh/

