package main.java.com.traker.Authenticate;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Password
{

    public static String generatePasswordHash(String password)
    {
        int iterations = 5000;
        char[] passChars = password.toCharArray();
        byte[] salt = getSalt();
        try
        {
            PBEKeySpec keySpec = new PBEKeySpec(passChars, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(keySpec).getEncoded();
            return iterations + ":" + toHex(salt) + ":" + toHex(hash);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException ex)
        {
            ex.printStackTrace();
        }

        return "";
    }

    public static byte[] getSalt()
    {
        SecureRandom sr = null;
        try
        {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
        }
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    public static String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  + paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    public static boolean validatePassword(String originalPassword, String storedPassword)
    {
        try
        {
            String[] parts = storedPassword.split(":");
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = fromHex(parts[1]);
            byte[] hash = fromHex(parts[2]);

            PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] testHash = skf.generateSecret(spec).getEncoded();
            int diff = hash.length ^ testHash.length;
            for(int i = 0; i < hash.length && i < testHash.length; i++)
            {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException ex)
        {
            ex.printStackTrace();
        }

        return false;
    }

    public static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static String getRandomPasswordToken(int length){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        List<Character> temp = chars.chars().mapToObj(i -> (char) i).collect(Collectors.toList());
        Collections.shuffle(temp, new SecureRandom());
        return temp.stream()
                .map(Object::toString)
                .limit(length)
                .collect(Collectors.joining());
    }
}
