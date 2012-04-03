package model;

import static org.junit.Assert.*;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
public class CommentTest {

  private static int COMMENT_SUFFIX = 1;

  @Autowired
  private SessionFactory sessionFactory;

  private Session session;

  public static Comment getTestComment() {
    return getTestComment(PostTest.getTestPost());
  }

  public static Comment getTestComment(Post post) {
    return new Comment(post, "Test-" + COMMENT_SUFFIX++, "Blah blah blah...", new Date(), UserTest.getTestUser());
  }

  @Before
  public void setUp() {
    this.session = sessionFactory.getCurrentSession();
  }

  @Test
  @Transactional
  public void testCreate() {
    Comment comment = getTestComment();
    this.session.save(comment);
    this.session.flush();
    this.session.clear();
    Comment createdComment = (Comment)this.session.createQuery("from model.Comment comment where comment.title=?")
    .setString(0, comment.getTitle())
    .uniqueResult();
    assertNotNull(createdComment);
    assertEquals(createdComment, comment);
  }

  @Test
  @Transactional
  public void testUpdate() {
    Comment comment = getTestComment();
    this.session.save(comment);
    this.session.flush();
    this.session.clear();
    Comment createdComment = (Comment)this.session.createQuery("from model.Comment comment where comment.title=?")
    .setString(0, comment.getTitle())
    .uniqueResult();
    createdComment.setTitle("different");
    this.session.update(createdComment);
    this.session.flush();
    this.session.clear();
    Comment updatedComment = (Comment)this.session.createQuery("from model.Comment comment where comment.title=?")
    .setString(0, "different")
    .uniqueResult();
    assertNotNull(updatedComment);
    assertEquals(updatedComment, createdComment);
  }

  @Test
  @Transactional
  public void testDelete() {
    Comment comment = getTestComment();
    this.session.save(comment);
    this.session.flush();
    this.session.clear();
    Comment createdComment = (Comment)this.session.createQuery("from model.Comment comment where comment.title=?")
    .setString(0, comment.getTitle())
    .uniqueResult();
    this.session.delete(createdComment);
    this.session.flush();
    this.session.clear();
    Comment deletedComment = (Comment)this.session.createQuery("from model.Comment comment where comment.title=?")
    .setString(0, comment.getTitle())
    .uniqueResult();
    assertNull(deletedComment);
  }
}
