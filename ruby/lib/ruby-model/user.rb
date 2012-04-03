class User
  include MongoMapper::Document

  key :rdbms_id, Integer
  key :username, String
  validates_uniqueness_of :username
  many :posts, :class_name => 'Post'
  many :comments, :class_name => 'Comment'
end
