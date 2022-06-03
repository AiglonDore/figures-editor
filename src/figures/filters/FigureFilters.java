package figures.filters;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import figures.Figure;

/**
 * A Composite {@link Figure} filter composed of a collection of {@link FigureFilter}
 * (all filtering {@link Figure}s based on elements of type T).
 * The predicated is verified if ALL of the filters in the collection
 * returns true.
 * @param <T> The type of objects to be filtered
 * @author davidroussel
 */
public class FigureFilters<T> extends FigureFilter<T> implements Collection<FigureFilter<T>>
{
	/**
	 * filters collection
	 */
	private Collection<FigureFilter<T>> filters;

	/**
	 * Default constructor
	 */
	public FigureFilters()
	{
		filters = new Vector<FigureFilter<T>>();
	}

	/**
	 * Predicate assessment
	 * @param figure the figure to test
	 * @return true if at least one of the filters predicate returned true,
	 * false otherwise.
	 * Always return true when {@link #filters} is empty.
	 * @see figures.filters.FigureFilter#test(figures.Figure)
	 */
	@Override
	public boolean test(Figure figure)
	{
		boolean result = false;
		for (FigureFilter<T> filter : this)
		{
			result |= filter.test(figure);
		}
		return result;
	}

	/**
	 * Filters collection size
	 * @return the size of the collecion of filters
	 */
	@Override
	public int size()
	{
		return filters.size();
	}

	/**
	 * Test for empty filters collection
	 * @return true si la filters collection is empty
	 */
	@Override
	public boolean isEmpty()
	{
		return filters.isEmpty();
	}

	/**
	 * Check if the provided object is part of the collection of filters
	 * @param o the object to search in {@link #filters}
	 * @return true if provided object is part of {@link #filters}
	 */
	@Override
	public boolean contains(Object o)
	{
		return filters.contains(o);
	}

	/**
	 * Iterator over {@link #filters}
	 * @return an iterator over {@link #filters}
	 */
	@Override
	public Iterator<FigureFilter<T>> iterator()
	{
		return filters.iterator();
	}

	/**
	 * Object array conversion
	 * @return an array of {@link Object} containing all filters in {@link #filters}
	 */
	@Override
	public Object[] toArray()
	{
		return filters.toArray();
	}

	/**
	 * Generic Array conversion
	 * @param a a generic array specimen
	 * @return a genetic array of {@link FigureFilters}<T>
	 */
	@Override
	@SuppressWarnings("hiding")
	public <T> T[] toArray(T[] a)
	{
		return filters.toArray(a);
	}

	/**
	 * Adds a new filter to {@link #filters} iff such a {@link FigureFilter}
	 * is not already part of {@link #filters}
	 * @param filter the filter to add
	 * @return true si if the provided filter was not already part of
	 * {@link #filters} and has been added to {@link #filters}
	 */
	@Override
	public boolean add(FigureFilter<T> filter)
	{
		if (!contains(filter))
		{
			return filters.add(filter);
		}
		else
		{
			return false;
		}
	}

	/**
	 * Removes provided object from {@link #filters}
	 * @param o the object to remove from {@link #filters}
	 * @return true if provided object was a {@link FigureFilter} which was part
	 * of {@link #filters} and has been removed from {@link #filters}.
	 */
	@Override
	public boolean remove(Object o)
	{
		return filters.remove(o);
	}

	/**
	 * Remove from this collection any filter with filtering element identical
	 * to provided element
	 * @param element the filtering element used to find the corresponding
	 * filter in {@link #filters}
	 * @return true if such a filter has been found and removed, false
	 * otherwise.
	 */
	public boolean removeFilterWith(T element)
	{
		for (FigureFilter<T> filter : filters)
		{
			if (filter.getElement().equals(element))
			{
				return remove(filter);
			}
		}
		return false;
	}

	/**
	 * Check if all elements in c are part of {@link #filters}
	 * @param c the collection to check
	 * @return true if all elements in c were found in {@link #filters},
	 * false otherwise.
	 */
	@Override
	public boolean containsAll(Collection<?> c)
	{
		return filters.containsAll(c);
	}

	/**
	 * Add a collection of {@link FigureFilter} to {@link #filters}
	 * @param c the collection of {@link FigureFilter} to add
	 * @return true if {@link #filters} has been modified (meaning at least one
	 * element of c has been added to {@link #filters}).
	 */
	@Override
	public boolean addAll(Collection<? extends FigureFilter<T>> c)
	{
		boolean added = false;
		for (FigureFilter<T> ff : c)
		{
			if (!contains(ff))
			{
				added |= add(ff);
			}
		}

		return added;
	}

	/**
	 * Removes all elements contained in the provided collection from
	 * {@link #filters}
	 * @param c the collection to remove from {@link #filters}
	 * @return true if {@link #filters} has been modified by the operation
	 * (Meaning at least one element has been removed from {@link #filters})
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		return filters.removeAll(c);
	}

	/**
	 * Retain in {@link #filters} only elements found in provided collection
	 * @param c collection containing elements to retain in {@link #filters}
	 * @return true if {@link #filters} has been modified by this operation
	 * (Meaning at least one element has been removed from {@link #filters})
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		return filters.retainAll(c);
	}

	/**
	 * Clears {@link #filters}
	 */
	@Override
	public void clear()
	{
		filters.clear();
	}

	/**
	 * String representation of this collection of {@link FigureFilter}s
	 * @return A new String "[<number of filters>]\n<filter1>\n ...<filtern>"
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(getClass().getSimpleName());
		sb.append("[");
		sb.append(filters.size());
		sb.append("]\n");
		for (FigureFilter<T> ff : filters)
		{
			sb.append(ff.toString() + "\n");
		}
		return sb.toString();
	}
}
