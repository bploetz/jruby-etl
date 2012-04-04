package etl.translators;

import static org.junit.Assert.*;

import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import etl.TranslationException;

import model.User;
import model.UserTest;


/**
 * Integration test for UserTranslator
 *
 * @author bploetz
 */
public class UserTranslatorTest extends TranslatorTest {

  static final void assertTranslated(User javaUser, IRubyObject rubyUser) {
    assertEquals(JavaUtil.convertJavaToRuby(RUNTIME, javaUser.getId()), rubyUser.callMethod(CTX, "rdbms_id"));
    assertEquals(javaUser.getUsername(), rubyUser.callMethod(CTX, "username").asJavaString());
  }

  @Test
  @Transactional
  public void testTranslate() {
    try {
      User javaUser = UserTest.getTestUser();
      UserTranslator translator = new UserTranslator();
      IRubyObject rubyUser = translator.translate(javaUser);
      assertTranslated(javaUser, rubyUser);
    } catch (TranslationException te) {
      fail(te.getMessage());
    }
  }
}
