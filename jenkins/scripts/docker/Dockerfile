FROM airhacks/wildfly
RUN mkdir -p /opt/downloder/bin/
RUN curl -L https://yt-dl.org/downloads/latest/youtube-dl -o /opt/downloder/bin/youtube-dl
RUN chmod +x /opt/downloder/bin/youtube-dl
USER root
RUN yum install epel-release -y
RUN yum update -y
RUN rpm --import http://li.nux.ro/download/nux/RPM-GPG-KEY-nux.ro
RUN rpm -Uvh http://li.nux.ro/download/nux/dextop/el7/x86_64/nux-dextop-release-0-5.el7.nux.noarch.rpm
RUN yum install ffmpeg ffmpeg-devel -y
#USER serveradmin
RUN mkdir -p /opt/downloder/jobs/
ENV PATH="/opt/downloder/bin:${PATH}"
ENV DOWNLOADER_SSH_PROPERTIES /opt/downloder/config/ssh-params.properties
ENV DOWNLOADER_TRANSFER_PROPERTIES /opt/downloder/config/transfer.properties
COPY downloader.war ${DEPLOYMENT_DIR}
