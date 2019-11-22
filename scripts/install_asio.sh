 #############################################################################
 # Copyright (c) 2017 Cisco and/or its affiliates.
 # Licensed under the Apache License, Version 2.0 (the "License");
 # you may not use this file except in compliance with the License.
 # You may obtain a copy of the License at:
 #
 #     http://www.apache.org/licenses/LICENSE-2.0
 #
 # Unless required by applicable law or agreed to in writing, software
 # distributed under the License is distributed on an "AS IS" BASIS,
 # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 # See the License for the specific language governing permissions and
 # limitations under the License.
 ##############################################################################

#!/bin/bash

set -ex
INSTALLATION_DIR=$1
OS=`echo $OS | tr '[:upper:]' '[:lower:]'`
export BASE_DIR=`pwd`
mkdir -p ${INSTALLATION_DIR}
mkdir -p ${INSTALLATION_DIR}/include


if [ ! -d ${INSTALLATION_DIR}/include/asio ]; then
    cd src
    echo "Asio not found"
    if [ ! -d asio ]; then
        echo "Asio directory not found"
        git clone https://github.com/chriskohlhoff/asio.git
        cd asio
        git checkout tags/asio-1-12-2
    fi
    cp -r asio/asio/include/asio.hpp ${INSTALLATION_DIR}/include/
    cp -r asio/asio/include/asio ${INSTALLATION_DIR}/include/
    cd ..
fi
