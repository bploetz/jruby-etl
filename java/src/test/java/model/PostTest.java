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
public class PostTest {

  private static int POST_SUFFIX = 1;

  @Autowired
  private SessionFactory sessionFactory;

  private Session session;

  public static Post getTestPost() {
    return getTestPost(BlogTest.getTestBlog());
  }

  public static Post getTestPost(Blog blog) {
    return new Post(blog, "Test-" + POST_SUFFIX++, "Blah blah blah...", new Date(), UserTest.getTestUser());
  }

  @Before
  public void setUp() {
    this.session = sessionFactory.getCurrentSession();
  }

  @Test
  @Transactional
  public void testCreate() {
    Post post = getTestPost();
    this.session.save(post);
    this.session.flush();
    this.session.clear();
    Post createdPost = (Post)this.session.createQuery("from model.Post post where post.title=?")
    .setString(0, post.getTitle())
    .uniqueResult();
    assertNotNull(createdPost);
    assertEquals(createdPost, post);
  }

  @Test
  @Transactional
  public void testUpdate() {
    Post post = getTestPost();
    this.session.save(post);
    this.session.flush();
    this.session.clear();
    Post createdPost = (Post)this.session.createQuery("from model.Post post where post.title=?")
    .setString(0, post.getTitle())
    .uniqueResult();
    createdPost.setTitle("different");
    this.session.update(createdPost);
    this.session.flush();
    this.session.clear();
    Post updatedPost = (Post)this.session.createQuery("from model.Post post where post.title=?")
    .setString(0, "different")
    .uniqueResult();
    assertNotNull(updatedPost);
    assertEquals(updatedPost, createdPost);
  }

  @Test
  @Transactional
  public void testDelete() {
    Post post = getTestPost();
    this.session.save(post);
    this.session.flush();
    this.session.clear();
    Post createdPost = (Post)this.session.createQuery("from model.Post post where post.title=?")
    .setString(0, post.getTitle())
    .uniqueResult();
    this.session.delete(createdPost);
    this.session.flush();
    this.session.clear();
    Post deletedPost = (Post)this.session.createQuery("from model.Post post where post.title=?")
    .setString(0, post.getTitle())
    .uniqueResult();
    assertNull(deletedPost);
  }

  @Test
  @Transactional
  public void testAddComment() {
    Post post = getTestPost();
    post.getComments().add(CommentTest.getTestComment(post));
    this.session.save(post);
    this.session.flush();
    this.session.clear();
    Post createdPost = (Post)this.session.createQuery("from model.Post post where post.title=?")
    .setString(0, post.getTitle())
    .uniqueResult();
    assertNotNull(createdPost);
    assertEquals(createdPost.getComments().size(), 1);
  }
}
