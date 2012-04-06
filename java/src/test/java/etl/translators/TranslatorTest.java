package etl.translators;

import java.io.InputStream;
import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import etl.EntityToDocumentTranslator;


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

  protected static final Ruby RUNTIME = Ruby.getGlobalRuntime();
  protected static final ThreadContext CTX = RUNTIME.getCurrentContext();


  @After
  public void tearDown() {
    // This is excessive to parse this every time, but the JRuby runtime
    // isn't set until the etl ModelToDocumentTranslator is loaded. *shrug*
    RUNTIME.runNormally(RUNTIME.parseEval(TranslatorTest.getFileContents("mongo_dropper.rb"), "mongo_dropper.rb", CTX.getCurrentScope(), 0));
    IRubyObject mongoDbDropper = RUNTIME.evalScriptlet("MongoDBDropper.new");
    mongoDbDropper.callMethod(CTX, "drop", JavaUtil.convertJavaToRuby(RUNTIME, EntityToDocumentTranslator.getMongoMapperFileContents()));
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
