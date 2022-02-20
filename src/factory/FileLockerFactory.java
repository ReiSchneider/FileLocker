package factory;

import service.FileLocker;
import service.FileLockerImpl;
import service.SecuredFileLockerImpl;

/**
 * Factory for creating instances of the {@link FileLocker} interface
 *
 * @author Kyle Cancio
 */
public final class FileLockerFactory {
    private FileLockerFactory() {
    }

    public static FileLocker getFileLocker() {
        return new FileLockerImpl();
    }

    public static FileLocker getSecuredFileLocker() {
        return new SecuredFileLockerImpl();
    }
}
