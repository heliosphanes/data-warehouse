#!/bin/bash

docker build --build-arg JANUS_VERSION=0.4.0 -t janusgraph .
