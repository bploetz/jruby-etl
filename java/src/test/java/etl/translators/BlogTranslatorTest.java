package etl.translators;

import static org.junit.Assert.*;

import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import etl.TranslationException;

import model.Blog;
import model.BlogTest;


/**
 * Integration test for BlogTranslator
 *
 * @author bploetz
 */
public class BlogTranslatorTest extends TranslatorTest {

  static final void assertTranslated(Blog javaBlog, IRubyObject rubyBlog) {
    assertEquals(JavaUtil.convertJavaToRuby(RUNTIME, javaBlog.getId()), rubyBlog.callMethod(CTX, "rdbms_id"));
    assertEquals(javaBlog.getTitle(), rubyBlog.callMethod(CTX, "title").asJavaString());
  }

  @Test
  @Transactional
  public void testTranslate() {
    try {
      Blog javaBlog = BlogTest.getTestBlog();
      BlogTranslator translator = new BlogTranslator();
      IRubyObject rubyBlog = translator.translate(javaBlog);
      assertTranslated(javaBlog, rubyBlog);
    } catch (TranslationException te) {
      fail(te.getMessage());
    }
  }
}
