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
import tools.AbstractDrawingTool;
import tools.AbstractMultiStepDrawingTool;
import tools.AbstractTool;

/**
 * Base Class of all tools creating new {@link Figure} / {@link Shape}
 * in a drawing {@link Pane}
 * @author davidroussel
 */
public abstract class AbstractCreationTool
    extends AbstractMultiStepDrawingTool<Pane>
{
	/**
	 * The {@link Figure} to create and draw in {@link AbstractTool#root} node
	 * (in this case a {@link Pane})
	 */
	protected Figure figure = null;

	/**
	 * Initial point where the {@link #figure} is created
	 * @see #mousePressed(MouseEvent)
	 */
	protected Point2D startPoint = null;

	/**
	 * Final point where the {@link #figure} has been updated can be added to
	 * {@link Drawing} model
	 * @implNote if distance between {@link #startPoint} and {@link #endPoint}
	 * is less than {@link #minDistance} then the {@link #figure} is not added to
	 * {@link AbstractDrawingTool#drawingModel} and removed from
	 * {@link AbstractTool#root} Pane.
	 * @see #mouseReleased(MouseEvent)
	 */
	protected Point2D endPoint = null;

	/**
	 * Minimal distance between {@link #startPoint} and {@link #endPoint}
	 * for the Shape to be added to {@link Drawing} model.
	 */
	protected static final double minDistance = 4.0;

	/**
	 * Valued constructor
	 * @param root The {@link Pane} in which a {@link Shape} will be created
	 * @param model model the Drawing Model containing {@link figures.Figure}s
	 * @param label Label to show info messages in
	 * @param steps the number of steps required to perform operations with this tool
	 * @param eventsMask The bit mask of events we should listen to.
	 * e.g. (PRESSED|RELEASED) will only register #mousePressed and
	 * #mouseReleased methods to listen to mouse events on {@link #root} node
	 * @param manager the history manager to manage undo / redos during operations
	 * @param parentLogger the parent logger
	 * tool (will also creates the required number of {@link AbstractMultiStepDrawingTool#tips})
	 */
	public AbstractCreationTool(Pane root,
	                            Drawing model,
	                            Label label,
	                            int steps,
	                            int eventsMask,
	                            HistoryManager<Figure> manager,
	                            Logger parentLogger)
	{
		super(root,
		      model,
		      label,
		      steps,
		      eventsMask,
		      true,
		      true,
		      manager,
		      parentLogger);
	}

	/**
	 * Creates Initial {@link Figure} at point p
	 * @param p The point where to initiate the figure
	 * @post {@link #figure} has been created at point p
	 * @see Drawing#initiateFigure(double, double)
	 */
	protected void createFigure(Point2D p)
	{
		/*
		 * TODO AbstractCreationTool#createFigure
		 * 	- setup #figure with request #drawingModel to initiateFigure
		 */
	}

	/**
	 * Update {@link #figure} with the provided Point2D
	 * @param p the point to update the {@link #figure}
	 * @see Figure#setLastPoint(Point2D)
	 */
	protected void updateFigure(Point2D p)
	{
		/*
		 * TODO AbstractCreationTool#updateFigure
		 * set last point of figure
		 */
	}

	/**
	 * Reset for next use:
	 * <ul>
	 * 	<li>Reset {@link #figure} to null</li>
	 * 	<li>Reset {@link #startPoint} to null</li>
	 * 	<li>Reset {@link #endPoint} to null</li>
	 * 	<li>Reset {@link AbstractMultiStepDrawingTool#currentStep} to 0</li>
	 * </ul>
	 */
	protected void reset()
	{
		figure = null;
		startPoint = null;
		endPoint = null;
		currentStep = 0;
	}

	/**
	 * Cancels currently created figure by removing it from
	 * {@link AbstractTool#root} and {@link #reset()} this tool
	 */
	protected void cancelFigure()
	{
		if (figure != null)
		{

			/*
			 * TODO AbstractCreationTool#cancelFigure
			 * 	- remove figure's root from #root
			 * 	- check removed and log warning if not removed
			 */
		}
		else
		{
			logger.warning("null figure");
		}
		reset();
	}

	/**
	 * Terminate {@link Figure} creation by
	 * <ol>
	 * 	<li>Removing it from {@link AbstractTool#root} node</li>
	 * 	<li>Asks the #historyManager to create a new record</li>
	 * 	<li>Then adding the figure to the {@link AbstractDrawingTool#drawingModel}</li>
	 * 	<li>Move to {@link #nextStep()}</li>
	 * 	<li>and {@link #reset()} this tool</li>
	 * </ol>
	 */
	protected void terminateFigure()
	{
		// This is a valid figure
		// although maybe null if all Figure subclasses are not finished yet
		if (figure != null)
		{
			/*
			 * TODO AbstractCreationTool#terminateFigure ...
			 * 	- remove figure from #root
			 * 	- save current state in history manager
			 * 	- adds figure to #drawingModel
			 */
		}
		else
		{
			logger.warning("null figure");
		}
		nextStep();
		reset();
	}
}
