package history;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A State composed of a list of elements of type E to save in this Memento
 * @param <E> the type of elements in the list to save
 * @author davidroussel
 * @note Elements should implement the {@link Prototype} interface so they can be
 * effectively cloned within the Memento
 */
public class Memento<E extends Prototype<E>>
{
	/**
	 * The list of elements of type E constitutes the state to save
	 */
	private List<E> state;

	/**
	 * Valued constructor
	 * Stores copies of elements in things in the {@link #state}, which is why
	 * elements to store in the Memento needs to be {@link Prototype}s.
	 * @param things the things to store in this memento
	 */
	public Memento(List<E> things)
	{
		this.state = new ArrayList<E>();
		for (E elt : things)
		{
			this.state.add(elt.clone());
		}
	}

	/**
	 * State list accessor
	 * @return The state stored in this Memento
	 */
	public List<E> getState()
	{
		return state;
	}

	/**
	 * The hashcode of this Memento (for comparison purposes)
	 * @return the hash code of the state composing this Memento
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int hash = 1;
		for (E elt : state)
		{
			hash += (prime * hash) + (elt != null ? elt.hashCode() : 0);
		}
		return hash;
	}

	/**
	 * Comparison with another {@link Object}.
	 * Allow to check there is no duplicate states in {@link HistoryManager}.
	 * @param the object to compare with this Memento
	 * @return true if the other object is a Memento containing the same
	 * elements in the same order, false otherwise
	 */
	@Override
	public boolean equals(Object obj)
	{
		// TODO Memento#equals ...
		return false;
	}

	/**
	 * String representation of this object (for debug purposes)
	 * @return a String representing this object; e.g. "[element1, element2, ...]"
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append('[');
		for (Iterator<E> it = state.iterator(); it.hasNext();)
		{
			sb.append(it.next());
			if (it.hasNext())
			{
				sb.append(", ");
			}
		}
		sb.append(']');

		return sb.toString();
	}
}
