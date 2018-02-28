cd ..
cd kettle-plugins
call buildall.bat
cd ..
cd coolkettleintegration
cd lib
call mvn clean install
cd ..
cd file-uploader
call mvn clean install
cd ..
cd file-get-properties
call mvn clean install
cd ..
cd file-set-properties
call mvn clean install
cd ..
cd file-delete
call mvn clean install
cd ..
cd file-search
call mvn clean install
cd ..
cd notification-send
call mvn clean install
cd ..
cd filerepo-uploader
call mvn clean install
cd ..
cd template-render
call mvn clean install
cd ..