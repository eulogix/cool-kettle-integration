#!/usr/bin/env bash
cd lib
mvn -Dmaven.test.skip=true clean install
cd ../file-uploader
mvn -Dmaven.test.skip=true clean install
cd ../file-get-properties
mvn -Dmaven.test.skip=true clean install
cd ../file-set-properties
mvn -Dmaven.test.skip=true clean install
cd ../file-delete
mvn -Dmaven.test.skip=true clean install
cd ../file-search
mvn -Dmaven.test.skip=true clean install
cd ../notification-send
mvn -Dmaven.test.skip=true clean install
cd ../filerepo-uploader
mvn -Dmaven.test.skip=true clean install
cd ../template-render
mvn -Dmaven.test.skip=true clean install