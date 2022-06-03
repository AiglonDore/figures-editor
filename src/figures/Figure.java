package figures;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import figures.enums.LineType;
import history.Prototype;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import logger.LoggerFactory;
import utils.ColorFactory;

/**
 * Abstract Base class for all figures.
 * Implements:
 * <ul>
 * <li>{@link Prototype}<Figure> in order to publicly clone itself (useful in
 * undo / redo transactions)</li>
 * </ul>
 * Contains:
 * <ul>
 * 	<li>a {@link #root} {@link Group} containing both the {@link #shape} to draw
 * 	and the {@link #selectionRectangle} indicating if this figure is selected.
 * 	The {@link #root} can be added to the drawing {@link Pane}</li>
 * 	<li>a {@link #shape} {@link Shape} representing this figure to draw</li>
 * 	<li>a {@link #selectionRectangle} {@link Rectangle} representing this
 * 	figure's selection state</li>
 * 	<li>a optional {@link #fillColor} {@link Color} to apply on the
 * 	{@link #shape} (if {@link #fillColor} is empty, then {@link #edgeColor} shold
 * 	not be empty)</li>
 * 	<li>an optional {@link #edgeColor} {@link Color} to apply on the
 * 	{@link #shape} (if {@link #edgeColor} is empty then {@link #fillColor} should
 * 	not be empty)</li>
 * 	<li>a {@link #lineType} {@link LineType} to select the kind of edge to apply
 * 	on the {@link #shape}</li>
 * 	<li>a {@link #lineWidth} to select the edge width of the {@link #shape}</li>
 * 	<li>a {@link #selected} flag indicating this figure is selected (also
 * 	triggering the addition of {@link #selectionRectangle} to the
 * 	{@link #root})</li>
 * 	<li>a {@link #logger} {@link Logger} to issue messages</li>
 * 	<li>a {@link #threshold} used to compare numeric values or distances in
 * 	sub-classes {@link #equals(Figure)} methods</li>
 * </ul>
 * @author davidroussel
 */
public abstract class Figure implements Prototype<Figure>
{
	/**
	 * Our figures are represented in JavaFX {@link javafx.scene.Scene} graph by
	 * {@link #shape} and also (whenever a figure
	 * is selected) by {@link #selectionRectangle} indicating this shape is
	 * currently selected. Both of them need to stick togeteher through all
	 * transformations (translate, scale, rotate). The simplest way do to it is
	 * to group both of them in a {@link Group} node that can be added to JavaFX
	 * Scene graph. When a figure is selected {@link #selectionRectangle} is
	 * added to the group, and removed when the figure is not selected anymore.
	 */
	protected Group root;

	/**
	 * The shape to draw for this figure (contained in {@link #root})
	 * @implNote All subclasses must enforce non null {@link #shape}
	 * @implNote {@link Shape} and all other {@link javafx.scene.Node}s do not
	 * implement {@link Object#equals(Object)} and {@link Object#hashCode()}
	 * methods and are therefore non comparable in terms of equality
	 */
	protected Shape shape;

	/**
	 * The JavaFX {@link Rectangle} representing the current selection state of
	 * this figure (contained in {@link #root}).
	 */
	protected Rectangle selectionRectangle;

	/**
	 * The optional fill color for this figure.
	 * If this figure has no fill Color then this optional is empty.
	 * However if this fill Color is empty then the {@link #edgeColor} shall not
	 * be empty as well (and vice versa).
	 */
	protected Optional<Color> fillColor;

	/**
	 * The optional edge color for this figure.
	 * If this figure has no edge Color then this optional is empty.
	 * However if this edge Color is empty then the {@link #fillColor} shall not
	 * be empty as well (and vice versa)
	 */
	protected Optional<Color> edgeColor;

	/**
	 * The line type of the edge of this figure {@link LineType#SOLID},
	 * {@link LineType#DASHED} or {@link LineType#NONE}
	 */
	protected LineType lineType;

	/**
	 * The line width of the edge of this figure
	 */
	protected double lineWidth;

	/**
	 * Instance number of this figure.
	 * To be set in every sub-classes where the first instance of each figure
	 * is numbered 0 and subsequent instances uses growing numbers
	 */
	protected int instanceNumber;

	/**
	 * Flag indicating this figure is currently selected
	 */
	protected boolean selected;

	/**
	 * Logger to display messages
	 */
	protected Logger logger;

	/**
	 * Minimum threshold to compare figures attributes such as distances, width,
	 * heights, etc. to be used in {@link #equals(Figure)} methods of sub-classes.
	 */
	public final static double threshold = 1e-6;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Valued constructor (to be called in subclasses)
	 * @param fillColor the fill color (or null if there is no fill color).
	 * The fill color set in this figure shall be set from {@link ColorFactory}.
	 * @param edgeColor the edge color (or null if there is no edge color)
	 * The edge color set in this figure shall be set from {@link ColorFactory}.
	 * @param lineType line type (Either {@link LineType#SOLID},
	 * {@link LineType#DASHED} or {@link LineType#NONE}).
	 * @param lineWidth line width of this figure.
	 * @param parentLogger a parent logger used to initialize the current logger
	 * @throws IllegalStateException if we try to set both fillColor and
	 * edgecolor as nulls
	 */
	protected Figure(Color fillColor,
	                 Color edgeColor,
	                 LineType lineType,
	                 double lineWidth,
	                 Logger parentLogger)
	    throws IllegalStateException
	{
		logger = LoggerFactory.getParentLogger(getClass(),
		                                       parentLogger,
		                                       (parentLogger == null ?
		                                    	Level.INFO : null)); // null level to inherit parent logger's level

		this.fillColor = (fillColor != null ?
			Optional.of(ColorFactory.getColor(fillColor)) :
			Optional.empty());
		if (this.fillColor.isEmpty() && (edgeColor == null))
		{
			String message = "both fill & edge are null";
			logger.severe(message);
			throw new IllegalStateException(message);
		}
		this.edgeColor = (edgeColor != null ?
			Optional.of(ColorFactory.getColor(edgeColor)) :
			Optional.empty());
		this.lineType = (edgeColor != null ? lineType : LineType.NONE);
		this.lineWidth = (edgeColor != null ? Math.abs(lineWidth) : 0);

		root = new Group();
		shape = null;	// Must be set in sub-classes and added to #root
		selectionRectangle = null;	// Set in #setSelected
		// instanceNumber shall be set in sub-classes constructors
		selected = false;
	}

	/**
	 * Copy constructor (to be called in subclasses)
	 * Creates a distinct copy of the provided figure (with same
	 * {@link #instanceNumber})
	 * @param figure the figure to copy
	 */
	protected Figure(Figure figure)
	{
		/*
		 * Note: the provided figure is supposed to be in a consistant state:
		 * Meaning it can not have both edge and fill colors as nulls.
		 */
		this((figure.fillColor.isEmpty() ? null : figure.fillColor.get()),
		     (figure.edgeColor.isEmpty() ? null : figure.edgeColor.get()),
		     figure.lineType,
		     figure.lineWidth,
		     figure.logger);
		instanceNumber = figure.instanceNumber;
		if (figure.shape == null)
		{
			String message = "null provided shape";
			logger.severe(message);
			throw new NullPointerException(message);
		}
		root.getChildren().add(figure.shape);
		setSelected(figure.selected);
	}

	// -------------------------------------------------------------------------
	// Accessors and Mutators
	// -------------------------------------------------------------------------

	/**
	 * Internal {@link #shape} accessor
	 * @return the JavaFX shape of this figure
	 * @implNote Migh be replaced by getGroup
	 */
	public Shape getShape()
	{
		return shape;
	}

	/**
	 * Internal {@link #selectionRectangle} accessor
	 * @return the JavaFX selection rectangle of this figure
	 * @implNote Migh be replaced by getGroup
	 */
	public Rectangle getSelectionRectangle()
	{
		return selectionRectangle;
	}

	/**
	 * Internal {@link #root} {@link Group} accessor
	 * @return the group of this figure containing the {@link #shape} and
	 * eventually the {@link #selectionRectangle} of this figure whenever this
	 * figure is selected
	 */
	public Group getRoot()
	{
		return root;
	}

	/**
	 * Indicates if this figure has a fill color
	 * @return true if {@link #fillColor} has data
	 */
	public boolean hasFillColor()
	{
		// TODO Figure#hasFillColor ...
		return false;
	}

	/**
	 * Fill Color accessor
	 * @return the fillColor or null if there is no fill color
	 */
	public Color getFillColor()
	{
		// TODO Figure#getFillColor
		return null;
	}

	/**
	 * Fill color mutator.
	 * Sets both {@link #fillColor} and {@link #shape} with
	 * {@link Shape#setFill(javafx.scene.paint.Paint)}
	 * @param fillColor the new fillColor to set. If provided fillColor is null
	 * then the {@link #fillColor} shall be set to shall be set to empty
	 * and internal shape color to {@link Color#TRANSPARENT}
	 * @implNote Only one of {@link #fillColor} and {@link #edgeColor} can be
	 * empty
	 * @throws IllegalStateException if we try to set a null fillColor when
	 * internal {@link #edgeColor} is already empty
	 */
	public void setFillColor(Color fillColor) throws IllegalStateException
	{
		// TODO Figure#setFillColor ...
	}

	/**
	 * Indicates if this figure has an edge color
	 * @return true if {@link #edgeColor} has data
	 */
	public boolean hasEdgeColor()
	{
		// TODO Figure#hasEdgeColor ...
		return false;
	}

	/**
	 * Edge color accessor
	 * @return the edgeColor or null if there is no edge color
	 */
	public Color getEdgeColor()
	{
		// TODO Figure#getEdgeColor ...
		return null;
	}

	/**
	 * Edge color mutator
	 * Sets both {@link #edgeColor} and {@link #shape} with
	 * {@link Shape#setStroke(javafx.scene.paint.Paint)}
	 * @param edgeColor the new edgeColor to set. If provided edgeColor is null
	 * then the {@link #edgeColor} shall be set to empty
	 * and internal shape color to {@link Color#TRANSPARENT}
	 * @implNote Only one of {@link #edgeColor} and {@link #fillColor} can be
	 * empty.
	 * @throws IllegalStateException if we try to set a null edgeColor when
	 * internal {@link #fillColor} is already empty
	 */
	public void setEdgeColor(Color edgeColor) throws IllegalStateException
	{
		// TODO Figure#setEdgeColor ...
	}

	/**
	 * Get the line type
	 * @return the lineType
	 */
	public LineType getLineType()
	{
		return lineType;
	}

	/**
	 * Line Type setter.
	 * Sets both {@link #lineType} and {@link #shape} with
	 * {@link Shape#getStrokeDashArray()}
	 * @param lineType the lineType to set
	 */
	public void setLineType(LineType lineType)
	{
		this.lineType = lineType;
		/*
		 * TODO Figure#setLineType ...
		 * 	- if NONE set internal shape stroke to Color#TRANSPARENT
		 * 	- if SOLID then clears internal shape StrokeDashArray
		 * 	- if DASHED then setup internal shape StrokeDashArray
		 */
	}

	/**
	 * Line width
	 * @return the lineWidth
	 */
	public double getLineWidth()
	{
		return lineWidth;
	}

	/**
	 * Line width setter
	 * @param lineWidth the lineWidth to set
	 */
	public void setLineWidth(double lineWidth)
	{
		this.lineWidth = lineWidth;

		// TODO Figure#setLineWidth ...
	}

	/**
	 * Figure's instance number
	 * @return the instanceNumber
	 */
	public int getInstanceNumber()
	{
		return instanceNumber;
	}

	/**
	 * Indicates if this figure is currently selected
	 * @return the selected status of this figure
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * Sets the selection status of this figure
	 * @param selected the selected status to set
	 */
	public void setSelected(boolean selected)
	{
		logger.info(this + " set selected = " + (selected ? "true" : "false"));
		this.selected = selected;
		if (shape != null)
		{
			/*
			 * TODO Figure#setSelected ...
			 * 	- if selected then add a new JavaFX Rectangle to #root: #selectionRectangle
			 * 		- location from topLeftPoint
			 * 		- size from width() & height()
			 * 		- dashed gray stroke
			 * 		- transparent fill
			 * 	- if not selected then remove #selectionRectangle from #root
			 */
		}
		else
		{
			logger.warning("can't change selected state: null shape");
		}
	}

	/**
	 * Center Point of this figure
	 * @return the center point of this figure
	 */
	public abstract Point2D getCenter();

	/**
	 * Width of this figure
	 * @return the width of this figure
	 */
	public abstract double width();

	/**
	 * Height of this figure
	 * @return the width of this figure
	 */
	public abstract double height();

	/**
	 * Top left corner of this figure
	 * @return the top left {@link Point2D} of this figure
	 */
	public abstract Point2D topLeft();

	/**
	 * Bottom right corner of this figure
	 * @return the bottom right {@link Point2D} of this figure
	 */
	public abstract Point2D bottomRight();

	// -------------------------------------------------------------------------
	// Operations on Figures
	// -------------------------------------------------------------------------
	/**
	 * Creates actual {@link #shape} at specified position and apply
	 * parameters
	 * @param x the x coordinate of the initial point where to create the new shape
	 * @param y the y coordinate of the initial point where to create the new shape
	 * @post a new {@link #shape} has been created with a new
	 * {@link #instanceNumber} with {@link #fillColor}, {@link #edgeColor},
	 * {@link #lineType} & {@link #lineWidth} applied with
	 * {@link #applyParameters(Shape)}
	 */
	public abstract void createShape(double x, double y);

	/**
	 * Sets the last point of this figure.
	 * This method can be implemented in subclasses to set the actual
	 * {@link #shape} size by moving the mouse cursor around and/or clicking to
	 * add points.
	 * @param lastPoint the last point to set
	 */
	public abstract void setLastPoint(Point2D lastPoint);

	/**
	 * Apply Fill and Edge Color, LineType and Line width to provided {@link Shape}
	 * @param shape the Shape to apply parameters on
	 */
	public void applyParameters(Shape shape)
	{
		if (shape == null)
		{
			logger.warning("null shape: abort");
			return;
		}

		/*
		 * TODO Figure#applyParameters ...
		 * 	- if there is a #fillColor set it on #shape, otherwise set Color#TRANSPARENT
		 * 	- if there is a #edgeColor set it on #shape
		 * 	- if #lineType is DASHED then set #shape strokeDashArray
		 * 	- in any cases set #shape
		 * 		- StrokeLineJoin.ROUND
		 * 		- StrokeLineCap.ROUND
		 * 		- stroke width with #lineWidth
		 */

	}

	/**
	 * Update {@link #selectionRectangle} iff non null
	 * @see #topLeft()
	 * @see #width()
	 * @see #height()
	 */
	public void updateSelectionFrame()
	{
		// TODO Figure#updateSelectionFrame ...
	}

	// -------------------------------------------------------------------------
	// Prototype<Figure> methods implementation
	// -------------------------------------------------------------------------
	/**
	 * Creates a copy of this figure (with the same name and instance number)
	 * @return A distinct copy of this figure
	 * @implNote Since this class is abstract, this method has to be implemented
	 * in sub-classes
	 * @implSpec this method is required in order to create copies of figures
	 * to create a copy of the current {@link Drawing} state in
	 * {@link history.HistoryManager}
	 */
	@Override
	public abstract Figure clone();

	// -------------------------------------------------------------------------
	// Object methods overloads (to be used in sub-classes)
	// -------------------------------------------------------------------------
	/**
	 * Compares this figure with another one.
	 * @param figure the other figure to compare with
	 * @return true if the other figure is not null, has the same class
	 * and features the same content (except for {@link #fillColor},
	 * {@link #edgeColor}, {@link #lineType} and {@link #lineWidth} which are
	 * checked in {@link #equals(Object)} and {@link #instanceNumber} and
	 * {@link #selected} which are not taken into account)
	 */
	protected abstract boolean equals(Figure figure);

	/**
	 * Base algorithm to compare with another object
	 * @return true if the other figure is not null, has the same class
	 * and features the same {@link #fillColor}, {@link #edgeColor},
	 * {@link #lineType} and {@link #lineWidth} (± {@link #threshold})
	 * and {@link #equals(Figure)} returns true.
	 * @see #equals(Figure)
	 */
	@Override
	public boolean equals(Object obj)
	{
		/*
		 * TODO Figure#equals(Object) ...
		 * 	- Compares
		 * 		- Class
		 * 		- equals(Figure)
		 * 		- fillColor
		 * 		- edgeColor
		 * 		- lineType
		 * 		- lineWidth up to threshold
		 * Note: Since colors should be provided by ColorFactory comparing
		 * colors with == should be enough
		 */

		return false;
	}

	/**
	 * <b>Partial</b> hashCode based solely on {@link #fillColor},
	 * {@link #edgeColor}, {@link #lineType} and {@link #lineWidth}.
	 * Meaning this method can NOT be used alone to compute figure hashCode: It
	 * has to be used in sub-classes overloads of this method.
	 * @return a <b>Partial</b> hashCode based on the fields declared in this
	 * class
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((fillColor == null) ? 0 : fillColor.hashCode());
		result = (prime * result) + ((edgeColor == null) ? 0 : edgeColor.hashCode());
		result = (prime * result) + ((lineType == null) ? 0 : lineType.hashCode());
		long temp;
		temp = Double.doubleToLongBits(lineWidth);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * String representation of this figure.
	 * @return a new String containg the actual class name and instance number (e.g. "Circle 2")
	 */
	@Override
	public String toString()
	{
		return new String(getClass().getSimpleName() + " " + instanceNumber);
	}
}
