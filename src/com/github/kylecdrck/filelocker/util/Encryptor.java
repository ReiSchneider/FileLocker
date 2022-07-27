package com.github.kylecdrck.filelocker.util;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Utility class for providing encryption and decryption tools
 * <p>
 * v0.0.1
 *
 * @author KyleCancio
 */
public final class Encryptor {

    // Replace with your own salt key (max 16 chars)
    private static final byte[] WRAP_KEY_SALT = "f1l3L0ck3r1x2022".getBytes(StandardCharsets.UTF_8);

    private Encryptor() {
    }

    /**
     * Encrypt the given {@code data} given the provided {@code key}
     *
     * @param data     Bytes of the data to be encrypted
     * @param password Password to be used for encryption
     * @return A byte array of encrypted bytes
     * @throws NoSuchPaddingException    See {@link NoSuchPaddingException}
     * @throws IllegalBlockSizeException See {@link IllegalBlockSizeException}
     * @throws NoSuchAlgorithmException  See {@link NoSuchAlgorithmException}
     * @throws BadPaddingException       See {@link BadPaddingException}
     * @throws InvalidKeyException       See {@link InvalidKeyException}
     */
    public static byte[] encrypt(byte[] data, String password) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        return cipherBytes(data, generateKeyFromPassword(password), Cipher.ENCRYPT_MODE);
    }

    /**
     * Decrypt the given {@code data} given the provided {@code key}
     *
     * @param data     Bytes of the data to be decrypted
     * @param password Password to be used for decryption
     * @return A byte array of decrypted bytes
     * @throws NoSuchPaddingException    See {@link NoSuchPaddingException}
     * @throws IllegalBlockSizeException See {@link IllegalBlockSizeException}
     * @throws NoSuchAlgorithmException  See {@link NoSuchAlgorithmException}
     * @throws BadPaddingException       See {@link BadPaddingException}
     * @throws InvalidKeyException       See {@link InvalidKeyException}
     */
    public static byte[] decrypt(byte[] data, String password) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        return cipherBytes(data, generateKeyFromPassword(password), Cipher.DECRYPT_MODE);
    }

    /**
     * Generate a properly encoded and wrapped key from {@code password} for all encryption and decryption processes.
     * The password is transformed into a {@link SecretKey}  by encoding it with a salt value using PBKDF2WithHmacSHA256 algorithm
     *
     * @param password Password to be used for encryption/decryption
     * @return A byte array of the w key
     * @throws NoSuchAlgorithmException  See {@link NoSuchAlgorithmException}
     * @throws InvalidKeySpecException See {@link InvalidKeySpecException}
     */
    private static Key generateKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), WRAP_KEY_SALT, 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
    }

    /**
     * @param data       Bytes of the data to be processed for encryption or decryption
     * @param key        Instance of a {@link Key} to be used for encryption or decryption
     * @param cipherMode See {@link Cipher}. 1=Encrypt, 2=Decrypt
     * @return Byte array of ciphered bytes
     * @throws NoSuchPaddingException    See {@link NoSuchPaddingException}
     * @throws NoSuchAlgorithmException  See {@link NoSuchAlgorithmException}
     * @throws InvalidKeyException       See {@link InvalidKeyException}
     * @throws IllegalBlockSizeException See {@link IllegalBlockSizeException}
     * @throws BadPaddingException       See {@link BadPaddingException}
     */
    private static byte[] cipherBytes(byte[] data, Key key, int cipherMode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(cipherMode, key);

        return cipher.doFinal(data);
    }
}
