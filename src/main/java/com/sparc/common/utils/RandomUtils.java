package com.sparc.common.utils;

import org.apache.commons.lang.RandomStringUtils;

/**
 * User: Dayel Ostraco
 * Date: 10/24/12
 * Time: 10:56 PM
 */
public class RandomUtils {

    public static int getRandomIntBetween(int min, int max){

        //Quick check to ensure user entered min and max correctly
        if(min>max){
            throw new IllegalArgumentException("Cannot draw random int from invalid range [" + min + ", " + max + "].");
        }

        return Math.abs(min + (int)(Math.random() * ((max - min) + 1)));
    }

    public static String getRandomString(int minLength, int maxLength){
        return RandomStringUtils.randomAlphanumeric(getRandomIntBetween(minLength, maxLength));
    }
}
