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
ABI=$1
INSTALLATION_DIR=$2
OS=`echo $OS | tr '[:upper:]' '[:lower:]'`
export BASE_DIR=`pwd`
mkdir -p ${INSTALLATION_DIR}
mkdir -p ${INSTALLATION_DIR}/include

if [ ! -d ${INSTALLATION_DIR}/include/openssl ]; then
	echo "OpenSSL Libs not found!"
	echo "Compile OpenSSL"
	cd ${BASE_DIR}/external
	export ANDROID_NDK_ROOT=${BASE_DIR}/sdk/ndk-bundle
	if [ "$ABI" = "arm" ]; then
       bash ${BASE_DIR}/scripts/build-openssl-android.sh 26 26 armeabi
       cp ${BASE_DIR}/external/openssl-lib/armeabi/*.a ${INSTALLATION_DIR}/lib/
       cp -r ${BASE_DIR}/external/openssl-lib/armeabi/include/openssl ${INSTALLATION_DIR}/include/
    elif [ "$ABI" = "arm64" ]; then
        bash ${BASE_DIR}/scripts/build-openssl-android.sh 26 26 arm64-v8a
        cp ${BASE_DIR}/external/openssl-lib/arm64-v8a/*.a ${INSTALLATION_DIR}/lib/
        cp -r ${BASE_DIR}/external/openssl-lib/arm64-v8a/include/openssl ${INSTALLATION_DIR}/include/
    elif [ "$ABI" = "x86" ]; then
        bash ${BASE_DIR}/scripts/build-openssl-android.sh 26 26 x86
        cp ${BASE_DIR}/external/openssl-lib/x86/*.a ${INSTALLATION_DIR}/lib/
        cp -r ${BASE_DIR}/external/openssl-lib/x86/include/openssl ${INSTALLATION_DIR}/include/
    else
    	bash ${BASE_DIR}/scripts/build-openssl-android.sh 26 26 x86_64
        cp ${BASE_DIR}/external/openssl-lib/x86_64/*.a ${INSTALLATION_DIR}/lib/
        cp -r ${BASE_DIR}/external/openssl-lib/x86_64/include/openssl ${INSTALLATION_DIR}/include/
	fi
	touch ${VERSIONS_FILE}
    ${SED} -i "/${ABI}_openssl/d" ${VERSIONS_FILE}
	echo ${ABI}_openssl=1.1.0h >> ${VERSIONS_FILE}
fi

