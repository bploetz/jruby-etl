package app;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import model.*;
import etl.translators.*;


/**
 * Coordinates the ETL process of various Model objects
 *
 * @author bploetz
 */
public class ETLManager implements App {

  private final Logger logger = Logger.getLogger(ETLManager.class.getName());

  @Autowired
  private SessionFactory sessionFactory;

  @Transactional
  public void run() {
    translateUsers();
    translateBlogs();
  }

  @Transactional
  public void translateUsers() {
    try {
      Session session = sessionFactory.getCurrentSession();
      Criteria criteria = session.createCriteria(User.class);
      criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
      List<User> users = (List<User>) criteria.list();
      for (User user : users) {
        UserTranslator translator = new UserTranslator();
        try {
          translator.translate(user);
        } catch (Exception e) {
          logger.log(Level.SEVERE, "Error translating User (id=" + user.getId() + ")", e);
        }
      }
    } catch (Exception ex) {
      logger.log(Level.SEVERE, "Unexpected error", ex);
    }
  }

  @Transactional
  public void translateBlogs() {
    try {
      Session session = sessionFactory.getCurrentSession();
      Criteria criteria = session.createCriteria(Blog.class);
      criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
      List<Blog> blogs = (List<Blog>) criteria.list();
      for (Blog blog : blogs) {
        BlogTranslator translator = new BlogTranslator();
        try {
          translator.translate(blog);
        } catch (Exception e) {
          logger.log(Level.SEVERE, "Error translating Blog (id=" + blog.getId() + ")", e);
        }
      }
    } catch (Exception ex) {
      logger.log(Level.SEVERE, "Unexpected error", ex);
    }
  }
}
