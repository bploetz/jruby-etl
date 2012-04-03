package etl;


/**
 * Exception thrown when an error occurs translating a model.
 *
 * @author bploetz
 */
public class TranslationException extends Exception {
  private static final long serialVersionUID = 1L;

	public TranslationException(Exception _ex){
		super(_ex);
	}
}
