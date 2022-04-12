package service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Utility class for providing encryption and decryption tools
 * <p>
 * v0.0.1
 *
 * @author KyleCancio
 */
public final class Encryptor {

    private static final int KEY_BYTE_LENGTH = 16;

    private Encryptor() {
    }

    /**
     * Encrypt the given {@code data} given the provided {@code key}
     *
     * @param data Bytes of the data to be encrypted
     * @param key  Bytes of the key to be used for encryption
     * @return A byte array of encrypted bytes
     * @throws NoSuchPaddingException    See {@link NoSuchPaddingException}
     * @throws IllegalBlockSizeException See {@link IllegalBlockSizeException}
     * @throws NoSuchAlgorithmException  See {@link NoSuchAlgorithmException}
     * @throws BadPaddingException       See {@link BadPaddingException}
     * @throws InvalidKeyException       See {@link InvalidKeyException}
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return cipherBytes(data, wrapKey(key), Cipher.ENCRYPT_MODE);
    }

    /**
     * Decrypt the given {@code data} given the provided {@code key}
     *
     * @param data Bytes of the data to be decrypted
     * @param key  Bytes of the key to be used for decryption
     * @return A byte array of decrypted bytes
     * @throws NoSuchPaddingException    See {@link NoSuchPaddingException}
     * @throws IllegalBlockSizeException See {@link IllegalBlockSizeException}
     * @throws NoSuchAlgorithmException  See {@link NoSuchAlgorithmException}
     * @throws BadPaddingException       See {@link BadPaddingException}
     * @throws InvalidKeyException       See {@link InvalidKeyException}
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return cipherBytes(data, wrapKey(key), Cipher.DECRYPT_MODE);
    }

    /**
     * Wrap/truncate, and encode the provided {@code key} to fit the key-length of {@literal 16}
     *
     * @param key Bytes of the key to be used for encryption/decryption
     * @return A byte array of the updated key
     */
    private static byte[] wrapKey(byte[] key) {
        byte[] finalKey = new byte[KEY_BYTE_LENGTH];

        key = Base64.getEncoder().encode(key);

        if (key.length < KEY_BYTE_LENGTH) {
            int i;

            for (i = 0; i < key.length; i++) {
                finalKey[i] = key[i];
            }

            for (; i < KEY_BYTE_LENGTH; i++) {
                finalKey[i] = '*';
            }

        } else {
            System.arraycopy(key, 0, finalKey, 0, KEY_BYTE_LENGTH);
        }

        return finalKey;
    }

    /**
     * @param data       Bytes of the data to be processed for encryption or decryption
     * @param key        Bytes of the key to be used for encryption or decryption
     * @param cipherMode See {@link Cipher}. 1=Encrypt, 2=Decrypt
     * @return Byte array of ciphered bytes
     * @throws NoSuchPaddingException    See {@link NoSuchPaddingException}
     * @throws NoSuchAlgorithmException  See {@link NoSuchAlgorithmException}
     * @throws InvalidKeyException       See {@link InvalidKeyException}
     * @throws IllegalBlockSizeException See {@link IllegalBlockSizeException}
     * @throws BadPaddingException       See {@link BadPaddingException}
     */
    private static byte[] cipherBytes(byte[] data, byte[] key, int cipherMode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        cipher.init(cipherMode, secretKey);

        return cipher.doFinal(data);
    }
}
