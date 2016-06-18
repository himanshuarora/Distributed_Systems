#!/bin/bash
export PATH=$PATH:$HADOOP_PREFIX/bin
export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar

cd $HADOOP_PREFIX

mkdir /root/input
cp -R /root/input.txt /root/input/input.txt

echo "#####JM#####"
hadoop fs -mkdir -p /input

hdfs dfs -put /root/input/* /input

bin/hadoop com.sun.tools.javac.Main WordCount.java PairOfWritables.java

jar cf wc.jar WordCount*.class PairOfWritables*.class

bin/hadoop jar wc.jar WordCount /input /output
