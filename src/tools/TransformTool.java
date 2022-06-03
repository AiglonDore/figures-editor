/**
 *
 */
package tools;

import java.util.logging.Logger;

import figures.Drawing;
import figures.Figure;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

/**
 * Tool allowing to move, scale or rotate {@link Shape}s
 * (and consequently {@link Figure}s under cursor
 * @author davidroussel
 */
public class TransformTool extends FocusedFigureTool
{
	/**
	 * Message Label to show current transform
	 */
	protected Label messageLabel;

	/**
	 * Internal current step
	 */
	protected int step;

	/**
	 * The root of the Shape to move / rotate / scale
	 * @see Figure#getRoot()
	 */
	protected Group figureRoot;

	/**
	 * The type of motion to apply on {@link FocusedFigureTool#focusedFigure}.
	 * Determined durin {@link #mousePressed(MouseEvent)} by checking for
	 * Ctrl pressed and Shift pressed:
	 * <ul>
	 * 	<li>If no modifier key is pressed, translation motion is applied</li>
	 * 	<li>If Shift modifier is pressed, rotation motion is applied</li>
	 * 	<li>If Ctrl modifier is pressed, scale motion is applied</li>
	 * </ul>
	 * @implNote Modifier keys {@link MouseEvent#isControlDown()} or
	 * {@link MouseEvent#isShiftDown()} should be checked only during
	 * {@link #mousePressed(MouseEvent)} and can be released afterwards.
	 */
	protected Motion motionType;

	/**
	 * Starting point of the motion.
	 * Used to define translation during {@link Motion#TRANSLATION}.
	 */
	protected Point2D initialPoint;

	/**
	 * Initial {@link FocusedFigureTool#focusedFigure} center;
	 */
	protected Point2D initialCenter;

	/**
	 * Initial vector between {@link #initialPoint} and {@link #initialCenter}.
	 * Used to define rotation vector (with {@link Point2D#angle(Point2D)}
	 * if converted to unit vector during {@link Motion#ROTATION}) or scale
	 * factor during {@link Motion#SCALE}.
	 */
	protected Point2D initialVector;

	/**
	 * {@link #initialVector} magnitude.
	 * Used during {@link Motion#SCALE} to compute the relative scale factor
	 * between cursor start and current cursor.
	 */
	protected double initialVectorMagnitude;

	/**
	 * Initial translation (tx, ty) of the group to move.
	 * (initialized during {@link #mousePressed(MouseEvent)})
	 */
	protected Point2D initialTranslation;

	/**
	 * Initial rotation of the group to rotate.
	 * (initialized during {@link #mousePressed(MouseEvent)})
	 */
	protected double initialRotation;

	/**
	 * Initial scale (sx, sy) of the group to scale.
	 * (initialized during {@link #mousePressed(MouseEvent)})
	 */
	protected Point2D initialScale;

	/**
	 * Valued constructor
	 * @param pane The pane to listen to events
	 * @param model The Drawing model to act upon
	 * @param message The message label used to show current transform.
	 * @param parentLogger Parent logger
	 */
	public TransformTool(Pane pane, Drawing model, Label message, Logger parentLogger)
	{
		super(pane, model, (PRESSED|DRAGGED|RELEASED), parentLogger);
		messageLabel = message;
		step = 0;
		figureRoot = null;
		motionType = Motion.NONE;
		initialPoint = null;
		initialVector = null;
		initialTranslation = null;
		initialRotation = 0.0;
		initialScale = null;
	}

	/**
	 * Handle mouse pressed events: starts moving figure under cursor (if any)
	 * @param event the {@link MouseEvent#MOUSE_PRESSED} event to process
	 */
	@Override
	public void mousePressed(MouseEvent event)
	{
		MouseButton button = event.getButton();
		if (button != MouseButton.PRIMARY)
		{
			return;
		}

		if (step == 0)
		{
			if (focusedFigure != null)
			{
				figureRoot = focusedFigure.getRoot();
				boolean controlDown = event.isMetaDown();
				boolean shiftDown = event.isShiftDown();

				 /*
				  * TODO TransformTool#mousePressed ... initialize motion by setting
				  * 	- motionType
				  * 	- initialCenter
				  * 	- initialVector
				  * 	- initialVectorMagnitude
				  * 	- initialScale
				  * 	- ...
				  * depending of the type of motion
				  */

				step++;
				event.consume();
			}
		}
	}

	/**
	 * Handle mouse dragged events: Drag figure under cursor
	 * @param event the {@link MouseEvent#MOUSE_DRAGGED} event to process
	 */
	@Override
	public void mouseDragged(MouseEvent event)
	{
		MouseButton button = event.getButton();
		if (button != MouseButton.PRIMARY)
		{
			return;
		}

		if (step == 1)
		{
			switch (motionType)
			{
				case TRANSLATION:
				{
					/*
					 * TODO TransformTool#mouseDragged: Apply translation on #figureRoot:
					 * 	- tx = initialTranslation.x + (event.x - initialPoint.x)
					 */
					break;
				}
				case ROTATION:
				{
					/*
					 * TODO TransformTool#mouseDragged:Apply rotation on #figureRoot:
					 * 	- v = (event - center) vector
					 * 	- r = initialRotation + v.angle(initialVector)
					 */
					break;
				}
				case SCALE:
				{
					/*
					 * TODO TransformTool#mouseDragged: Apply scale on #figureRoot
					 * 	- v = (event - center) vector
					 * 	- sx = initialScale.x + (|v| / |initialVector|)
					 */
					break;
				}
				default:
					logger.warning("unkown motion type " + motionType);
					break;
			}
			event.consume();
		}
	}

	/**
	 * Handle mouse released events: Stops movin figure under cursor
	 * @param event the {@link MouseEvent#MOUSE_RELEASED} event to process
	 */
	@Override
	public void mouseReleased(MouseEvent event)
	{
		MouseButton button = event.getButton();
		if (button != MouseButton.PRIMARY)
		{
			return;
		}

		if (step == 1)
		{
			step = 0;
			figureRoot = null;
			motionType = Motion.NONE;

			switch (motionType)
			{
				case TRANSLATION:
					initialPoint = null;
					initialTranslation = null;
					break;
				case ROTATION:
					initialVector = null;
					initialRotation = 0.0;
					initialVectorMagnitude = 1.0;
					break;
				case SCALE:
					initialVector = null;
					initialScale = null;
					break;
				default:
					logger.warning("unknown motion type " + motionType);
					break;
			}
			if (messageLabel != null)
			{
				messageLabel.setText("");
			}
			event.consume();
		}
	}

	/**
	 * Show Title and vector values in {@link #messageLabel} iff non null
	 * @param title the Title of the message
	 * @param vector the points containing values to show
	 */
	protected void showVector(String title, Point2D vector)
	{
		if (messageLabel != null)
		{
			messageLabel.setText(String.format("%s: (%5.1f, %5.1f)",
			                                   title,
			                                   vector.getX(),
			                                   vector.getY()));
		}
	}

	/**
	 * Show Title and vector values in {@link #messageLabel} iff non null
	 * @param title the Title of the message
	 * @param x the x part of the vector
	 * @param y the y part of the vector
	 */
	protected void showVector(String title, double x, double y)
	{
		if (messageLabel != null)
		{
			messageLabel.setText(String.format("%s: (%5.1f, %5.1f)",
			                                   title,
			                                   x,
			                                   y));
		}
	}

	/**
	 * Show Title and value in {@link #messageLabel} iff non null
	 * @param title the Title of the message
	 * @param value the value to show
	 */
	protected void showValue(String title, double value)
	{
		if (messageLabel != null)
		{
			messageLabel.setText(String.format("%s: %5.1f",
			                                   title,
			                                   value));
		}
	}

	/**
	 * Type of Motion to apply to shape
	 * @author davidroussel
	 */
	private enum Motion
	{
		/**
		 * Translation motion
		 */
		TRANSLATION,
		/**
		 * Rotation motion (when shift is pressed)
		 */
		ROTATION,
		/**
		 * Scale motion (when ctrl is pressed)
		 */
		SCALE,
		/**
		 * No motion defined yet
		 */
		NONE;

		/**
		 * String representation of this enum
		 * @return a String representing this enum's value
		 */
		@Override
		public String toString()
		{
			switch (this)
			{
				case TRANSLATION:
					return "Translation";
				case ROTATION:
					return "Rotation";
				case SCALE:
					return "Scale";
				case NONE:
					return "None";
				default:
					throw new IllegalArgumentException("Unexpected value: "
					    + this);
			}
		}
	}
}
