package etl.translators;

import java.io.InputStream;
import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import etl.ModelToDocumentTranslator;


/**
 * Base super class for all Translator test classes.
 *
 * @author bploetz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
public abstract class TranslatorTest {

  private static final Logger logger = Logger.getLogger(TranslatorTest.class.getName());

  @Autowired
  private SessionFactory sessionFactory;


  @After
  public void tearDown() {
    Ruby ruby = Ruby.getGlobalRuntime();
    // This is excessive to parse this every time, but the JRuby runtime
    // isn't set until the etl ModelToDocumentTranslator is loaded. *shrug*
    ruby.runNormally(ruby.parseEval(TranslatorTest.getFileContents("mongo_dropper.rb"), "mongo_dropper.rb", ruby.getCurrentContext().getCurrentScope(), 0));
    IRubyObject mongoDbDropper = ruby.evalScriptlet("MongoDBDropper.new");
    mongoDbDropper.callMethod(ruby.getCurrentContext(), "drop", JavaUtil.convertJavaToRuby(ruby, ModelToDocumentTranslator.getMongoMapperFileContents()));
  }

  private static String getFileContents(String filename) {
    try {
      InputStream stream = TranslatorTest.class.getResourceAsStream(filename);
      byte b[] = new byte[stream.available()];
      stream.read(b);
      stream.close();
      String str = new String(b);
      return str;
    } catch (Exception ex) {
      logger.severe(ex.getMessage());
    }
    return null;
  }
}
