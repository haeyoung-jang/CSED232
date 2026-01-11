package edu.postech.csed232;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A number translator that converts numbers into words in Korean. For example,
 * 1234567890 is translated to "십이억삼천사백오십육만칠천팔백구십".
 * <p>
 * In this assignment, explicitly include "일"  for 100, 1000, 10000, and so on,
 * except for 10. For example, 10 is translated to "십", and 100 is translated
 * to "일백", and 1000 is translated to "일천".
 * <p>
 * Note: You should implement the abstract methods defined in the superclass.
 * Feel free to add more (private) methods and fields, if necessary. However, do
 * not change the signature of the public methods, or do not add any public method.
 */
public class KoreanNumberTranslator extends AbstractNumberTranslator
        implements NumberTranslator {

    KoreanNumberTranslator() {
        super(Locale.KOREAN);
    }

    //words
    String[] less_than_ten = {
            "", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"
    };
    String[] under_thousand = {
            "", "십", "백", "천"
    };
    String[] korean_units = {
            "", "만", "억", "조", "경", "해", "자"
    };

    @Override
    String[] split_chunks(long number) {
        ArrayList<String> chunks = new ArrayList<>();
        //String[] chunks = new String[(int) number / 4 + 1];
        String str_number = Long.toString(number);
        int num_length = str_number.length();
        int first_length = num_length % 4;

        if (first_length != 0) {
            chunks.add(str_number.substring(0, first_length));
        }
        for (int i = first_length; i < num_length; i += 4) {
            chunks.add(str_number.substring(i, i + 4));
        }
        return chunks.toArray(new String[0]);
    }

    @Override
    String translate(String chunk) {
        Integer number = Integer.valueOf(chunk);
        String chunk_word = "";
        // 0인 경우
        if (number == 0) {
            chunk_word = "영";
        } else {
            if (number / 1000 != 0) {
                chunk_word = less_than_ten[number / 1000] + under_thousand[3];
            }
            if ((number / 100) % 10 != 0) {
                chunk_word += less_than_ten[(number / 100) % 10] + under_thousand[2];
            }
            if ((number % 100) / 10 != 0) {
                if (number % 100 / 10 == 1) {
                    chunk_word += under_thousand[1];
                } else {
                    chunk_word += less_than_ten[(number % 100) / 10] + under_thousand[1];
                }
            }
            if ( number % 10 != 0) {
                chunk_word += less_than_ten[number % 10];
            }
        }
        return chunk_word;

    }

    @Override
    String unit(int k) {
        return korean_units[k];
    }

    public static void main(String[] args) {
        var input = 10000001201L;
        var translator = new KoreanNumberTranslator();

        System.out.println(input + " -> " + translator.toWords(input));
    }

}
