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

OS=`echo $OS | tr '[:upper:]' '[:lower:]'`
export BASE_DIR=`pwd`

mkdir -p src
cd src

if [ ! -d viper ]; then
	echo "cframework not found"
	git clone -b viper/master https://gerrit.fd.io/r/cicn viper
fi

if [ ! -d curl ]; then
	echo "curl  not found"
	git clone https://github.com/curl/curl.git
	cd curl
	git checkout tags/curl-7_66_0
	cd ..
fi

if [ ! -d libxml2 ]; then
	echo "libxml2 not found"
	git clone https://github.com/GNOME/libxml2.git
	cd libxml2
	git checkout tags/v2.9.9
	cd ..
	cp $BASE_DIR/external/libxml2/CMakeLists.txt libxml2
	cp $BASE_DIR/external/libxml2/xmlversion.h libxml2/include/libxml
	cp $BASE_DIR/external/libxml2/config.h libxml2
	${SED} -i '1s/^/#include <errno.h>/' libxml2/triodef.h
fi

if [ ! -d libevent ]; then
    echo "libevent not found"
	git clone https://github.com/libevent/libevent.git
	cd libevent
	git checkout tags/release-2.1.11-stable
	cd ..
fi

if [ ! -d asio ]; then
	echo "Asio directory not found"
	git clone https://github.com/chriskohlhoff/asio.git
	cd asio
	git checkout tags/asio-1-12-2
	cd ..
fi

if [ ! -d libconfig ]; then
	echo "libconfig not found"
	git clone https://github.com/hyperrealm/libconfig.git
	cd libconfig
	git checkout a6b370e78578f5bf594f8efe0802cdc9b9d18f1a
	cd ..
	${SED} -i -- '2s/$/include(CheckSymbolExists)/' libconfig/CMakeLists.txt 
fi

