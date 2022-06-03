package application.cells;

import java.util.logging.Level;
import java.util.logging.Logger;

import figures.Figure;
import figures.enums.FigureType;
import javafx.scene.image.Image;
import logger.LoggerFactory;
import utils.IconFactory;

/**
 * Icon Sub-Factory dedicated to Figure Types icons.
 * Suitable for {@link FigureCellController}s and
 * {@link FigureTypeCellController}s
 * @author davidroussel
 */
public class FigureIconsFactory
{
	/**
	 * Circle figure icon
	 */
	private static Image circleIcon = IconFactory.getIcon("Circle");

	/**
	 * Ellipse figure icon
	 */
	private static Image ellipseIcon = IconFactory.getIcon("Ellipse");

	/**
	 * Rectangle figure icon
	 */
	private static Image rectangleIcon = IconFactory.getIcon("Rectangle");

	/**
	 * Rounded Rectangle figure icon
	 */
	private static Image roundedRectangleIcon = IconFactory.getIcon("Rounded_Rectangle");

	/**
	 * Polygon figure icon
	 */
	private static Image polygonIcon = IconFactory.getIcon("Polygon");

	/**
	 * Ngon figure icon
	 */
	private static Image nGonIcon = IconFactory.getIcon("Ngon");

	/**
	 * Star figure icon
	 */
	private static Image starIcon = IconFactory.getIcon("Star");

	/**
	 * Logger to use
	 */
	private static Logger logger = LoggerFactory.getParentLogger(FigureIconsFactory.class,
	                                                             IconFactory.getLogger(),
	                                                             (IconFactory.getLogger() == null ?
	                                                              Level.INFO : null)); // null level to inherits parent logger's level

	/**
	 * Retrieve icon from Figure instance
	 * @param figure the figure to retrieve icon for
	 * @return the corresponding icon image
	 * @throws NullPointerException if provided figure is null and {@link FigureType}
	 * can't be determined
	 */
	public static Image getIconFromInstance(Figure figure)
	{
		if (figure == null)
		{
			String message = "null instance";
			logger.severe(message);
			throw new NullPointerException(message);
		}

		return getIconFromType(FigureType.fromFigure(figure));
	}

	/**
	 * Retrieve icon image from figure Type
	 * @param type the type of figure
	 * @return the corresponding icon image
	 * @throws IllegalArgumentException if type is out of {@link FigureType} enum values
	 */
	public static Image getIconFromType(FigureType type)
	{
		switch (type)
		{
			case CIRCLE:
			{
				return circleIcon;
			}
			case ELLIPSE:
			{
				return ellipseIcon;
			}
			case RECTANGLE:
			{
				return rectangleIcon;
			}
			case ROUNDED_RECTANGLE:
			{
				return roundedRectangleIcon;
			}
			case POLYGON:
			{
				return polygonIcon;
			}
			case NGON:
			{
				return nGonIcon;
			}
			case STAR:
			{
				return starIcon;
			}
			default:
				String message = "Unexpected value: " + type.toString();
				logger.severe(message);
				throw new IllegalArgumentException(message);
		}
	}
}
