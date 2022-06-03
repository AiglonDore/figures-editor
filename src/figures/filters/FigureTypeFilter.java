package figures.filters;

import figures.Figure;
import figures.enums.FigureType;

/**
 * Filter filtering figures based on their {@link FigureType}
 * @author davidroussel
 */
public class FigureTypeFilter extends FigureFilter<FigureType>
{
	/**
	 * Valued constructor
	 * @param element the type of figure to filter with
	 */
	public FigureTypeFilter(FigureType element)
	{
		super(element);
	}

	/**
	 * Predicate assessment
	 * @param f the figure to test
	 * @return true if the provided figure is of the same type as
	 * {@link FigureFilter#element}
	 * @see java.util.function.Predicate#test(java.lang.Object)
	 */
	@Override
	public boolean test(Figure f)
	{
		return FigureType.fromFigure(f) == element;
	}
}
