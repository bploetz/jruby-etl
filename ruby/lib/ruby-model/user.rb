class User
  include MongoMapper::Document

  key :rdbms_id, Integer
  key :username, String
  validates_uniqueness_of :username
end
