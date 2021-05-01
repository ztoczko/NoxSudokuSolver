package pl.nox.sudokusolver.model;

import java.util.ArrayList;
import java.util.List;

public class SudokuField {


    private boolean solved;
    private List<Integer> possibleValues;

    public void setPossibleValues(List<Integer> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public SudokuField(boolean solved, int solvedValue) {
        this.solved = solved;

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

    public SudokuField(boolean solved, List<Integer> possibleValues) {
        this(solved, solved ? possibleValues.get(0) : 0);
        setPossibleValues(possibleValues);
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public List<Integer> getPossibleValues() {
        return possibleValues;
    }

    public SudokuField clone() {
       return new SudokuField(solved, this.clonePossibleValues());
    }

    public List<Integer> clonePossibleValues() {
        List<Integer> clonedValues = new ArrayList<Integer>();
        for (Integer i : possibleValues) {
            clonedValues.add(i);
        }
        return clonedValues;
    }
}
