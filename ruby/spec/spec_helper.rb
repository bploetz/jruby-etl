require 'rubygems'
require 'bundler/setup'
Bundler.setup(:default, :development) if defined?(Bundler)
require 'ruby-model'
require 'mongo_mapper'

MongoMapper.connection = Mongo::Connection.new
MongoMapper.database = 'jrubyetltest'

RSpec.configure do |config|
  config.before(:each) do
    MongoMapper.database.collections.each(&:remove)
  end

  config.mock_with :rspec
end
