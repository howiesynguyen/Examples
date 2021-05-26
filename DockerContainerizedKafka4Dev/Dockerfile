# Copyright (c) ${date} Nguyen, Howie S. (github.com/howiesynguyen)
#  
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
# 	http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# #distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Dockfile for both Kafka and Zookeeper. This is just an example and it should be used as a reference for learning/dev purposes. 

#Using a Debian base image 
#FROM openjdk:11-jre-slim-buster
FROM openjdk:11-jre

ENV SCALA_VERSION 2.13
ENV KAFKA_VERSION 2.8.0
ENV KAFKA_TGZ_FILE kafka_"$SCALA_VERSION"-"$KAFKA_VERSION".tgz
ENV KAFKA_HOME /opt/kafka

RUN apt-get update -y

#Once zookeeper installation is finished, it will be started as a daemon automatically
RUN apt-get install -y zookeeper 

RUN apt-get install -y wget
RUN wget -q https://downloads.apache.org/kafka/$KAFKA_VERSION/$KAFKA_TGZ_FILE -O /tmp/$KAFKA_TGZ_FILE
RUN mkdir $KAFKA_HOME
RUN tar xfz /tmp/$KAFKA_TGZ_FILE --strip-components 1 -C $KAFKA_HOME 
RUN rm /tmp/$KAFKA_TGZ_FILE

#Install Supervisor that is used get Zookeeper and Kafka processes started in order. Supervisor is to help managers background processes/services
RUN apt-get install -y supervisor
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

ADD kafka-start.sh /usr/bin/kafka-start.sh

RUN apt-get clean

#Kafka port 9092 by default 
EXPOSE 9092

#Zookeeper port 2181 by default 
EXPOSE 2181 

CMD ["/usr/bin/kafka-start.sh"]