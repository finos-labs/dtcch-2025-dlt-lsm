#!/usr/bin/env bash

WEB3J_CLI_VERSION=$1
CONTRACT_NAMES=(${2//;/ })
TARGET_PACKAGE=$3
TARGET_DIRECTORY=${TARGET_PACKAGE//./\/}
PWD=$(pwd)

echo "$PWD/scripts/web3j/web3jSourceGenerator.sh \"$WEB3J_CLI_VERSION\" \"$2\"  \"$TARGET_PACKAGE\""

mkdir -p build/tmp/web3j
cd build/tmp/web3j

if [ ! -d web3j-"${WEB3J_CLI_VERSION#*v}" ]; then
    curl -L -O https://github.com/web3j/web3j-cli/releases/download/"$WEB3J_CLI_VERSION"/web3j-"${WEB3J_CLI_VERSION#*v}".zip

    unzip web3j-"${WEB3J_CLI_VERSION#*v}".zip
    rm -Rf web3j-"${WEB3J_CLI_VERSION#*v}".zip
fi
cd ../../../
mkdir -p src/main/java/"$TARGET_DIRECTORY"

# Get Contracts
for CONTRACT in "${CONTRACT_NAMES[@]}"
do
    IFS=':' read -r -a contractUri <<< "$CONTRACT"
    FINAL_PACKAGE="$TARGET_PACKAGE"
    if [[ "${contractUri[0]}" == "." ]]; then
        CONTRACT_URL=artifacts/contracts/"${contractUri[1]}".sol/"${contractUri[1]}".json
    else
        CONTRACT_URL=artifacts/contracts/"${contractUri[0]}"/"${contractUri[1]}".sol/"${contractUri[1]}".json
        FINAL_PACKAGE="$TARGET_PACKAGE.${contractUri[0]}"
    fi
    cp "$CONTRACT_URL" "${contractUri[1]}".json
    ./build/tmp/web3j/web3j-"${WEB3J_CLI_VERSION#*v}"/bin/web3j generate truffle --truffle-json="${contractUri[1]}".json -p "$FINAL_PACKAGE" -o src/main/java/

    rm "${contractUri[1]}".json
done
