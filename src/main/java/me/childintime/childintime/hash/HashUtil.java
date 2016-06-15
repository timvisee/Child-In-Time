package me.childintime.childintime.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    /**
     * Hashing method that is used.
     */
    public static final String HASH_METHOD = "MD5";

    /**
     * Calculate the hash of a string.
     *
     * @param secret String to hash.
     *
     * @return Hash.
     *
     * @throws NoSuchAlgorithmException Throws if an error occurred.
     */
    public static String hash(String secret) throws NoSuchAlgorithmException {
        // Get the message digest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        // Update the digest
        messageDigest.update(secret.getBytes());

        // Calculate the digest
        byte[] digest = messageDigest.digest();

        // Build the hash string
        StringBuffer hash = new StringBuffer();
        for(byte b : digest)
            hash.append(String.format("%02x", b & 0xff));

        // Return the hash
        return hash.toString();
    }

    /**
     * Validate a hash.
     *
     * @param hash Hash.
     * @param secret Secret.
     *
     * @return True if valid, false if not.
     */
    public static boolean validate(String hash, String secret) {
        try {
            // Validate the hash
            return hash(secret).toUpperCase().equals(secret.toUpperCase())

        } catch(NoSuchAlgorithmException e) {
            // Print the stack trace
            e.printStackTrace();

            // Return false
            return false;
        }
    }
}
