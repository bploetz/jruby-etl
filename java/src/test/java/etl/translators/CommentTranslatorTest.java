package etl.translators;

import static org.junit.Assert.*;

import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import etl.EntityToDocumentTranslator;
import etl.TranslationException;

import model.Comment;
import model.CommentTest;


/**
 * Integration test for CommentTranslator
 *
 * @author bploetz
 */
public class CommentTranslatorTest extends TranslatorTest {

  static final void assertTranslated(Comment javaComment, IRubyObject rubyComment) {
    assertEquals(JavaUtil.convertJavaToRuby(RUNTIME, javaComment.getId()), rubyComment.callMethod(CTX, "rdbms_id"));
    assertEquals(javaComment.getTitle(), rubyComment.callMethod(CTX, "title").asJavaString());
    assertEquals(javaComment.getContent(), rubyComment.callMethod(CTX, "content").asJavaString());
    assertEquals(EntityToDocumentTranslator.format(javaComment.getPostDate()), EntityToDocumentTranslator.format(rubyComment.callMethod(CTX, "post_date")));
    assertEquals(javaComment.getAuthor().getUsername(), rubyComment.callMethod(CTX, "author_username").asJavaString());
    UserTranslatorTest.assertTranslated(javaComment.getAuthor(), rubyComment.callMethod(CTX, "author"));
  }

  @Test
  @Transactional
  public void testTranslate() {
    try {
      Comment javaComment = CommentTest.getTestComment();
      CommentTranslator translator = new CommentTranslator();
      IRubyObject rubyComment = translator.translate(javaComment);
      assertTranslated(javaComment, rubyComment);
    } catch (TranslationException te) {
      fail(te.getMessage());
    }
  }
}
