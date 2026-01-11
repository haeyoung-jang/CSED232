package edu.postech.csed232;

import com.sun.jdi.LongValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An abstract superclass that provides a skeletal implementation for converting numbers
 * into words. The toWords method is declared final so that they cannot be overridden by
 * subclasses. This is because the overall algorithm for translating numbers to words is
 * the same for all locales, and only the details of the translation differ between locales.
 * <p>
 * Note: You should define abstract methods to handle language-specific details, such as
 * splitting a number into chunks of 10^k, and translating each chunk into words (where
 * k=3 for English and k=4 for Korean). Each subclass should implement these abstract
 * methods to provide the details of the translation algorithm for its locale.
 * <p>
 * Note: Feel free to add more methods and fields, if necessary. However, do not change
 * the signature of the public methods, or do not add any public method.
 */
public abstract class AbstractNumberTranslator implements NumberTranslator {

    private final Locale locale; // the locale of this translator

    AbstractNumberTranslator(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    // abstract methods
    abstract String[] split_chunks(long number);
    abstract String translate(String chunk);
    abstract String unit(int k);

    @Override
    final public String toWords(long number) {
        // TODO: implement this method. This method cannot be overridden by subclasses. It
        // should call the abstract methods that you define to split the number into chunks
        // and translate each chunk into words.
        // 예외처리
        if (number < 0 || number > Long.MAX_VALUE) {
            throw new IllegalArgumentException("Number out of range");
        }
        List<String> words = new ArrayList<>();

        if (number == 0) {
            words.add(translate("0"));
        }else {
            // 수 분할
            String[] chunks = split_chunks(number);
            // 번역 + 단위
            for (int i = 0; i < chunks.length; i++) {
                if (!chunks[i].equals("000") && !chunks[i].equals("0000")) {
                    words.add(translate(chunks[i]) + unit(chunks.length - i - 1));
                }
            }
        }

        return String.join("", words).trim();
    }
}
