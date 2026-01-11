package edu.postech.csed232;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HopingRabbitsGameTest {

    @Test
    void testTwo() {
        var game = new HopingRabbitsGame(2);
        assertEquals("xx_oo", game.toString());
        assertTrue(game.move(Rabbit.X));
        assertEquals("x_xoo", game.toString());
        assertTrue(game.move(Rabbit.O));
        assertEquals("xox_o", game.toString());
        assertTrue(game.move(Rabbit.X));
        assertEquals("xo_xo", game.toString());
        assertTrue(game.move(Rabbit.X));
        assertEquals("_oxxo", game.toString());
        assertTrue(game.move(Rabbit.O));
        assertEquals("o_xxo", game.toString());
        assertTrue(game.isStuck());
    }
}
