package edu.postech.csed232.control;

import edu.postech.csed232.base.Observer;
import edu.postech.csed232.base.Subject;
import edu.postech.csed232.event.ActivationEvent;
import edu.postech.csed232.event.Event;
import edu.postech.csed232.view.CellUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Controller that observes a Cell model and updates the associated CellUI accordingly.
 */
public class CellActivationController implements Observer {

    @NotNull
    private final CellUI cellUI;

    /**
     * Constructs the controller and registers it to the observed Cell.
     *
     * @param cellUI the associated view component
     */
    public CellActivationController(@NotNull CellUI cellUI) {
        this.cellUI = cellUI;
        cellUI.getCell().addObserver(this);
    }

    /**
     * Reacts to changes in the Cell model and updates the view accordingly.
     *
     * @param caller the observed subject
     * @param arg    the event describing the change
     */
    @Override
    public void update(@NotNull Subject caller, @NotNull Event arg) {
        if (arg instanceof ActivationEvent(boolean activated)) {
            SwingUtilities.invokeLater(() -> {
                // TODO: implement this, and remove the following throw statement
                cellUI.setActivated(activated);
                var doc = cellUI.getDocument();
                if (doc instanceof CellInputDocument cid) {
                    cid.notifyStuckIfNeeded(activated);
                }
            });
        }
    }
}
