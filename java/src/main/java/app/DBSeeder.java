package app;

import java.util.Date;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import model.*;


/**
 * Seeds the RDBMS with test data.
 *
 * @author bploetz
 */
public class DBSeeder implements App {

  private final Logger logger = Logger.getLogger(DBSeeder.class.getName());

  @Autowired
  private SessionFactory sessionFactory;

  @Transactional
  public void run() {
    Session session = sessionFactory.getCurrentSession();
    
    User sam = new User("sam");
    session.save(sam);
    logger.info("Created User '" + sam.getUsername() + "' (id=" + sam.getId() + ")");

    User jane = new User("jane");
    session.save(jane);
    logger.info("Created User '" + jane.getUsername() + "' (id=" + jane.getId() + ")");

    User bill = new User("bill");
    session.save(bill);
    logger.info("Created User '" + bill.getUsername() + "' (id=" + bill.getId() + ")");


    Blog techBlog = new Blog("Technology Blog");
    session.save(techBlog);
    logger.info("Created Blog '" + techBlog.getTitle() + "' (id=" + techBlog.getId() + ")");

    Post techPost1 = new Post(techBlog, "Tech Post #1", "Blah blah blah...", new Date(), sam);
    session.save(techPost1);
    logger.info("Created Post '" + techPost1.getTitle() + "' in Blog '" + techBlog.getTitle() + "' (id=" + techBlog.getId() + ")");

    techPost1.getComments().add(new Comment(techPost1, "I agree!", "You're very smart", new Date(), jane));
    techPost1.getComments().add(new Comment(techPost1, "I disagree!", "You're not very smart", new Date(), bill));
    logger.info("Added two comments to Post '" + techPost1.getTitle() + "' in Blog '" + techBlog.getTitle() + "' (id=" + techBlog.getId() + ")");

    Post techPost2 = new Post(techBlog, "Tech Post #2", "Yadda yadda yadda...", new Date(), jane);
    session.save(techPost2);
    logger.info("Created Post '" + techPost2.getTitle() + "' in Blog '" + techBlog.getTitle() + "' (id=" + techBlog.getId() + ")");

    techPost2.getComments().add(new Comment(techPost2, "Insightful", "You make some good points", new Date(), bill));
    logger.info("Added one comment to Post '" + techPost2.getTitle() + "' in Blog '" + techBlog.getTitle() + "' (id=" + techBlog.getId() + ")");


    Blog businessBlog = new Blog("Business Blog");
    session.save(businessBlog);
    logger.info("Created Blog '" + businessBlog.getTitle() + "' (id=" + businessBlog.getId() + ")");

    Post businessPost1 = new Post(businessBlog, "Business Post #1", "Blah blah blah...", new Date(), jane);
    session.save(businessPost1);
    logger.info("Created Post '" + businessPost1.getTitle() + "' in Blog '" + businessBlog.getTitle() + "' (id=" + businessBlog.getId() + ")");

    businessPost1.getComments().add(new Comment(businessPost1, "Factually incorrect", "Check your facts", new Date(), sam));
    businessPost1.getComments().add(new Comment(businessPost1, "You're wrong sam", "This is correct", new Date(), jane));
    businessPost1.getComments().add(new Comment(businessPost1, "I agree with jane", "She's right", new Date(), bill));
    businessPost1.getComments().add(new Comment(businessPost1, "Ooops, sorry, I was wrong", "My bad guys", new Date(), sam));
    logger.info("Added four comments to Post '" + businessPost1.getTitle() + "' in Blog '" + businessBlog.getTitle() + "' (id=" + businessBlog.getId() + ")");


    Blog entertainmentBlog = new Blog("Entertainment Blog");
    session.save(entertainmentBlog);
    logger.info("Created Blog '" + entertainmentBlog.getTitle() + "' (id=" + entertainmentBlog.getId() + ")");

    Post entertainmentPost1 = new Post(entertainmentBlog, "Entertainment Post #1", "I like movies", new Date(), bill);
    session.save(entertainmentPost1);
    logger.info("Created Post '" + entertainmentPost1.getTitle() + "' in Blog '" + entertainmentBlog.getTitle() + "' (id=" + entertainmentBlog.getId() + ")");

    entertainmentPost1.getComments().add(new Comment(entertainmentPost1, "I like movies too", "I can't wait to see the new action flick", new Date(), sam));
    entertainmentPost1.getComments().add(new Comment(entertainmentPost1, "Me too!", "It's gonna be *awesome*", new Date(), jane));
    logger.info("Added two comments to Post '" + entertainmentPost1.getTitle() + "' in Blog '" + entertainmentBlog.getTitle() + "' (id=" + entertainmentBlog.getId() + ")");
  }
}
