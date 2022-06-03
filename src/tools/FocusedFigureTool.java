package tools;

import java.util.logging.Logger;

import figures.Drawing;
import figures.Figure;
import javafx.event.EventTarget;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

/**
 * Tool managing cursor entering in of exiting {@link Shape}s drawn in a
 * {@link Pane}.
 * When the cursor enters a {@link Shape} the {@link #focusedFigure} attribute
 * is set to the corresponding {@link Figure}.
 * When the cursor exits a {@link Shape} the {@link #focusedFigure} is set to
 * null.
 * @author davidroussel
 */
public class FocusedFigureTool extends AbstractDrawingTool<Pane>
{

	/**
	 * The entered figure (defined using {@link MouseEvent#MOUSE_ENTERED_TARGET}
	 * and {@link MouseEvent#MOUSE_EXITED_TARGET} events
	 * This figure can then be selected or deselected using
	 * {@link MouseEvent#MOUSE_CLICKED} events
	 */
	protected Figure focusedFigure;

	/**
	 * Default constructor initializeing all attributes (Potentially triggered by
	 * {@link javafx.fxml.FXMLLoader#load()} method.
	 * @apiNote This controller requires later a call to
	 * {@link #setup(Pane, Drawing, int, Logger)} in order to work properly.
	 */
	public FocusedFigureTool()
	{
		focusedFigure = null;
	}

	/**
	 * Valued Constructor
	 * @param pane the panel to listen for {@link MouseEvent}s
	 * @param model the Drawing model to determine the figure
	 * @param eventsMask Event mask to add to already registered
	 * (ENTERED_TARGET|EXITED_TARGET) events. Can be {@link AbstractTool#NONE}
	 * if no other events than {@link MouseEvent#MOUSE_ENTERED_TARGET} and
	 * {@link MouseEvent#MOUSE_EXITED_TARGET} are required.
	 * @param parentLogger parent logger
	 */
	public FocusedFigureTool(Pane pane,
	                         Drawing model,
	                         int eventsMask,
	                         Logger parentLogger)
	{
		super(pane,		// Drawing Pane to listen for events
		      model,	// Drawing model to search for Figure containing the focused Shape
		      (ENTERED_TARGET|EXITED_TARGET|eventsMask),	// to be refined in subclasses with more events
		      false,	// events are not captured events, rather bubbling events
		      false,	// events are not cosumed (no need)
		      parentLogger);
	}

	/**
	 * Setup method to provides values to attributes
	 * @param pane the Pane thistool is attached to
	 * @param model the Drawing Model containing {@link figures.Figure}s
	 * @param eventsMask Event mask to add to already registered
	 * (ENTERED_TARGET|EXITED_TARGET) events. Can be {@link AbstractTool#NONE}
	 * if no other events than {@link MouseEvent#MOUSE_ENTERED_TARGET} and
	 * @param parentLogger parent logger
	 */
	public void setup(Pane pane,
	                  Drawing model,
	                  int eventsMask,
	                  Logger parentLogger)
	{
		super.setup(pane, model, (ENTERED_TARGET|EXITED_TARGET|eventsMask), false, false, logger);
	}

	/**
	 * Handle mouse entered target events: Defines the figure under cursor
	 * {@link #focusedFigure}, or the abscence of figure under cursor).
	 * @param event the {@link MouseEvent#MOUSE_ENTERED_TARGET} event to process
	 * @see Drawing#fromShape(Shape)
	 */
	@Override
	public void mouseEnteredTarget(MouseEvent event)
	{
		EventTarget target = event.getTarget();

		if (target instanceof Shape)
		{
			/*
			 * TODO FocusedFigureTool#mouseEnteredTarget ...
			 * 	- finds the corresponding Figure in drawingModel (if any)
			 */
			focusedFigure = null;
		}
	}

	/**
	 * Handle mouse entered target events: reset the {@link #focusedFigure}
	 * to null as there is no figure under cursor
	 * @implNote For this method to be successfull the {@link #captureEvents} flag
	 * should be set to false in order to capture events during bubbling phase.
	 * @param event the {@link MouseEvent#MOUSE_ENTERED_TARGET} event to process
	 */
	@Override
	public void mouseExitedTarget(MouseEvent event)
	{
		EventTarget target = event.getTarget();
		if (target instanceof Shape)
		{
			focusedFigure = null;
		}
	}
}
