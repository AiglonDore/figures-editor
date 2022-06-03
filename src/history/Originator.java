package history;

/**
 * Interface for classes creating and setting up {@link Memento}s containing
 * their internal states
 * @param <E> The type of state to store in {@link Memento}s.
 * @author davidroussel
 */
public interface Originator<E extends Prototype<E>>
{
	/**
	 * Method to create a new {@link Memento} containing the state to save
	 * @return a new {@link Memento} containing the state to save
	 */
	public abstract Memento<E> createMemento();

	/**
	 * Replace the current state with the state contained in the provided
	 * {@link Memento}
	 * @param memento the new state to set.
	 * @post the state contained in the provided memento has replaced the current
	 * state, if and only if the provided memento was not null
	 */
	public abstract void setMemento(Memento<E> memento);
}
