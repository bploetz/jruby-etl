# Set up gems listed in the Gemfile.
# This assumes the BUNDLE_GEMFILE environment
# variable has been set pointing at the Gemfile to use
begin
  require 'rubygems'
  require 'bundler/setup'
  require 'ruby-model'
  require 'mongo_mapper'
  require 'logger'
rescue Bundler::GemNotFound => e
  STDERR.puts e.message
  STDERR.puts "Try running `bundle install`."
  exit!
end

class PropertyLoader
  def loadProperties(str)
    logger = Logger.new(STDOUT)
    logger.level = Logger::DEBUG
    @mongo_config = YAML.load(str)
    MongoMapper.setup(@mongo_config, 'connector', {:logger => logger})
  end
end
