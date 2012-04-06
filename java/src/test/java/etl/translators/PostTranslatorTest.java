package etl.translators;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.jruby.RubyArray;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import etl.EntityToDocumentTranslator;
import etl.TranslationException;

import model.Comment;
import model.CommentTest;
import model.Post;
import model.PostTest;


/**
 * Integration test for PostTranslator
 *
 * @author bploetz
 */
public class PostTranslatorTest extends TranslatorTest {

  static final void assertTranslated(Post javaPost, IRubyObject rubyPost) {
    assertEquals(JavaUtil.convertJavaToRuby(RUNTIME, javaPost.getId()), rubyPost.callMethod(CTX, "rdbms_id"));
    assertEquals(javaPost.getTitle(), rubyPost.callMethod(CTX, "title").asJavaString());
    assertEquals(javaPost.getContent(), rubyPost.callMethod(CTX, "content").asJavaString());
    assertEquals(EntityToDocumentTranslator.format(javaPost.getPostDate()), EntityToDocumentTranslator.format(rubyPost.callMethod(CTX, "post_date")));
    assertEquals(javaPost.getAuthor().getUsername(), rubyPost.callMethod(CTX, "author_username").asJavaString());
    UserTranslatorTest.assertTranslated(javaPost.getAuthor(), rubyPost.callMethod(CTX, "author"));

    RubyArray rubyComments = rubyPost.callMethod(CTX, "comments").convertToArray();
    assertEquals(JavaUtil.convertJavaToRuby(RUNTIME, javaPost.getComments().size()), rubyComments.callMethod(CTX, "size"));
    for (Comment javaComment : javaPost.getComments()) {
      boolean found = false;
      Iterator<?> rubyCommentsIterator = rubyComments.iterator();
      while (rubyCommentsIterator.hasNext()) {
        IRubyObject rubyComment = (IRubyObject)rubyCommentsIterator.next();
        if (JavaUtil.convertJavaToRuby(RUNTIME, javaComment.getId()).eql(rubyComment.callMethod(CTX, "rdbms_id"))) {
          found = true;
          break;
        }
        assertEquals(found, true);
      }
    }
  }

  @Test
  @Transactional
  public void testTranslate() {
    try {
      Post javaPost = PostTest.getTestPost();
      Comment testComment1 = CommentTest.getTestComment(javaPost);
      Comment testComment2 = CommentTest.getTestComment(javaPost);
      javaPost.getComments().add(testComment1);
      javaPost.getComments().add(testComment2);
      PostTranslator translator = new PostTranslator();
      IRubyObject rubyPost = translator.translate(javaPost);
      assertTranslated(javaPost, rubyPost);
    } catch (TranslationException te) {
      fail(te.getMessage());
    }
  }
}
