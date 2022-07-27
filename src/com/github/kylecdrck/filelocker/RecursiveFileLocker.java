package com.github.kylecdrck.filelocker;

/**
 * Interface for implementing a FileLocker with directory depth traversal controls
 *
 * @author Kyle Cancio
 */
public interface RecursiveFileLocker extends FileLocker {

    /**
     * Lock file/s provided by the {@code targetPath} through encryption with the provided {@code password}
     *
     * @param targetPath Valid system path to an existing file or directory
     * @param password   Password to be used for encryption
     * @param lockDepth  Level of subdirectories to be traversed for the locking process
     * @return {@link FileLockDetail} Contain details about the encryption execution
     * @throws Exception Any exception thrown from the encryption
     */
    FileLockDetail lockFile(String targetPath, String password, int lockDepth) throws Exception;

    /**
     * Unlock file/s provided by the {@code targetPath} through decryption with the provided {@code password}
     *
     * @param targetPath Valid system path to an existing file or directory
     * @param password   Password to be used for decryption
     * @param unlockDepth  Level of subdirectories to be traversed for the unlocking process
     * @return {@link FileLockDetail} Contain details about the decryption execution
     * @throws Exception Any exception thrown from the encryption
     */
    FileLockDetail unlockFile(String targetPath, String password, int unlockDepth) throws Exception;
}
