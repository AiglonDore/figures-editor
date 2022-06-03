package application.cells;

import figures.Figure;
import figures.enums.FigureType;

/**
 * Controller for cells representing {@link FigureType}s
 * @author davidroussel
 */
public class FigureCellController extends AbstractCustomCellController<Figure>
{
	/**
	 * Set image in {@link #iconView} depending on the provided figure
	 * @param figure the figure to provide icon to
	 */
	@Override
	public void setIcon(Figure figure)
	{
		setImage(FigureIconsFactory.getIconFromInstance(figure));
	}
}
