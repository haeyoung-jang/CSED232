package edu.postech.csed232;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static edu.postech.csed232.AExp.*;

/**
 * Evaluator for arithmetic expressions defined by {@link AExp}.
 * Provides methods to collect variables, evaluate expressions, and
 * expand expressions into canonical polynomial form.
 *
 * @param <T> the numeric type (e.g., Integer, Double)
 */
public final class AExpEvaluator<T extends Number> {

    /**
     * The arithmetic implementation providing numeric operations for
     * evaluating and expanding expressions.
     */
    private final @NotNull Arithmetic<T> num;
    private final PolynomialEvaluator<T> polyEval;

    /**
     * Construct an evaluator with the given numeric semantics.
     *
     * @param num the {@link Arithmetic} implementation for type T
     */
    public AExpEvaluator(@NotNull Arithmetic<T> num) {
        this.num = num;
        this.polyEval = new PolynomialEvaluator<>(num);
    }

    /**
     * Collects all variable identifiers used in the expression.
     * <p>
     * For example, given the expression {@code x1 + (x2 * x3)},
     * this function returns the set {@code {1, 2, 3}}.
     *
     * @param expr the arithmetic expression
     * @return a set of variable IDs appearing in {@code expr}; empty if none
     */
    public @NotNull Set<Integer> getVariables(@NotNull AExp<T> expr) {
        // TODO: implement this, and remove the following throw statement
        Set<Integer> vars = new HashSet<>();
        switch (expr){
            case Const<T> c -> {}
            case Var<T> v -> vars.add(v.id());
            case Neg<T> n -> vars.addAll(getVariables(n.expr()));
            case Add<T> a -> {
                vars.addAll(getVariables(a.left()));
                vars.addAll(getVariables(a.right()));
            }
            case Mul<T> m -> {
                vars.addAll(getVariables(m.left()));
                vars.addAll(getVariables(m.right()));
            }
        }
        return vars;
    }

    /**
     * Evaluates the expression under the given variable assignment.
     * <p>
     * For example, evaluating x1 + 2 with the assignment x1=5 returns 7.
     *
     * @param expr the arithmetic expression
     * @param env  mapping from variable IDs to values
     * @return the numeric result of evaluating expr
     * @throws IllegalArgumentException if a variable in expr is missing in env
     */
    public @NotNull T evaluate(@NotNull AExp<T> expr, @NotNull Map<Integer, T> env) {
        // TODO: implement this, and remove the following throw statement
        return switch (expr){
            case Const<T> c -> c.value();
            case Var<T> v -> {
                T value = env.get(v.id());
                if (value == null){
                    throw new IllegalArgumentException("Variable " + v.id() + " not found");
                }
                yield value;
            }
            case Neg<T> n -> num.neg(evaluate(n.expr(), env));
            case Add<T> a -> num.add(evaluate(a.left(), env), evaluate(a.right(), env));
            case Mul<T> m -> num.mul(evaluate(m.left(), env), evaluate(m.right(), env));
        };
    }

    /**
     * Expands the arithmetic expression into its canonical polynomial form.
     * <p>
     * For example, expanding (x1 + 2) * x3 yields the polynomial x1*x3 + 2*x3.
     *
     * @param expr the arithmetic expression
     * @return a {@link Polynomial} representing the expanded form
     */
    public @NotNull Polynomial<T> expand(@NotNull AExp<T> expr) {
        // TODO: implement this, and remove the following throw statement
        return switch (expr){
            case Const<T> c -> {
                if (c.value().equals(num.zero())){
                    yield new PolyZero<>();
                } else {
                    yield new PolySum<>(Map.of(new PolyOne<>(), c.value()));
                }
            }
            case Var<T> v -> {
                Map<Var<T>, Integer> vars = new HashMap<>();
                vars.put(v, 1);
                PolyTerm<T> mono = new PolyTerm<>(vars);
                yield new PolySum<>(Map.of(mono, num.one()));
            }
            case Neg<T> n -> polyEval.negatePoly(expand(n.expr()));
            case Add<T> a -> polyEval.polySum(expand(a.left()), expand(a.right()));
            case Mul<T> m -> polyEval.polyProduct(expand(m.left()), expand(m.right()));
        };
    }

    /**
     * Pretty-prints an arithmetic expression in infix notation with parentheses.
     *
     * @param e   the expression to format
     * @param <T> the numeric type of the expression
     * @return a human-readable string representation of e
     */
    public static <T extends Number> @NotNull String toPrettyString(@NotNull AExp<T> e) {
        return switch (e) {
            case Const<T> c -> c.value().toString();
            case Var<T> v -> "x" + v.id();
            case Neg<T> n -> "-(" + toPrettyString(n.expr()) + ")";
            case Add<T> a -> "(" + toPrettyString(a.left()) + " + " + toPrettyString(a.right()) + ")";
            case Mul<T> m -> "(" + toPrettyString(m.left()) + " * " + toPrettyString(m.right()) + ")";
        };
    }
}
