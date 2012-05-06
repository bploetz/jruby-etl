require 'spec_helper'

describe BlogTranslator do
  before :each do
    @mock_java_blog = double("Blog")
    @mock_java_blog.stub(:getId) {67890}
    @mock_java_blog.stub(:getTitle) {"Test Blog"}

    @mock_java_user1 = double("User")
    @mock_java_user1.stub(:getId) {83493}
    @mock_java_user1.stub(:getUsername) {"fred"}

    @mock_java_user2 = double("User")
    @mock_java_user2.stub(:getId) {16577}
    @mock_java_user2.stub(:getUsername) {"sam"}

    @mock_java_comment1 = double("Comment")
    @mock_java_comment1.stub(:getId) {73483}
    @mock_java_comment1.stub(:getTitle) {"Comment 1"}
    @mock_java_comment1.stub(:getContent) {"Awesome"}
    @mock_java_comment1.stub(:getPostDate) {DateTime.civil(2012, 05, 07, 11, 13, 06)}
    @mock_java_comment1.stub(:getAuthor) {@mock_java_user1}

    @mock_java_comment2 = double("Comment")
    @mock_java_comment2.stub(:getId) {49122}
    @mock_java_comment2.stub(:getTitle) {"Comment 2"}
    @mock_java_comment2.stub(:getContent) {"Agreed"}
    @mock_java_comment2.stub(:getPostDate) {DateTime.civil(2012, 05, 17, 12, 49, 35)}
    @mock_java_comment2.stub(:getAuthor) {@mock_java_user2}

    @mock_java_post1 = double("Post")
    @mock_java_post1.stub(:getId) {48573}
    @mock_java_post1.stub(:getTitle) {"Post 1"}
    @mock_java_post1.stub(:getContent) {"Blah blah blah..."}
    @mock_java_post1.stub(:getPostDate) {DateTime.civil(2012, 05, 03, 23, 02, 23)}
    @mock_java_post1.stub(:getAuthor) {@mock_java_user1}

    @mock_comments1 = double("Comments")
    @mock_comments1.stub(:iterator) {[@mock_java_comment1, @mock_java_comment2]}
    @mock_java_post1.stub(:getComments) {@mock_comments1}

    @mock_java_post2 = double("Post")
    @mock_java_post2.stub(:getId) {20112}
    @mock_java_post2.stub(:getTitle) {"Post 2"}
    @mock_java_post2.stub(:getContent) {"Yup"}
    @mock_java_post2.stub(:getPostDate) {DateTime.civil(2012, 05, 04, 00, 12, 15)}
    @mock_java_post2.stub(:getAuthor) {@mock_java_user2}

    @mock_comments2 = double("Comments")
    @mock_comments2.stub(:iterator) {[@mock_java_comment2]}
    @mock_java_post2.stub(:getComments) {@mock_comments2}

    @mock_blog_posts = double("Posts")
    @mock_blog_posts.stub(:iterator) {[@mock_java_post1, @mock_java_post2]}
    @mock_java_blog.stub(:getPosts) {@mock_blog_posts}
    @blog_translator = BlogTranslator.new
  end

  context "#translate" do
    it "should create blogs if they are not found" do
      @blog_translator.translate(@mock_java_blog)
      @blog = Blog.where(:rdbms_id => @mock_java_blog.getId).first
      @blog.should_not be_nil
      @blog.rdbms_id.should == 67890
      @blog.title.should == "Test Blog"
      @blog.posts.size.should == 2
      @blog.posts[0].rdbms_id.should == @mock_java_post1.getId
      @blog.posts[0].title.should == @mock_java_post1.getTitle
      @blog.posts[0].content.should == @mock_java_post1.getContent
      @blog.posts[0].post_date.should == @mock_java_post1.getPostDate
      @blog.posts[0].author_username.should == @mock_java_post1.getAuthor.getUsername
      @blog.posts[0].author.should_not be_nil
      @blog.posts[0].comments.size.should == 2
      @blog.posts[0].comments[0].rdbms_id.should == @mock_java_comment1.getId
      @blog.posts[0].comments[0].title.should == @mock_java_comment1.getTitle
      @blog.posts[0].comments[0].content.should == @mock_java_comment1.getContent
      @blog.posts[0].comments[0].post_date.should == @mock_java_comment1.getPostDate
      @blog.posts[0].comments[0].author_username.should == @mock_java_comment1.getAuthor.getUsername
      @blog.posts[0].comments[0].author.should_not be_nil
      @blog.posts[0].comments[1].rdbms_id.should == @mock_java_comment2.getId
      @blog.posts[0].comments[1].title.should == @mock_java_comment2.getTitle
      @blog.posts[0].comments[1].content.should == @mock_java_comment2.getContent
      @blog.posts[0].comments[1].post_date.should == @mock_java_comment2.getPostDate
      @blog.posts[0].comments[1].author_username.should == @mock_java_comment2.getAuthor.getUsername
      @blog.posts[0].comments[1].author.should_not be_nil

      @blog.posts[1].rdbms_id.should == @mock_java_post2.getId
      @blog.posts[1].title.should == @mock_java_post2.getTitle
      @blog.posts[1].content.should == @mock_java_post2.getContent
      @blog.posts[1].post_date.should == @mock_java_post2.getPostDate
      @blog.posts[1].author_username.should == @mock_java_user2.getUsername
      @blog.posts[1].author.should_not be_nil
      @blog.posts[1].comments.size.should == 1
      @blog.posts[1].comments[0].rdbms_id.should == @mock_java_comment2.getId
      @blog.posts[1].comments[0].title.should == @mock_java_comment2.getTitle
      @blog.posts[1].comments[0].content.should == @mock_java_comment2.getContent
      @blog.posts[1].comments[0].post_date.should == @mock_java_comment2.getPostDate
      @blog.posts[1].comments[0].author_username.should == @mock_java_comment2.getAuthor.getUsername
      @blog.posts[1].comments[0].author.should_not be_nil
    end

    it "should update blogs if they are found" do
      @blog_translator.translate(@mock_java_blog)
      @mock_java_blog.stub(:getTitle) {"Another Blog Title"}
      @blog_translator.translate(@mock_java_blog)
      @blog = Blog.where(:rdbms_id => @mock_java_blog.getId).first
      @blog.should_not be_nil
      @blog.rdbms_id.should == 67890
      @blog.title.should == "Another Blog Title"
    end
  end
end
