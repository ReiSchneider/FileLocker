package com.github.kylecdrck.filelocker;

import com.github.kylecdrck.filelocker.util.Encryptor;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Basic implementation of the {@link FileLocker} interface
 * Provides a basic procedure of encrypting/decrypting files with a user-defined key, and an optional recursive depth
 *
 * @author Kyle Cancio
 */
public class BasicFileLocker implements RecursiveFileLocker {

    private static final Logger logger = Logger.getLogger(BasicFileLocker.class.getName());

    @Override
    public FileLockDetail lockFile(String targetPath, String password) {
        return lockFile(targetPath, password, 1);
    }

    private FileLockDetail lockFiles(Path targetPath, String password, int depth) {

        List<Path> fileList = getFileList(targetPath, depth);

        int total = fileList.size();
        int success = 0, failed = 0;

        for (Path path : fileList) {
            if (lockFileContent(path, password)) {
                success++;
            } else {
                failed++;
            }
        }

        return new FileLockDetail(total, success, failed, null);
    }

    private boolean lockFileContent(Path filePath, String password) {
        logger.info(String.format("Encrypting file: %s", filePath));
        try {
            String pFileName = filePath.getFileName().toString();
            Files.writeString(filePath, "\nLOCKED FILE::" + pFileName, StandardOpenOption.APPEND);

            byte[] b = Files.readAllBytes(filePath);

            b = Encryptor.encrypt(b, password);

            Path newPath = filePath.getParent().resolve(UUID.randomUUID().toString());

            Files.deleteIfExists(filePath);
            Files.write(newPath, b);
            return true;
        } catch (Exception e) {
            logger.warning("Failed to encrypt " + filePath + " caused by " + e.getMessage());
        }
        return false;
    }

    @Override
    public FileLockDetail unlockFile(String targetPath, String password) {
        return unlockFile(targetPath, password, 1);
    }

    private FileLockDetail unlockFiles(Path targetPath, String password, int depth) {

        List<Path> fileList = getFileList(targetPath, depth);

        int total = fileList.size();
        int success = 0, failed = 0;

        for (Path path : fileList) {
            if (unlockFileContent(path, password)) {
                success++;
            } else {
                failed++;
            }
        }

        return new FileLockDetail(total, success, failed, null);
    }

    private boolean unlockFileContent(Path filePath, String password) {
        logger.info(String.format("Decrypting file: %s", filePath));
        try {
            byte[] fileBytes = Files.readAllBytes(filePath);

            fileBytes = Encryptor.decrypt(fileBytes, password);

            String[] st = new String(fileBytes).split("\n");

            String fileName = st[st.length - 1];

            if (!fileName.contains("LOCKED FILE::")) {
                logger.warning("Invalid file: " + filePath);
                return false;
            }

            String newFileName = fileName.replace("LOCKED FILE::", "");

            byte[] oldStr = fileName.getBytes(StandardCharsets.UTF_8);
            byte[] newFile = new byte[fileBytes.length - oldStr.length];

            System.arraycopy(fileBytes, 0, newFile, 0, newFile.length);

            Path p1 = filePath.getParent().resolve(newFileName);
            Files.deleteIfExists(filePath);
            Files.write(p1, newFile);

            return true;
        } catch (Exception e) {
            logger.warning("Failed to decrypt " + filePath + " caused by " + e.getMessage());
        }
        return false;
    }

    @Override
    public FileLockDetail lockFile(String targetPath, String password, int lockDepth) {
        Path p = Path.of(targetPath);

        if (!Files.exists(p)) {
            logger.warning("File or directory does not exist");
            return new FileLockDetail(0, 0, 0, "File or directory does not exist");
        }

        return lockFiles(p, password, lockDepth);
    }

    @Override
    public FileLockDetail unlockFile(String targetPath, String password, int unlockDepth) {
        Path p = Path.of(targetPath);

        if (!Files.exists(p)) {
            logger.warning("File or directory does not exist");
            return new FileLockDetail(0, 0, 0, "File or directory does not exist");
        }

        return unlockFiles(p, password, unlockDepth);

    }
}
