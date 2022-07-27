package com.github.kylecdrck.filelocker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * Interface for implementing a FileLocker
 *
 * @author Kyle Cancio
 */
public interface FileLocker {

    /**
     * Lock file/s provided by the {@code targetPath} through encryption with the provided {@code password}
     *
     * @param targetPath Valid system path to an existing file or directory
     * @param password   Password to be used for encryption
     * @return {@link FileLockDetail} Contain details about the encryption execution
     * @throws Exception Any exception thrown from the encryption
     */
    FileLockDetail lockFile(String targetPath, String password) throws Exception;

    /**
     * Unlock file/s provided by the {@code targetPath} through decryption with the provided {@code password}
     *
     * @param targetPath Valid system path to an existing file or directory
     * @param password   Password to be used for decryption
     * @throws Exception Any exception thrown from the encryption
     */
    FileLockDetail unlockFile(String targetPath, String password) throws Exception;


    /**
     * Get list of {@link Path} to be processed by the locker
     *
     * @param targetPath Target root path to be traversed
     * @param depth      Level of subdirectories to be traversed
     * @return List of {@link Path}
     */
    default List<Path> getFileList(Path targetPath, int depth) {
        try (var filePaths = Files.walk(targetPath, depth).filter(p -> !Files.isDirectory(p))) {
            return filePaths.toList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
