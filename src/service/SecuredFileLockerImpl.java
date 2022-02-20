package service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
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
            encryptFiles(p, key);
        } else {
            encryptFile(p, key);
        }

        return true;

    }

    private void encryptFile(Path p, String key) throws Exception {
        logger.info(String.format("Encrypting file: %s", p));

        String pFileName = p.getFileName().toString();

        byte[] b = Files.readAllBytes(p);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        b = Encryptor.encrypt(b, keyBytes);

        Files.write(p, b);

        Files.writeString(p, "\nxx>>" + pFileName, StandardOpenOption.APPEND);
    }

    private void encryptFiles(Path p, String key) throws Exception {
        Files.walk(p)
                .filter(filePath -> !filePath.equals(p))
                .forEach(filePath -> {
                    try {
                        if (Files.isDirectory(filePath)) {
                            encryptFiles(filePath, key);
                        } else {
                            encryptFile(filePath, key);
                        }
                    } catch (Exception e) {
                        logger.warning(e.getMessage());
                    }
                });
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
            decryptFiles(p, key);
        } else {
            decryptFile(p, key);
        }

        return true;
    }

    private void decryptFile(Path p, String key) throws Exception {
        logger.info(String.format("Decrypting file: %s", p));

        byte[] fileBytes = Files.readAllBytes(p);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        String[] st = new String(fileBytes).split("\n");

        String fileName = st[st.length - 1];

        if (!fileName.contains("xx>>")) {
            logger.warning("Invalid encrypted file");
            return;
        }

        String newFileName = fileName.replace("xx>>", "");

        byte[] oldStr = fileName.getBytes(StandardCharsets.UTF_8);
        byte[] newFile = new byte[fileBytes.length - oldStr.length];

        for (int i = 0; i < newFile.length; i++) {
            newFile[i] = fileBytes[i];
        }

        newFile = Encryptor.decrypt(newFile, keyBytes);

        Path p1 = p.getParent().resolve(newFileName);
        Files.write(p1, newFile);
    }

    private void decryptFiles(Path p, String key) throws Exception {
        Files.walk(p)
                .filter(filePath -> !filePath.equals(p))
                .forEach(filePath -> {
                    try {
                        if (Files.isDirectory(filePath)) {
                            decryptFiles(filePath, key);
                        } else {
                            decryptFile(filePath, key);
                        }
                    } catch (Exception e) {
                        logger.warning(e.getMessage());
                    }
                });
    }
}
