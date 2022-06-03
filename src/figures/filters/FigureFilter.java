package figures.filters;

import java.util.function.Predicate;

import figures.Figure;

/**
 * A Predicate allowing to filter {@link Figure}s based on comparing elements
 * of type T. Either:
 * <ul>
 * 	<li>figures type: {@link figures.enums.FigureType}</li>
 *	<li>figures edge or fill colors : {@link javafx.scene.paint.Color}</li>
 *	<li>figures edge type: {@link figures.enums.LineType}</li>
 * </ul>
 * @param <T> The type of elements to filter {@link Figure}s with (to be specified
 * in concrete sub-classes)
 * @author davidroussel
 */
public abstract class FigureFilter<T> implements Predicate<Figure>
{
	/**
	 * The element of comparison to filter {@link Figure}s with.
	 */
	protected T element;

	/**
	 * Default constructor.
	 */
	public FigureFilter()
	{
		element = null;
	}

	/**
	 * Figure Filter valued constructor
	 * @param element the element of comparison to compare figures with
	 */
	public FigureFilter(T element)
	{
		this.element = element;
	}

	/**
	 * Element of comparison accessor
	 * @return the element of comparison
	 */
	public T getElement()
	{
		return element;
	}

	/**
	 * Predicate assessment
	 * @param f the figure to test
	 * @return true if the element to be compared in figure f is equal to
	 * {@link #element}.
	 * @see java.util.function.Predicate#test(java.lang.Object)
	 */
	@Override
	public abstract boolean test(Figure f);

	/**
	 * Comparison with another object
	 * @return true if object obj is also a {@link FigureFilter} with the same
	 * element (also tested with {@link Object#equals(Object)}) overload).
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return  false;
		}

		if (obj == this)
		{
			return true;
		}

		if (!(obj instanceof FigureFilter<?>))
		{
			return false;
		}

		FigureFilter<?> ff = (FigureFilter<?>) obj;
		if ((ff.element != null) && (element != null))
		{
			if (ff.element.getClass() == element.getClass())
			{
				@SuppressWarnings("unchecked")
				FigureFilter<T> fft = (FigureFilter<T>)ff;
				return element.equals(fft.element);
			}
			else
			{
				if ((element != null) || (ff.element != null))
				{
					return false;
				}
				else
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Hashcode of this filter.
	 * Uses the parent hash code + prime * the hash code of the element
	 * @return the hash code of this filter
	 */
	@Override
	public int hashCode()
	{
		return super.hashCode() + (31 * (element == null ? 0 : element.hashCode()));
	}

	/**
	 * String representation of this filter
	 * @return a new String representing this filter
	 */
	@Override
	public String toString()
	{
		return new String(getClass().getSimpleName() + "<"
		    + (element != null ? element.getClass().getSimpleName() : "null")
		    + ">(" + (element != null ? element.toString() : "") + ")");
	}
}
