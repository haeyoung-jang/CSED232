package edu.postech.csed232.model;

import edu.postech.csed232.base.GameInstance;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.postech.csed232.base.GameConstants.GRID_SIZE;
import static edu.postech.csed232.base.GameConstants.BOARD_SIZE;
import static edu.postech.csed232.base.GameConstants.MIN_CELL_NUMBER;
import static edu.postech.csed232.base.GameConstants.MAX_CELL_NUMBER;

/**
 * Represents an X-Sudoku board. It organizes a 9x9 grid of cells into rows, columns,
 * squares, and two main diagonals, enforcing Sudoku constraints across these groups.
 */
public class Board {
    //TODO: add private member variables and private methods as needed
    private final Cell[][] cells;
    private final List<Group> groups;

    /**
     * Constructs an X-Sudoku board with initial values taken from a game instance.
     * All constraint groups (rows, columns, squares, diagonals) are initialized,
     * and cells are wired into those groups accordingly.
     *
     * @param game an initial game instance providing the starting configuration
     */
    public Board(@NotNull GameInstance game) {
        //TODO: implement this
        cells = new Cell[BOARD_SIZE][BOARD_SIZE];
        groups = new ArrayList<>();

        // make cell instance
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                cells[row][col] = new Cell();
            }
        }

        // row group
        for (int row = 0; row < BOARD_SIZE; row++) {
            Group row_group = new Group();
            groups.add(row_group);
            for (int col = 0; col < BOARD_SIZE; col++) {
                row_group.addCell(cells[row][col]);
            }
        }

        // col group
        for (int col = 0; col < BOARD_SIZE; col++) {
            Group col_group = new Group();
            groups.add(col_group);
            for (int row = 0; row < BOARD_SIZE; row++) {
                col_group.addCell(cells[row][col]);
            }
        }

        // 3*3 box group
        for (int box_row = 0; box_row < GRID_SIZE; box_row++) {
            for (int box_col = 0; box_col < GRID_SIZE; box_col++) {
                Group box_group = new Group();
                groups.add(box_group);
                for(int row = box_row*GRID_SIZE; row < (box_row+1)*GRID_SIZE; row++) {
                    for (int col = box_col*GRID_SIZE; col < (box_col+1)*GRID_SIZE; col++) {
                        box_group.addCell(cells[row][col]);
                    }
                }
            }
        }

        // diagonal group
        Group diag1_group = new Group();
        groups.add(diag1_group);
        for (int i = 0;  i < BOARD_SIZE; i++) {
            diag1_group.addCell(cells[i][i]);
        }
        Group diag2_group = new Group();
        groups.add(diag2_group);
        for (int i = 0;  i < BOARD_SIZE; i++) {
            diag2_group.addCell(cells[i][BOARD_SIZE -1 -i]);
        }

        // set initial value
        for (int row = 0;  row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Optional<Integer> num = game.getNumbers(row+1, col+1);
                num.ifPresent(cells[row][col]::setNumber);
            }
        }
    }

    /**
     * Returns the cell located in the specified row and column.
     * Indices are 1-based to match standard Sudoku notation.
     *
     * @param i the 1-based row index (1 to 9)
     * @param j the 1-based column index (1 to 9)
     * @return the cell at position (i, j)
     */
    @NotNull
    public Cell getCell(int i, int j) {
        //TODO: implement this, and remove the following throw statement
        if (i < 1 || i> BOARD_SIZE || j < 1 || j > BOARD_SIZE) {
            throw new IndexOutOfBoundsException("Invalid cell index");
        }
        return cells[i-1][j-1];
    }

}
