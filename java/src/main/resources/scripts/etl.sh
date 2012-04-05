#!/bin/sh

. ${jruby-etl-install.dir}/bin/set-classpath
export BUNDLE_GEMFILE=${jruby-etl-install.dir}/conf/Gemfile
bundle install
java -Djava.util.logging.config.file=${jruby-etl-install.dir}/conf/logging.properties app.AppRunner etlManager
