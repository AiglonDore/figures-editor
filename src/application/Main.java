package application;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import logger.LoggerFactory;


/**
 * JavaFX Figure Editor Application
 * @author davidroussel
 */
public class Main extends Application
{

	/**
	 * Verbose status indicating if debug messages should be displayed
	 * on the console or only sent to a log file
	 */
	private boolean verbose = true;

	/**
	 * Logger used to display debug or info messages
	 * @implNote Needs to be initialized {@link #init()}
	 */
	private Logger logger = null;

	/**
	 * Application initialization method.
	 * Called after construction and before actual starting
	 */
	@Override
	public void init() throws Exception
	{
		super.init();

		Application.Parameters appParameters = getParameters();
		List<String> rawParameters = appParameters.getRaw();

		if (rawParameters.contains("--verbose"))
		{
			verbose = true;
		}

		/*
		 * logger instantiation
		 */
		logger = null;
		Class<?> runningClass = getClass();
		String logFilename =
		    (verbose ? null : runningClass.getSimpleName() + ".log");
		Logger parent = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		Level level = (verbose ? Level.ALL : Level.INFO);
		try
		{
			logger = LoggerFactory.getLogger(runningClass,
			                                 verbose,
			                                 logFilename,
			                                 false,
			                                 parent,
			                                 level);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			System.exit(ex.hashCode());
		}

		setAttributes(rawParameters);
	}

	/**
	 * The main entry point for all JavaFX applications.
	 * The start method is called after the init method has returned,
	 * and after the system is ready for the application to begin running.
	 * NOTE: This method is called on the JavaFX Application Thread.
	 */
	@Override
	public void start(Stage primaryStage)
	{
		// --------------------------------------------------------------------
		// Loads Scene from FXML
		// --------------------------------------------------------------------
		logger.info("Loading FXML file ...");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EditorFrame.fxml"));
		BorderPane root = null;
		try
		{
			/*
			 * TODO Main#start Complete EditorFrame.fxml with SceneBuilder until
			 * every FXML annotated attribute defined in Controller is assigned
			 * as an fx:id in the fxml file. Otherwise the load method below
			 * will raise an IOException.
			 */
			root = loader.<BorderPane>load();
		}
		catch (IOException e)
		{
			logger.severe("Can't load FXML file " + e.getMessage());
			System.exit(e.hashCode());
		}

		// --------------------------------------------------------------------
		// Get controller's instance and get/set some values such as
		//	- set parent logger to issue messages
		//	- set parent stage on controller so it can be closed later
		// --------------------------------------------------------------------
		logger.info("Setting up controller");
		Controller controller = (Controller) loader.getController();
		controller.setParentLogger(logger);
		controller.setParentStage(primaryStage);

		// --------------------------------------------------------------------
		// Finally launch GUI
		// --------------------------------------------------------------------
		logger.info("Setting up GUI...");
		Scene scene = new Scene(root, 800, 600, true, SceneAntialiasing.BALANCED);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		/*
		 * Setup App icon
		 */
		primaryStage.setTitle("Figures Editor v6.0");
		Image image = new Image("icons/create_new-48.png");
		if (!image.isError())
		{
			primaryStage.getIcons().add(image);
		}
		else
		{
			logger.severe("Couldn't load icon");
		}
		/*
		 * Set up quit logic to stage close request by using
		 * Controller#quitAction_Impl as the EventHandler of the
		 * setOnCloseRequest of the primaryStage.
		 * This ensure proper quit logic is performed even if we directly closes
		 * the window instead of properly quitting
		 */
		primaryStage.setOnCloseRequest(controller::quitActionImpl);
		primaryStage.show();
	}

	/**
	 * This method is called when the application should stop, and provides a
	 * convenient place to prepare for application exit and destroy resources.
	 * NOTE: This method is called on the JavaFX Application Thread.
	 */
	@Override
	public void stop() throws Exception
	{
		/*
		 * Cleanup before quitting (if required)
		 */
		super.stop();
	}

	/**
	 * Main program to launch Application
	 * @param args main program arguments
	 */
	public static void main(String[] args)
	{
		launch(args);
	}

	/**
	 * Sets attributes values based on argument parsing
	 * @param args parameters for setting attributes values
	 * for {@link #port}, {@link #host}, {@link #name}
	 */
	protected void setAttributes(List<String> args)
	{
		/*
		 * Attributes are initialized to their default value
		 */
		verbose = false;

		/*
		 * Arguments parsing
		 * 	-v | --verbose : for verbose setting
		 */
		for (Iterator<String> argIt = args.iterator(); argIt.hasNext();)
		{
			String arg = argIt.next();
			if (arg.startsWith("-")) // option argument
			{
				if (arg.equals("--verbose") || arg.equals("-v"))
				{
					logger.info("Setting verbose on");
					verbose = true;
				}
			}
		}
	}
}
