#!/bin/sh

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

#This is just an example and it should be used as a reference for learning/dev purposes.

export KAFKA_HOME=/opt/kafka

echo "Executing kafka-start.sh..."

if [ ! -z "$ADVERTISED_LISTENERS" ]; then
    echo "Set advertised.listeners: $ADVERTISED_LISTENERS in $KAFKA_HOME/config/server.properties" 
    sed -i 's+#advertised.listeners=PLAINTEXT://your.host.name:9092+advertised.listeners=PLAINTEXT://'$ADVERTISED_LISTENERS'+g' $KAFKA_HOME/config/server.properties
fi

/usr/bin/supervisord