class Blog
  include MongoMapper::Document

  safe
  timestamps!

  key :rdbms_id, Integer
  key :title, String
  many :posts, :class_name => 'Post'
end
