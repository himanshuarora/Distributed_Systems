FROM sequenceiq/hadoop-docker:2.7.0
MAINTAINER Jayant Malani <jmalani@eng.ucsd.edu>
COPY files/* /tmp/

RUN mkdir /usr/local/hadoop/dfs && \
mkdir /usr/local/hadoop/dfs/name && \
mkdir /usr/local/hadoop/dfs/name/data

# RUN /bin/bash
RUN echo "export PATH=$PATH:$HADOOP_PREFIX/bin" > /root/.bashrc

RUN mv /tmp/core-site.xml /usr/local/hadoop/etc/hadoop/core-site.xml && \
mv /tmp/hdfs-site.xml /usr/local/hadoop/etc/hadoop/hdfs-site.xml && \
mv /tmp/mapred-site.xml /usr/local/hadoop/etc/hadoop/mapred-site.xml && \
mv /tmp/hadoop-env.sh /usr/local/hadoop/etc/hadoop/hadoop-env.sh && \
mv /tmp/start-slave.sh /root/start-slave.sh

RUN chmod +x /root/start-slave.sh
