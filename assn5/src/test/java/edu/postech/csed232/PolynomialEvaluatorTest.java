package edu.postech.csed232;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import static edu.postech.csed232.AExp.*;
import static edu.postech.csed232.Polynomial.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PolynomialEvaluatorTest {
    static final Arithmetic<Double> doubleNum = AExpEvaluatorTest.doubleNum;
    static final Arithmetic<BigInteger> bigIntNum = AExpEvaluatorTest.bigIntNum;

    @Test
    void testGetVariablesMonomial() {
        var pev = new PolynomialEvaluator<>(doubleNum);
        Monomial<Double> mono = term(Map.of(1, 2, 2, 1)); // x1^2 * x2
        assertEquals(Set.of(1, 2), pev.getVariables(mono));
    }

    @Test
    void testEvaluateProduct() {
        var pev = new PolynomialEvaluator<>(doubleNum);
        var env = Map.of(1, 2.0, 2, 3.0);

        var p = sum(Map.of(term(Map.of(1, 1)), 2.0)); // 2*x1
        var q = sum(Map.of(term(Map.of(1, 2)), 3.0)); // 3*x1^2
        assertEquals(6.0 * Math.pow(2.0, 3), pev.evaluate(pev.polyProduct(p, q), env));
    }

    @Test
    void testPolynomialNegation() {
        var pev = new PolynomialEvaluator<>(doubleNum);
        // 3*x1 + -5
        var poly = sum(Map.of(
                term(Map.of(1, 1)), 3.0,
                one(), -5.0));
        var negPoly = pev.negatePoly(poly);
        var env = Map.of(1, 3.0);
        assertEquals(-4.0, pev.evaluate(negPoly, env));
    }

    @Test
    void toPrettyStringSum() {
        // 3.0*x2^3*x1 + 4.0*x2 + -5.0
        var poly = sum(Map.of(
                term(Map.of(2, 3, 1, 1)), 3.0,
                term(Map.of(2, 1)), 4.0,
                Polynomial.one(), -5.0
        ));
        assertEquals("-5.0 + 3.0*x1*x2^3 + 4.0*x2", PolynomialEvaluator.toPrettyString(poly));
    }

    // TODO: Write more test cases to achieve more code coverage, if needed.
    @Test
    void testZeroOneGetVariables() {
        var pev = new PolynomialEvaluator<>(doubleNum);
        var zero = new PolyZero<Double>();
        var one = new PolyOne<Double>();
        assertEquals(Set.of(), pev.getVariables(zero));
        assertEquals(Set.of(), pev.getVariables(one));
    }

    @Test
    void testSumGetVariables() {
        var pev = new PolynomialEvaluator<>(doubleNum);
        var sum = sum(Map.of(
                term(Map.of(2, 3, 1, 1)), 3.0,
                term(Map.of(2, 1)), 4.0,
                Polynomial.one(), -5.0
        ));
        assertEquals(Set.of(1, 2), pev.getVariables(sum));
    }

    @Test
    void testEvaluateZero() {
        var pev = new PolynomialEvaluator<>(doubleNum);
        var zero = new PolyZero<Double>();
        var env = Map.of(1, 2.0, 2, 3.0);
        assertEquals(0, pev.evaluate(zero, env));
    }

    @Test
    void testEvaluateNull() {
        var pev = new PolynomialEvaluator<>(doubleNum);
        Monomial<Double> p = term(Map.of(0, 1, 1, 2));
        var env = Map.of(0, 0.1, 2, 0.2);
        assertThrows(IllegalArgumentException.class, () -> pev.evaluate(p, env));
    }

    @Test
    void testToPrettyStringZeroOne() {
        var zero = new PolyZero<Double>();
        var one = new PolyOne<Double>();
        assertEquals("0", zero.toPrettyString());
        assertEquals("1", one.toPrettyString());
    }

    @Test
    void testNegatePolyZero() {
        var pev = new PolynomialEvaluator<>(doubleNum);
        var zero = new PolyZero<Double>();
        assertEquals(zero, pev.negatePoly(zero));
    }

    @Test
    void testMonoNegatePoly() {
        var pev = new PolynomialEvaluator<>(doubleNum);
        Monomial<Double> monomial = term(Map.of(0, 1, 1, 2));
        var env = Map.of(0, 2.0, 1, 1.0);
        assertEquals(-2, pev.evaluate(pev.negatePoly(monomial), env));
    }

    @Test
    void testPolySumZero() {
        var pev = new PolynomialEvaluator<>(doubleNum);
        var p = new PolyZero<Double>();
        var q  = sum(Map.of(term(Map.of(1, 1)), 3.0));
        assertEquals(q, pev.polySum(p, q));
        assertEquals(q, pev.polySum(q, p));
    }

    @Test
    void testHelperPolySum() {
        var pev = new PolynomialEvaluator<>(doubleNum);
        Monomial<Double> term = term(Map.of(0, 1, 1, 2));
        var one = new PolyOne<Double>();
        assertEquals("1.0 + 1.0*x0*x1^2", pev.polySum(term, one).toPrettyString());
    }

    @Test
    void testPolyProductZero() {
        var pev = new PolynomialEvaluator<>(doubleNum);
        Polynomial<Double> zeroPoly1 = zero();
        Polynomial<Double> zeroPoly2 = zero();
        Polynomial<Double> onePoly = one();
        Monomial<Double> term = term(Map.of(0, 1, 1, 2));

        assertEquals(zeroPoly1, pev.polyProduct(zeroPoly1, zeroPoly2));
        assertEquals(zeroPoly1, pev.polyProduct(zeroPoly1, onePoly));
        assertEquals(zeroPoly1, pev.polyProduct(onePoly, zeroPoly2));
        assertEquals(term, pev.polyProduct(onePoly, term));
        assertEquals(term, pev.polyProduct(term, onePoly));
    }

    @Test
    void testNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Var<Double>(-1);
        });
    }

    @Test
    void testPolyTermCompareByLength() {
        Monomial<Double> shortTerm = new PolyTerm<>(Map.of(new Var<>(0), 1));
        Monomial<Double> longTerm = new PolyTerm<>(Map.of(
                new Var<>(0), 1,
                new Var<>(1), 1
        ));
        assertTrue(shortTerm.compareTo(longTerm) < 0);
        assertTrue(longTerm.compareTo(shortTerm) > 0);
    }
}
