package lv.celokopa.app.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by alex on 16.19.7.
 */
public class RandomStringGenerator {
    private static SecureRandom random = new SecureRandom();

    public static String getRandomString(int numBits) {
        return new BigInteger(numBits, random).toString(32);
    }
}
