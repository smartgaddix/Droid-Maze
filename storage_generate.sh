#!/bin/bash

echo 'Cleaning src ang bin folders'

find -E src -regex '.*/datastore/.*' -name '*.java' -exec rm -v -- {} +
find -E bin/classes -regex '.*/datastore/.*' -name '*.class' -exec rm -v -- {} +
find -E bin/classes -regex '.*/game/GameProfile.class' -exec rm -v -- {} +

echo 'Compiling...'

find -E src -regex '.*/game/GameProfile.java' > storage_classes

javac @storage_options @storage_classes 

echo '...done'