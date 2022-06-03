package application.panels;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import application.cells.FigureIconsFactory;
import figures.Drawing;
import figures.Figure;
import figures.enums.LineType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tools.AbstractTool;
import tools.FocusedFigureTool;
import utils.IconFactory;

/**
 * Controller of InfoPanel.fxml implemented as an {@link FocusedFigureTool}.
 * This controller listens to {@link MouseEvent#MOUSE_ENTERED_TARGET} and
 * {@link MouseEvent#MOUSE_EXITED_TARGET} events on the drawing Pane where these
 * events came from the contained {@link javafx.scene.Group} / {@link javafx.scene.shape.Shape}s.
 * When entering a {@link javafx.scene.shape.Shape} the Info Panel described in InfoPanel.fxml and
 * reflected in FXML related attributes should be updated with Data
 * corresponding to the {@link FocusedFigureTool#focusedFigure} containing the
 * {@link javafx.scene.shape.Shape} entered.
 * When exiting a {@link javafx.scene.shape.Shape} the Info Panel should be cleared of its data.
 * @author davidroussel
 */
public class InfoPanelController extends FocusedFigureTool
    implements Initializable
{
	// ------------------------------------------------------------------------
	// FXML attributes
	// ------------------------------------------------------------------------
	/**
	 * Figure Name Label of selected Figure in info panel.
	 * Name of selected figure can be set through {@link Label#setText(String)}
	 */
	@FXML
	private Label figureNameLabel;

	/**
	 * ImageView showing the current {@link figures.enums.FigureType} of
	 * selected Figure in info panel.
	 * Image of selected figure can be set with
	 * {@link ImageView#setImage(javafx.scene.image.Image)}
	 * using the {@link FigureIconsFactory#getIconFromType(figures.enums.FigureType)}
	 */
	@FXML
	private ImageView figureTypeImageView;

	/**
	 * Circle representing the current Fill Color of selected Figure.
	 * Color of selected figure can be set with
	 * {@link Circle#setFill(javafx.scene.paint.Paint)}
	 */
	@FXML
	private Circle fillColorCircle;

	/**
	 * Circle representing the current Edge Color of selected Figure.
	 */
	@FXML
	private Circle edgeColorCircle;

	/**
	 * ImageView showing the {@link LineType} of selected Figure
	 */
	@FXML
	private ImageView lineTypeImageView;

	/**
	 * Label showing selected Figure top left corner X coordinate
	 * with "%4.0f" format
	 */
	@FXML
	private Label topLeftXLabel;

	/**
	 * Label showing selected Figure top left corner Y coordinate
	 * with "%4.0f" format
	 */
	@FXML
	private Label topLeftYLabel;

	/**
	 * Label showing selected Figure bottom right corner X coordinate
	 * with "%4.0f" format
	 */
	@FXML
	private Label bottomRightXLabel;

	/**
	 * Label showing selected Figure bottom right corner Y coordinate
	 * with "%4.0f" format
	 */
	@FXML
	private Label bottomRightYLabel;

	/**
	 * Label showing selected Figure width
	 * with "%4.0f" format
	 */
	@FXML
	private Label widthLabel;

	/**
	 * Label showing selected Figure height
	 * with "%4.0f" format
	 */
	@FXML
	private Label heightLabel;

	/**
	 * Label showing selected figure center X coordinate
	 * with "%4.0f" format
	 */
	@FXML
	private Label centerXLabel;

	/**
	 * Label showing selected figure center Y coordinate
	 * with "%4.0f" format
	 */
	@FXML
	private Label centerYLabel;

	/**
	 * Label for current X translation
	 * with "%5.1f" format
	 */
	@FXML
	private Label translationXLabel;

	/**
	 * Label for current Y translation
	 * with "%5.1f" format
	 */
	@FXML
	private Label translationYLabel;

	/**
	 * Label for current rotation (in degrees)
	 * with "%5.1f" format
	 */
	@FXML
	private Label rotationLabel;

	/**
	 * Label for current scale (No need to show both scaleX & scaleY since we
	 * only apply the same X & Y scale on figures)
	 * with "%5.1f" format
	 */
	@FXML
	private Label scaleLabel;

	/**
	 * Icon to use for solid lines
	 */
	private final static Image solidIcon = IconFactory.getIcon("Stroke_Solid");

	/**
	 * Icon to use for dashed lines
	 */
	private final static Image dashedIcon = IconFactory.getIcon("Stroke_Dashed");

	/**
	 * Icon to use for no lines (and also anywhere we need empty icons)
	 */
	private final static Image noneIcon = IconFactory.getIcon("Stroke_None");

	/**
	 * Default constructor (triggered by {@link javafx.fxml.FXMLLoader#load()} method)
	 * @apiNote This controller requires later a call to {@link #setup(Pane, Drawing, Logger)}
	 * in order to work properly.
	 */
	public InfoPanelController()
	{
		super();
	}

	/**
	 * Set up internal attributes
	 * @param pane as {@link AbstractTool#root} node
	 * @param model the drawing model to operate on
	 * @param parentLogger parent Loger to issue messages
	 */
	public void setup(Pane pane,
	                  Drawing model,
	                  Logger parentLogger)
	{
		super.setup(pane,
		            model,
		            NONE,	// no more than (ENTERED_TARGET|EXITED_TARGET)
		            parentLogger);
	}

	/**
	 * Controller initialization to initialize FXML related attributes.
	 * @param location The location used to resolve relative paths for the root
	 * object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null
	 * if the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		clearInfoPanel();
	}

	/**
	 * Handle mouse entered target events by filling FXML related attributes
	 * with data corresponding to the figure under cursor which can be extracted
	 * from #drawingModel with {@link Drawing#fromShape(javafx.scene.shape.Shape)}.
	 * The {@link javafx.scene.shape.Shape} can be obtained as the event target
	 * with {@link Event#getTarget}
	 * @implNote For this method to be successfull the {@link #captureEvents} flag
	 * should be set to false in order to capture events during bubbling phase.
	 * @param event the {@link MouseEvent#MOUSE_ENTERED_TARGET} event to process
	 * @see #fillInfoPanel(Figure)
	 */
	@Override
	public void mouseEnteredTarget(MouseEvent event)
	{
		super.mouseEnteredTarget(event);
		if (focusedFigure != null)
		{
			fillInfoPanel(focusedFigure);
		}
	}

	/**
	 * Handle mouse exited events by clearing all FXML related attributes.
	 * @implNote For this method to be successfull the {@link #captureEvents} flag
	 * should be set to false in order to capture events during bubbling phase.
	 * @param event the {@link MouseEvent#MOUSE_EXITED_TARGET} event to process
	 * @see #clearInfoPanel()
	 */
	@Override
	public void mouseExitedTarget(MouseEvent event)
	{
		super.mouseExitedTarget(event);
		clearInfoPanel();
	}

	/**
	 * Fill Info Panel with data from provided figure.
	 * This method should be used on {@link MouseEvent#MOUSE_ENTERED_TARGET} events
	 * if this event's target is a {@link javafx.scene.shape.Shape}
	 * @param figure the figure to display in the Info Panel
	 * @implNote CAUTION the provided figure might be null if just cancelled. In
	 * such cases, log severe and return
	 */
	private void fillInfoPanel(Figure figure)
	{
		if (figure == null)
		{
			logger.severe("null provided figure");
			return;
		}

		/*
		 * TODO InfoPanelController#fillInfoPanel Fill this panel's FXML attributes with data from figure
		 */
	}

	/**
	 * Clears Info Panel.
	 * clearing all FXML related attributes either
	 * with no text for {@link Label}s, {@link Color#TRANSPARENT} color for
	 * {@link Circle}s and {@link #noneIcon} for emty images.
	 */
	private void clearInfoPanel()
	{
		/*
		 * TODO InfoPanelController#clearInfoPanel Clears this panel's FXML attributes content
		 */
	}
}
