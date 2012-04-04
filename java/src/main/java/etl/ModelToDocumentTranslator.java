package etl;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

import model.Model;


/**
 * A ModelToDocumentTranslator is a class which translates model.Model objects
 * to Ruby MongoDB document objects. Subclasses implement the translate()
 * method to provide the specific translation logic for each entity.
 *
 * @author bploetz
 */
public abstract class ModelToDocumentTranslator {

  protected static final String JAVA_UTC_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss Z";
  protected static final String RUBY_UTC_TIME_FORMAT = "%Y-%m-%d %H:%M:%S %z";
  protected static final Ruby RUNTIME = Ruby.getGlobalRuntime();
  protected static final ThreadContext CTX = RUNTIME.getCurrentContext();

  static {
    try {
      RUNTIME.getLoadService().init(new ArrayList<String>());
      RUNTIME.runNormally(RUNTIME.parseEval(ModelToDocumentTranslator.getInitFileContents(), "init.rb", CTX.getCurrentScope(), 0));
      IRubyObject loader = RUNTIME.evalScriptlet("PropertyLoader.new");
      loader.callMethod(CTX, "loadProperties", JavaUtil.convertJavaToRuby(RUNTIME, ModelToDocumentTranslator.getMongoMapperFileContents()));
    } catch (Exception e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  /**
   * Method which subclasses must implement to translate a Model object
   * to it's associated Ruby Document object counterpart.
   * 
   * @param model the Model object to translate
   * @return the translated Ruby Document object
   * @throws TranslationException if an error occurs while translating
   */
  public abstract IRubyObject translate(Model model) throws TranslationException;

  /**
   * Converts a Java Date to it's Ruby representation.
   */
  public static IRubyObject convertToRuby(Date javaDate) {
    if (javaDate == null) {
      return RUNTIME.evalScriptlet("nil");
    }
    SimpleDateFormat formatter = new SimpleDateFormat(JAVA_UTC_TIME_FORMAT);
    formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    String formattedTime = formatter.format(javaDate);
    RUNTIME.evalScriptlet("require 'time'");
    return RUNTIME.evalScriptlet("Time.parse(\"" + formattedTime + "\").utc");
  }

  /**
   * Formats a Java Date as JAVA_UTC_TIME_FORMAT
   */
  public static String format(Date javaDate) {
    if (javaDate == null) {
      return "";
    }
    SimpleDateFormat formatter = new SimpleDateFormat(JAVA_UTC_TIME_FORMAT);
    formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    return formatter.format(javaDate);
  }

  /**
   * Converts a Ruby Date to it's Java representation.
   */
  public static Date convertToJava(IRubyObject rubyDate) {
    if (rubyDate.isNil()) {
      return null;
    }
    String formattedTime = rubyDate.callMethod(CTX, "strftime", JavaUtil.convertJavaToRuby(rubyDate.getRuntime(), RUBY_UTC_TIME_FORMAT)).asJavaString();
    SimpleDateFormat formatter = new SimpleDateFormat(JAVA_UTC_TIME_FORMAT);
    formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    try {
      return formatter.parse(formattedTime);
    } catch (ParseException pe) {
      return null;
    }
  }

  /**
   * Formats a Ruby Date as RUBY_UTC_TIME_FORMAT
   */
  public static String format(IRubyObject rubyDate) {
    if (rubyDate == null || rubyDate.isNil()) {
      return "";
    }
    return rubyDate.callMethod(CTX, "strftime", JavaUtil.convertJavaToRuby(rubyDate.getRuntime(), RUBY_UTC_TIME_FORMAT)).asJavaString();
  }

  /**
   * Gets the contents of init.rb as a String
   */
  private static String getInitFileContents() {
    return ModelToDocumentTranslator.getFileContents("init.rb");
  }

  /**
   * Gets the contents of mongo_mapper.yml as a String
   */
  public static String getMongoMapperFileContents() {
    return ModelToDocumentTranslator.getFileContents("mongo_mapper.yml");
  }

  private static String getFileContents(String filename) {
    try {
      InputStream stream = ModelToDocumentTranslator.class.getResourceAsStream(filename);
      if (stream == null) {
        stream = ModelToDocumentTranslator.class.getClassLoader().getResource(filename).openStream();
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
