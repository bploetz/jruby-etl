package app;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.jruby.embed.ScriptingContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import model.*;


/**
 * Coordinates the ETL process of various Model objects
 *
 * @author bploetz
 */
public class ETLManager implements App {

  private final Logger logger = Logger.getLogger(ETLManager.class.getName());

  private static final ScriptingContainer CONTAINER = new ScriptingContainer();

  @Autowired
  private SessionFactory sessionFactory;


  static {
    try {
      CONTAINER.runScriptlet(ETLManager.getInitFileContents());
      Object propertyLoader = CONTAINER.runScriptlet("PropertyLoader.new");
      CONTAINER.callMethod(propertyLoader, "loadProperties", ETLManager.getMongoMapperFileContents(), Object.class);
    } catch (Exception e) {
      throw new ExceptionInInitializerError(e);
    }
  }


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
        try {
          CONTAINER.callMethod(CONTAINER.runScriptlet("UserTranslator.new"), "translate", user);
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
        try {
          CONTAINER.callMethod(CONTAINER.runScriptlet("BlogTranslator.new"), "translate", blog);
        } catch (Exception e) {
          logger.log(Level.SEVERE, "Error translating Blog (id=" + blog.getId() + ")", e);
        }
      }
    } catch (Exception ex) {
      logger.log(Level.SEVERE, "Unexpected error", ex);
    }
  }

  /**
   * Gets the contents of init.rb as a String
   */
  private static String getInitFileContents() {
    return ETLManager.getFileContents("init.rb");
  }

  /**
   * Gets the contents of mongo_mapper.yml as a String
   */
  private static String getMongoMapperFileContents() {
    return ETLManager.getFileContents("mongo_mapper.yml");
  }

  private static String getFileContents(String filename) {
    try {
      InputStream stream = ETLManager.class.getResourceAsStream(filename);
      if (stream == null) {
        stream = ETLManager.class.getClassLoader().getResource(filename).openStream();
      }
      if (stream == null) {
        throw new RuntimeException("Cannot find file in classpath: " + filename);
      }
      byte b[] = new byte[stream.available()];
      stream.read(b);
      stream.close();
      String str = new String(b);
      return str;
    } catch (Exception ex) {
      throw new RuntimeException("Couldn't read file " + filename, ex);
    }
  }
}
