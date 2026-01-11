package edu.postech.csed232.base;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * An X Sudoku puzzle
 */
public interface GameInstance {
    /**
     * Initial numbers (null if a cell is empty)
     *
     * @param i row index
     * @param j column index
     * @return the number in the i-th row of j-th column
     */
    @NotNull
    Optional<Integer> getNumbers(int i, int j);
}
