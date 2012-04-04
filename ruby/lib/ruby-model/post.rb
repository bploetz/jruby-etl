class Post
  include MongoMapper::EmbeddedDocument

  key :rdbms_id, Integer
  key :title, String
  key :content, String
  key :post_date, Time
  key :author_username, String
  belongs_to :author, :class_name => 'User'
  many :comments, :class_name => 'Comment'
end
