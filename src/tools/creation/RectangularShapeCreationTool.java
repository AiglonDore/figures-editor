/**
 *
 */
package tools.creation;

import java.util.logging.Logger;

import figures.Drawing;
import figures.Figure;
import history.HistoryManager;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import tools.AbstractMultiStepDrawingTool;
import tools.AbstractTool;

/**
 * Base Class Tools creating new rectangular {@link Figure}s in the drawing
 * {@link Pane} and also in the {@link Drawing} model of the editor.
 * Operates with the following actions:
 * <ul>
 * <li>a {@link MouseEvent#MOUSE_PRESSED} event initiates the {@link Shape} /
 * {@link Figure} creation</li>
 * <li>Then {@link MouseEvent#MOUSE_DRAGGED} events updates the newly created
 * {@link Shape} / {@link Figure}</li>
 * <li>Then event {@link MouseEvent#MOUSE_RELEASED} event ends the {@link Shape} /
 * {@link Figure} update and if the figure is valid (with a size greater than
 * {@link AbstractCreationTool#minDistance} the created {@link Shape} /
 * {@link Figure} is added to the {@link Drawing} model</li>
 * </ul>
 * @author davidroussel
 */
public class RectangularShapeCreationTool extends AbstractCreationTool
{
	/**
	 * Protected Valued constructor (to be used in other constructor and
	 * sub-classes).
	 * @param root The {@link Pane} in which a {@link Shape} will be created
	 * @param model model the Drawing Model containing {@link figures.Figure}s
	 * @param label Label to show info messages in
	 * @param steps the number of steps required to perform operations with this tool
	 * @param eventsMask The bit mask of events we should listen to.
	 * In this case (PRESSED|RELEASED|DRAGGED) will register #mousePressed,
	 * #mouseDragged and #mouseReleased.
	 * @param manager the history manager to manage undo / redos during operations
	 * @param parentLogger the parent logger
	 * tool (will also creates the required number of {@link AbstractMultiStepDrawingTool#tips})
	 */
	protected RectangularShapeCreationTool(Pane root,
	                                       Drawing model,
	                                       Label label,
	                                       int steps,
	                                       int eventsMask,
	                                       HistoryManager<Figure> manager,
	                                       Logger parentLogger)
	{
		super(root, model, label, steps, eventsMask, manager, parentLogger);
	}

	/**
	 * Valued constructor
	 * @param root The {@link Pane} in which a {@link Shape} will be created
	 * @param model model the Drawing Model containing {@link figures.Figure}s
	 * @param label Label to show info messages in
	 * @param manager the history manager to manage undo / redos during operations
	 * @param parentLogger the parent logger
	 * tool (will also creates the required number of {@link AbstractMultiStepDrawingTool#tips})
	 */
	public RectangularShapeCreationTool(Pane root,
	                                    Drawing model,
	                                    Label label,
	                                    HistoryManager<Figure> manager,
	                                    Logger parentLogger)
	{
		this(root, model, label, 2, (PRESSED|RELEASED|DRAGGED), manager, parentLogger);
	}

	/**
	 * Handle mouse pressed events by creating {@link #startPoint} and
	 * new {@link #figure}, attach (temporarily so it can be showed) #figure's
	 * {@link Figure#getRoot()} to {@link AbstractTool#root}
	 * @param event the {@link MouseEvent#MOUSE_PRESSED} event to process
	 * @see #createFigure(Point2D)
	 */
	@Override
	public void mousePressed(MouseEvent event)
	{
		if (currentStep != 0)
		{
			logger.warning("Received Mouse pressed event in phase "
			    + String.valueOf(currentStep));
			return;
		}

		if (startPoint == null)
		{
			// Creates #startPoint
			startPoint = new Point2D(event.getX(), event.getY());
		}

		if (figure == null)
		{
			// Creates #figure with #createFigure
			createFigure(startPoint);
			// Adds newly created figure's root node to #root Pane
			if (figure != null)
			{
				root.getChildren().add(figure.getRoot());
			}
			else
			{
				logger.warning("null created figure");
			}
		}

		nextStep();
	}

	/**
	 * Handle mouse dragged event by upating the created {@link #figure} with
	 * mouse event coordinates
	 */
	@Override
	public void mouseDragged(MouseEvent event)
	{
		if (currentStep != 1)
		{
			logger.warning("Received mouse dragged event at step "
			    + currentStep);
			return;
		}

		if (figure != null)
		{
			Point2D point = new Point2D(event.getX(), event.getY());
			updateFigure(point);
		}
	}

	/**
	 * Handle mouse released by finishing the created {@link #figure}:
	 * <ol>
	 * <li>Setup {@link #endPoint} from provided event</li>
	 * <li>If distance between {@link #startPoint} and {@link #endPoint} >
	 * {@link #minDistance} then the figure is valid</li>
	 * <li>If the figure is valid then {@link #terminateFigure()}</li>
	 * </ol>
	 * @param event the mouse event to set #lastPoint
	 * @see AbstractCreationTool#terminateFigure()
	 */
	@Override
	public void mouseReleased(MouseEvent event)
	{
		if (currentStep != 1)
		{
			logger.warning("received mouse release event at step "
			    + currentStep);
			return;
		}

		endPoint = new Point2D(event.getX(), event.getY());

		if (startPoint.distance(endPoint) < minDistance)
		{
			logger.warning("figure too small: cancel");
			cancelFigure();
			return;
		}

		terminateFigure();
	}

	/**
	 * Set {@link #tips} content
	 * @post {@link #tips} content has been set
	 */
	@Override
	protected void setTips()
	{
		tips[0] = "Press to initiate Figure";
		tips[1] = "Drag to set Figure's size and Release to terminate Figure";
	}
}
