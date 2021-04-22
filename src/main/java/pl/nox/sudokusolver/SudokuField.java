package pl.nox.sudokusolver;

import java.util.ArrayList;
import java.util.List;

public class SudokuField {


    private boolean solved;
    private boolean beingGuessed;
    private List<Integer> possibleValues;

    public void setPossibleValues(List<Integer> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public SudokuField(boolean solved, int solvedValue) {
        this.solved = solved;
        this.beingGuessed = false;

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

    public List<Integer> getPossibleValues() {
        return possibleValues;
    }
}
