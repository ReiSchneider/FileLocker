package service;

/**
 * Interface for implementing a FileLocker system
 *
 * @author Kyle Cancio
 */
public interface FileLocker {
    boolean encryptFile(String path, String key) throws Exception;
    boolean decryptFile(String path, String key) throws Exception;
}
