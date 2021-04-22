package pl.nox.sudokusolver;

import java.util.ArrayList;
import java.util.List;

public class SudokuField {


    private boolean solved;
    private boolean beingGuessed;
    private int box;
    private List<Integer> possibleValues;

    public void setPossibleValues(List<Integer> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public SudokuField(boolean solved, int row, int column, int solvedValue) {
        this.solved = solved;
        this.beingGuessed = false;
        if (row <= 3 && column <= 3) {
            box = 0;
        }
        if (row <= 3 && column > 3 && column <= 6) {
            box = 1;
        }
        if (row <= 3 && column > 6) {
            box = 2;
        }
        if (row > 3 && row <= 6 && column <= 3) {
            box = 3;
        }
        if (row > 3 && row <= 6 && column > 3 && column <= 6) {
            box = 4;
        }
        if (row > 3 && row <= 6 && column > 6) {
            box = 5;
        }
        if (row > 6 && column <= 3) {
            box = 6;
        }
        if (row > 6 && column > 3 && column <= 6) {
            box = 7;
        }
        if (row > 6 && column > 6) {
            box = 8;
        }
        List<Integer> possibleValues = new ArrayList<>();
        if (solved) {
            possibleValues.add(solvedValue);
        } else {
            for (int i = 1; i <= 9; i++) {
                possibleValues.add(i);
            }
        }
        this.possibleValues = possibleValues;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public int getBox() {
        return box;
    }

    public List<Integer> getPossibleValues() {
        return possibleValues;
    }
}
