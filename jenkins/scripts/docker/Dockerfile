FROM airhacks/wildfly
RUN mkdir -p /opt/downloder/bin/
RUN curl -L https://yt-dl.org/downloads/latest/youtube-dl -o /opt/downloder/bin/youtube-dl
RUN mkdir -p /opt/downloder/jobs/
ENV PATH="/opt/downloder/bin:${PATH}"
ENV DOWNLOADER_SSH_PROPERTIES /opt/downloder/config/ssh-params.properties
ENV DOWNLOADER_TRANSFER_PROPERTIES /opt/downloder/config/transfer.properties
COPY downloader.war ${DEPLOYMENT_DIR}
