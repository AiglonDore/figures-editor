package tools;

import java.util.logging.Logger;

import figures.Drawing;
import figures.Figure;
import history.HistoryManager;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * Abstract Base Event Handler for all Tools operating with multiple steps on
 * {@link MouseEvent}s in an {@link AbstractTool#root} node and interacting
 * with a {@link Drawing} model.
 * @author davidroussel
 * @param <T> The Type of JavaFX Node this Handler should be attached to.
 */
public abstract class AbstractMultiStepDrawingTool<T extends Node> extends AbstractDrawingTool<T>
{
	/**
	 * A label to show User's tips for each steps
	 */
	protected Label messagesLabel;

	/**
	 * Number of steps required by this tool to perform operations
	 */
	protected int nbSteps;

	/**
	 * The current step index;
	 */
	protected int currentStep;

	/**
	 * The tips to show in {@link #messagesLabel} depending on the current
	 * step of the tool.
	 */
	protected String[] tips;

	/**
	 * The history manager to manage Undo / Redos during operations
	 */
	protected HistoryManager<Figure> historyManager;

	/**
	 * Valued constructor.
	 * Sets {@link #root} and {@link #drawingModel} and registers itself
	 * as handler of {@link MouseEvent}s, either as an Event filter (to receive
	 * events during capture phase) with
	 * {@link Node#addEventFilter(EventType, EventHandler)} or as a
	 * Event Handler (to receive events during bubbling phase) with
	 * {@link Node#addEventHandler(EventType, EventHandler)}.
	 * @param root the JavaFX root {@link Node} this handler is attached to
	 * @param model the Drawing Model containing {@link figures.Figure}s
	 * @param label Label to show info messages in
	 * @param steps the number of steps required to perform operations with this
	 * tool (will also creates the required number of {@link #tips})
	 * @param eventsMask The bit mask of events we should listen to.
	 * e.g. (PRESSED|RELEASED) will only register #mousePressed and
	 * #mouseReleased methods to listen to mouse events on {@link #root} node
	 * @param capture if true indicate this tool should be registered on the
	 * root as an Event Filter, otherwise it sholud be registered on the root as
	 * an {@link EventHandler}
	 * @param consume infdicates if events should be consumed after being
	 * processed or not.
	 * @param manager the history manager to manage undo / redos during
	 * operations
	 * @param parentLogger parent logger
	 */
	public AbstractMultiStepDrawingTool(T root,
	                                    Drawing model,
	                                    Label label,
	                                    int steps,
	                                    int eventsMask,
	                                    boolean capture,
	                                    boolean consume,
	                                    HistoryManager<Figure> manager,
	                                    Logger parentLogger)
	{
		super(root, model, eventsMask, capture, consume, parentLogger);
		this.root = root;
		messagesLabel = label;
		nbSteps = steps;
		tips = new String[steps];
		// Subc-classes must set tips content
		setTips();
		currentStep = 0;
		historyManager = manager;
	}

	/**
	 * Specialized constructor with {@link #captureEvents} and
	 * {@link #consumeEvents} automatically set to true.
	 * @param root the JavaFX root {@link Node} this handler is attached to
	 * @param model the Drawing Model containing {@link figures.Figure}s
	 * @param label Label to show info messages in
	 * @param steps the number of steps required to perform operations with this
	 * tool (will also creates the required number of {@link #tips})
	 * @param eventsMask The bit mask of events we should listen to.
	 * e.g. (PRESSED|RELEASED) will only register #mousePressed and
	 * #mouseReleased methods to listen to mouse events on {@link #root} node
	 * @param capture if true indicate this tool should be registered on the
	 * root as an Event Filter, otherwise it sholud be registered on the root as
	 * an {@link EventHandler}
	 * @param manager the history manager to manage undo / redos during
	 * operations
	 * @param parentLogger parent logger
	 */
	public AbstractMultiStepDrawingTool(T root,
	                                    Drawing model,
	                                    Label label,
	                                    int steps,
	                                    int eventsMask,
	                                    HistoryManager<Figure> manager,
	                                    Logger parentLogger)
	{
		this(root, model, label, steps, eventsMask, true, true, manager, parentLogger);
	}

	/**
	 * Goes to next step by incrementing {@link #currentStep} and updating
	 * {@link #messagesLabel} with next tip
	 * @implNote sub-classes may override this method to perform other step
	 * related operations
	 */
	protected void nextStep()
	{
		currentStep = (currentStep + 1) % nbSteps;
		if (messagesLabel != null)
		{
			if (tips[currentStep] != null)
			{
				logger.info("step " + currentStep + " - " + tips[currentStep]);
				messagesLabel.setText(tips[currentStep]);
			}
			else
			{
				messagesLabel.setText("");
			}
		}
	}

	/**
	 * Sets new {@link Drawing} Model (very unlikely)
	 * @param model the new Drawing Model
	 */
	public void setDrawingModel(Drawing model)
	{
		drawingModel = model;
	}

	/**
	 * Sets new messages {@link Label}
	 * @param label the new Label to set
	 */
	public void setMessageLabel(Label label)
	{
		messagesLabel = label;
	}

	/**
	 * Set {@link #tips} content
	 * @post {@link #tips} content has been set
	 */
	protected abstract void setTips();

	/**
	 * Unregister this tool from its {@link AbstractTool#root} node and
	 * clears {@link #messagesLabel}
	 */
	@Override
	public void unregister()
	{
		super.unregister();
		messagesLabel.setText("");
	}
}
