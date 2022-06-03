package application.cells;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Abstract Base class for all controllers of custom cells containing either
 * {@link figures.Figure} or {@link figures.enums.FigureType}
 * or {@link figures.enums.LineType}
 * @author davidroussel
 * @param <T> The type of content in the cell to control
 * @author davidroussel
 */
public abstract class AbstractCustomCellController<T>
{
	/**
	 * Figure, Figure Type or Line Type label
	 * @see #setName(Object)
	 */
	@FXML
	protected Label label;

	/**
	 * Image View to set icon in.
	 * @see #setIcon(Object)
	 */
	@FXML
	protected ImageView iconView;

	/**
	 * Set {@link #label} from text
	 * @param value the value used to fill the label using value's
	 * {@link Object#toString()} overload.
	 */
	public void setName(T value)
	{
		label.setText(value.toString());
	}

	/**
	 * Set image in {@link #iconView} depending on the provided value.
	 * Subclasses can implement this method according to their needs
	 * @param value the value to decide which {@link Image} to set in
	 * {@link #iconView}. When an image is available it can be set with
	 * {@link #setImage(Image)}.
	 * @see #setImage(Image)
	 */
	public abstract void setIcon(T value);

	/**
	 * Sets image in {@link #iconView}.
	 * To be used in implementations of {@link #setIcon(Object)}
	 * @param image the image to set in {@link #iconView}
	 */
	protected void setImage(Image image)
	{
		iconView.setImage(image);
	}
}
