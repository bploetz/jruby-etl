# Translates model.Blog Java objects to Blog Ruby objects
class BlogTranslator
  def translate(java_blog)
    ruby_blog = Blog.where(:rdbms_id => java_blog.getId).first
    ruby_blog ||= Blog.new
    ruby_blog.rdbms_id = java_blog.getId
    ruby_blog.title = java_blog.getTitle
    posts = Array.new
    java_blog.getPosts.iterator.each do |post|
      post_translator = PostTranslator.new
      posts << post_translator.translate(post)
    end
    ruby_blog.posts = posts
    ruby_blog.save!
    ruby_blog
  end
end