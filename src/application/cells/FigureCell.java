package application.cells;

import figures.Figure;

/**
 * Custom cell to display {@link Figure}s in a {@link javafx.scene.control.ListView}.
 * Example:
 * {@code myListView.setCellFactory(listView -> new FigureCell());}
 * @author davidroussel
 */
public class FigureCell extends CustomCell<Figure>
{
	/**
	 * Default constructor
	 * Loads FXML file FigureCell.fxml to layout the cell and binds controller
	 * @implSpec The controller specified in FigureCell.fxml is a {@link FigureCellController}
	 */
	public FigureCell()
	{
		super("FigureCell.fxml");
	}
}
