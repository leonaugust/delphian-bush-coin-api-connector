#!/usr/bin/env bash
export CLASSPATH="$(find target/ -type f -name '*.jar'| grep '\-package' | tr '\n' ':')"
    docker build . -t delphianbush/coin-api-connector:0.0.1
    docker run --name rates-connector --net=host --rm -t \
           -v $(pwd)/offsets:/coin-api-connector/offsets \
           delphianbush/coin-api-connector:0.0.1
