package service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class Encryptor {

    private static int KEY_BYTE_LENGTH = 16;

    private Encryptor() {
    }

    public static byte[] encrypt(byte[] data, byte[] key) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return cipherBytes(data, key, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decrypt(byte[] data, byte[] key) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return cipherBytes(data, key, Cipher.DECRYPT_MODE);
    }

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

    private static byte[] cipherBytes(byte[] data, byte[] key, int cipherMode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        cipher.init(cipherMode, secretKey);

        return cipher.doFinal(data);
    }
}
