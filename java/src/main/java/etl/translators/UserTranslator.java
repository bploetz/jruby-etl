package etl.translators;

import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

import etl.ModelToDocumentTranslator;
import etl.TranslationException;

import model.Model;
import model.User;


/**
 * Translates model.User objects to User documents
 *
 * @author bploetz
 */
public class UserTranslator extends ModelToDocumentTranslator {

  private static final String RUBY_CLASS = "User";
  private static final String NEW_USER = "User.new";
  private static final String SET_USERNAME = "username=";


  @Override
  public IRubyObject translate(Model model) throws TranslationException {
    User javaUser = (User)model;

    ThreadContext ctx = RUNTIME.getCurrentContext();
    String expr = RUBY_CLASS + ".where(:rdbms_id => \"" + javaUser.getId() + "\").first";
    IRubyObject rubyUser = RUNTIME.evalScriptlet(expr);
    if(rubyUser.isNil()){
      rubyUser = RUNTIME.evalScriptlet(NEW_USER);
    }
    rubyUser.callMethod(ctx, SET_USERNAME, JavaUtil.convertJavaToRuby(RUNTIME, javaUser.getUsername()));
    rubyUser.callMethod(ctx, "save!");
    return rubyUser;
  }
}
