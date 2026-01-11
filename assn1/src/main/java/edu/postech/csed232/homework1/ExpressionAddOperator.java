package edu.postech.csed232.homework1;

/**
 * Given two non-negative integers, number and target, determine if target is equivalent to
 * an expression obtained by inserting '+' or '-' between the digits of number.
 * <p>
 * Hint: Use recursion to try all possible combinations of '+' and '-'.
 */
public class ExpressionAddOperator {
    /**
     * Determines if it is possible to insert '+' or '-' between the digits of 'number' to
     * obtain 'target'. Both 'number' and 'target' are non-negative integers. For example,
     * if number = 1234 and target = 20, the method should return true since 1 + 23 - 4 = 20.
     *
     * @param number a non-negative integer
     * @param target a non-negative integer
     * @return true if 'target' can be obtained from 'number'
     * @throws IllegalArgumentException if number or target is negative
     */
    public boolean solve(int number, int target) {
        if (number < 0 || target < 0) {
            throw new IllegalArgumentException("Number and target must be non-negative.");
        }

        // local class for backtraking
        class Backtrack{
            boolean backtracking(String str_number, int index, int current, int target) {
                if (index == str_number.length()) {  // if index equals to length of str_number then finish to compare
                    if (current == target){
                        return true;
                    }
                    return false;
                }
                int cal_num = 0;
                // check for one digit until last digit
                for (int i = index; i < str_number.length(); i++) {
                    cal_num = cal_num*10 + (str_number.charAt(i) - '0'); // convert str to int with ASCII code

                    if (index == 0) {
                        // not add a operator to first digit
                        if (backtracking(str_number, i + 1, cal_num, target)){
                            return true;
                        }
                    } else{
                        // if add +
                        if (backtracking(str_number, i + 1, current + cal_num, target)){
                            return true;
                        }
                        // if add -
                        if (backtracking(str_number, i + 1, current - cal_num, target)){
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        String str_num = String.valueOf(number);
        Backtrack backtrack = new Backtrack();
        return backtrack.backtracking(str_num, 0, 0, target);
    }

    public static void main(String[] args) {
        var number = 1234;
        var target = 20;
        var checker = new ExpressionAddOperator();

        System.out.println("Can " + number + " form " + target + "? " + checker.solve(number, target));
    }
}
