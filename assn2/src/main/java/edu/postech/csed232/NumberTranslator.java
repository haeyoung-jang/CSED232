package edu.postech.csed232;

import java.util.Locale;

/**
 * A number translator translates a number to words and vice versa, according
 * to its locale. Each subclass of this class considers a specific locale.
 * In this assignment, you should implement EnglishNumberTranslator and
 * KoreanNumberTranslator.
 */
public interface NumberTranslator {

    /**
     * Convert a number to words, according to the locale of this translator.
     * The number should be in the range [0, Long.MAX_VALUE], and an exception
     * should be thrown if the number is not in the range.
     *
     * @param number a number
     * @return a string of words
     * @throws IllegalArgumentException if the number is not in the range
     */
    String toWords(long number);

    /**
     * Return the locale of this translator.
     *
     * @return a locale
     */
    Locale getLocale();
}
