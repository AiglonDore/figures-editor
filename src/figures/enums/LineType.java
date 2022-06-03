package figures.enums;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The type of stroke (solid, dashed or none) to apply on {@link figures.Figure}s.
 * @author davidroussel
 */
public enum LineType
{
	/**
	 * No stroke
	 */
	NONE,
	/**
	 * Plain stroke
	 */
	SOLID,
	/**
	 * Dashed stroke
	 */
	DASHED;

	/**
	 * Possible number of line types
	 */
	public static final int NbLineTypes = 3;

	/*
	 * Note: Enum.class provides default final hashCode and equals methods,
	 * so it's no use trying to overload them
	 */

	/**
	 * Convertion from integer index
	 * @param i the index
	 * @return the corresponding enum value.
	 * If i is outside of the range [0..NbLineTypes[, {@link #NONE} is returned
	 */
	public static LineType fromInteger(int i)
	{
		switch (i)
		{
			case 0:
				return NONE;
			case 1:
				return SOLID;
			case 2:
				return DASHED;
			default:
				return NONE;
		}
	}

	/**
	 * Conversion to integer
	 * @return the index of this enum value
	 * @throws AssertionError if this enum has unexpected value
	 */
	public int intValue() throws AssertionError
	{
		switch (this)
		{
			case NONE:
				return 0;
			case SOLID:
				return 1;
			case DASHED:
				return 2;
		}

		throw new AssertionError("LineType unknown assertion: " + this);
	}

	/*
	 * Note: Enum.class provides a default final hashCode method, so it's no use
	 * trying to overload int hashCode() method
	 */

	/**
	 * String representation of this enum
	 * @return a new String representing this value
	 * @throws AssertionError if this enum has unexpected value
	 */
	@Override
	public String toString() throws AssertionError
	{
		switch (this)
		{
			case NONE:
				return new String("None");
			case SOLID:
				return new String("Solid");
			case DASHED:
				return new String("Dashed");
		}

		throw new AssertionError("LineType Unknown assertion " + this);
	}

	/**
	 * Obtention d'un tableau de string contenant tous les noms des types.
	 * A utiliser lors de la création d'un combobox avec :
	 * LineType.stringValues()
	 * @return un tableau de string contenant tous les noms des types
	 */
	public static String[] stringValues()
	{
		LineType[] values = LineType.values();
		String[] stringValues = new String[values.length];
		for (int i = 0; i < values.length; i++)
		{
			stringValues[i] = values[i].toString();
		}

		return stringValues;
	}

	/**
	 * Creates a collection of all possible Line types.
	 * Usefull to fill a {@link javafx.scene.control.ComboBox}
	 * Can be used to fill a {@link javafx.scene.control.ComboBox}<Linetype>
	 * @return a collection of all possible Line types
	 * @see application.Controller#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	public static Collection<LineType> all()
	{
		Collection<LineType> list = new ArrayList<>();
		list.add(NONE);
		list.add(SOLID);
		list.add(DASHED);
		return list;
	}
}
