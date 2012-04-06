package etl.translators;

import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;

import etl.EntityToDocumentTranslator;
import etl.TranslationException;

import model.Model;
import model.Post;
import model.Comment;


/**
 * Translates model.Post objects to Post documents
 *
 * @author bploetz
 */
public class PostTranslator extends EntityToDocumentTranslator {

  private static final String NEW_POST = "Post.new";
  private static final String SET_RDBMS_ID = "rdbms_id=";
  private static final String SET_TITLE = "title=";
  private static final String SET_CONTENT = "content=";
  private static final String SET_POST_DATE = "post_date=";
  private static final String SET_AUTHOR_USERNAME = "author_username=";
  private static final String SET_AUTHOR = "author=";
  private static final String SET_COMMENTS = "comments=";


  @Override
  public IRubyObject translate(Model model) throws TranslationException {
    Post javaPost = (Post)model;

    IRubyObject rubyPost = RUNTIME.evalScriptlet(NEW_POST);
    rubyPost.callMethod(CTX, SET_RDBMS_ID, JavaUtil.convertJavaToRuby(RUNTIME, javaPost.getId()));
    rubyPost.callMethod(CTX, SET_TITLE, JavaUtil.convertJavaToRuby(RUNTIME, javaPost.getTitle()));
    rubyPost.callMethod(CTX, SET_CONTENT, JavaUtil.convertJavaToRuby(RUNTIME, javaPost.getContent()));
    rubyPost.callMethod(CTX, SET_POST_DATE, convertToRuby(javaPost.getPostDate()));

    IRubyObject commentsArray = RUNTIME.evalScriptlet("Array.new");
    for (Comment comment : javaPost.getComments()) {
      commentsArray.callMethod(CTX, "<<", new CommentTranslator().translate(comment));
    }
    rubyPost.callMethod(CTX, SET_COMMENTS, commentsArray);
    rubyPost.callMethod(CTX, SET_AUTHOR_USERNAME, JavaUtil.convertJavaToRuby(RUNTIME, javaPost.getAuthor().getUsername()));
    rubyPost.callMethod(CTX, SET_AUTHOR, new UserTranslator().translate(javaPost.getAuthor()));
    rubyPost.callMethod(CTX, "save!");
    return rubyPost;
  }
}
