package tools;

import java.util.logging.Logger;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * A Tool listening to {@link MouseEvent#MOUSE_MOVED} and
 * {@link MouseEvent#MOUSE_EXITED} mouse events to update cursor position in
 * a {@link Pane} using 2 {@link Label}s.
 * @author davidroussel
 */
public class CursorTool extends AbstractTool<Pane>
{
	/**
	 * Label to show {@link MouseEvent#MOUSE_MOVED} x coordinates
	 */
	private Label xLabel;
	/**
	 * Label to show {@link MouseEvent#MOUSE_MOVED} y coordinates
	 */
	private Label yLabel;

	/**
	 * Constructor using the labels to update and a logger (all other required
	 * parameters of the super constructor will be set to null)
	 * @param root the panel where to intercept mouse events
	 * @param xLabel The Label containing the x coordinate of the cursor to
	 * update
	 * @param yLabel The Label containing the y coordinate of the cursor to
	 * update
	 * @param parentLogger the parent logger
	 */
	public CursorTool(Pane root, Label xLabel, Label yLabel, Logger parentLogger)
	{
		super(root, (MOVED|EXITED), true, true, parentLogger);
		this.xLabel = xLabel;
		this.yLabel = yLabel;
	}

	/**
	 * Handle mouse moved events by updating {@link #xLabel} and {@link #yLabel}
	 * with events X & Y coordinates
	 * @param event the {@link MouseEvent#MOUSE_MOVED} event to process
	 */
	@Override
	public void mouseMoved(MouseEvent event)
	{
		/*
		 * TODO CursorTool#mouseMoved
		 * Display event's X & Y in xLabel & yLabel using String.format("%4.0f"...)
		 */
	}

	/**
	 * Handle mouse exited events by clearing {@link #xLabel} and {@link #yLabel}
	 * @param event the {@link MouseEvent#MOUSE_EXITED} event to process
	 */
	@Override
	public void mouseExited(MouseEvent event)
	{
		/*
		 * TODO CursorTool#mouseExited
		 * Clears xLabel & yLabel
		 */
	}
}
