package model;

import static org.junit.Assert.*;

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
public class BlogTest {

  private static int BLOG_SUFFIX = 1;

  @Autowired
  private SessionFactory sessionFactory;

  private Session session;

  public static Blog getTestBlog() {
    return new Blog("Test-" + BLOG_SUFFIX++);
  }

  @Before
  public void setUp() {
    this.session = sessionFactory.getCurrentSession();
  }

  @Test
  @Transactional
  public void testCreate() {
    Blog blog = getTestBlog();
    this.session.save(blog);
    this.session.flush();
    this.session.clear();
    Blog createdBlog = (Blog)this.session.createQuery("from model.Blog blog where blog.title=?")
    .setString(0, blog.getTitle())
    .uniqueResult();
    assertNotNull(createdBlog);
    assertEquals(createdBlog, blog);
  }

  @Test
  @Transactional
  public void testUpdate() {
    Blog blog = getTestBlog();
    this.session.save(blog);
    this.session.flush();
    this.session.clear();
    Blog createdBlog = (Blog)this.session.createQuery("from model.Blog blog where blog.title=?")
    .setString(0, blog.getTitle())
    .uniqueResult();
    createdBlog.setTitle("different");
    this.session.update(createdBlog);
    this.session.flush();
    this.session.clear();
    Blog updatedBlog = (Blog)this.session.createQuery("from model.Blog blog where blog.title=?")
    .setString(0, "different")
    .uniqueResult();
    assertNotNull(updatedBlog);
    assertEquals(updatedBlog, createdBlog);
  }

  @Test
  @Transactional
  public void testDelete() {
    Blog blog = getTestBlog();
    this.session.save(blog);
    this.session.flush();
    this.session.clear();
    Blog createdBlog = (Blog)this.session.createQuery("from model.Blog blog where blog.title=?")
    .setString(0, blog.getTitle())
    .uniqueResult();
    this.session.delete(createdBlog);
    this.session.flush();
    this.session.clear();
    Blog deletedBlog = (Blog)this.session.createQuery("from model.Blog blog where blog.title=?")
    .setString(0, blog.getTitle())
    .uniqueResult();
    assertNull(deletedBlog);
  }

  @Test
  @Transactional
  public void testAddPost() {
    Blog blog = getTestBlog();
    blog.getPosts().add(PostTest.getTestPost(blog));
    this.session.save(blog);
    this.session.flush();
    this.session.clear();
    Blog createdBlog = (Blog)this.session.createQuery("from model.Blog blog where blog.title=?")
    .setString(0, blog.getTitle())
    .uniqueResult();
    assertNotNull(createdBlog);
    assertEquals(createdBlog.getPosts().size(), 1);
  }
}
