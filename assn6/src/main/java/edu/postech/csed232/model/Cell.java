package edu.postech.csed232.model;

import edu.postech.csed232.base.Observer;
import edu.postech.csed232.base.Subject;
import edu.postech.csed232.event.ActivationEvent;
import edu.postech.csed232.event.NumberEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static edu.postech.csed232.base.GameConstants.MAX_CELL_NUMBER;
import static edu.postech.csed232.base.GameConstants.MIN_CELL_NUMBER;

/**
 * A cell that has a number and a set of possibilities. A cell may have a number of observers,
 * and notifies events to the observers.
 */
public class Cell extends Subject {
    //TODO: add private members variables and private methods as needed
    @NotNull
    private Optional<Integer> number;
    @NotNull
    private final Set<Integer> possibilities;
    @NotNull
    private final List<CellConstraint> constraints;


    /**
     * Creates an empty cell. Initially, all numbers are possible.
     */
    public Cell() {
        //TODO: implement this
        this.number = Optional.empty();
        this.possibilities = new HashSet<>();
        for (int n = MIN_CELL_NUMBER; n <= MAX_CELL_NUMBER; n++){
            possibilities.add(n);
        }
        this.constraints = new ArrayList<>();
    }

    /**
     * Returns the number assigned to this cell.
     *
     * @return the number, or Optional.empty() if the cell is empty
     */
    @NotNull
    public Optional<Integer> getNumber() {
        //TODO: implement this, and remove the following throw statement
        return this.number;
    }

    /**
     * Assigns a number to this cell if it is empty and the number is allowed.
     * Notifies observers (using NumberEvent) if the number is set.
     *
     * @param number the number to set
     * @return true if the number is successfully set
     */
    public boolean setNumber(int number) {
        //TODO: implement this, and remove the following throw statement
        if (this.number.isEmpty() && containsPossibility(number)) {
            this.number = Optional.of(number);
            notifyObservers(new NumberEvent(number, true));
            return true;
        }
        return false;
    }

    /**
     * Unsets the assigned number if present.
     * Notifies observers (using NumberEvent) if the number is removed.
     *
     * @return true if the number was successfully removed
     */
    public boolean unsetNumber() {
        //TODO: implement this, and remove the following throw statement
        if (this.number.isPresent()) {
            int old = this.number.get();
            this.number = Optional.empty();
            notifyObservers(new NumberEvent(old, false));
            return true;
        }
        return false;
    }

    /**
     * Registers a constraint (i.e., a group) for this cell and adds it as an
     * observer if applicable.
     *
     * @param constraint the constraint to register
     */
    public void registerConstraint(@NotNull CellConstraint constraint) {
        if (constraint instanceof Observer observer)
            addObserver(observer);

        //TODO: implement this, and remove the following throw statement
        this.constraints.add(constraint);
    }

    /**
     * Returns whether a number is currently considered possible.
     *
     * @param number the number to check
     * @return true if the number is a valid possibility
     */
    public boolean containsPossibility(int number) {
        //TODO: implement this, and remove the following throw statement
        return possibilities.contains(number);
    }

    /**
     * Returns true if the cell has no possible values remaining.
     *
     * @return true if the set of possibilities is empty
     */
    public boolean hasNoPossibility() {
        //TODO: implement this, and remove the following throw statement
        return possibilities.isEmpty();
    }

    /**
     * Adds a possibility to this cell, if valid under all constraints. If the cell was previously
     * deactivated (no possibilities) and now becomes active, notifies with ActivationEvent(true).
     *
     * @param number the number to add as a possibility
     */
    public void addPossibility(int number) {
        //TODO: implement this, and remove the following throw statement
        if(!possibilities.contains(number)){
            for (CellConstraint constraint : constraints){
                if (!constraint.isAvailable(number)){
                    return;
                }
            }
            boolean wasEmpty = possibilities.isEmpty();
            possibilities.add(number);
            if (wasEmpty){
                notifyObservers(new ActivationEvent(true));
            }
        }
    }

    /**
     * Removes a possibility from this cell. If the cell becomes deactivated (no possibilities left),
     * notifies with ActivationEvent(false).
     *
     * @param number the number to remove
     */
    public void removePossibility(int number) {
        //TODO: implement this, and remove the following throw statement
        if (possibilities.remove(number) && possibilities.isEmpty()){
            notifyObservers(new ActivationEvent(false));
        }
    }

}
