package history;

/**
 * Interface for prototypes declaring a public {@link #clone()} method to
 * copy their content. {@ling Cloneable} interface implementation means it's
 * legal to call {@link #clone()} method.
 * @param <E> The type of object returned by the {@link #clone()} method
 * @author davidroussel
 */
public interface Prototype<E> extends Cloneable
{
	/**
	 * Copy creation with distinct instance but same content
	 * @return the object distinct copy
	 */
	public E clone();
}
