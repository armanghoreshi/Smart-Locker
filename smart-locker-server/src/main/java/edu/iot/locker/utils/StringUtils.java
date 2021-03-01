package edu.iot.locker.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class StringUtils {

    /*
     * use for hashing password before saving to database
     */
    public static String getHashedString(String content) {
        return DigestUtils.sha256Hex(content);
    }

    /*
     * generate random token
     */
    public static String generateRandomString() {
        return RandomStringUtils.randomAlphanumeric(32);
    }
}
