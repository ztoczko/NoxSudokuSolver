package pl.nox.sudokusolver;

import java.util.ArrayList;
import java.util.List;

public class Sudoku {

    private SudokuField[][] fields;
    private boolean isSolved;
    private boolean isPartOfMultipleSolutions;
    private boolean invalid;

    public Sudoku(int[] fieldArray) {

        SudokuField[][] result = new SudokuField[9][];

        for (int i = 0; i < 9; i++) {
            result[i] = new SudokuField[9];
            for (int j = 0; j < 9; j++) {
                result[i][j] = fieldArray[i * 9 + j] == 0 ? new SudokuField(false, i + 1, j + 1, 0) :
                        new SudokuField(true, i + 1, j + 1, fieldArray[i * 9 + j]);
            }
        }
        fields = result;

        isSolved = false;
        isPartOfMultipleSolutions = false;
        invalid = false;
    }

    public boolean getIsSolved() {
        return isSolved;
    }

    public boolean getIsPartOfMultipleSolutions() {
        return isPartOfMultipleSolutions;
    }

    public boolean heuristicSolve() {

        eraseImpossibleValues();

//        while (!invalid) {
            while (!invalid) {

                if (!findNakedSingles() && !findHiddenSingles()) {
                    break;
                }
                eraseImpossibleValues();
            }
//            if (!findAllPairs()) {
//                break;
//            }
//            eraseImpossibleValues();
//        }

        checkIfInvalid();
        if (!invalid) {
            checkIfSolved();
            return isSolved;
        } else {
            return false;
        }
    }

    public boolean findAllPairs() {

        boolean isChange = false;

        for (int i = 0; i < 9; i++) {

            while (findNakedPairsRow(i) || findNakedPairsColumn(i) || findNakedPairsBox(i) || findHiddenPairsRow(i) || findHiddenPairsColumn(i) || findHiddenPairsBox(i)) {
                isChange = true;
            }
        }
        return isChange;
    }

    public boolean findHiddenPairsRow(int row) {

        List<Integer> columnsWithValue1 = new ArrayList<>();
        List<Integer> columnsWithValue2 = new ArrayList<>();
        boolean isChanged = false;

        for (int number1 = 1; number1 < 9; number1++) {
            columnsWithValue1 = new ArrayList<>();

            for (int i = 0; i < 9; i++) {

                if (fields[row][i].getPossibleValues().contains(number1)) {
                    columnsWithValue1.add(i);
                }
            }
            if (columnsWithValue1.size() == 2) {

                for (int number2 = number1 + 1; number2 <= 9; number2++) {
                    columnsWithValue2 = new ArrayList<>();

                    for (int i = 0; i < 9; i++) {

                        if (fields[row][i].getPossibleValues().contains(number2)) {
                            columnsWithValue2.add(i);
                        }
                    }
                    if (columnsWithValue2.size() == 2 && columnsWithValue1.get(0) == columnsWithValue2.get(0) && columnsWithValue1.get(1) == columnsWithValue2.get(1)) {

                        if (fields[row][columnsWithValue1.get(0)].getPossibleValues().size() > 2 || fields[row][columnsWithValue1.get(1)].getPossibleValues().size() > 2) {
                            fields[row][columnsWithValue1.get(0)].setPossibleValues(new ArrayList<>());
                            fields[row][columnsWithValue1.get(1)].setPossibleValues(new ArrayList<>());
                            fields[row][columnsWithValue1.get(0)].getPossibleValues().add(number1);
                            fields[row][columnsWithValue1.get(0)].getPossibleValues().add(number2);
                            fields[row][columnsWithValue1.get(1)].getPossibleValues().add(number1);
                            fields[row][columnsWithValue1.get(1)].getPossibleValues().add(number2);
                            isChanged = true;
                        }
                    }
                }
            }
        }
        return isChanged;
    }


    public boolean findHiddenPairsColumn(int column) {

        List<Integer> rowsWithValue1 = new ArrayList<>();
        List<Integer> rowsWithValue2 = new ArrayList<>();
        boolean isChanged = false;

        for (int number1 = 1; number1 < 9; number1++) {
            rowsWithValue1 = new ArrayList<>();

            for (int i = 0; i < 9; i++) {
                if (fields[i][column].getPossibleValues().contains(number1)) {
                    rowsWithValue1.add(i);
                }
            }
            if (rowsWithValue1.size() == 2) {

                for (int number2 = number1 + 1; number2 <= 9; number2++) {
                    rowsWithValue2 = new ArrayList<>();

                    for (int i = 0; i < 9; i++) {
                        if (fields[i][column].getPossibleValues().contains(number2)) {
                            rowsWithValue2.add(i);
                        }
                    }
                    if (rowsWithValue2.size() == 2 && rowsWithValue1.get(0) == rowsWithValue2.get(0) && rowsWithValue1.get(1) == rowsWithValue2.get(1)) {
                        if (fields[rowsWithValue1.get(0)][column].getPossibleValues().size() > 2 || fields[rowsWithValue1.get(1)][column].getPossibleValues().size() > 2) {
                            fields[rowsWithValue1.get(0)][column].setPossibleValues(new ArrayList<>());
                            fields[rowsWithValue1.get(1)][column].setPossibleValues(new ArrayList<>());
                            fields[rowsWithValue1.get(0)][column].getPossibleValues().add(number1);
                            fields[rowsWithValue1.get(0)][column].getPossibleValues().add(number2);
                            fields[rowsWithValue1.get(1)][column].getPossibleValues().add(number1);
                            fields[rowsWithValue1.get(1)][column].getPossibleValues().add(number2);
                            isChanged = true;
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    public boolean findHiddenPairsBox(int box) {

        List<Integer> fieldsWithValue1 = new ArrayList<>();
        List<Integer> fieldsWithValue2 = new ArrayList<>();
        boolean isChanged = false;

        for (int number1 = 1; number1 < 9; number1++) {
            fieldsWithValue1 = new ArrayList<>();

            for (int i = 0; i < 9; i++) {

                if (fields[box / 3 * 3 + (i / 3)][box % 3 * 3 + (i % 3)].getPossibleValues().contains(number1)) {
                    fieldsWithValue1.add(i);
                }
            }
            if (fieldsWithValue1.size() == 2) {

                for (int number2 = number1 + 1; number2 <= 9; number2++) {
                    fieldsWithValue2 = new ArrayList<>();

                    for (int i = 0; i < 9; i++) {

                        if (fields[box / 3 * 3 + (i / 3)][box % 3 * 3 + (i % 3)].getPossibleValues().contains(number2)) {
                            fieldsWithValue2.add(i);
                        }
                    }
                    if (fieldsWithValue2.size() == 2 && fieldsWithValue1.get(0) == fieldsWithValue2.get(0) && fieldsWithValue1.get(1) == fieldsWithValue2.get(1)) {

                        if (fields[box / 3 * 3 + (fieldsWithValue1.get(0) / 3)][box % 3 * 3 + (fieldsWithValue1.get(0) % 3)].getPossibleValues().size() > 2 || fields[box / 3 * 3 + (fieldsWithValue1.get(1) / 3)][box % 3 * 3 + (fieldsWithValue1.get(0) % 3)].getPossibleValues().size() > 2) {
                            fields[box / 3 * 3 + (fieldsWithValue1.get(0) / 3)][box % 3 * 3 + (fieldsWithValue1.get(0) % 3)].setPossibleValues(new ArrayList<>());
                            fields[box / 3 * 3 + (fieldsWithValue1.get(1) / 3)][box % 3 * 3 + (fieldsWithValue1.get(1) % 3)].setPossibleValues(new ArrayList<>());
                            fields[box / 3 * 3 + (fieldsWithValue1.get(0) / 3)][box % 3 * 3 + (fieldsWithValue1.get(0) % 3)].getPossibleValues().add(number1);
                            fields[box / 3 * 3 + (fieldsWithValue1.get(0) / 3)][box % 3 * 3 + (fieldsWithValue1.get(0) % 3)].getPossibleValues().add(number2);
                            fields[box / 3 * 3 + (fieldsWithValue1.get(1) / 3)][box % 3 * 3 + (fieldsWithValue1.get(1) % 3)].getPossibleValues().add(number1);
                            fields[box / 3 * 3 + (fieldsWithValue1.get(1) / 3)][box % 3 * 3 + (fieldsWithValue1.get(1) % 3)].getPossibleValues().add(number2);
                            isChanged = true;
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    public boolean findNakedPairsRow(int row) {

        boolean isChanged = false;
        List<Integer> fieldsWithBothValues = new ArrayList<>();

        for (int number1 = 1; number1 < 9; number1++) {

            for (int number2 = number1 + 1; number2 <= 9; number2++) {
                fieldsWithBothValues = new ArrayList<>();
                for (int i = 0; i < 9; i++) {

                    if (fields[row][i].getPossibleValues().size() == 2 && fields[row][i].getPossibleValues().contains(number1) && fields[row][i].getPossibleValues().contains(number2)) {
                        fieldsWithBothValues.add(i);
                    }
                }
                if (fieldsWithBothValues.size() == 2) {
                    for (int i = 0; i < 9; i++) {
                        if (i != fieldsWithBothValues.get(0) && i != fieldsWithBothValues.get(1) && (fields[row][i].getPossibleValues().contains(number1) || fields[row][i].getPossibleValues().contains(number2))) {
                            isChanged = true;
                            fields[row][i].getPossibleValues().remove((Integer) number1);
                            fields[row][i].getPossibleValues().remove((Integer) number2);
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    public boolean findNakedPairsColumn(int column) {

        boolean isChanged = false;
        List<Integer> fieldsWithBothValues = new ArrayList<>();

        for (int number1 = 1; number1 < 9; number1++) {

            for (int number2 = number1 + 1; number2 <= 9; number2++) {
                fieldsWithBothValues = new ArrayList<>();
                for (int i = 0; i < 9; i++) {

                    if (fields[i][column].getPossibleValues().size() == 2 && fields[i][column].getPossibleValues().contains(number1) && fields[i][column].getPossibleValues().contains(number2)) {
                        fieldsWithBothValues.add(i);
                    }
                }
                if (fieldsWithBothValues.size() == 2) {
                    for (int i = 0; i < 9; i++) {
                        if (i != fieldsWithBothValues.get(0) && i != fieldsWithBothValues.get(1) && (fields[i][column].getPossibleValues().contains(number1) || fields[i][column].getPossibleValues().contains(number2))) {
                            isChanged = true;
                            fields[i][column].getPossibleValues().remove((Integer) number1);
                            fields[i][column].getPossibleValues().remove((Integer) number2);
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    public boolean findNakedPairsBox(int box) {

        boolean isChanged = false;
        List<Integer> fieldsWithBothValues = new ArrayList<>();

        for (int number1 = 1; number1 < 9; number1++) {

            for (int number2 = number1 + 1; number2 <= 9; number2++) {
                fieldsWithBothValues = new ArrayList<>();
                for (int i = 0; i < 9; i++) {

                    if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().size() == 2 && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number1) && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number2)) {
                        fieldsWithBothValues.add(i);
                    }
                }
                if (fieldsWithBothValues.size() == 2) {
                    for (int i = 0; i < 9; i++) {
                        if (i != fieldsWithBothValues.get(0) && i != fieldsWithBothValues.get(1) && (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number1) || fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number2))) {
                            isChanged = true;
                            fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().remove((Integer) number1);
                            fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().remove((Integer) number2);
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    public boolean findHiddenSingles() {

        boolean isChanged = false;
        List<Integer> fieldsWithValue = new ArrayList<>();

        //select number
        for (int number = 1; number <= 9; number++) {

            //checking rows
            for (int i = 0; i < 9; i++) {
                fieldsWithValue = new ArrayList<>();
                for (int j = 0; j < 9; j++) {
                    if (fields[i][j].isSolved() && fields[i][j].getPossibleValues().get(0) == number) {
                        fieldsWithValue = new ArrayList<>();
                        break;
                    }
                    if (fields[i][j].getPossibleValues().contains(number)) {
                        fieldsWithValue.add(j);
                    }
                }
                if (fieldsWithValue.size() == 1) {
                    fields[i][fieldsWithValue.get(0)].setPossibleValues(new ArrayList<>());
                    fields[i][fieldsWithValue.get(0)].getPossibleValues().add(number);
                    fields[i][fieldsWithValue.get(0)].setSolved(true);
                    isChanged = true;
                }
            }
            //checking columns
            for (int i = 0; i < 9; i++) {
                fieldsWithValue = new ArrayList<>();
                for (int j = 0; j < 9; j++) {
                    if (fields[j][i].isSolved() && fields[j][i].getPossibleValues().get(0) == number) {
                        fieldsWithValue = new ArrayList<>();
                        break;
                    }
                    if (fields[j][i].getPossibleValues().contains(number)) {
                        fieldsWithValue.add(j);
                    }
                }
                if (fieldsWithValue.size() == 1) {
                    fields[fieldsWithValue.get(0)][i].setPossibleValues(new ArrayList<>());
                    fields[fieldsWithValue.get(0)][i].getPossibleValues().add(number);
                    fields[fieldsWithValue.get(0)][i].setSolved(true);
                    isChanged = true;
                }
            }
            //checking boxes
            for (int i = 0; i < 9; i++) {
                fieldsWithValue = new ArrayList<>();
                for (int j = 0; j < 9; j++) {
                    if (fields[i / 3 * 3 + j / 3][i % 3 * 3 + j % 3].isSolved() && fields[i / 3 * 3 + j / 3][i % 3 * 3 + j % 3].getPossibleValues().get(0) == number) {
                        fieldsWithValue = new ArrayList<>();
                        break;
                    }
                    if (fields[i / 3 * 3 + j / 3][i % 3 * 3 + j % 3].getPossibleValues().contains(number)) {
                        fieldsWithValue.add(j);
                    }
                }
                if (fieldsWithValue.size() == 1) {
                    fields[i / 3 * 3 + fieldsWithValue.get(0) / 3][i % 3 * 3 + fieldsWithValue.get(0) % 3].setPossibleValues(new ArrayList<>());
                    fields[i / 3 * 3 + fieldsWithValue.get(0) / 3][i % 3 * 3 + fieldsWithValue.get(0) % 3].getPossibleValues().add(number);
                    fields[i / 3 * 3 + fieldsWithValue.get(0) / 3][i % 3 * 3 + fieldsWithValue.get(0) % 3].setSolved(true);
                    isChanged = true;
                }
            }
        }
        return isChanged;
    }

    public boolean findNakedSingles() {

        boolean isChanged = false;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!fields[i][j].isSolved() && fields[i][j].getPossibleValues().size() == 1) {
                    fields[i][j].setSolved(true);
                    isChanged = true;
                }
            }
        }
        return isChanged;
    }


    public boolean eraseImpossibleValues() {

        boolean isChanged = false;

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) { //finding solved fields

                if (fields[i][j].isSolved()) {

                    int number = fields[i][j].getPossibleValues().get(0);
                    int box = i / 3 * 3 + j / 3;
                    //eliminating solved number from row, column and box:
                    for (int k = 0; k < 9; k++) {
                        //check row
                        if (k != j && fields[i][k].getPossibleValues().contains(number)) {
                            fields[i][k].getPossibleValues().remove((Integer) number);
                            switch (fields[i][k].getPossibleValues().size()) {
                                case 0:
                                    invalid = true;
                                    fields[i][k].setSolved(false);
                                    break;
                                case 1:
                                    fields[i][k].setSolved(false);
                                    break;
                                default:
                                    break;
                            }
                            isChanged = true;
                        }
                        //check column
                        if (k != i && fields[k][j].getPossibleValues().contains(number)) {
                            fields[k][j].getPossibleValues().remove((Integer) number);
                            switch (fields[k][j].getPossibleValues().size()) {
                                case 0:
                                    invalid = true;
                                    fields[k][j].setSolved(false);
                                    break;
                                case 1:
                                    fields[k][j].setSolved(false);
                                    break;
                                default:
                                    break;
                            }
                            isChanged = true;
                        }
                        //check box
                        if (i != box / 3 * 3 + k / 3 && j != box % 3 * 3 + k % 3 && fields[box / 3 * 3 + k / 3][box % 3 * 3 + k % 3].getPossibleValues().contains(number)) {
                            fields[box / 3 * 3 + k / 3][box % 3 * 3 + k % 3].getPossibleValues().remove((Integer) number);
                            switch (fields[box / 3 * 3 + k / 3][box % 3 * 3 + k % 3].getPossibleValues().size()) {
                                case 0:
                                    invalid = true;
                                    fields[box / 3 * 3 + k / 3][box % 3 * 3 + k % 3].setSolved(false);
                                    break;
                                case 1:
                                    fields[box / 3 * 3 + k / 3][box % 3 * 3 + k % 3].setSolved(true);
                                    break;
                                default:
                                    break;
                            }
                            isChanged = true;
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    public void checkIfSolved() {

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {

                if (!fields[i][j].isSolved()) {
                    isSolved = false;
                    return;
                }
            }
        }
        isSolved = true;
    }

    public void checkIfInvalid() {

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {

                if (fields[row][column].getPossibleValues().size() == 0) {
                    invalid = true;
                    return;
                }

                if (fields[row][column].isSolved()) {
                    int box = row / 3 * 3 + column / 3;
                    for (int i = 0; i < 9; i ++){

                        if ((i != column && fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == fields[row][column].getPossibleValues().get(0)) ||
                                (i != row && fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == fields[row][column].getPossibleValues().get(0)) ||
                                (i != (row % 3 * 3 + column % 3) && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == fields[row][column].getPossibleValues().get(0))) {
//                            System.out.println(row);
//                            System.out.println(column);
//                            System.out.println(box / 3 * 3 + i / 3);
//                            System.out.println(box % 3 * 3 + i % 3);
//                            System.out.println((i != column && fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == fields[row][column].getPossibleValues().get(0)) );
//                            System.out.println((i != row && fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == fields[i][column].getPossibleValues().get(0)));
//                            System.out.println((i != (row % 3 * 3 + column % 3) && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == fields[row][column].getPossibleValues().get(0)));
                            invalid = true;
                            return;
                        }
                    }
                }
            }
        }
        invalid = false;
    }


    public String toString() {

        String result = new String();

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {
                result += fields[i][j].getPossibleValues().get(0) + " ";
            }
            result += "\n";
        }
        return result;
    }
}

