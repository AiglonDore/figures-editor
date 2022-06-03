package utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import logger.LoggerFactory;

/**
 * Factory providing {@link Image} icons in order to reuse frequently used images
 * @author davidroussel
 */
public class IconFactory
{
	/**
	 * Path beginning for all searched images
	 */
	private final static String ImagePrefix = "icons/";

	/**
	 * Path end for all searched images
	 */
	private final static String ImagePostfix = "-32.png";

	/**
	 * La factory stockant et fournissant les icônes
	 */
	static private FlyweightFactory<Image> iconFactory =
		new FlyweightFactory<Image>();

	/**
	 * Logger from {@link #iconFactory}
	 */
	static private Logger logger = LoggerFactory
	    .getParentLogger(IconFactory.class,
	                     iconFactory.getLogger(),
	                     (iconFactory .getLogger() == null ?
	                      Level.INFO : null)); // null level to inherit parent logger's level

	/**
	 * Factory method retrieving an Image icon based on a provided icon name
	 * @param name the name of the icon to search for (e.g. "Circle" will trigger
	 * a search for "Circle-32.png" file)
	 * @return The image corresponding to this name or null if there is no such
	 * image.
	 */
	static public Image getIcon(String name)
	{
		if ((name == null) || name.isEmpty())
		{
			logger.severe("<EMPTY or NULL NAME>");
			return null;
		}

		int hash = name.hashCode();
		Image icon = iconFactory.get(hash);

		if (icon == null)
		{
			String fileName = new String(ImagePrefix + name + ImagePostfix);
			try
			{
				icon = new Image(fileName);
			}
			catch (IllegalArgumentException iae)
			{
				logger.severe(name + ": couldn't load file " + fileName);
			}

			if ((icon != null) && !icon.isError())
			{
				iconFactory.put(hash, icon);
			}
			icon = iconFactory.get(hash);
		}
		return icon;
	}

	/**
	 * Logger accessor
	 * @return the current logger of this factory
	 */
	public static Logger getLogger()
	{
		return logger;
	}
}
