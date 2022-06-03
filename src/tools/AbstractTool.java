package tools;

import java.util.logging.Level;
import java.util.logging.Logger;

import figures.Drawing;
import history.HistoryManager;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import logger.LoggerFactory;

/**
 * Base class of all tools intercepting {@link MouseEvent}s either as an Event
 * Filter using {@link Node#addEventFilter(EventType, EventHandler)} to
 * intercept events during capture phase (closer to the source), or as an Event
 * Handler using {@link Node#addEventHandler(EventType, EventHandler)} to
 * intercept events during the bubbling phase (farther from the source,
 * typically in a parent node}
 * @author davidroussel
 * @param <T> The type of root node this Handler should be attached to
 */
public class AbstractTool<T extends Node> implements EventHandler<MouseEvent>
{
	/**
	 * The root {@link Node} this Handler should be registered to.
	 * root node can also be used later to {@link #unregister()} this tool from it.
	 */
	protected T root;

	/**
	 * The logger to use to issue messages
	 */
	protected Logger logger;

	/**
	 * Boolean flag indicating if this {@link EventHandler} should capture events
	 * during events capture phase (faster) or during events bubbling phase
	 * (slower, but required to capture events bubbling from children nodes).
	 */
	protected boolean captureEvents;

	/**
	 * Boolean flag indicating events should be consumed after being processed.
	 * captured events should typically be consumed, bubbling events don't need
	 * to be consumed (but they also can be consumed)
	 */
	protected boolean consumeEvents;

	/**
	 * The bitmask of events to listen to.
	 * A New bit mask can be composed with bitwise ORs : e.g.
	 * <code>(PRESSED|RELEASED)</code>.
	 * Checking for specific events can be performed with listenXXXEvents() methods
	 * @see #listenPressedEvents()
	 * @see #listenReleasedEvents()
	 * @see #listenClickedEvents()
	 * @see #listenMovedEvents()
	 * @see #listenDraggedEvents()
	 */
	protected int bitMask;

	/**
	 * Constant to check with {@link #bitMask} in
	 * {@link #AbstractTool(Node, Drawing, Label, int, HistoryManager, Logger)}
	 * to define No events at all.
	 */
	protected static final int NONE = 0;

	/**
	 * Constant to check with {@link #bitMask} in
	 * {@link #AbstractTool(Node, Drawing, Label, int, HistoryManager, Logger)}
	 * to define if {@link #mousePressed(MouseEvent)} should be triggered on
	 * {@link MouseEvent#MOUSE_PRESSED} events
	 */
	protected static final int PRESSED = 1;

	/**
	 * Constant to check with {@link #bitMask} in
	 * {@link #AbstractTool(Node, Drawing, Label, int, HistoryManager, Logger)}
	 * to define if {@link #mousePressed(MouseEvent)} should be triggered on
	 * {@link MouseEvent#MOUSE_PRESSED} events
	 */
	protected static final int RELEASED = 2;

	/**
	 * Constant to check with {@link #bitMask} in
	 * {@link #AbstractTool(Node, Drawing, Label, int, HistoryManager, Logger)}
	 * to define if {@link #mousePressed(MouseEvent)} should be triggered on
	 * {@link MouseEvent#MOUSE_PRESSED} events
	 */
	protected static final int CLICKED = 4;

	/**
	 * Constant to check with {@link #bitMask} in
	 * {@link #AbstractTool(Node, Drawing, Label, int, HistoryManager, Logger)}
	 * to define if {@link #mousePressed(MouseEvent)} should be triggered on
	 * {@link MouseEvent#MOUSE_PRESSED} events
	 */
	protected static final int MOVED = 8;

	/**
	 * Constant to check with {@link #bitMask} in
	 * {@link #AbstractTool(Node, Drawing, Label, int, HistoryManager, Logger)}
	 * to define if {@link #mousePressed(MouseEvent)} should be triggered on
	 * {@link MouseEvent#MOUSE_PRESSED} events
	 */
	protected static final int DRAGGED = 16;

	/**
	 * Constant to check with {@link #bitMask} in
	 * {@link #AbstractTool(Node, Drawing, Label, int, HistoryManager, Logger)}
	 * to define if {@link #mousePressed(MouseEvent)} should be triggered on
	 * {@link MouseEvent#MOUSE_ENTERED} events
	 */
	protected static final int ENTERED = 32;

	/**
	 * Constant to check with {@link #bitMask} in
	 * {@link #AbstractTool(Node, Drawing, Label, int, HistoryManager, Logger)}
	 * to define if {@link #mousePressed(MouseEvent)} should be triggered on
	 * {@link MouseEvent#MOUSE_EXITED} events
	 */
	protected static final int EXITED = 64;

	/**
	 * Constant to check with {@link #bitMask} in
	 * {@link #AbstractTool(Node, Drawing, Label, int, HistoryManager, Logger)}
	 * to define if {@link #mousePressed(MouseEvent)} should be triggered on
	 * {@link MouseEvent#MOUSE_ENTERED_TARGET} events
	 */
	protected static final int ENTERED_TARGET = 128;

	/**
	 * Constant to check with {@link #bitMask} in
	 * {@link #AbstractTool(Node, Drawing, Label, int, HistoryManager, Logger)}
	 * to define if {@link #mousePressed(MouseEvent)} should be triggered on
	 * {@link MouseEvent#MOUSE_EXITED} events
	 */
	protected static final int EXITED_TARGET = 256;

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
	public AbstractTool()
	{
		root = null;
		logger = null;
		captureEvents = false;
		consumeEvents = false;
		bitMask = 0;
	}

	/**
	 * Setup method to provide values to all attributes.
	 * Register this tool either as an Event Filter with
	 * {@link Node#addEventFilter(EventType, EventHandler)} or as an Event
	 * Handler with {@link Node#addEventHandler(EventType, EventHandler)} on
	 * the {@link #root} node to listen to {@link MouseEvent}s specified in the
	 * eventsMask.
	 * @param root the JavaFX root {@link Node} this handler is attached to
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
	                  int eventsMask,
	                  boolean capture,
	                  boolean consume,
	                  Logger parentLogger)
	{
		this.root = root;
		bitMask = eventsMask;
		captureEvents = capture;
		consumeEvents = consume;

		/*
		 * Register this tool on the #root node so it can listen to MouseEvents
		 */
		if (listenPressedEvents())
		{
			register(MouseEvent.MOUSE_PRESSED);
		}
		if (listenReleasedEvents())
		{
			register(MouseEvent.MOUSE_RELEASED);
		}
		if (listenClickedEvents())
		{
			register(MouseEvent.MOUSE_CLICKED);
		}
		if (listenMovedEvents())
		{
			register(MouseEvent.MOUSE_MOVED);
		}
		if (listenDraggedEvents())
		{
			register(MouseEvent.MOUSE_DRAGGED);
		}
		if (listenEnteredEvents())
		{
			register(MouseEvent.MOUSE_ENTERED);
		}
		if (listenExitedEvents())
		{
			register(MouseEvent.MOUSE_EXITED);
		}
		if (listenEnteredTargetEvents())
		{
			register(MouseEvent.MOUSE_ENTERED_TARGET);
		}
		if (listenExitedTargetEvents())
		{
			register(MouseEvent.MOUSE_EXITED_TARGET);
		}

		logger = LoggerFactory.getParentLogger(getClass(),
		                                       parentLogger,
		                                       (parentLogger == null ?
		                                    	Level.INFO : null)); // null level to inherite parent's logger's level
	}

	/**
	 * Valued constructor.
	 * Register this tool either as an Event Filter with
	 * {@link Node#addEventFilter(EventType, EventHandler)} or as an Event
	 * Handler with {@link Node#addEventHandler(EventType, EventHandler)} on
	 * the {@link #root} node to listen to {@link MouseEvent}s specified in the
	 * eventsMask.
	 * @param root the JavaFX root {@link Node} this handler is attached to
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
	public AbstractTool(T root,
	                    int eventsMask,
	                    boolean capture,
	                    boolean consume,
	                    Logger parentLogger)
	{
		super();
		if ((root != null) && (eventsMask != 0) && (parentLogger != null))
		{
			setup(root, eventsMask, capture, consume, parentLogger);
		}
		else
		{
			this.root  = null;
			logger = null;
			captureEvents = capture;
			consumeEvents = consume;
			bitMask = 0;
		}
	}

	/**
	 * Specialized constructor with {@link #captureEvents} and
	 * {@link #consumeEvents} automatically set to true.
	 * Register this tool either as an Event Filter with
	 * {@link Node#addEventFilter(EventType, EventHandler)} or as an Event
	 * Handler with {@link Node#addEventHandler(EventType, EventHandler)} on
	 * the {@link #root} node to listen to {@link MouseEvent}s specified in the
	 * eventsMask.
	 * @param root the JavaFX root {@link Node} this handler is attached to
	 * @param eventsMask The bit mask of events we should listen to.
	 * e.g. (PRESSED|RELEASED) will only register #mousePressed and
	 * #mouseReleased
	 * @param parentLogger parent logger
	 */
	public AbstractTool(T root,
	                    int eventsMask,
	                    Logger parentLogger)
	{
		this(root, eventsMask, true, true, parentLogger);
	}

	/**
	 * Handle mouse events and dispatch events to processing methods
	 * @param event the mouse event to process
	 * @see #mousePressed(MouseEvent)
	 * @see #mouseReleased(MouseEvent)
	 * @see #mouseClicked(MouseEvent)
	 * @see #mouseMoved(MouseEvent)
	 * @see #mouseDragged(MouseEvent)
	 * @see #mouseEntered(MouseEvent)
	 * @see #mouseExited(MouseEvent)
	 * @see #mouseEnteredTarget(MouseEvent)
	 * @see #mouseExitedTarget(MouseEvent)
	 */
	@Override
	public final void handle(MouseEvent event)
	{
		EventType<? extends MouseEvent> eventType = event.getEventType();

		if (listenPressedEvents() && (eventType == MouseEvent.MOUSE_PRESSED))
		{
			mousePressed(event);
		}

		if (listenReleasedEvents() && (eventType == MouseEvent.MOUSE_RELEASED))
		{
			mouseReleased(event);
		}

		if (listenClickedEvents() && (eventType == MouseEvent.MOUSE_CLICKED))
		{
			mouseClicked(event);
		}

		if (listenMovedEvents() && (eventType == MouseEvent.MOUSE_MOVED))
		{
			mouseMoved(event);
		}

		if (listenDraggedEvents() && (eventType == MouseEvent.MOUSE_DRAGGED))
		{
			mouseDragged(event);
		}

		if (listenEnteredEvents() && (eventType == MouseEvent.MOUSE_ENTERED))
		{
			mouseEntered(event);
		}

		if (listenExitedEvents() && (eventType == MouseEvent.MOUSE_EXITED))
		{
			mouseExited(event);
		}

		if (listenEnteredTargetEvents() && (eventType == MouseEvent.MOUSE_ENTERED_TARGET))
		{
			mouseEnteredTarget(event);
		}

		if (listenExitedTargetEvents() && (eventType == MouseEvent.MOUSE_EXITED_TARGET))
		{
			mouseExitedTarget(event);
		}

		if (consumeEvents)
		{
			event.consume();
		}

//		logger.info(event.toString());
	}

	/**
	 * Indicates if this tool should listen to {@link MouseEvent#MOUSE_PRESSED} events
	 * @return true if this tool should listen to {@link MouseEvent#MOUSE_PRESSED} events
	 */
	protected final boolean listenPressedEvents()
	{
		return (bitMask & PRESSED) == PRESSED;
	}

	/**
	 * Indicates if this tool should listen to {@link MouseEvent#MOUSE_RELEASED} events
	 * @return true if this tool should listen to {@link MouseEvent#MOUSE_RELEASED} events
	 */
	protected final boolean listenReleasedEvents()
	{
		return (bitMask & RELEASED) == RELEASED;
	}

	/**
	 * Indicates if this tool should listen to {@link MouseEvent#MOUSE_CLICKED} events
	 * @return true if this tool should listen to {@link MouseEvent#MOUSE_CLICKED} events
	 */
	protected final boolean listenClickedEvents()
	{
		return (bitMask & CLICKED) == CLICKED;
	}

	/**
	 * Indicates if this tool should listen to {@link MouseEvent#MOUSE_MOVED} events
	 * @return true if this tool should listen to {@link MouseEvent#MOUSE_MOVED} events
	 */
	protected final boolean listenMovedEvents()
	{
		return (bitMask & MOVED) == MOVED;
	}

	/**
	 * Indicates if this tool should listen to {@link MouseEvent#MOUSE_DRAGGED} events
	 * @return true if this tool should listen to {@link MouseEvent#MOUSE_DRAGGED} events
	 */
	protected final boolean listenDraggedEvents()
	{
		return (bitMask & DRAGGED) == DRAGGED;
	}

	/**
	 * Indicates if this tool should listen to {@link MouseEvent#MOUSE_ENTERED} events
	 * @return true if this tool should listen to {@link MouseEvent#MOUSE_ENTERED} events
	 */
	protected final boolean listenEnteredEvents()
	{
		return (bitMask & ENTERED) == ENTERED;
	}

	/**
	 * Indicates if this tool should listen to {@link MouseEvent#MOUSE_EXITED} events
	 * @return true if this tool should listen to {@link MouseEvent#MOUSE_EXITED} events
	 */
	protected final boolean listenExitedEvents()
	{
		return (bitMask & EXITED) == EXITED;
	}

	/**
	 * Indicates if this tool should listen to {@link MouseEvent#MOUSE_ENTERED_TARGET} events
	 * @return true if this tool should listen to {@link MouseEvent#MOUSE_ENTERED_TARGET} events
	 */
	protected final boolean listenEnteredTargetEvents()
	{
		return (bitMask & ENTERED_TARGET) == ENTERED_TARGET;
	}

	/**
	 * Indicates if this tool should listen to {@link MouseEvent#MOUSE_EXITED} events
	 * @return true if this tool should listen to {@link MouseEvent#MOUSE_EXITED} events
	 */
	protected final boolean listenExitedTargetEvents()
	{
		return (bitMask & EXITED_TARGET) == EXITED_TARGET;
	}

	/**
	 * Register this tool on the {@link #root} node either as an Event Filter (to
	 * intercept events during capture phase) or as an Event Handler (to
	 * intercept events during bubbling phase) according to
	 * {@link #captureEvents}'s value.
	 * @param eventType the type of events this tool should intercept
	 */
	protected final void register(EventType<MouseEvent> eventType)
	{
		if (captureEvents)
		{
			root.addEventFilter(eventType, this);
		}
		else
		{
			root.addEventHandler(eventType, this);
		}
	}

	/**
	 * Unregister this tool from the {@link #root} node either as an Event Filter
	 * or as an Event Handler according to {@link #captureEvents}'s value.
	 * @param eventType the type of events this tool should STOP intercepting
	 */
	protected final void unregister(EventType<MouseEvent> eventType)
	{
		if (captureEvents)
		{
			root.removeEventFilter(eventType, this);
		}
		else
		{
			root.removeEventHandler(eventType, this);
		}
	}

	/**
	 * Unregister this tool from the {@link #root} node
	 */
	public void unregister()
	{
		if (listenPressedEvents())
		{
			unregister(MouseEvent.MOUSE_PRESSED);
		}
		if (listenReleasedEvents())
		{
			unregister(MouseEvent.MOUSE_RELEASED);
		}
		if (listenClickedEvents())
		{
			unregister(MouseEvent.MOUSE_CLICKED);
		}
		if (listenMovedEvents())
		{
			unregister(MouseEvent.MOUSE_MOVED);
		}
		if (listenDraggedEvents())
		{
			unregister(MouseEvent.MOUSE_DRAGGED);
		}
		if (listenEnteredEvents())
		{
			unregister(MouseEvent.MOUSE_ENTERED);
		}
		if (listenExitedEvents())
		{
			unregister(MouseEvent.MOUSE_EXITED);
		}
		if (listenEnteredTargetEvents())
		{
			unregister(MouseEvent.MOUSE_ENTERED_TARGET);
		}
		if (listenExitedTargetEvents())
		{
			unregister(MouseEvent.MOUSE_EXITED_TARGET);
		}
	}

	/**
	 * Handle mouse pressed events (to be implemented by sub-classes)
	 * @param event the {@link MouseEvent#MOUSE_PRESSED} event to process
	 */
	public void mousePressed(MouseEvent event)
	{
		// Does Nothing: to be re-implemented in sub-classes
	}

	/**
	 * Handle mouse released events (to be re-implemented by sub-classes)
	 * @param event the {@link MouseEvent#MOUSE_RELEASED} event to process
	 */
	public void mouseReleased(MouseEvent event)
	{
		// Does Nothing: to be re-implemented in sub-classes
	}

	/**
	 * Handle mouse clicked events (to be re-implemented by sub-classes)
	 * @param event the {@link MouseEvent#MOUSE_CLICKED} event to process
	 */
	public void mouseClicked(MouseEvent event)
	{
		// Does Nothing: to be re-implemented in sub-classes
	}

	/**
	 * Handle mouse moved events (to be re-implemented by sub-classes)
	 * @param event the {@link MouseEvent#MOUSE_MOVED} event to process
	 */
	public void mouseMoved(MouseEvent event)
	{
		// Does Nothing: to be re-implemented in sub-classes
	}

	/**
	 * Handle mouse dragged events (to be re-implemented by sub-classes)
	 * @param event the {@link MouseEvent#MOUSE_DRAGGED} event to process
	 */
	public void mouseDragged(MouseEvent event)
	{
		// Does Nothing: to be re-implemented in sub-classes
	}

	/**
	 * Handle mouse entered events (to be re-implemented by sub-classes)
	 * @param event the {@link MouseEvent#MOUSE_ENTERED} event to process
	 */
	public void mouseEntered(MouseEvent event)
	{
		// Does Nothing: to be re-implemented in sub-classes
	}

	/**
	 * Handle mouse exited events (to be re-implemented by sub-classes)
	 * @param event the {@link MouseEvent#MOUSE_EXITED} event to process
	 */
	public void mouseExited(MouseEvent event)
	{
		// Does Nothing: to be re-implemented in sub-classes
	}

	/**
	 * Handle mouse entered target events (to be re-implemented by sub-classes)
	 * @implNote For this method to be successfull the {@link #captureEvents} flag
	 * should be set to false in order to capture events during bubbling phase.
	 * @param event the {@link MouseEvent#MOUSE_ENTERED_TARGET} event to process
	 */
	public void mouseEnteredTarget(MouseEvent event)
	{
		// Does Nothing: to be re-implemented in sub-classes
	}

	/**
	 * Handle mouse exited events (to be re-implemented by sub-classes)
	 * @implNote For this method to be successfull the {@link #captureEvents} flag
	 * should be set to false in order to capture events during bubbling phase.
	 * @param event the {@link MouseEvent#MOUSE_EXITED_TARGET} event to process
	 */
	public void mouseExitedTarget(MouseEvent event)
	{
		// Does Nothing: to be re-implemented in sub-classes
	}
}
