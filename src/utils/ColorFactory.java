package utils;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

/**
 * Color Factory to get frequently used {@link Color}s.
 * @author davidroussel
 */
public class ColorFactory
{
	/**
	 * Map associant des noms de couleurs standard à des {@link Paint} standards
	 */
	/**
	 * Map containing simple colors (black, blue, ..., white)
	 */
	private static final Map<String, Color> standardColors = standardColorsMap();

	/**
	 * Flyweight factory containing all requested colors
	 */
	private static FlyweightFactory<Color> colorFactory =
		new FlyweightFactory<Color>();

	/**
	 * Simple Colors construction
	 * @return a map containing some of the simple named colors
	 */
	private static Map<String, Color> standardColorsMap()
	{
		Map<String, Color> map = new HashMap<String, Color>();
		map.put("Black", Color.BLACK);
		map.put("Blue", Color.BLUE);
		map.put("Cyan", Color.CYAN);
		map.put("Green", Color.GREEN);
		map.put("Magenta", Color.MAGENTA);
		map.put("None", Color.TRANSPARENT); // Or null
		map.put("Orange", Color.ORANGE);
		map.put("Pink", Color.PINK);
		map.put("Red", Color.RED);
		map.put("White", Color.WHITE);
		map.put("Yellow", Color.YELLOW);

		return map;
	}

	/**
	 * Access to the required color from factory.
	 * If this clolor is not aloready part of the factory it is added and then
	 * returned
	 * @param color the required color
	 * @return an instance of the required color from the factory
	 */
	public static Color getColor(Color color)
	{
		if (color == null)
		{
			return null;
		}
		return colorFactory.get(color);
	}

	/**
	 * Access to the required color by name
	 * @param colorName the name of the required color
	 * @return the required color if found in the standard colors, or null if such
	 * a color name is unknown.
	 */
	public static Color getColor(String colorName)
	{
		if ((colorName == null) || colorName.isEmpty()
		    || !standardColors.containsKey(colorName))
		{
			return null;
		}
		return colorFactory.get(standardColors.get(colorName));
	}
}
