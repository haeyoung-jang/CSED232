package edu.postech.csed232.homework1;

/**
 * A number translator that converts numbers to words in English.
 */
@SuppressWarnings("GrazieInspection")
public class EnglishNumberTranslator {

    /**
     * Convert a number to words in English. The number should be in the range
     * [0, Long.MAX_VALUE], and otherwise an exception should be thrown. For example,
     * 1234567890 should be translated to "one billion two hundred thirty four million
     * five hundred sixty seven thousand eight hundred ninety".
     *
     * @param number a number
     * @return a string of words
     * @throws IllegalArgumentException if the number is not in the range
     */
    public String toWords(long number) {
        // english words that less than twenty
        String[] less_than_twenty = {
            "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
            "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
        };
        // english words that tens
        String[] tens = {
            "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
        };
        // english words that thousands
        String[] thousands = {
            "", "thousand", "million", "billion", "trillion", "quadrillion", "quintillion"
        };

        // illegal argument exception
        if (number < 0 || number > Long.MAX_VALUE) {
            throw new IllegalArgumentException("Number out of range (0 to Long.MAX_VALUE)");
        }
        // if zero
        if (number == 0) {
            return "zero";
        }

        // to convert hundreds integer to english
        class Hundred {
            String hundred(int num) {
                String hun_word = "";
                if (num == 0){
                    return "";
                }
                if (num < 20) {
                    hun_word = less_than_twenty[num];
                } else if(num < 100) {  // if a number is less than 100, not add hundreds
                    if ((num % 100 / 10) > 1){
                        hun_word = tens[num % 100 / 10] + " " + less_than_twenty[num % 10];
                    }
                    else {
                        hun_word = less_than_twenty[num % 100];
                    }
                } else {  // if a number is more than 100, add hundreds
                    if ((num % 100 / 10) > 1) {
                        hun_word = less_than_twenty[num / 100] + " hundred " + tens[num % 100 / 10] + " " + less_than_twenty[num % 10];
                    } else {
                        hun_word = less_than_twenty[num / 100] + " hundred " + less_than_twenty[num % 100];
                    }
                }
                return hun_word.trim();  // remove blank at side
            }
        }

        Hundred hundred = new Hundred();
        String word = "";
        int thousand_index = 0;
        // translate numebr until number become 0
        while (number > 0) {
            if (number % 1000 != 0) {
                int hund_num = (int) (number % 1000);
                String thou_word = hundred.hundred(hund_num) + " " + thousands[thousand_index]+ " ";
                word = thou_word + word;
            }
            number /= 1000;
            thousand_index++;
        }
        return word.trim();   // remove blank at side
    }


    public static void main(String[] args) {
        var input = 31057642375L;
        var translator = new EnglishNumberTranslator();

        System.out.println(input + " -> " + translator.toWords(input));
    }

}
