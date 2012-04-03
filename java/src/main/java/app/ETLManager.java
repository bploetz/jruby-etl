package app;

import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import model.*;


/**
 * Coordinates the ETL process of various model objects
 *
 * @author bploetz
 */
public class ETLManager implements App {

  private final Logger logger = Logger.getLogger(ETLManager.class.getName());

  @Autowired
  private SessionFactory sessionFactory;

  @Transactional
  public void run() {
    Session session = sessionFactory.getCurrentSession();
  }
}
