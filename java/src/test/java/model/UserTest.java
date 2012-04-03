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
public class UserTest {

  private static int USER_SUFFIX = 1;

  @Autowired
  private SessionFactory sessionFactory;

  private Session session;

  public static User getTestUser() {
    return new User("user-" + USER_SUFFIX++);
  }

  @Before
  public void setUp() {
    this.session = sessionFactory.getCurrentSession();
  }

  @Test
  @Transactional
  public void testCreate() {
    User user = getTestUser();
    this.session.save(user);
    this.session.flush();
    this.session.clear();
    User createdUser = (User)this.session.createQuery("from model.User user where user.username=?")
    .setString(0, user.getUsername())
    .uniqueResult();
    assertNotNull(createdUser);
    assertEquals(createdUser, user);
  }

  @Test
  @Transactional
  public void testDelete() {
    User user = getTestUser();
    this.session.save(user);
    this.session.flush();
    this.session.clear();
    User createdUser = (User)this.session.createQuery("from model.User user where user.username=?")
    .setString(0, user.getUsername())
    .uniqueResult();
    this.session.delete(createdUser);
    this.session.flush();
    this.session.clear();
    User deletedUser = (User)this.session.createQuery("from model.User user where user.username=?")
    .setString(0, user.getUsername())
    .uniqueResult();
    assertNull(deletedUser);
  }
}
