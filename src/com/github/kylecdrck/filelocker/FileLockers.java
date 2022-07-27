package com.github.kylecdrck.filelocker;

/**
 * Factory and utility class for {@link FileLocker} and {@link RecursiveFileLocker} defined in this package
 * <p>
 * This class supports the following methods:
 * <ul>
 *   <li>Methods that create and return a {@link RecursiveFileLocker}
 *       set up with commonly useful configuration settings.
 * </ul>
 * <p>
 * Currently, both interfaces are implemented by {@link BasicFileLocker}
 *
 * @author KyleCancio
 */
public class FileLockers {
    public static RecursiveFileLocker newRecursiveFileLocker() {
        return new BasicFileLocker();
    }
}
