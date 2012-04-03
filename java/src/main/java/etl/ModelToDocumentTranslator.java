package etl;

import java.io.InputStream;
import java.util.ArrayList;

import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
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

  public static final Ruby RUNTIME = Ruby.getGlobalRuntime();

  static {
    try {
      // System.setProperty("jruby.base", jrubyHome);
      // System.setProperty("jruby.home", jrubyHome);
      // System.setProperty("jruby.lib", jrubyHome + "\\lib");
      RUNTIME.getLoadService().init(new ArrayList<String>());
      RUNTIME.runNormally(RUNTIME.parseEval(ModelToDocumentTranslator.getInitFileContents(), "init.rb", RUNTIME.getCurrentContext().getCurrentScope(), 0));
      IRubyObject loader = RUNTIME.evalScriptlet("PropertyLoader.new");
      loader.callMethod(RUNTIME.getCurrentContext(), "loadProperties", JavaUtil.convertJavaToRuby(RUNTIME, ModelToDocumentTranslator.getMongoMapperFileContents()));
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
   * Gets the contents of init.rb as a String
   */
  public static String getInitFileContents() {
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
