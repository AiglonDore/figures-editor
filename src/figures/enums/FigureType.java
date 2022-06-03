package figures.enums;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import figures.Circle;
import figures.Drawing;
import figures.Figure;
import history.HistoryManager;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import tools.creation.AbstractCreationTool;
import tools.creation.RectangularShapeCreationTool;

/**
 * Enum type for all different kind of figures.
 * This enum also contains factory methods to:
 * <ul>
 * 	<li>build a new figure with {@link #getFigure(Color, Color, LineType, double, Logger, double, double)}</li>
 * 	<li>build a new CreationListener for a kinfd of figure with {@link #getCreationTool(Pane, Drawing, HistoryManager, Label, HistoryManager, Logger)}</li>
 * </ul>
 * @author davidroussel
 */
public enum FigureType
{
	/**
	 * Circle figure type
	 */
	CIRCLE,
	/**
	 * Ellipse figure type
	 */
	ELLIPSE,
	/**
	 * Rectangle figure type
	 */
	RECTANGLE,
	/**
	 * Rounded rectangle figure type
	 */
	ROUNDED_RECTANGLE,
	/**
	 * Poygon figure type
	 */
	POLYGON,
	/**
	 * Regular polygon figure type
	 */
	NGON,
	/**
	 * Star figure type
	 */
	STAR;

	/**
	 * Maximum number of figure types (to be customised when figures are added)
	 */
	public final static int NbFigureTypes = 7;

	/**
	 * Get an instance of the {@link Figure} corresponding to this enum's
	 * value
	 * @param fillColor the fill color to set on the new figure
	 * @param edgeColor the edge color to set on the new figure
	 * @param lineType the line type to set on the new figure
	 * @param lineWidth the line width to set on the new figure
	 * @param parentLogger the parent logger to set on the new figure (parent
	 * logger can also be used to issue debug messages in this method if needed)
	 * @param x the initial x coordinate where to create the new figure
	 * @param y the initial y coordinate where to create the new figure
	 * @return a new instance of Figure's children class corresponding to this
	 * enum's value
	 * @throws AssertionError if this enum has unexpected value
	 */
	public Figure getFigure(Color fillColor,
	                        Color edgeColor,
	                        LineType lineType,
	                        double lineWidth,
	                        Logger parentLogger,
	                        double x,
	                        double y)
	    throws AssertionError
	{
		switch (this)
		{
			case CIRCLE:
				return new Circle(fillColor,
				                  edgeColor,
				                  lineType,
				                  lineWidth,
				                  parentLogger,
				                  x,
				                  y);
			case ELLIPSE:
				// TODO FigureType#getFigure ELLIPSE case ...
				parentLogger.severe(toString() + " case not yet implemented");
				return null;
			case RECTANGLE:
				// TODO FigureType#getFigure RECTANGLE case ...
				parentLogger.severe(toString() + " case not yet implemented");
				return null;
			case ROUNDED_RECTANGLE:
				// TODO FigureType#getFigure ROUNDED_RECTANGLE case ...
				parentLogger.severe(toString() + " case not yet implemented");
				return null;
			case POLYGON:
				// TODO FigureType#getFigure POLYGON case ...
				parentLogger.severe(toString() + " case not yet implemented");
				return null;
			case NGON:
				// TODO FigureType#getFigure NGON case ...
				parentLogger.severe(toString() + " case not yet implemented");
				return null;
			case STAR:
				// TODO FigureType#getFigure STAR case ...
				parentLogger.severe(toString() + " case not yet implemented");
				return null;
		}

		String message = "Unexpected value " + toString();
		parentLogger.severe(message);
		throw new AssertionError(message);
	}

	/**
	 * Get the Creation Tool for this particular type of Figure
	 * @param rootPane The {@link Pane} to draw in
	 * @param model the {@link Drawing} model containing figures
	 * @param history the {@link HistoryManager} for Undos/Redos
	 * @param tipLabel The tip {@link Label} to show in UI
	 * @param manager the history manager to provide to the returned {@link RectangularShapeCreationTool}
	 * in order to record current state before adding new Figure.
	 * @param parentLogger the parent logger to set on the new figure (parent
	 * logger can also be used to issue debug messages in this method if needed)
	 * @return a new {@link RectangularShapeCreationTool} instance adapted to this kind of Figure
	 * @throws AssertionError Whenever the value of this enum is unknown
	 */
	public AbstractCreationTool getCreationTool(Pane rootPane,
	                                            Drawing model,
	                                            Label tipLabel,
	                                            HistoryManager<Figure> manager,
	                                            Logger parentLogger)
		    throws AssertionError
	{
		switch (this)
		{
			case CIRCLE:
			case ELLIPSE:
			case RECTANGLE:
				/*
				 * Circles, Ellipses and Rectangle all require 2 steps creation
				 * tool:
				 * 	- 1 Click to initiate figure of size 0 at clicked point
				 * 	- 2 Drag mouse to set figure's size
				 * 	- and Release to end figure
				 */
				return new RectangularShapeCreationTool(rootPane,
				                                        model,
				                                        tipLabel,
				                                        manager,
				                                        parentLogger);
			case ROUNDED_RECTANGLE:
				// TODO FigureType#getCreationTool ROUNDED_RECTANGLE case ...
				parentLogger.severe(toString() + " case not yet implemented");
				return null;
			case POLYGON:
				// TODO FigureType#getCreationTool POLYGON case ...
				parentLogger.severe(toString() + " case not yet implemented");
				return null;
			case NGON:
				// TODO FigureType#getCreationTool NGON case ...
				parentLogger.severe(toString() + " case not yet implemented");
				return null;
			case STAR:
				// TODO FigureType#getCreationTool STAR case ...
				parentLogger.severe(toString() + " case not yet implemented");
				return null;
		}

		String message = "Unexpected value " + toString();
		parentLogger.severe(message);
		throw new AssertionError(message);
	}

	/*
	 * Note: Enum.class provides default final hashCode and equals methods,
	 * so it's no use trying to overload them
	 */

	/**
	 * Représentation sous forme de chaine de caractères
	 * @return une chaine de caractère représentant la valeur de cet enum
	 * @throws AssertionError if this value is outside enum values
	 */
	@Override
	public String toString() throws AssertionError
	{
		switch (this)
		{
			case CIRCLE:
				return new String("Circle");
			case ELLIPSE:
				return new String("Ellipse");
			case RECTANGLE:
				return new String("Rectangle");
			case ROUNDED_RECTANGLE:
				return new String("Rounded Rectangle");
			case POLYGON:
				return new String("Polygon");
			case NGON:
				return new String("Ngon");
			case STAR:
				return new String("Star");
		}

		throw new AssertionError(getClass().getSimpleName()
		    + ".toString() unknown assertion: " + this);
	}

//	/**
//	 * Converts index to FigureType
//	 * @param i the index to convert in Figuretype
//	 * @return the type of figure corresponding to this index
//	 * @throws AssertionError if the index is outside this enum values
//	 */
//	public static FigureType fromInteger(int i) throws AssertionError
//	{
//		switch (i)
//		{
//			case 0:
//				return CIRCLE;
//			case 1:
//				return ELLIPSE;
//			case 2:
//				return RECTANGLE;
//			case 3:
//				return ROUNDED_RECTANGLE;
//			case 4:
//				return POLYGON;
//			case 5:
//				return NGON;
//			case 6:
//				return STAR;
//			default:
//				throw new AssertionError(Signature
//				    .getClassName(FigureType.class) + "::"
//				    + Signature.getStaticMethodName() + " unknown index: " + i);
//		}
//	}

	/**
	 * Converts Figure to FigureType
	 * @param f the figure to investigate
	 * @return the type of figure corresponding to provided figure
	 * @throws AssertionError if {@link FigureType} can't be determined from
	 * provided {@link Figure}
	 */
	public static FigureType fromFigure(Figure f)
	{
		Class<? extends Figure> type = f.getClass();

		if (type == Circle.class)
		{
			return CIRCLE;
		}

		/*
		 * TODO FigureType#fromFigure add all other figures types
		 * 	- Ellipse.class				--> ELLIPSE
		 * 	- Rectangle.class			--> RECTANGLE
		 * 	- RoundedRectangle.class	--> ROUNDED_RECTANGLE
		 * 	- Polygon.class				--> POLYGON
		 * 	- NGon.class				--> NGON
		 * 	- Star.class				--> STAR
		 */
		throw new AssertionError(FigureType.class.getSimpleName()
		    + ".fromFigure(" + f.getClass().getSimpleName()
		    + ") unknown Figure class: " + type);
	}

	/**
	 * Creates a collection of all possible Figure types.
	 * Can be used to fill a {@link javafx.scene.control.ComboBox}
	 * @return a collection of all possible Figure types
	 * @see application.Controller#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	public static Collection<FigureType> all()
	{
		Collection<FigureType> list = new ArrayList<>();
		list.add(CIRCLE);
		list.add(ELLIPSE);
		list.add(RECTANGLE);
		list.add(ROUNDED_RECTANGLE);
		list.add(POLYGON);
		list.add(NGON);
		list.add(STAR);
		return list;
	}
}
