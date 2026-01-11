package edu.postech.csed232.control;

import edu.postech.csed232.base.GameConstants;
import edu.postech.csed232.base.Subject;
import edu.postech.csed232.event.MessageEvent;
import edu.postech.csed232.model.Cell;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Manages and communicates status messages within the UI.
 * It centralizes message generation and dispatch for various events.
 */
public class MessageService extends Subject {

    /**
     * Sends a message indicating that no cell is currently selected.
     */
    public void handleHintNoSelection() {
        dispatch(MessageType.HINT_NO_SELECTION);
    }

    /**
     * Sends a message indicating that the selected cell already has a number.
     */
    public void handleHintHasNumber() {
        // TODO: implement this, and remove the following throw statement
        dispatch(MessageType.HINT_HAS_NUMBER);
    }

    /**
     * Sends a message listing the available values for a given cell,
     * or a message indicating that there are no available values.
     *
     * @param cell the cell for which available possibilities are to be displayed
     */
    public void handleHintPossibilities(@NotNull Cell cell) {
        // TODO: implement this, and remove the following throw statement
        String nums = IntStream
                .rangeClosed(GameConstants.MIN_CELL_NUMBER, GameConstants.MAX_CELL_NUMBER)
                .filter(cell::containsPossibility)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "));
        if (nums.isEmpty()) {
            dispatch(MessageType.HINT_NO_AVAILABLE);
        } else {
            dispatch(MessageType.HINT_AVAILABLE, nums);
        }
    }

    /**
     * Sends a message indicating that the last input was invalid (not a digit between 1 and 9).
     */
    public void handleInvalidInput() {
        // TODO: implement this, and remove the following throw statement
        dispatch(MessageType.INVALID_INPUT);
    }

    /**
     * Sends a message indicating that the input violates Sudoku constraints
     */
    public void handleInvalidConstraint() {
        // TODO: implement this, and remove the following throw statement
        dispatch(MessageType.INVALID_CONSTRAINT);
    }

    /**
     * Sends a message indicating that a cell becomes stuck (no remaining possibilities),
     * or that a cell recovers from being stuck.
     *
     * @param activated   whether the cell was activated
     * @param isEmptyCell whether the cell has a number assigned
     */
    public void handleStuckStatus(boolean activated, boolean isEmptyCell) {
        // TODO: implement this, and remove the following throw statement
        if (!activated && isEmptyCell) {
            dispatch(MessageType.STUCK);
        } else if (activated) {
            dispatch(MessageType.CLEAR);
        }
    }

    /**
     * Sends a message to clear any previously displayed message.
     */
    public void handleClearMessage() {
        dispatch(MessageType.CLEAR);
    }

    private void dispatch(@NotNull MessageType type, @NotNull Object... args) {
        notifyObservers(new MessageEvent(type.text(args)));
    }
}