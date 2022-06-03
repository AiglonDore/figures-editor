package figures.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

import figures.Figure;

/**
 * A Composite {@link FigureFilter} composed of multiple filters.
 * The {@link #test(Figure)} method of this composite filter returns true if
 * all filters also returned true.
 * @author davidroussel
 */
public class CompositeFigureFilter implements Predicate<Figure>, Collection<FigureFilter<?>>
{
	/**
	 * The list of filters to check during {@link #test(Figure)}
	 */
	private ArrayList<FigureFilter<?>> filters;

	/**
	 * Boolean flag indicating if all filters in {@link #filters} should return
	 * true for the {@link #test(Figure)} method to return true or only one
	 * of the filters in {@link #filters}.
	 * @see
	 */
	private boolean exclusive;

	/**
	 * Constructor with exclusive flag.
	 * Creates an empty collection of filters
	 */
	public CompositeFigureFilter(boolean exclusive)
	{
		this.exclusive = exclusive;
		filters = new ArrayList<>();
	}

	/**
	 * Predicate verified by this filter: tests all internal filters
	 * @param figure the figure to test
	 * @return When {@link #exclusive} is true, returns true if ALL filters in
	 * {@link #filters} returned true. When {@link #exclusive} is false, returns
	 * true if at least one of the filters in {@link #filters} returned true.
	 * Always returns true when {@link #filters} is empty.
	 */
	@Override
	public boolean test(Figure figure)
	{
		if (filters.isEmpty())
		{
			return true;
		}
		if (exclusive)
		{
			return testExclusive(figure);
		}
		else
		{
			return testInclusive(figure);
		}
	}

	/**
	 * Predicate verified by this filter when {@link #exclusive} is true
	 * @param figure the figure to test
	 * @return true if ALL of the filters in this collection returned true,
	 * false otherwise.
	 */
	private boolean testExclusive(Figure figure)
	{
		for (FigureFilter<?> filter : filters)
		{
			if (!filter.test(figure))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Predicate verified by this filter when {@link #exclusive} is false
	 * @param figure the figure to test
	 * @return true if at least one of the filters in this collection returned true,
	 * false otherwise.
	 */
	private boolean testInclusive(Figure figure)
	{
		boolean result = false;
		for (FigureFilter<?> filter : filters)
		{
			if (filter.test(figure))
			{
				result|=true;
			}
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
	public Iterator<FigureFilter<?>> iterator()
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
	 * @throw {@link NullPointerException} if the provided filter is null
	 */
	@Override
	public boolean add(FigureFilter<?> filter)
	{
		if (filter == null)
		{
			throw new NullPointerException("null provided filter");
		}

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
	 * @param <T> the Type of element to search in {@link #filters}
	 * @param element the filtering element used to find the corresponding
	 * filter in {@link #filters}
	 * @return true if such a filter has been found and removed, false
	 * otherwise.
	 */
	public <T> boolean removeFilterWith(T element)
	{
		for (FigureFilter<?> filter : filters)
		{
			if (element.equals(filter.getElement()))
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
	public boolean addAll(Collection<? extends FigureFilter<?>> c)
	{
		boolean added = false;
		for (FigureFilter<?> ff : c)
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
		for (FigureFilter<?> ff : filters)
		{
			sb.append(ff.toString() + "\n");
		}
		return sb.toString();
	}

	/**
	 * HashCode of this collection of filters implemented as the sum of its
	 * elements hashCode (as the order of elements in this collection does not
	 * matter)
	 * @return the sum of hashCodes of its elements
	 */
	@Override
	public int hashCode()
	{
		int hash = 0;
		for (FigureFilter<?> filter : filters)
		{
			hash += filter.hashCode();
		}
		return hash;
	}

	/**
	 * Equality test with another object
	 * @param obj the object to test for equality
	 * @return true if the other object is a {@link CompositeFigureFilter}
	 * containing the same elements (although maybe not in the same order)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (obj == this)
		{
			return true;
		}

		if (!(obj instanceof CompositeFigureFilter))
		{
			return false;
		}

		CompositeFigureFilter other = (CompositeFigureFilter) obj;
		return containsAll(other) && other.containsAll(this);
	}
}
