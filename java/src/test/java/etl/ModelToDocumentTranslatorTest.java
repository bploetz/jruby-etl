package etl;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


/**
 * Tests for base methods in ModelToDocumentTranslator.
 *
 * @author bploetz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
public class ModelToDocumentTranslatorTest {

  @Test
  public void testConvertRubyToJava() {
    try {
      String sourceDateString = "2011-10-24 23:45:56 +0000";
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
      formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
      Date javaDate = formatter.parse(sourceDateString);
      String javaDateString = ModelToDocumentTranslator.format(javaDate);
      assertEquals(sourceDateString, javaDateString);
      IRubyObject rubyDate = ModelToDocumentTranslator.convertToRuby(javaDate);
      String rubyDateString = ModelToDocumentTranslator.format(rubyDate);
      assertEquals(rubyDateString, javaDateString);
    } catch (ParseException pe) {
      fail(pe.getMessage());
    }
  }

  @Test
  public void testConvertJavaToRuby() {
    try {
      String sourceDateString = "2011-10-24 23:45:56 +0000";
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
      formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
      Date javaDate = formatter.parse(sourceDateString);
      String javaDateString = ModelToDocumentTranslator.format(javaDate);
      IRubyObject rubyDate = ModelToDocumentTranslator.convertToRuby(javaDate);
      String rubyDateString = ModelToDocumentTranslator.format(rubyDate);
      assertEquals(rubyDateString, javaDateString);
    } catch (ParseException pe) {
      fail(pe.getMessage());
    }
  }
}
