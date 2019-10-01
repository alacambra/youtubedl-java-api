#!/usr/bin/env bash
set -x
cd ../../
mvn clean package
cp downloader-server/target/downloader.war ./downloader-server/war/
oc start-build --from-dir ./downloader-server/war/ downloader
