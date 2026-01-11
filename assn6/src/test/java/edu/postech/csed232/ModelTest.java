package edu.postech.csed232;

import edu.postech.csed232.base.GameInstanceFactory;
import edu.postech.csed232.base.Subject;
import edu.postech.csed232.event.ActivationEvent;
import edu.postech.csed232.event.Event;
import edu.postech.csed232.model.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static edu.postech.csed232.base.GameConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @Test
    void testInitialBoardConfiguration() {
        var board = new Board(GameInstanceFactory.createGameInstance());

        assertEquals(Optional.of(9), board.getCell(1, 3).getNumber());
        assertEquals(Optional.of(1), board.getCell(2, 6).getNumber());
        assertEquals(Optional.of(6), board.getCell(3, 5).getNumber());
        assertTrue(board.getCell(4, 6).getNumber().isPresent());
    }

    @Test
    void testCellInitiallyHasAllPossibilities() {
        Cell cell = new Cell();
        for (int n = MIN_CELL_NUMBER; n <= MAX_CELL_NUMBER; n++) {
            assertTrue(cell.containsPossibility(n));
        }
    }

    @Test
    void testCellReportsDeactivation() {
        Cell cell = new Cell();
        var disabled = new AtomicBoolean(false);

        cell.addObserver((caller, arg) -> {
            if (arg instanceof ActivationEvent(boolean activated))
                disabled.set(!activated);
        });

        for(int i = MIN_CELL_NUMBER; i <= MAX_CELL_NUMBER; i++) {
            assertFalse(cell.hasNoPossibility());
            cell.removePossibility(i);
        }
        assertTrue(cell.hasNoPossibility());
        assertTrue(disabled.get());
    }

    //TODO: Write more test cases to achieve more code coverage, if needed.
    // cell
    @Test
    void testCellSet_duplicatedNumber() {
        Cell cell = new Cell();
        assertTrue(cell.setNumber(7));
        assertFalse(cell.setNumber(7));
    }
    @Test
    void testCellUnsetNumber_false() {
        Cell cell = new Cell();
        assertFalse(cell.unsetNumber());
    }

    @Test
    void testCellUnsetNumber_true() {
        Cell cell = new Cell();
        assertTrue(cell.setNumber(7));
        assertTrue(cell.unsetNumber());
    }

    @Test
    void testCellAddPossibility_duplicatedNumber() {
        Cell cell = new Cell();
        assertTrue(cell.containsPossibility(3));
        cell.addPossibility(3);
        assertTrue(cell.containsPossibility(3));
    }

    @Test
    void testCellAddPossibility_false() {
        Cell cell1 = new Cell();
        Cell cell2 = new Cell();

        Group group = new Group();
        group.addCell(cell1);
        group.addCell(cell2);

        assertTrue(cell1.setNumber(5));
        cell2.removePossibility(5);
        assertFalse(cell2.setNumber(5));

        cell2.addPossibility(5);
        assertFalse(cell2.containsPossibility(5));
    }

    @Test
    void testCellAddPossibility_empty() {
        Cell cell = new Cell();
        TestObserver observer = new TestObserver();
        cell.addObserver(observer);

        for (int i = 1; i <= 9; i++) cell.removePossibility(i);

        assertTrue(cell.hasNoPossibility());

        observer.clear();

        cell.addPossibility(3);
        assertTrue(cell.containsPossibility(3));
        assertTrue(observer.activatedCalled); // 이벤트 발생 확인
    }

    private static class TestObserver implements edu.postech.csed232.base.Observer {
        boolean activatedCalled = false;

        @Override
        public void update(@NotNull Subject caller, @NotNull Event arg) {
            if (arg instanceof ActivationEvent(boolean activated)) {
                if (activated) {
                    activatedCalled = true;
                }
            }
        }

        void clear() {
            activatedCalled = false;
        }
    }

    @Test
    void testCellAddPossibility_constraint_isnotavailable() {
        Cell cell = new Cell();

        for (int i = 1; i <= 9; i++) {
            cell.removePossibility(i);
        }

        CellConstraint allowAll = number -> true;
        cell.registerConstraint(allowAll);
        cell.addPossibility(3);
        cell.addPossibility(5);

    }

    @Test
    void testGroup_isAvailable(){
        Cell cell = new Cell();
        Group group = new Group();
        group.addCell(cell);

        assertTrue(group.isAvailable(7));
        cell.setNumber(7);
        assertFalse(group.isAvailable(7));
        assertTrue(group.isAvailable(8));
    }

    @Test
    void testGroup_update(){
        Cell cell1 = new Cell();
        Cell cell2 = new Cell();
        Group group = new Group();
        group.addCell(cell1);
        group.addCell(cell2);

        assertTrue(cell1.setNumber(5));
        assertFalse(cell2.containsPossibility(5));

        assertTrue(cell1.unsetNumber());
        assertTrue(cell2.containsPossibility(5));
    }

    @Test
    void testGroupUpdate_shouldIgnoreIfEventNotNumberEvent() {
        Cell a = new Cell();
        Group group = new Group();

        assertDoesNotThrow(() -> group.update(a, new ActivationEvent(false)));
    }

    @Test
    void testGetCell_shouldThrowException_whenRowIsOutOfBoundsLow() {
        Board board = new Board(GameInstanceFactory.createGameInstance());
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCell(0, 1));
    }

    @Test
    void testGetCell_shouldThrowException_whenRowIsOutOfBoundsHigh() {
        Board board = new Board(GameInstanceFactory.createGameInstance());
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCell(10, 1));
    }

    @Test
    void testGetCell_shouldThrowException_whenColIsOutOfBoundsLow() {
        Board board = new Board(GameInstanceFactory.createGameInstance());
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCell(1, 0));
    }

    @Test
    void testGetCell_shouldThrowException_whenColIsOutOfBoundsHigh() {
        Board board = new Board(GameInstanceFactory.createGameInstance());
        assertThrows(IndexOutOfBoundsException.class, () -> board.getCell(1, 10));
    }


}
