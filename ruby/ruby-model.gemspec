# -*- encoding: utf-8 -*-

lib = File.expand_path('../lib/', __FILE__)
$:.unshift lib unless $:.include?(lib)

require 'bundler'

Gem::Specification.new do |s|
  s.name = 'ruby-model'
  s.version = '1.0'
  s.platform = Gem::Platform::RUBY
  s.authors = ["Brian Ploetz"]
  s.summary = 'Example Ruby domain model mapped to MongoDB'
  s.description = 'Example Ruby domain model mapped to MongoDB'
  s.files = Dir['lib/**/*']
  s.rdoc_options = ["--charset=UTF-8"]
  s.require_paths = ["lib"]
  s.required_rubygems_version = ">= 1.3.6"

  s.add_runtime_dependency("mongo", ["= 1.3.1"])
  s.add_runtime_dependency("bson", ["= 1.3.1"])
  s.add_runtime_dependency("mongo_mapper", ["= 0.9.1"])
  s.add_runtime_dependency("rake", ["= 0.8.7"])
  s.add_runtime_dependency("activemodel", ["= 3.0.7"])
  s.add_runtime_dependency("jruby-openssl", ["= 0.7.2"])
end
