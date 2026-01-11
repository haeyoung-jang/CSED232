package edu.postech.csed232;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KoreanNumberTranslatorTest {

    final KoreanNumberTranslator translator = new KoreanNumberTranslator();

    @ParameterizedTest
    @CsvSource({
            "0, 영",
            "1234, 일천이백삼십사",
            "111111, 십일만일천일백십일",
            "10000001201, 일백억일천이백일",
            "1234567890, 십이억삼천사백오십육만칠천팔백구십",
            "31057642375, 삼백십억오천칠백육십사만이천삼백칠십오"
    })
    void testToWords(long number, String expected) {
        assertEquals(expected, translator.toWords(number));
    }
}