package service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Implementation of the {@link FileLocker} interface using encryption
 *
 * @author Kyle Cancio
 */
public class SecuredFileLockerImpl implements FileLocker {

    private static final Logger logger = Logger.getLogger(SecuredFileLockerImpl.class.getName());

    public boolean encryptFile(String filePath, String key) throws Exception {

        if (Objects.isNull(key)) {
            logger.warning("Key is not present. Will use default key for encryption");
        }

        Path p = Path.of(filePath);

        if (!Files.exists(p)) {
            logger.warning("File or directory does not exist");
            return false;
        }

        if (Files.isDirectory(p)) {
            return encryptFiles(p, key);
        } else {
            return encryptFile(p, key);
        }
    }

    private boolean encryptFile(Path p, String key) {
        logger.info(String.format("Encrypting file: %s", p));
        try {
            String pFileName = p.getFileName().toString();
            Files.writeString(p, "\nxx>>" + pFileName, StandardOpenOption.APPEND);

            byte[] b = Files.readAllBytes(p);
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

            b = Encryptor.encrypt(b, keyBytes);

            Path newPath = p.getParent().resolve(UUID.randomUUID().toString());

            Files.deleteIfExists(p);
            Files.write(newPath, b);

            return true;
        } catch (Exception e) {
            logger.warning("Failed to encrypt " + p);
        }
        return false;
    }

    private boolean encryptFiles(Path p, String key) throws Exception {
        AtomicBoolean success = new AtomicBoolean(true);

        Files.walk(p)
                .filter(filePath -> !filePath.equals(p))
                .forEach(filePath -> {
                    try {
                        boolean successful = success.get();
                        if (Files.isDirectory(filePath)) {
                            successful &= encryptFiles(filePath, key);
                        } else {
                            successful &= encryptFile(filePath, key);
                        }
                        success.set(successful);
                    } catch (Exception e) {
                        logger.warning(e.getMessage());
                        logger.warning("Failed to decrypt file " + filePath);
                        success.set(false);
                    }
                });
        return success.get();
    }

    public boolean decryptFile(String filePath, String key) throws Exception {

        if (Objects.isNull(key)) {
            logger.warning("Key is not present. Will use default key for decryption");
        }

        Path p = Path.of(filePath);

        if (!Files.exists(p)) {
            logger.warning("File or directory does not exist");
            return false;
        }

        if (Files.isDirectory(p)) {
            return decryptFiles(p, key);
        } else {
            return decryptFile(p, key);
        }
    }

    private boolean decryptFile(Path p, String key) {
        logger.info(String.format("Decrypting file: %s", p));
        try {
            byte[] fileBytes = Files.readAllBytes(p);
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

            fileBytes = Encryptor.decrypt(fileBytes, keyBytes);

            String[] st = new String(fileBytes).split("\n");

            String fileName = st[st.length - 1];

            if (!fileName.contains("xx>>")) {
                logger.warning("Invalid encrypted file");
                return false;
            }

            String newFileName = fileName.replace("xx>>", "");

            byte[] oldStr = fileName.getBytes(StandardCharsets.UTF_8);
            byte[] newFile = new byte[fileBytes.length - oldStr.length];

            System.arraycopy(fileBytes, 0, newFile, 0, newFile.length);

            Path p1 = p.getParent().resolve(newFileName);
            Files.deleteIfExists(p);
            Files.write(p1, newFile);

            return true;
        } catch (Exception e) {
            logger.warning("Failed to decrypt " + p);
        }
        return false;
    }

    private boolean decryptFiles(Path p, String key) throws Exception {
        AtomicBoolean success = new AtomicBoolean(true);
        Files.walk(p)
                .filter(filePath -> !filePath.equals(p))
                .forEach(filePath -> {
                    try {
                        boolean successful = success.get();
                        if (Files.isDirectory(filePath)) {
                            successful &= decryptFiles(filePath, key);
                        } else {
                            successful &= decryptFile(filePath, key);
                        }
                        success.set(successful);
                    } catch (Exception e) {
                        logger.warning(e.getMessage());
                        logger.warning("Failed to decrypt file " + filePath);
                        success.set(false);
                    }
                });
        return success.get();
    }
}
