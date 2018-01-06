#!/bin/bash

./gradlew build
./blitz2k18 -f 2 -d "30 30" "java -jar build/libs/coveo-whitecat.jar" "java -jar build/libs/coveo-whitecat.jar" -t
