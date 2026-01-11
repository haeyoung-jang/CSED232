package edu.postech.csed232;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import static edu.postech.csed232.AExp.*;
import static org.junit.jupiter.api.Assertions.*;

public class AExpEvaluatorTest {

    static final Arithmetic<Double> doubleNum = new Arithmetic<>() {
        @Override
        public @NotNull Double add(@NotNull Double a, @NotNull Double b) {
            return a + b;
        }

        @Override
        public @NotNull Double mul(@NotNull Double a, @NotNull Double b) {
            return a * b;
        }

        @Override
        public @NotNull Double neg(@NotNull Double a) {
            return -a;
        }

        @Override
        public @NotNull Double zero() {
            return 0.0;
        }

        @Override
        public @NotNull Double one() {
            return 1.0;
        }
    };

    static final Arithmetic<BigInteger> bigIntNum = new Arithmetic<>() {
        @Override
        public @NotNull BigInteger add(@NotNull BigInteger a, @NotNull BigInteger b) {
            return a.add(b);
        }

        @Override
        public @NotNull BigInteger mul(@NotNull BigInteger a, @NotNull BigInteger b) {
            return a.multiply(b);
        }

        @Override
        public @NotNull BigInteger neg(@NotNull BigInteger a) {
            return a.negate();
        }

        @Override
        public @NotNull BigInteger zero() {
            return BigInteger.ZERO;
        }

        @Override
        public @NotNull BigInteger one() {
            return BigInteger.ONE;
        }
    };

    @Test
    void testDoubleGetVariables() {
        var aev = new AExpEvaluator<>(doubleNum);
        var p = add(x(1), add(x(0), c(1.0)));
        assertEquals(Set.of(0, 1), aev.getVariables(p));
    }

    @Test
    void testBigIntegerEvaluate() {
        var aev = new AExpEvaluator<>(bigIntNum);
        var p = add(x(1), add(x(0), c(BigInteger.ONE)));
        var env = Map.of(0, new BigInteger("10000"), 1, new BigInteger("20000"));
        assertEquals(new BigInteger("30001"), aev.evaluate(p, env));
    }

    @Test
    void testPrettyStringSimple() {
        var p = add(x(1), add(x(0), c(1.0)));
        assertEquals("(x1 + (x0 + 1.0))", p.toPrettyString());
    }

    @Test
    void testExpandSquaredPolynomialEvaluation() {
        var aev = new AExpEvaluator<>(doubleNum);
        var pev = new PolynomialEvaluator<>(doubleNum);

        var p = add(x(1), add(x(0), c(1.0)));
        var q = aev.expand(mul(p, p));
        var env = Map.of(0, 2.0, 1, 3.0);
        assertEquals(36.0, pev.evaluate(q, env));
    }

    // TODO: Write more test cases to achieve more code coverage, if needed.
    @Test
    void testNegGetVariables() {
        var aev = new AExpEvaluator<>(doubleNum);
        var p = add(x(1), add(x(0), c(1.0)));
        assertEquals(Set.of(0, 1), aev.getVariables(neg(p)));
    }

    @Test
    void testMulGetVariables() {
        var aev = new AExpEvaluator<>(doubleNum);
        var p = add(x(1), c(1.0));
        var q = add(x(0), c(1.0));
        assertEquals(Set.of(0, 1), aev.getVariables(mul(p, q)));
    }

    @Test
    void testNegEvaluate() {
        var aev = new AExpEvaluator<>(doubleNum);
        var p = add(x(1), add(x(0), c(1.0)));
        var env = Map.of(0, 0.1, 1, 0.2);
        assertEquals(-1.3, aev.evaluate(neg(p), env));
    }

    @Test
    void testMulEvaluate() {
        var aev = new AExpEvaluator<>(doubleNum);
        var p = add(x(1), add(x(0), c(1.0)));
        var q = add(x(0), c(1.0));
        var env = Map.of(0, 1.0, 1, 3.0);
        assertEquals(10, aev.evaluate(mul(p, q), env));
    }

    @Test
    void testNullEvaluate() {
        var aev = new AExpEvaluator<>(doubleNum);
        var p = add(x(1), add(x(0), c(1.0)));
        var env = Map.of(0, 0.1, 2, 0.2);
        assertThrows(IllegalArgumentException.class, () -> aev.evaluate(p, env));
    }

    @Test
    void testZeroExpand() {
        var aev = new AExpEvaluator<>(doubleNum);
        assertTrue(aev.expand(c(0.0)) instanceof PolyZero);
    }

    @Test
    void testNegExpand() {
        var aev = new AExpEvaluator<>(doubleNum);
        var p = add(x(0), c(1.0));
        var poly = aev.expand(neg(p));
        assertEquals("-1.0 + -1.0*x0", poly.toPrettyString());
    }

    @Test
    void testNegToPrettyString() {
        var p = neg(x(0));
        assertEquals("-(x0)", p.toPrettyString());
    }

    @Test
    void testMulToPrettyString() {
        var p = mul(x(0), c(1.0));
        assertEquals("(x0 * 1.0)", p.toPrettyString());

    }

}