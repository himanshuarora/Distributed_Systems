FROM sequenceiq/hadoop-docker:2.7.0
MAINTAINER Jayant Malani <jmalani@eng.ucsd.edu>

COPY files/* /tmp/
COPY files/hosts /etc/hosts

RUN mkdir /usr/local/hadoop/dfs && \
mkdir /usr/local/hadoop/dfs/name && \
mkdir /usr/local/hadoop/dfs/name/data

RUN echo "export PATH=$PATH:$HADOOP_PREFIX/bin" > /root/.bashrc
RUN echo "export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar" >> /root/.bashrc

RUN mv /tmp/core-site.xml /usr/local/hadoop/etc/hadoop/core-site.xml && \
mv /tmp/hdfs-site.xml /usr/local/hadoop/etc/hadoop/hdfs-site.xml && \
mv /tmp/mapred-site.xml /usr/local/hadoop/etc/hadoop/mapred-site.xml && \
mv /tmp/hadoop-env.sh /usr/local/hadoop/etc/hadoop/hadoop-env.sh && \
mv /tmp/slaves /usr/local/hadoop/etc/hadoop/slaves && \
mv /tmp/start.sh /root/start.sh && \
mv /tmp/bigram.sh /root/bigram.sh && \
mv /tmp/WordCount.java /usr/local/hadoop/WordCount.java && \
mv /tmp/input.txt /root/input.txt && \
mv /tmp/PairOfWritables.java /usr/local/hadoop/PairOfWritables.java

RUN chmod +x /root/start.sh && \
chmod +x /root/bigram.sh
