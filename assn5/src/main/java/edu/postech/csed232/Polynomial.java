package edu.postech.csed232;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a polynomial over variables with coefficients of type {@code T}.
 * <p>
 * This interface is implemented by various record types representing
 * sum of polynomials, monomials, and zero polynomial.
 *
 * @param <T> the numeric type of the polynomial coefficients
 */
public sealed interface Polynomial<T extends Number> permits PolySum, Monomial, PolyZero {

    /**
     * Create the zero polynomial (constant 0).
     *
     * @param <T> the numeric type
     * @return the zero polynomial
     */
    static <T extends Number> @NotNull Polynomial<T> zero() {
        return new PolyZero<>();
    }

    /**
     * Create the one polynomial (constant 1).
     *
     * @param <T> the numeric type
     * @return the one polynomial
     */
    static <T extends Number> @NotNull Monomial<T> one() {
        return new PolyOne<>();
    }

    /**
     * Create a monomial from a map of variable IDs to exponents.
     *
     * @param vars map from variable ID to exponent (must be > 0)
     * @param <T>  the numeric type
     * @return a PolyTerm representing the product of x_i^{e_i}, or PolyOne if empty
     */
    static <T extends Number> @NotNull Monomial<T> term(@NotNull Map<@NotNull Integer, @NotNull Integer> vars) {
        if (vars.isEmpty())
            return one();
        else
            return new PolyTerm<>(vars.entrySet().stream()
                    .collect(Collectors.toMap(entry ->
                            new Var<>(entry.getKey()), Map.Entry::getValue)));
    }

    /**
     * Create a sum polynomial from a map of monomials to coefficients.
     *
     * @param terms the map of monomials to coefficients
     * @param <T>   the numeric type
     * @return a PolySum representing the sum
     */
    static <T extends Number> @NotNull Polynomial<T> sum(@NotNull Map<@NotNull Monomial<T>, @NotNull T> terms) {
        return new PolySum<>(terms);
    }

    /**
     * Render this polynomial in human-readable form.
     *
     * @return a pretty-printed representation of this polynomial
     */
    default @NotNull String toPrettyString() {
        return PolynomialEvaluator.toPrettyString(this);
    }
}

/**
 * A sum of monomials with coefficients.
 *
 * @param terms map from monomial to its coefficient
 * @param <T>   the numeric type
 */
record PolySum<T extends Number>(@NotNull Map<@NotNull Monomial<T>, @NotNull T> terms) implements Polynomial<T> {
    public PolySum {
        if (terms.isEmpty())
            throw new IllegalArgumentException("empty polynomial");
        terms = Map.copyOf(terms);
    }
}

/**
 * The zero polynomial (constant 0).
 *
 * @param <T> the numeric type
 */
record PolyZero<T extends Number>() implements Polynomial<T> {
}

