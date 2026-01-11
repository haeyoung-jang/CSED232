package edu.postech.csed232;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Two teams of N rabbits each are placed facing each other in a row with 2N + 1
 * positions. Initially, the x team occupies the first N positions, the o team
 * occupies the last N positions, and the middle position is empty. The goal is
 * to swap the positions of the two teams by moving the rabbits. A rabbit can
 * move to an empty position or jump over a rival to an empty position.
 */
public class HopingRabbitsGame {
    /**
     * The state of the game, represented as an array of rabbits, where
     * the empty position is represented as EMPTY.
     */
    private Rabbit[] rabbits;

    //TODO: feel free to add any private fields and methods to implement this class,
    // if necessary. But do not change the signature of the public methods, or do
    // not add any public method, including constructors.

    /**
     * Create a game with n rabbits on each team. For example, if n = 3, the initial
     * state is xxx_yyy, where x represents a rabbit from the x team, y represents a
     * rabbit from the o team, and _ represents an empty position.
     *
     * @param n the number of rabbits on each team
     */
    HopingRabbitsGame(int n) {
        rabbits = new Rabbit[2 * n + 1];

        //TODO: feel free to add any other initialization code, if necessary.
        // initialization code 왼쪽 n개를 x로
        for (int i = 0; i < n; i++) {
            rabbits[i] = Rabbit.X;
        }

        // center is empty
        rabbits[n] = Rabbit.EMPTY;

        // initialization code 오른쪽 n개를 o로
        for (int i = n + 1 ; i < 2*n +1 ; i++) {
            rabbits[i] = Rabbit.O;
        }

    }

    /**
     * Move a rabbit to an empty position. Rabbits from the x team can only move to
     * the right, and rabbits from the o team can only move to the left. A rabbit is
     * allowed to advance one position if that position is empty. A rabbit can jump
     * over a rival if the position behind the rival is empty. For example, if the
     * current state is xxxo_oo, the x team can move to xx_oxoo.
     *
     * @param rabbit a rabbit
     * @return true if the rabbit can move, false otherwise
     */
    boolean move(Rabbit rabbit) {
        //TODO: implement this
        // X를 이동시키는 경우 = 오른쪽으로 이동시키는 경우
        if (rabbit == Rabbit.X) {
            for (int i = 0; i < rabbits.length; i++) {
                if (rabbits[i] == Rabbit.X) {
                    // 바로 오른쪽이 비어있다면 한 칸 이동
                    if (i+1 < rabbits.length && rabbits[i+1] == Rabbit.EMPTY) {
                        rabbits[i+1] = Rabbit.X;
                        rabbits[i] = Rabbit.EMPTY;
                        return true;
                    }
                    // 바로 오른쪽에 O가 있고 그 오른쪽이 비어있다면 점프
                    if (i+2 < rabbits.length && rabbits[i+1] == Rabbit.O && rabbits[i+2] == Rabbit.EMPTY) {
                        rabbits[i+2] = Rabbit.X;
                        rabbits[i] = Rabbit.EMPTY;
                        return true;
                    }
                }

            }
        }
        // O를 이동시키는 경우 = 왼쪽으로 이동시키는 경우
        else if (rabbit == Rabbit.O) {
            for (int i = rabbits.length-1; i >= 0; i--) {
                if (rabbits[i] == Rabbit.O) {
                    // 바로 왼쪽이 비어있다면 한 칸 이동
                    if (i-1 >= 0 && rabbits[i-1] == Rabbit.EMPTY) {
                        rabbits[i-1] = Rabbit.O;
                        rabbits[i] = Rabbit.EMPTY;
                        return true;
                    }
                    // 바로 왼쪽에 X가 있고 그 왼쪽이 비어있다면 점프
                    if (i-2 >= 0 && rabbits[i-1] == Rabbit.X && rabbits[i-2] == Rabbit.EMPTY) {
                        rabbits[i-2] = Rabbit.O;
                        rabbits[i] = Rabbit.EMPTY;
                        return true;
                    }
                }
            }
        }
        // 이동불가
        return false;
    }

    /**
     * Return true if the game is over. The game is over if the two teams have swapped
     * their initial positions: e.g., ooo_xxx, when N = 3.
     *
     * @return true if the game is over, false otherwise
     */
    boolean isGoal() {
        //TODO: implement this
        int n = (rabbits.length-1)/2;
        // check O
        for (int i = 0; i < n; i++) {
            if (rabbits[i] != Rabbit.O) {
                return false;
            }
        }
        // check center
        if (rabbits[n] != Rabbit.EMPTY) {
            return false;
        }
        // check X
        for (int i = n+1; i < rabbits.length; i++) {
            if (rabbits[i] != Rabbit.X) {
                return false;
            }
        }

        return true;
    }

    /**
     * Return true if the game is stuck. The game is stuck if no rabbit can move.
     *
     * @return true if the game is stuck, false otherwise
     */
    boolean isStuck() {
        //TODO: implement this
        // move메서드에서 이동할 조건을 하나라도 만족시키면 false
        for (int i = 0; i < rabbits.length; i++) {
            // X의 이동가능 여부 확인
            if (rabbits[i] == Rabbit.X) {
                // check moving
                if (i+1 < rabbits.length && rabbits[i+1] == Rabbit.EMPTY) {
                    return false;
                }
                // check jumping
                if (i+2 < rabbits.length && rabbits[i+1] == Rabbit.O && rabbits[i+2] == Rabbit.EMPTY) {
                    return false;
                }
            }
            else if (rabbits[i] == Rabbit.O) {
                // check moving
                if (i-1 >= 0 && rabbits[i-1] == Rabbit.EMPTY) {
                    return false;
                }
                // check jumping
                if (i-2 >= 0 && rabbits[i-1] == Rabbit.X && rabbits[i-2] == Rabbit.EMPTY) {
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * Return the string representation of the game state. The string has length 2N + 1
     * and consists of x, o, and _.
     *
     * @return a string of length 2N + 1 consisting of x, o, and _.
     */
    @Override
    public String toString() {
        return Arrays.stream(rabbits).map(rabbit -> switch (rabbit) {
            case X -> "x";
            case O -> "o";
            case EMPTY -> "_";
        }).collect(Collectors.joining());
    }

    public static void main(String[] args) {
        var game = new HopingRabbitsGame(3);

        System.out.println(game);
        game.move(Rabbit.X);
        System.out.println(game);
        game.move(Rabbit.O);
        System.out.println(game);
    }

}