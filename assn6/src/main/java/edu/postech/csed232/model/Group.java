package edu.postech.csed232.model;

import edu.postech.csed232.base.Observer;
import edu.postech.csed232.base.Subject;
import edu.postech.csed232.event.Event;
import edu.postech.csed232.event.NumberEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A group that observes a set of cells, and maintains the invariant: if one
 * of the members has a particular value, none of its other members can have
 * that value as a possibility. Also reacts to cell updates by updating the
 * set of allowed possibilities.
 */
public class Group implements Observer, CellConstraint {
    //TODO: add private member variables and methods as necessary
    private final List<Cell> cells;

    /**
     * Creates an empty group.
     */
    public Group() {
        //TODO: implement this
        this.cells = new ArrayList<>();
    }

    /**
     * Adds a cell to this group and registers this group as a constraint and observer.
     * Assumes the cell has not yet been assigned a number.
     *
     * @param cell a cell to be added
     */
    public void addCell(@NotNull Cell cell) {
        //TODO: implement this, and remove the following throw statement
        cells.add(cell);
        cell.registerConstraint(this);
    }

    /**
     * Returns true if the given number is not currently used in any member cell.
     *
     * @param number the number to check
     * @return true if the number is not used; false otherwise
     */
    @Override
    public boolean isAvailable(int number) {
        //TODO: implement this, and remove the following throw statement
        for (Cell cell : cells) {
            Optional<Integer> v = cell.getNumber();
            if (v.isPresent() && v.get() == number) {
                return false;
            }
        }
        return true;
    }

    /**
     * Responds to a number assignment or removal by updating possible values in the other
     * cells. If one of the cells has a particular value, none of its other members can have
     * the value as a possibility.
     *
     * @param caller the subject that triggered the event
     * @param arg    the event describing the number assignment or removal
     */
    @Override
    public void update(@NotNull Subject caller, @NotNull Event arg) {
        //TODO: implement this, and remove the following throw statement
        if (arg instanceof NumberEvent ne) {
            int num = ne.number();
            boolean isSet = ne.set();
            Cell src = (Cell) caller;
            for (Cell cell : cells) {
                if(cell != src){
                    if(isSet) {
                        cell.removePossibility(num);
                    } else {
                        cell.addPossibility(num);
                    }
                }
            }

        }
    }

}
