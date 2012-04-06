package etl.translators;

import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;

import etl.EntityToDocumentTranslator;
import etl.TranslationException;

import model.Model;
import model.Comment;


/**
 * Translates model.Comment objects to Comment documents
 *
 * @author bploetz
 */
public class CommentTranslator extends EntityToDocumentTranslator {

  private static final String NEW_COMMENT = "Comment.new";
  private static final String SET_RDBMS_ID = "rdbms_id=";
  private static final String SET_TITLE = "title=";
  private static final String SET_CONTENT = "content=";
  private static final String SET_POST_DATE = "post_date=";
  private static final String SET_AUTHOR_USERNAME = "author_username=";
  private static final String SET_AUTHOR = "author=";


  @Override
  public IRubyObject translate(Model model) throws TranslationException {
    Comment javaComment = (Comment)model;

    IRubyObject rubyComment = RUNTIME.evalScriptlet(NEW_COMMENT);
    rubyComment.callMethod(CTX, SET_RDBMS_ID, JavaUtil.convertJavaToRuby(RUNTIME, javaComment.getId()));
    rubyComment.callMethod(CTX, SET_TITLE, JavaUtil.convertJavaToRuby(RUNTIME, javaComment.getTitle()));
    rubyComment.callMethod(CTX, SET_CONTENT, JavaUtil.convertJavaToRuby(RUNTIME, javaComment.getContent()));
    rubyComment.callMethod(CTX, SET_POST_DATE, convertToRuby(javaComment.getPostDate()));
    rubyComment.callMethod(CTX, SET_AUTHOR_USERNAME, JavaUtil.convertJavaToRuby(RUNTIME, javaComment.getAuthor().getUsername()));
    rubyComment.callMethod(CTX, SET_AUTHOR, new UserTranslator().translate(javaComment.getAuthor()));
    rubyComment.callMethod(CTX, "save!");
    return rubyComment;
  }
}
