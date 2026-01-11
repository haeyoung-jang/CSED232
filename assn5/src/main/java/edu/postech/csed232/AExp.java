package edu.postech.csed232;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract expression for a simple arithmetic expression over numeric type T.
 * Supports constants, variables, negation, addition, and multiplication.
 *
 * @param <T> the numeric type (e.g., Integer, Double)
 */
public sealed interface AExp<T extends Number> permits Const, Var, Add, Mul, Neg {

    /**
     * Create a constant expression.
     *
     * @param value the constant value
     * @param <T>   numeric type
     * @return an expression representing the constant value
     */
    static <T extends Number> @NotNull AExp<T> c(@NotNull T value) {
        return new Const<>(value);
    }

    /**
     * Create a variable expression.
     *
     * @param id  the variable’s identifier (x_id)
     * @param <T> numeric type
     * @return an expression representing the variable x_id
     */
    static <T extends Number> @NotNull AExp<T> x(int id) {
        return new Var<>(id);
    }

    /**
     * Create an addition expression.
     *
     * @param left  the left operand
     * @param right the right operand
     * @param <T>   numeric type
     * @return an expression representing {@code left + right}
     */
    static <T extends Number> @NotNull AExp<T> add(@NotNull AExp<T> left, @NotNull AExp<T> right) {
        return new Add<>(left, right);
    }

    /**
     * Create a multiplication expression.
     *
     * @param left  the left operand
     * @param right the right operand
     * @param <T>   numeric type
     * @return an expression representing {@code left * right}
     */
    static <T extends Number> @NotNull AExp<T> mul(@NotNull AExp<T> left, @NotNull AExp<T> right) {
        return new Mul<>(left, right);
    }

    /**
     * Create a negation expression.
     *
     * @param expr the expression to negate
     * @param <T>  numeric type
     * @return an expression representing {@code -expr}
     */
    static <T extends Number> @NotNull AExp<T> neg(@NotNull AExp<T> expr) {
        return new Neg<>(expr);
    }

    /**
     * Render this expression in human‐readable infix form
     *
     * @return a pretty‐printed representation of this expression
     */
    default @NotNull String toPrettyString() {
        return AExpEvaluator.toPrettyString(this);
    }
}

/**
 * A constant value.
 *
 * @param value the constant value
 * @param <T>   numeric type
 */
record Const<T extends Number>(@NotNull T value) implements AExp<T> {
}

/**
 * A variable identified by an integer id.
 *
 * @param id  the variable's identifier (x_id), which must be non-negative
 * @param <T> numeric type
 */
record Var<T extends Number>(int id) implements AExp<T>, Comparable<Var<T>> {
    public Var {
        if (id < 0)
            throw new IllegalArgumentException("variable id cannot be negative");
    }

    @Override
    public int compareTo(@NotNull Var<T> o) {
        return Integer.compare(this.id, o.id());
    }
}

/**
 * A negation of an expression.
 *
 * @param expr the expression to negate
 * @param <T>  numeric type
 */
record Neg<T extends Number>(@NotNull AExp<T> expr) implements AExp<T> {
}

/**
 * A binary addition.
 *
 * @param left  the left operand
 * @param right the right operand
 * @param <T>   numeric type
 */
record Add<T extends Number>(@NotNull AExp<T> left, @NotNull AExp<T> right) implements AExp<T> {
}

/**
 * A binary multiplication.
 *
 * @param left  the left operand
 * @param right the right operand
 * @param <T>   numeric type
 */
record Mul<T extends Number>(@NotNull AExp<T> left, @NotNull AExp<T> right) implements AExp<T> {
}
