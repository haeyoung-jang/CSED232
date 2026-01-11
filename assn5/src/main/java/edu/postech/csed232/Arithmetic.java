package edu.postech.csed232;

import org.jetbrains.annotations.NotNull;

/**
 * Arithmetic interface for numbers with generic type T.
 * Provides methods for basic arithmetic operations.
 *
 * @param <T> the type of numbers
 */
public interface Arithmetic<T extends Number> {
    /**
     * Adds two numbers, and returns the result.
     *
     * @param a the first number
     * @param b the second number
     * @return the sum of a and b
     */
    @NotNull T add(@NotNull T a, @NotNull T b);

    /**
     * Multiplies two numbers, and returns the result.
     *
     * @param a the first number
     * @param b the second number
     * @return the product of a and b
     */
    @NotNull T mul(@NotNull T a, @NotNull T b);

    /**
     * Negates a number, and returns the result.
     *
     * @param a the number to negate
     * @return the negation of a
     */
    @NotNull T neg(@NotNull T a);

    /**
     * Returns the zero element of the arithmetic.
     *
     * @return the zero element
     */
    @NotNull T zero();

    /**
     * Returns the one element of the arithmetic.
     *
     * @return the one element
     */
    @NotNull T one();
}
