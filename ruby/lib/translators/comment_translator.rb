# Translates model.Comment Java objects to Comment Ruby objects
class CommentTranslator
  def translate(java_comment)
    ruby_comment = Comment.new
    ruby_comment.rdbms_id = java_comment.getId
    ruby_comment.title = java_comment.getTitle
    ruby_comment.content = java_comment.getContent
    ruby_comment.post_date = java_comment.getPostDate
    ruby_comment.author_username = java_comment.getAuthor.getUsername
    user_translator = UserTranslator.new
    ruby_comment.author = user_translator.translate(java_comment.getAuthor) 
    ruby_comment.save!
    ruby_comment
  end
end
