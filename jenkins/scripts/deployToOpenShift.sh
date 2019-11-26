#!/usr/bin/env bash
set -x

cd ../../

rm -Rf downloader-server/src/main/webapp/app/*
cp -R ui/src/* downloader-server/src/main/webapp/app

mvn clean package
cp downloader-server/target/downloader.war ./downloader-server/war/
oc start-build --from-dir ./downloader-server/war/ downloader
