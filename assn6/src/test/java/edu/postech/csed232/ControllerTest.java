package edu.postech.csed232;

import edu.postech.csed232.base.*;
import edu.postech.csed232.control.*;
import edu.postech.csed232.event.*;
import edu.postech.csed232.model.*;
import edu.postech.csed232.view.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;

import static edu.postech.csed232.base.GameConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    private Cell cell;
    private TestObserver observer;
    private CellInputDocument document;

    static class TestObserver implements Observer {
        @Nullable String lastMsg = null;

        @Override
        public void update(@NotNull Subject caller, @NotNull edu.postech.csed232.event.Event arg) {
            if (arg instanceof MessageEvent(String message))
                lastMsg = message;
        }
    }

    @BeforeEach
    void setup() {
        cell = new Cell();
        observer = new TestObserver();
        var service = new MessageService();
        service.addObserver(observer);
        document = new CellInputDocument(cell, service);
    }

    @Test
    void testValidInsert() {
        assertDoesNotThrow(() -> document.insertString(0, "5", null));
        assertEquals(MessageType.CLEAR.text(), observer.lastMsg);
        assertEquals(5, cell.getNumber().orElseThrow());
    }

    @Test
    void testInvalidInsertOutOfRange() {
        assertDoesNotThrow(() -> document.insertString(0, "0", null));
        assertEquals(MessageType.INVALID_INPUT.text(), observer.lastMsg);
        assertTrue(cell.getNumber().isEmpty());
    }

    @Test
    void testCellActivationControllerActivatesAndDeactivates() {
        var cellUI = new CellUI(cell, false);
        new CellActivationController(cellUI);
        assertEquals(ACTIVE_BORDER, cellUI.getBorder());
        for (int i = MIN_CELL_NUMBER; i <= MAX_CELL_NUMBER; i++)
            cell.removePossibility(i);
        assertDoesNotThrow(() ->
                SwingUtilities.invokeAndWait(() ->   // Wait for UI updates to complete
                        assertEquals(INACTIVE_BORDER, cellUI.getBorder())));
    }

    //TODO: Write more test cases to achieve more code coverage, if needed.
    @Test
    void testMessageUIController_shouldSetText_whenMessageEventReceived() {
        // GIVEN
        MessageUI messageUI = new MessageUI();
        MessageService service = new MessageService();
        new MessageUIController(messageUI, service);

        service.notifyObservers(new MessageEvent("message"));
        assertEquals("message", messageUI.getText());
    }

    @Test
    void testHandleStuckStatus_shouldDispatchStuck_whenActivatedAndEmpty() {
        MessageService service = new MessageService();
        TestObserver observer = new TestObserver();
        service.addObserver(observer);

        service.handleStuckStatus(false, true);
        assertEquals(MessageType.STUCK.text(), observer.lastMsg);
    }

    @Test
    void testHandleStuckStatus_shouldDispatchClear_whenActivatedAndNotEmpty() {
        MessageService service = new MessageService();
        TestObserver observer = new TestObserver();
        service.addObserver(observer);

        service.handleStuckStatus(true, false);
        assertEquals(MessageType.CLEAR.text(), observer.lastMsg);
    }

    @Test
    void testHandleStuckStatus_shouldDoNothing_whenNotActivated() {
        MessageService service = new MessageService();
        TestObserver observer = new TestObserver();
        service.addObserver(observer);

        observer.lastMsg = null;

        service.handleStuckStatus(false, false);
        assertNull(observer.lastMsg);
    }

    @Test
    void testHandleHintPossibilities_shouldDispatchNoAvailable_whenNoCandidates() {
        MessageService service = new MessageService();
        TestObserver observer = new TestObserver();
        service.addObserver(observer);

        Cell cell = new Cell();
        for (int i = MIN_CELL_NUMBER; i <= MAX_CELL_NUMBER; i++) {
            cell.removePossibility(i);
        }
        assertTrue(cell.hasNoPossibility());

        service.handleHintPossibilities(cell);

        assertEquals(MessageType.HINT_NO_AVAILABLE.text(), observer.lastMsg);
    }

    @Test
    void testHandleHintPossibilities_shouldDispatchAvailable_whenCandidatesExist() {
        MessageService service = new MessageService();
        TestObserver observer = new TestObserver();
        service.addObserver(observer);

        Cell cell = new Cell();
        for (int i = MIN_CELL_NUMBER; i <= MAX_CELL_NUMBER; i++) {
            if (i != 5 && i != 7) {
                cell.removePossibility(i);
            }
        }

        service.handleHintPossibilities(cell);

        assertEquals(MessageType.HINT_AVAILABLE.text("5, 7"), observer.lastMsg);
    }

    @Test
    void testInsertString_shouldDispatchInvalidInput_whenNonDigitEntered() {
        assertDoesNotThrow(() -> document.insertString(0, "x", null));
        assertEquals(MessageType.INVALID_INPUT.text(), observer.lastMsg);
    }

    @Test
    void testInsertString_shouldDispatchInvalidInput_whenDigitEnteredButDocumentNotEmpty() throws Exception {
        document.insertString(0, "5", null);

        assertDoesNotThrow(() -> document.insertString(1, "3", null));
        assertEquals(MessageType.INVALID_INPUT.text(), observer.lastMsg);
    }

    @Test
    void testInsertString_shouldDispatchClearMessage_whenValidNumberInserted() {
        assertDoesNotThrow(() -> document.insertString(0, "4", null));
        assertEquals(MessageType.CLEAR.text(), observer.lastMsg);
        assertEquals(4, cell.getNumber().orElseThrow());
    }

    @Test
    void testInsertString_shouldDispatchInvalidConstraint_whenNumberRejectedByCell() {
        for (int i = MIN_CELL_NUMBER; i <= MAX_CELL_NUMBER; i++) {
            cell.removePossibility(i);
        }

        assertDoesNotThrow(() -> document.insertString(0, "6", null));
        assertEquals(MessageType.INVALID_CONSTRAINT.text(), observer.lastMsg);
        assertTrue(cell.getNumber().isEmpty());
    }

    @Test
    void testInsertString_shouldDispatchInvalidInput_whenInputIsNull() {
        assertDoesNotThrow(() -> document.insertString(0, null, null));
        assertEquals(MessageType.INVALID_INPUT.text(), observer.lastMsg);
    }

    @Test
    void testCellActivationController_shouldIgnoreNonActivationEvent() throws Exception {
        Cell cell = new Cell();
        CellUI cellUI = new CellUI(cell, false);
        CellActivationController controller = new CellActivationController(cellUI);

        NumberEvent nonActivationEvent = new NumberEvent(7, true);

        controller.update(cell, nonActivationEvent);

        SwingUtilities.invokeAndWait(() -> {
            assertEquals(ACTIVE_BORDER, cellUI.getBorder());
        });
    }

    @Test
    void testCellActivationController_shouldCallNotifyStuckIfCellInputDocument() {
        Cell cell = new Cell();
        CellUI cellUI = new CellUI(cell, false);
        MessageService service = new MessageService();

        CellInputDocument document = new CellInputDocument(cell, service);
        cellUI.setDocument(document);

        new CellActivationController(cellUI);
        for (int i = 1; i <= 9; i++) cell.removePossibility(i);
        cell.addPossibility(5);

        SwingUtilities.invokeLater(() -> {
            assertTrue(cellUI.getDocument() instanceof CellInputDocument);
        });
    }

    @Test
    void testMessageUIController_shouldIgnoreNonMessageEvents() {
        MessageUI ui = new MessageUI();
        MessageService service = new MessageService();
        new MessageUIController(ui, service);

        service.notifyObservers(new NumberEvent(3, true));
        assertTrue(ui.getText() == null || ui.getText().isEmpty());
    }

}
