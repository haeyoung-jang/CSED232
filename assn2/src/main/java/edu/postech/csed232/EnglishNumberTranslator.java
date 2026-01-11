package edu.postech.csed232;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A number translator that converts numbers to words in English. For example,
 * 1234567890 is translated to "one billion two hundred thirty four million five
 * hundred sixty seven thousand eight hundred ninety".
 * <p>
 * Note: You should implement the abstract methods defined in the superclass.
 * Feel free to add more (private) methods and fields, if necessary. However, do
 * not change the signature of the public methods, or do not add any public method.
 */
public class EnglishNumberTranslator extends AbstractNumberTranslator
        implements NumberTranslator {

    EnglishNumberTranslator() {
        super(Locale.ENGLISH);
    }

    // words
    String[] less_than_twenty = {
            "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
            "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
    };
    String[] tens = {
            "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
    };
    String[] thousands = {
            "", " thousand ", " million ", " billion ", " trillion ", " quadrillion ", " quintillion "
    };

    @Override
    String[] split_chunks(long number) {
        ArrayList<String> chunks = new ArrayList<>();
        String str_number = Long.toString(number);
        int num_length = str_number.length();
        int first_length = num_length % 3;

        if (first_length != 0) {
            chunks.add(str_number.substring(0, first_length));
        }
        for (int i = first_length; i < num_length; i += 3) {
            chunks.add(str_number.substring(i, i + 3));
        }
        return chunks.toArray(new String[0]);
    }

    @Override
    String translate(String chunk) {
        Integer number = Integer.valueOf(chunk);
        String chunk_word  = "" ;
        // 0인 경우
        if (number == 0) {
            chunk_word = "zero";
        }
        // 20이하인 경우
        if (number < 20 && number > 0){
            chunk_word = less_than_twenty[number];
        }
        // 20이상인 경우
        else {
            if (number >= 100) {
                chunk_word = less_than_twenty[number / 100] + " hundred ";
                // 10의 자리 숫자가 2 이상인 경우
                if ((number % 100) / 10 > 1) {
                    chunk_word += tens[(number % 100) / 10] + " " + less_than_twenty[number % 10] + " ";
                } else{
                    chunk_word += less_than_twenty[number % 100];
                }
            } else {
                // 10의 자리 숫자가 2 이상인 경우
                if ((number % 100) / 10 > 1) {
                    chunk_word += tens[(number % 100) / 10] + " " + less_than_twenty[number % 10] + " ";
                } else {
                    chunk_word += less_than_twenty[number % 100];
                }
            }
        }
        return chunk_word.trim();
    }

    @Override
    String unit(int k) {
        return thousands[k];
    }

    public static void main(String[] args) {
        var input = 10000001201L;
        var translator = new EnglishNumberTranslator();

        System.out.println(input + " -> " + translator.toWords(input));
    }
}
