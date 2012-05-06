# Translates model.Post Java objects to Post Ruby objects
class PostTranslator
  def translate(java_post)
    ruby_post = Post.new
    ruby_post.rdbms_id = java_post.getId
    ruby_post.title = java_post.getTitle
    ruby_post.content = java_post.getContent
    ruby_post.post_date = java_post.getPostDate
    ruby_post.author_username = java_post.getAuthor.getUsername
    user_translator = UserTranslator.new
    ruby_post.author = user_translator.translate(java_post.getAuthor) 
    comments = Array.new
    java_post.getComments.iterator.each do |comment|
      comment_translator = CommentTranslator.new
      comments << comment_translator.translate(comment)
    end
    ruby_post.comments = comments
    ruby_post.save!
    ruby_post
  end
end
