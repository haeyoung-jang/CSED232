package edu.postech.csed232;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnglishNumberTranslatorTest {

    final EnglishNumberTranslator translator = new EnglishNumberTranslator();

    @ParameterizedTest
    @CsvSource({
            "0, zero",
            "1234, one thousand two hundred thirty four",
            "111111, one hundred eleven thousand one hundred eleven",
            "10000001201, ten billion one thousand two hundred one",
            "1234567890, one billion two hundred thirty four million five hundred sixty seven thousand eight hundred ninety",
            "31057642375, thirty one billion fifty seven million six hundred forty two thousand three hundred seventy five"
    })
    void testToWords(long number, String expected) {
        assertEquals(expected, translator.toWords(number));
    }

}