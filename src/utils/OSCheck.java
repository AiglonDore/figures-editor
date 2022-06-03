package utils;

/**
 * Utility class to detect OS type
 * @author DavidRoussel
 */
public final class OSCheck
{
	/**
	 * OS types
	 */
	public enum OSType
	{
		/**
		 * Windows systems
		 */
		Windows,
		/**
		 * Mac OS System
		 */
		MacOS,
		/**
		 * Linuxes sytem
		 */
		Linux,
		/**
		 * Other kind of system
		 */
		Other
	};

	/**
	 * cached result of OS detection
	 */
	protected static OSType detectedOS = null;

	/**
	 * Detects the operating system from the os.name System property and cache
	 * the result so it can be retrieved faster next time
	 * @return the operating system detected
	 */
	public static OSType getOperatingSystemType()
	{
		if (detectedOS == null)
		{
			String os = System.getProperty("os.name", "generic").toLowerCase();
			if ((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0))
			{
				detectedOS = OSType.MacOS;
			}
			else if (os.indexOf("win") >= 0)
			{
				detectedOS = OSType.Windows;
			}
			else if (os.indexOf("nux") >= 0)
			{
				detectedOS = OSType.Linux;
			}
			else
			{
				detectedOS = OSType.Other;
			}
		}
		return detectedOS;
	}
}
