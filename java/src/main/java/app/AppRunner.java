package app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Utility for running stand alone applications managed by Spring.
 *
 * @author bploetz
 */
public class AppRunner {

  protected final String applicationBeanName;

  public AppRunner(final String[] args) {
    applicationBeanName = args[0];
  }

  public void runApplication() {
    final ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    final App app = appContext.getBean(applicationBeanName, App.class);
    app.run();
  }

  public static void main(final String[] args) {
    if (args.length < 1)
      throw new RuntimeException("Must pass the name of the application bean to run");

    AppRunner runner = new AppRunner(args);
    runner.runApplication();
  }
}
