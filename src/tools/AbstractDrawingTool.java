package tools;

import java.util.logging.Logger;

import figures.Drawing;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Abstract Base Event Handler for all tools intercepting {@link MouseEvent}s on
 * a {@link AbstractTool#root} node and interacting with a {@link Drawing} model.
 * @author davidroussel
 * @param <T> The type of root node this Handler should be attached to
 */
public class AbstractDrawingTool<T extends Node> extends AbstractTool<T>
{

	/**
	 * The Drawing Model to operate on.
	 * {@link figures.Figure}s are managed by this model
	 */
	protected Drawing drawingModel;

	/**
	 * Default Constructor.
	 * Initialize all attributes to their default value
	 * Using this constructor will require to use
	 * {@link #setup(Node, int, boolean, boolean, Logger)} later to properly
	 * set up attributes.
	 * @apiNote This constructor is NOT suitable to create a working Tool, since
	 * there is no {@link #root} to listen to, nor events mask to define events.
	 * @apiNote This constructor is however suitable to create an FXML controller
	 * since loading FXML associated with a controller will only trigger the
	 * default constructor of this controller.
	 */
	public AbstractDrawingTool()
	{
		super();
	}

	/**
	 * Valued constructor
	 * @param root the JavaFX root {@link Node} this handler is attached to
	 * @param model the Drawing Model containing {@link figures.Figure}s
	 * @param eventsMask The bit mask of events we should listen to.
	 * e.g. (PRESSED|RELEASED) will only register #mousePressed and
	 * #mouseReleased
	 * @param capture if true indicate this tool should be registered on the
	 * root as an Event Filter, otherwise it sholud be registered on the root as
	 * an {@link EventHandler}
	 * @param consume infdicates if events should be consumed after being
	 * processed or not.
	 * @param parentLogger parent logger
	 */
	public AbstractDrawingTool(T root,
	                           Drawing model,
	                           int eventsMask,
	                           boolean capture,
	                           boolean consume,
	                           Logger parentLogger)
	{
		super(root, eventsMask, capture, consume, parentLogger);
		drawingModel = model;
	}

	/**
	 * Setup method to provide values to all attributes.
	 * @param root the JavaFX root {@link Node} this handler is attached to
	 * @param model the Drawing Model containing {@link figures.Figure}s
	 * @param eventsMask The bit mask of events we should listen to.
	 * e.g. (PRESSED|RELEASED) will only register #mousePressed and
	 * #mouseReleased
	 * @param capture if true indicate this tool should be registered on the
	 * root as an Event Filter, otherwise it sholud be registered on the root as
	 * an {@link EventHandler}
	 * @param consume infdicates if events should be consumed after being
	 * processed or not.
	 * @param parentLogger parent logger
	 */
	public void setup(T root,
	                  Drawing model,
	                  int eventsMask,
	                  boolean capture,
	                  boolean consume,
	                  Logger parentLogger)
	{
		drawingModel = model;
		super.setup(root, eventsMask, capture, consume, parentLogger);
	}

	/**
	 * Specialized constructor with {@link #captureEvents} and
	 * {@link #consumeEvents} automatically set to <b>false</b>.
	 * CAUTION : This constructor is NOT suitable to create new {@link javafx.scene.shape.Shape}s
	 * in a Drawing {@link Pane} since it will capture events during bubbling
	 * phase and do NOT consume processed events.
	 * @param root the JavaFX root {@link Node} this handler is attached to
	 * @param model the Drawing Model containing {@link figures.Figure}s
	 * @param eventsMask The bit mask of events we should listen to.
	 * e.g. (PRESSED|RELEASED) will only register #mousePressed and
	 * #mouseReleased
	 * @param parentLogger parent logger
	 */
	public AbstractDrawingTool(T root,
	                           Drawing model,
	                           int eventsMask,
	                           Logger parentLogger)
	{
		this(root, model, eventsMask, false, false, parentLogger);
	}
}
