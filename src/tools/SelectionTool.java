/**
 *
 */
package tools;

import java.util.logging.Logger;

import figures.Drawing;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

/**
 * Tool Managing selection of figures based on selection of {@link Shape}s in the
 * Drawing area.
 * A click triggers a figure search in {@link AbstractDrawingTool#drawingModel}.
 * Once a figure is found, then it becomes selected if it wasn't selected or
 * becomes deselected if it was selected
 * @author davidroussel
 */
public class SelectionTool extends FocusedFigureTool
{
	/**
	 * Valued Constructor
	 * @param pane the panel to listen for {@link MouseEvent}s
	 * @param model the Drawing model to determine the figure
	 * @param parentLogger parent logger
	 * @implSpec {@link AbstractTool#ENTERED_TARGET} and
	 * {@link AbstractTool#EXITED_TARGET} are already registered in super
	 * constructor, so only additional events are required here. In this case
	 * {@link AbstractTool#CLICKED}
	 */
	public SelectionTool(Pane pane,
	                     Drawing model,
	                     Logger parentLogger)
	{
		super(pane, model, CLICKED, parentLogger);
	}

	/**
	 * Handle mouse clicked events: When there is a non null
	 * {@link #focusedFigure}, then select or unselect this figure in the drawingModel
	 * @param event the {@link MouseEvent#MOUSE_CLICKED} event to process
	 */
	@Override
	public void mouseClicked(MouseEvent event)
	{
		// TODO SelectionTool#mouseClicked ...
	}
}
