package etl.translators;

import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;

import etl.ModelToDocumentTranslator;
import etl.TranslationException;

import model.Model;
import model.Blog;
import model.Post;


/**
 * Translates model.Blog objects to Blog documents
 *
 * @author bploetz
 */
public class BlogTranslator extends ModelToDocumentTranslator {

  private static final String RUBY_CLASS = "Blog";
  private static final String NEW_BLOG = "Blog.new";
  private static final String SET_RDBMS_ID = "rdbms_id=";
  private static final String SET_TITLE = "title=";
  private static final String SET_POSTS = "posts=";


  @Override
  public IRubyObject translate(Model model) throws TranslationException {
    Blog javaBlog = (Blog)model;

    String expr = RUBY_CLASS + ".where(:rdbms_id => " + javaBlog.getId() + ").first";
    IRubyObject rubyBlog = RUNTIME.evalScriptlet(expr);
    if(rubyBlog.isNil()){
      rubyBlog = RUNTIME.evalScriptlet(NEW_BLOG);
    }

    rubyBlog.callMethod(CTX, SET_RDBMS_ID, JavaUtil.convertJavaToRuby(RUNTIME, javaBlog.getId()));
    rubyBlog.callMethod(CTX, SET_TITLE, JavaUtil.convertJavaToRuby(RUNTIME, javaBlog.getTitle()));
    IRubyObject postsArray = RUNTIME.evalScriptlet("Array.new");
    for (Post post : javaBlog.getPosts()) {
      PostTranslator postTranslator = new PostTranslator();
      postsArray.callMethod(CTX, "<<", postTranslator.translate(post));
    }
    rubyBlog.callMethod(CTX, SET_POSTS, postsArray);
    rubyBlog.callMethod(CTX, "save!");
    return rubyBlog;
  }
}
