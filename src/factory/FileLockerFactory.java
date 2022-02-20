package factory;

import service.FileLocker;
import service.FileLockerImpl;

/**
 * Factory for creating instances of the {@link FileLocker} interface
 *
 * @author Kyle Cancio
 */
public final class FileLockerFactory {
    private FileLockerFactory() {}

    public static FileLocker getFileLocker() {
        return new FileLockerImpl();
    }
}
