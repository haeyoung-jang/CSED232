package edu.postech.csed232;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.postech.csed232.Polynomial.sum;

/**
 * Evaluates and manipulates canonical polynomials whose coefficients
 * are of numeric type {@code T}.
 * <p>
 * Supports collecting variables, numeric evaluation under an environment,
 * pretty-printing, negation, addition, and multiplication of polynomials.
 *
 * @param <T> the numeric type of polynomial coefficients (e.g., Integer, Double)
 */
public final class PolynomialEvaluator<T extends Number> {

    /**
     * Arithmetic operations for the coefficient type {@code T}.
     */
    private final @NotNull Arithmetic<T> num;

    /**
     * Constructs a new evaluator using the given arithmetic semantics.
     *
     * @param num the {@link Arithmetic} implementation for coefficients
     */
    public PolynomialEvaluator(@NotNull Arithmetic<T> num) {
        this.num = num;
    }

    /**
     * Returns the set of all variable indices that occur in the given polynomial.
     * <p>
     * For example, for the polynomial 3*x1^2 + x3, this returns {1, 3}.
     *
     * @param poly the polynomial to analyze
     * @return a set of variable IDs used in poly; empty if none
     */
    public @NotNull Set<Integer> getVariables(@NotNull Polynomial<T> poly) {
        // TODO: implement this, and remove the following throw statement
        Set<Integer> vars = new HashSet<>();
        switch (poly) {
            case PolyZero<T> zero -> {}
            case PolyOne<T> one -> {}
            case PolyTerm<T> term -> {
                for (Var<T> var : term.vars().keySet()){
                    vars.add(var.id());
                }
            }
            case PolySum<T> sum -> {
                for (var entry : sum.terms().entrySet()){
                    vars.addAll(getVariables(entry.getKey()));
                }
            }
        }
        return vars;
    }

    /**
     * Evaluates the polynomial under the given variable assignment.
     * <p>
     * For example, evaluating 2*x1 + x2^2 with assignment {x1=3, x2=4} yields
     * 2*3 + 4^2 = 6 + 16 = 22.
     *
     * @param poly the polynomial to evaluate
     * @param env  mapping from variable IDs to values
     * @return the numeric result of evaluating poly
     * @throws IllegalArgumentException if a variable is missing in env
     */
    public @NotNull T evaluate(@NotNull Polynomial<T> poly, @NotNull Map<Integer, T> env) {
        // TODO: implement this, and remove the following throw statement
        return switch (poly){
            case PolyZero<T> zero -> num.zero();
            case PolyOne<T> one -> num.one();
            case PolyTerm<T> term -> {
                T result = num.one();
                for (var entry : term.vars().entrySet()){
                    Var<T> variable = entry.getKey();
                    int exp = entry.getValue();
                    T base = env.get(variable.id());
                    if (base == null){
                        throw new IllegalArgumentException("Variable " + variable.id() + " not found");
                    }
                    for (int i = 0; i<exp; i++){
                        result = num.mul(result, base);
                    }
                }
                yield result;
            }
            case PolySum<T> sum -> {
                T total = num.zero();
                for (var entry : sum.terms().entrySet()){
                    Monomial<T> monomial = entry.getKey();
                    T coeffi = entry.getValue();
                    T evaluatedMono = evaluate(monomial, env);
                    total = num.add(total, num.mul(coeffi, evaluatedMono));
                }
                yield total;
            }
        };
    }

    /**
     * Pretty-prints a polynomial in “c1*v1^e1*v2^e2 + c2*v3^e3*v4^e4 …” form,
     * with the following ordering guarantees:
     * <ul>
     *   <li>Within each monomial, variables (v1, v2, …) appear in increasing order
     *       according to {@code Var.compareTo()}.</li>
     *   <li>Across the sum, monomials appear in increasing order
     *       according to their {@code Comparable<Monomial>} implementation.</li>
     * </ul>
     *
     * @param poly the polynomial to format
     * @param <T>  the numeric type of the coefficients
     * @return a human-readable string representation of {@code poly}
     */
    public static <T extends Number>
    @NotNull String toPrettyString(@NotNull Polynomial<T> poly) {
        // TODO: implement this, and remove the following throw statement
        return switch (poly){
            case PolyZero<T> zero -> "0";
            case PolyOne<T> one -> "1";
            case PolyTerm<T> term -> {
                List<String> parts = new ArrayList<>();
                List<Map.Entry<Var<T>, Integer>> sortedVars = new ArrayList<>(term.vars().entrySet());
                sortedVars.sort(Map.Entry.comparingByKey());
                for(var entry : sortedVars){
                    String part = "x" + entry.getKey().id();
                    if (entry.getValue() > 1){
                        part += "^" + entry.getValue();
                    }
                    parts.add(part);
                }
                yield String.join("*", parts);
            }
            case PolySum<T> sum -> {
                List<String> parts = new ArrayList<>();
                List<Map.Entry<Monomial<T>, T>> sortedTerms = new ArrayList<>(sum.terms().entrySet());
                sortedTerms.sort(Map.Entry.comparingByKey());
                for (var termEntry : sortedTerms) {
                    T coeffi = termEntry.getValue();
                    Monomial<T> monomial = termEntry.getKey();

                    if (monomial instanceof PolyOne){
                        parts.add(coeffi.toString());
                    }
                    else{
                        parts.add(coeffi + "*" + toPrettyString(monomial));
                    }
                }
                String result = String.join(" + ", parts);
                yield result;
            }
        };
    }

    /**
     * Returns the polynomial representing the negation of poly.
     * <p>
     * For example, negating x1 + 2 yields "-1*x1 + -2".
     *
     * @param poly the polynomial to negate
     * @return a new polynomial equal to -poly
     */
    public @NotNull Polynomial<T> negatePoly(@NotNull Polynomial<T> poly) {
        return switch (poly) {
            case PolyZero<T> zero -> zero;
            case Monomial<T> mono -> sum(Map.of(mono, num.neg(num.one())));
            case PolySum<T> p -> {
                Map<Monomial<T>, T> negTerms = new HashMap<>();
                for (var entry : p.terms().entrySet()) {
                    var term = entry.getKey();
                    T coefficient = entry.getValue();
                    negTerms.put(term, num.neg(coefficient));
                }
                yield sum(negTerms);
            }
        };
    }

    //helper private method to convert poly something to polySum
    private @NotNull PolySum<T> toPolySum(@NotNull Polynomial<T> poly) {
        return switch (poly){
            case PolyZero<T> zero -> new PolySum<>(Map.of());
            case PolyOne<T> one -> new PolySum<>(Map.of(new PolyOne<>(), num.one()));
            case PolyTerm<T> term -> new PolySum<>(Map.of(term, num.one()));
            case PolySum<T> sum -> sum;
        };
    }

    /**
     * Returns the sum of two canonical polynomials.
     * <p>
     * For example, adding x1 and 2*x2^2 yields "x1 + 2*x2^2".
     *
     * @param p the first polynomial
     * @param q the second polynomial
     * @return a new polynomial equal to p + q
     */
    public @NotNull Polynomial<T> polySum(@NotNull Polynomial<T> p, @NotNull Polynomial<T> q) {
        // TODO: implement this, and remove the following throw statement
        if (p instanceof PolyZero) return q;
        if (q instanceof PolyZero) return p;
        PolySum<T> sumP = toPolySum(p);
        PolySum<T> sumQ = toPolySum(q);

        Map<Monomial<T>, T> combined = new HashMap<>(sumP.terms());
        for (var entry : sumQ.terms().entrySet()) {
            combined.merge(entry.getKey(), entry.getValue(), num::add);
        }
        return new PolySum<>(combined);
        /*
        return switch (p) {
            case PolySum<T> pSum -> switch (q) {
                case PolySum<T> qSum -> {
                    Map<Monomial<T>, T> combined = new HashMap<>(pSum.terms());
                    for (var entry: qSum.terms().entrySet()) {
                        combined.merge(entry.getKey(), entry.getValue(), num::add);
                    }
                    yield new PolySum<>(combined);
                }
                default -> throw new IllegalStateException("Unexpected value: " + q);
            };
            default -> throw new IllegalStateException("Unexpected value: " + p);
        };
         */
    }

    /**
     * Returns the product of two canonical polynomials.
     * <p>
     * For example, multiplying x1 by x2^2 yields "x1*x2^2".
     *
     * @param p the first polynomial
     * @param q the second polynomial
     * @return a new polynomial equal to p * q
     */
    public @NotNull Polynomial<T> polyProduct(@NotNull Polynomial<T> p, @NotNull Polynomial<T> q) {
        // TODO: implement this, and remove the following throw statement
        if (p instanceof PolyZero || q instanceof PolyZero) return new PolyZero<>();
        if (p instanceof PolyOne) return q;
        if (q instanceof PolyOne) return p;

        PolySum<T> sumP = toPolySum(p);
        PolySum<T> sumQ = toPolySum(q);

        Map<Monomial<T>, T> result = new HashMap<>();
        for (var pEntry : sumP.terms().entrySet()) {
            for (var qEntry : sumQ.terms().entrySet()) {
                Monomial<T> monoProduct = Mul(pEntry.getKey(), qEntry.getKey());
                T coeffProduct = num.mul(pEntry.getValue(), qEntry.getValue());
                result.merge(monoProduct, coeffProduct, num::add);
            }
        }
        return new PolySum<>(result);
        /*
        return switch (p) {
            case PolySum<T> pSum -> switch (q) {
                case PolySum<T> qSum -> {
                    Map<Monomial<T>, T> result = new HashMap<>();
                    for (var pEntry : pSum.terms().entrySet()) {
                        for (var qEntry : qSum.terms().entrySet()) {
                            Monomial<T> monoProduct = Mul(pEntry.getKey(), qEntry.getKey());
                            T coeffProduct = num.mul(pEntry.getValue(), qEntry.getValue());
                            result.merge(monoProduct, coeffProduct, num::add);
                        }
                    }
                    yield new PolySum<>(result);
                }
                default -> throw new IllegalStateException("Unexpected value: " + q);
            };
            default -> throw new IllegalStateException("Unexpected value: " + p);
        };
         */
    }
    // add private method to calculate monomial product
    private @NotNull Monomial<T> Mul(@NotNull Monomial<T> p, @NotNull Monomial<T> q) {
        if (p instanceof PolyOne) return q;
        if (q instanceof PolyOne) return p;

        if (p instanceof PolyTerm<T> termP && q instanceof PolyTerm<T> termQ) {
            Map<Var<T>, Integer> newVars = new HashMap<>(termP.vars());
            for (var entry : termQ.vars().entrySet()) {
                newVars.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
            return new PolyTerm<>(newVars);
        }
        throw new IllegalArgumentException("not Monomial");
    }
}