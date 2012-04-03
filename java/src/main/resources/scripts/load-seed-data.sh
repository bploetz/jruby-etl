#!/bin/sh

. ${jruby-etl-install.dir}/bin/set-classpath

java -Djava.util.logging.config.file=${jruby-etl-install.dir}/conf/logging.properties app.AppRunner dbSeeder
