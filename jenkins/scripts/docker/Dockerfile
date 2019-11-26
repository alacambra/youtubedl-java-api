FROM airhacks/s2i-wildfly:0.0.3
RUN mkdir -p /opt/downloder/bin/
RUN curl -L https://yt-dl.org/downloads/latest/youtube-dl -o /opt/downloder/bin/youtube-dl
RUN chmod +x /opt/downloder/bin/youtube-dl
USER root
RUN yum install epel-release -y
RUN yum update -y
RUN rpm --import http://li.nux.ro/download/nux/RPM-GPG-KEY-nux.ro
RUN rpm -Uvh http://li.nux.ro/download/nux/dextop/el7/x86_64/nux-dextop-release-0-5.el7.nux.noarch.rpm
RUN yum install ffmpeg ffmpeg-devel -y
RUN mkdir -p /opt/downloder/jobs/
ENV PATH="/opt/downloder/bin:${PATH}"
RUN chown -R 1001 /opt/downloder
USER 1001
