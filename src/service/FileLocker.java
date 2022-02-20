package service;

/**
 * Interface for implementing a FileLocker system
 *
 * @author Kyle Cancio
 */
public interface FileLocker {

    /**
     * Encrypt an entire file provided by the {@code path} with the provided {@code key}
     *
     * @param path Valid system path of an existing file or directory
     * @param key  Optional key to be used for encryption
     * @return True if the entire process is successful. False, otherwise, if any problem occurs
     * @throws Exception Any exception thrown from the encryption
     */
    boolean encryptFile(String path, String key) throws Exception;

    /**
     * Decrypt an entire file provided by the {@code path} with the provided {@code key}
     *
     * @param path Valid system path of an existing file or directory
     * @param key  Optional key to be used for encryption
     * @return True if the entire process is successful. False, otherwise, if any problem occurs
     * @throws Exception Any exception thrown from the encryption
     */
    boolean decryptFile(String path, String key) throws Exception;
}
