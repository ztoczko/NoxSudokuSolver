package pl.nox.sudokusolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Sudoku {

    public SudokuField[][] fields; //change to private after test phase
    private boolean isSolved;
    private boolean isPartOfMultipleSolutions;

    public boolean isInvalid() {
        return invalid;
    }

    private boolean invalid;

    public Sudoku(int[] fieldArray) {

        SudokuField[][] result = new SudokuField[9][];

        for (int i = 0; i < 9; i++) {
            result[i] = new SudokuField[9];
            for (int j = 0; j < 9; j++) {
                result[i][j] = fieldArray[i * 9 + j] == 0 ? new SudokuField(false, 0) :
                        new SudokuField(true, fieldArray[i * 9 + j]);
            }
        }
        fields = result;

        isSolved = false;
        isPartOfMultipleSolutions = false;
        invalid = false;
    }

    public Sudoku(String sudokuSeed) {

        SudokuField[][] result = new SudokuField[9][];

        for (int i = 0; i < 9; i++) {
            result[i] = new SudokuField[9];
            for (int j = 0; j < 9; j++) {
                result[i][j] = Integer.parseInt(sudokuSeed.substring(i * 9 + j, i * 9 + j + 1)) == 0 ? new SudokuField(false, 0) :
                        new SudokuField(true, Integer.parseInt(sudokuSeed.substring(i * 9 + j, i * 9 + j + 1)));
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

    public boolean heuristicSolve() { //add triples

        eraseImpossibleValues();

        while (!invalid) {
            while (!invalid) {

                if (!findNakedSingles() && !findHiddenSingles()) {
                    break;
                }
                eraseImpossibleValues();
            }
            if (!findAllPairs() && !findAllTriples() && !findAllQuads() && !findXWings()) {
                break;
            }
            eraseImpossibleValues();
        }

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

    public boolean findAllTriples() {

        boolean isChange = false;

        for (int i = 0; i < 9; i++) {

            while (findNakedTriplesRow(i) || findNakedTriplesColumn(i) || findNakedTriplesBox(i) || findHiddenTriplesRow(i) || findHiddenTriplesColumn(i) || findHiddenTriplesBox(i)) {
                isChange = true;
            }
        }
        return isChange;
    }

    public boolean findAllQuads() {

        boolean isChange = false;

        for (int i = 0; i < 9; i++) {

            while (findNakedQuadsRow(i) || findNakedQuadsColumn(i) || findNakedQuadsBox(i) || findHiddenQuadsRow(i) || findHiddenQuadsColumn(i) || findHiddenQuadsBox(i)) {
                isChange = true;
            }
        }
        return isChange;
    }

    public boolean findXWings() {

        return findXWingRows() || findXWingColumns();
    }

    public boolean findXWingRows() {

        boolean isChange = false;
        boolean numberAlreadySolved = false;
        List<Integer> columnsWithValueInRow1 = new ArrayList<>();
        List<Integer> columnsWithValueInRow2 = new ArrayList<>();

        for (int number = 1; number <= 9; number++) {

            for (int row1 = 0; row1 < 8; row1++) {

                columnsWithValueInRow1.clear();
                numberAlreadySolved = false;

                for (int i = 0; i < 9; i++) {

                    if (fields[row1][i].isSolved() && fields[row1][i].getPossibleValues().get(0) == number) {
                        numberAlreadySolved = true;
                        break;
                    }
                    if (fields[row1][i].getPossibleValues().contains(number)) {
                        columnsWithValueInRow1.add(i);
                    }
                }
                if (numberAlreadySolved) {
                    continue;
                }
                if (columnsWithValueInRow1.size() == 2) {

                    for (int row2 = row1 + 1; row2 < 9; row2++) {

                        columnsWithValueInRow2.clear();
                        numberAlreadySolved = false;

                        for (int i = 0; i < 9; i++) {

                            if (fields[row2][i].isSolved() && fields[row2][i].getPossibleValues().get(0) == number) {
                                numberAlreadySolved = true;
                                break;
                            }
                            if (fields[row2][i].getPossibleValues().contains(number)) {
                                columnsWithValueInRow2.add(i);
                            }
                        }
                        if (numberAlreadySolved) {
                            continue;
                        }
                        if (columnsWithValueInRow2.size() == 2 && columnsWithValueInRow1.containsAll(columnsWithValueInRow2)) {

                            for (int i = 0; i < 9; i++) {

                                if (i != row1 && i != row2 && (fields[i][columnsWithValueInRow1.get(0)].getPossibleValues().contains(number) || fields[i][columnsWithValueInRow1.get(1)].getPossibleValues().contains(number))) {

                                    isChange = true;
                                    fields[i][columnsWithValueInRow1.get(0)].getPossibleValues().remove(Integer.valueOf(number));
                                    fields[i][columnsWithValueInRow1.get(1)].getPossibleValues().remove(Integer.valueOf(number));
                                }
                            }
                        }
                    }
                }
            }
        }
        return isChange;
    }

    public boolean findXWingColumns() {

        boolean isChange = false;
        boolean numberAlreadySolved = false;
        List<Integer> rowsWithValueInColumn1 = new ArrayList<>();
        List<Integer> rowsWithValueInColumn2 = new ArrayList<>();

        for (int number = 1; number <= 9; number++) {

            for (int column1 = 0; column1 < 8; column1++) {

                rowsWithValueInColumn1.clear();
                numberAlreadySolved = false;

                for (int i = 0; i < 9; i++) {

                    if (fields[i][column1].isSolved() && fields[i][column1].getPossibleValues().get(0) == number) {
                        numberAlreadySolved = true;
                        break;
                    }
                    if (fields[i][column1].getPossibleValues().contains(number)) {
                        rowsWithValueInColumn1.add(i);
                    }
                }
                if (numberAlreadySolved) {
                    continue;
                }
                if (rowsWithValueInColumn1.size() == 2) {

                    for (int column2 = column1 + 1; column2 < 9; column2++) {

                        rowsWithValueInColumn2.clear();
                        numberAlreadySolved = false;

                        for (int i = 0; i < 9; i++) {

                            if (fields[i][column2].isSolved() && fields[i][column2].getPossibleValues().get(0) == number) {
                                numberAlreadySolved = true;
                                break;
                            }
                            if (fields[i][column2].getPossibleValues().contains(number)) {
                                rowsWithValueInColumn2.add(i);
                            }
                        }
                        if (numberAlreadySolved) {
                            continue;
                        }
                        if (rowsWithValueInColumn2.size() == 2 && rowsWithValueInColumn1.containsAll(rowsWithValueInColumn2)) {

                            for (int i = 0; i < 9; i++) {

                                if (i != column1 && i != column2 && (fields[rowsWithValueInColumn1.get(0)][i].getPossibleValues().contains(number) || fields[rowsWithValueInColumn1.get(1)][i].getPossibleValues().contains(number))) {

                                    isChange = true;
                                    fields[rowsWithValueInColumn1.get(0)][i].getPossibleValues().remove(Integer.valueOf(number));
                                    fields[rowsWithValueInColumn1.get(1)][i].getPossibleValues().remove(Integer.valueOf(number));
                                }
                            }
                        }
                    }
                }
            }
        }
        return isChange;
    }


    public boolean findHiddenQuadsRow(int row) {

        boolean isChanged = false;
        boolean numberAlreadySolved = false;
        List<Integer> columnsWithValue1 = new ArrayList<>();
        List<Integer> columnsWithValue2 = new ArrayList<>();
        List<Integer> columnsWithValue3 = new ArrayList<>();
        List<Integer> columnsWithValue4 = new ArrayList<>();
        Set<Integer> tempSet = new HashSet<>();
        List<Integer> numbers = new ArrayList<>();

        for (int number1 = 1; number1 < 7; number1++) {

            columnsWithValue1.clear();
            numberAlreadySolved = false;

            for (int i = 0; i < 9; i++) {

                if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
                if (fields[row][i].getPossibleValues().contains(number1)) {
                    columnsWithValue1.add(i);
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            if (columnsWithValue1.size() > 1 && columnsWithValue1.size() < 5) {

                for (int number2 = number1 + 1; number2 < 8; number2++) {

                    columnsWithValue2.clear();
                    numberAlreadySolved = false;

                    for (int i = 0; i < 9; i++) {

                        if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number2) {
                            numberAlreadySolved = true;
                            break;
                        }
                        if (fields[row][i].getPossibleValues().contains(number2)) {
                            columnsWithValue2.add(i);
                        }

                    }
                    if (numberAlreadySolved) {
                        continue;
                    }

                    if (columnsWithValue2.size() > 1 && columnsWithValue2.size() < 5) {

                        for (int number3 = number2 + 1; number3 < 9; number3++) {

                            columnsWithValue3.clear();
                            numberAlreadySolved = false;
                            for (int i = 0; i < 9; i++) {

                                if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number3) {
                                    numberAlreadySolved = true;
                                    break;
                                }
                                if (fields[row][i].getPossibleValues().contains(number3)) {
                                    columnsWithValue3.add(i);
                                }

                            }
                            if (numberAlreadySolved) {
                                continue;
                            }
                            if (columnsWithValue3.size() > 1 && columnsWithValue3.size() < 5) {

                                for (int number4 = number3 + 1; number4 <= 9; number4++) {

                                    columnsWithValue4.clear();
                                    numberAlreadySolved = false;
                                    for (int i = 0; i < 9; i++) {

                                        if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number4) {
                                            numberAlreadySolved = true;
                                            break;
                                        }
                                        if (fields[row][i].getPossibleValues().contains(number4)) {
                                            columnsWithValue4.add(i);
                                        }
                                    }
                                    if (numberAlreadySolved) {
                                        continue;
                                    }

                                    if (columnsWithValue4.size() > 1 && columnsWithValue4.size() < 5) {
                                        tempSet.clear();
                                        tempSet.addAll(columnsWithValue1);
                                        tempSet.addAll(columnsWithValue2);
                                        tempSet.addAll(columnsWithValue3);
                                        tempSet.addAll(columnsWithValue4);
                                        numbers.clear();
                                        numbers.add(number1);
                                        numbers.add(number2);
                                        numbers.add(number3);
                                        numbers.add(number4);

                                        if (tempSet.size() == 4) {

                                            for (Integer col : tempSet) {

                                                if (!numbers.containsAll(fields[row][col].getPossibleValues())) {

                                                    isChanged = true;
                                                    fields[row][col].getPossibleValues().clear();
                                                    if (columnsWithValue1.contains(col)) {
                                                        fields[row][col].getPossibleValues().add(Integer.valueOf(number1));
                                                    }

                                                    if (columnsWithValue2.contains(col)) {
                                                        fields[row][col].getPossibleValues().add(Integer.valueOf(number2));
                                                    }

                                                    if (columnsWithValue3.contains(col)) {
                                                        fields[row][col].getPossibleValues().add(Integer.valueOf(number3));
                                                    }

                                                    if (columnsWithValue4.contains(col)) {
                                                        fields[row][col].getPossibleValues().add(Integer.valueOf(number4));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    public boolean findHiddenQuadsColumn(int column) {

        boolean isChanged = false;
        boolean numberAlreadySolved = false;
        List<Integer> rowsWithValue1 = new ArrayList<>();
        List<Integer> rowsWithValue2 = new ArrayList<>();
        List<Integer> rowsWithValue3 = new ArrayList<>();
        List<Integer> rowsWithValue4 = new ArrayList<>();
        Set<Integer> tempSet = new HashSet<>();
        List<Integer> numbers = new ArrayList<>();

        for (int number1 = 1; number1 < 7; number1++) {

            rowsWithValue1.clear();
            numberAlreadySolved = false;

            for (int i = 0; i < 9; i++) {

                if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
                if (fields[i][column].getPossibleValues().contains(number1)) {
                    rowsWithValue1.add(i);
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            if (rowsWithValue1.size() > 1 && rowsWithValue1.size() < 5) {

                for (int number2 = number1 + 1; number2 < 8; number2++) {

                    rowsWithValue2.clear();
                    numberAlreadySolved = false;

                    for (int i = 0; i < 9; i++) {

                        if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number2) {
                            numberAlreadySolved = true;
                            break;
                        }
                        if (fields[i][column].getPossibleValues().contains(number2)) {
                            rowsWithValue2.add(i);
                        }

                    }
                    if (numberAlreadySolved) {
                        continue;
                    }

                    if (rowsWithValue2.size() > 1 && rowsWithValue2.size() < 5) {

                        for (int number3 = number2 + 1; number3 < 9; number3++) {

                            rowsWithValue3.clear();
                            numberAlreadySolved = false;
                            for (int i = 0; i < 9; i++) {

                                if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number3) {
                                    numberAlreadySolved = true;
                                    break;
                                }
                                if (fields[i][column].getPossibleValues().contains(number3)) {
                                    rowsWithValue3.add(i);
                                }

                            }
                            if (numberAlreadySolved) {
                                continue;
                            }
                            if (rowsWithValue3.size() > 1 && rowsWithValue3.size() < 5) {

                                for (int number4 = number3 + 1; number4 <= 9; number4++) {

                                    rowsWithValue4.clear();
                                    numberAlreadySolved = false;
                                    for (int i = 0; i < 9; i++) {

                                        if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number4) {
                                            numberAlreadySolved = true;
                                            break;
                                        }
                                        if (fields[i][column].getPossibleValues().contains(number4)) {
                                            rowsWithValue4.add(i);
                                        }
                                    }
                                    if (numberAlreadySolved) {
                                        continue;
                                    }

                                    if (rowsWithValue4.size() > 1 && rowsWithValue4.size() < 5) {
                                        tempSet.clear();
                                        tempSet.addAll(rowsWithValue1);
                                        tempSet.addAll(rowsWithValue2);
                                        tempSet.addAll(rowsWithValue3);
                                        tempSet.addAll(rowsWithValue4);
                                        numbers.clear();
                                        numbers.add(number1);
                                        numbers.add(number2);
                                        numbers.add(number3);
                                        numbers.add(number4);

                                        if (tempSet.size() == 4) {

                                            for (Integer row : tempSet) {

                                                if (!numbers.containsAll(fields[row][column].getPossibleValues())) {

                                                    isChanged = true;
                                                    fields[row][column].getPossibleValues().clear();
                                                    if (rowsWithValue1.contains(row)) {
                                                        fields[row][column].getPossibleValues().add(Integer.valueOf(number1));
                                                    }

                                                    if (rowsWithValue2.contains(row)) {
                                                        fields[row][column].getPossibleValues().add(Integer.valueOf(number2));
                                                    }

                                                    if (rowsWithValue3.contains(row)) {
                                                        fields[row][column].getPossibleValues().add(Integer.valueOf(number3));
                                                    }

                                                    if (rowsWithValue4.contains(row)) {
                                                        fields[row][column].getPossibleValues().add(Integer.valueOf(number4));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return isChanged;
    }


    public boolean findHiddenQuadsBox(int box) {

        boolean isChanged = false;
        boolean numberAlreadySolved = false;
        List<Integer> fieldsWithValue1 = new ArrayList<>();
        List<Integer> fieldsWithValue2 = new ArrayList<>();
        List<Integer> fieldsWithValue3 = new ArrayList<>();
        List<Integer> fieldsWithValue4 = new ArrayList<>();
        Set<Integer> tempSet = new HashSet<>();
        List<Integer> numbers = new ArrayList<>();

        for (int number1 = 1; number1 < 7; number1++) {

            fieldsWithValue1.clear();
            numberAlreadySolved = false;

            for (int i = 0; i < 9; i++) {

                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number1)) {
                    fieldsWithValue1.add(i);
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            if (fieldsWithValue1.size() > 1 && fieldsWithValue1.size() < 5) {

                for (int number2 = number1 + 1; number2 < 8; number2++) {

                    fieldsWithValue2.clear();
                    numberAlreadySolved = false;

                    for (int i = 0; i < 9; i++) {

                        if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number2) {
                            numberAlreadySolved = true;
                            break;
                        }
                        if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number2)) {
                            fieldsWithValue2.add(i);
                        }

                    }
                    if (numberAlreadySolved) {
                        continue;
                    }

                    if (fieldsWithValue2.size() > 1 && fieldsWithValue2.size() < 5) {

                        for (int number3 = number2 + 1; number3 < 9; number3++) {

                            fieldsWithValue3.clear();
                            numberAlreadySolved = false;
                            for (int i = 0; i < 9; i++) {

                                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number3) {
                                    numberAlreadySolved = true;
                                    break;
                                }
                                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number3)) {
                                    fieldsWithValue3.add(i);
                                }

                            }
                            if (numberAlreadySolved) {
                                continue;
                            }
                            if (fieldsWithValue3.size() > 1 && fieldsWithValue3.size() < 5) {

                                for (int number4 = number3 + 1; number4 <= 9; number4++) {

                                    fieldsWithValue4.clear();
                                    numberAlreadySolved = false;
                                    for (int i = 0; i < 9; i++) {

                                        if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number4) {
                                            numberAlreadySolved = true;
                                            break;
                                        }
                                        if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number4)) {
                                            fieldsWithValue4.add(i);
                                        }
                                    }
                                    if (numberAlreadySolved) {
                                        continue;
                                    }

                                    if (fieldsWithValue4.size() > 1 && fieldsWithValue4.size() < 5) {
                                        tempSet.clear();
                                        tempSet.addAll(fieldsWithValue1);
                                        tempSet.addAll(fieldsWithValue2);
                                        tempSet.addAll(fieldsWithValue3);
                                        tempSet.addAll(fieldsWithValue4);
                                        numbers.clear();
                                        numbers.add(number1);
                                        numbers.add(number2);
                                        numbers.add(number3);
                                        numbers.add(number4);

                                        if (tempSet.size() == 4) {

                                            for (Integer field : tempSet) {

                                                if (!numbers.containsAll(fields[box / 3 * 3 + field / 3][box % 3 * 3 + field % 3].getPossibleValues())) {

                                                    isChanged = true;
                                                    fields[box / 3 * 3 + field / 3][box % 3 * 3 + field % 3].getPossibleValues().clear();
                                                    if (fieldsWithValue1.contains(field)) {
                                                        fields[box / 3 * 3 + field / 3][box % 3 * 3 + field % 3].getPossibleValues().add(Integer.valueOf(number1));
                                                    }

                                                    if (fieldsWithValue2.contains(field)) {
                                                        fields[box / 3 * 3 + field / 3][box % 3 * 3 + field % 3].getPossibleValues().add(Integer.valueOf(number2));
                                                    }

                                                    if (fieldsWithValue3.contains(field)) {
                                                        fields[box / 3 * 3 + field / 3][box % 3 * 3 + field % 3].getPossibleValues().add(Integer.valueOf(number3));
                                                    }

                                                    if (fieldsWithValue4.contains(field)) {
                                                        fields[box / 3 * 3 + field / 3][box % 3 * 3 + field % 3].getPossibleValues().add(Integer.valueOf(number4));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return isChanged;
    }


    public boolean findNakedQuadsRow(int row) {

        boolean isChanged = false;
        boolean numberAlreadySolved = false;
        List<Integer> columnsWithValues = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        for (int number1 = 1; number1 < 7; number1++) {

            numberAlreadySolved = false;
            for (int i = 0; i < 9; i++) {

                if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            for (int number2 = number1 + 1; number2 < 8; number2++) {

                numberAlreadySolved = false;
                for (int i = 0; i < 9; i++) {

                    if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number2) {
                        numberAlreadySolved = true;
                        break;
                    }
                }
                if (numberAlreadySolved) {
                    continue;
                }


                for (int number3 = number2 + 1; number3 < 9; number3++) {

                    numberAlreadySolved = false;
                    for (int i = 0; i < 9; i++) {

                        if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number3) {
                            numberAlreadySolved = true;
                            break;
                        }
                    }
                    if (numberAlreadySolved) {
                        continue;
                    }

                    for (int number4 = number3 + 1; number4 <= 9; number4++) {

                        columnsWithValues.clear();
                        numbers.clear();
                        numbers.add(number1);
                        numbers.add(number2);
                        numbers.add(number3);
                        numbers.add(number4);
                        numberAlreadySolved = false;

                        for (int i = 0; i < 9; i++) {

                            if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number4) {
                                columnsWithValues.clear();
                                numberAlreadySolved = true;
                                break;
                            }
                            if (fields[row][i].getPossibleValues().size() < 5 && fields[row][i].getPossibleValues().size() > 1 && numbers.containsAll(fields[row][i].getPossibleValues())) {
                                columnsWithValues.add(i);
                            }
                        }
                        if (numberAlreadySolved) {
                            continue;
                        }
                        if (columnsWithValues.size() == 4) {

                            for (int i = 0; i < 9; i++) {

                                if (!columnsWithValues.contains(i) && !fields[row][i].isSolved() && !Collections.disjoint(fields[row][i].getPossibleValues(), numbers)) {
                                    isChanged = true;
                                    fields[row][i].getPossibleValues().removeAll(numbers);
                                }
                            }
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    public boolean findNakedQuadsColumn(int column) {

        boolean isChanged = false;
        boolean numberAlreadySolved = false;
        List<Integer> rowsWithValues = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        for (int number1 = 1; number1 < 7; number1++) {

            numberAlreadySolved = false;
            for (int i = 0; i < 9; i++) {

                if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            for (int number2 = number1 + 1; number2 < 8; number2++) {

                numberAlreadySolved = false;
                for (int i = 0; i < 9; i++) {

                    if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number2) {
                        numberAlreadySolved = true;
                        break;
                    }
                }
                if (numberAlreadySolved) {
                    continue;
                }


                for (int number3 = number2 + 1; number3 < 9; number3++) {

                    numberAlreadySolved = false;
                    for (int i = 0; i < 9; i++) {

                        if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number3) {
                            numberAlreadySolved = true;
                            break;
                        }
                    }
                    if (numberAlreadySolved) {
                        continue;
                    }

                    for (int number4 = number3 + 1; number4 <= 9; number4++) {

                        rowsWithValues.clear();
                        numbers.clear();
                        numbers.add(number1);
                        numbers.add(number2);
                        numbers.add(number3);
                        numbers.add(number4);
                        numberAlreadySolved = false;

                        for (int i = 0; i < 9; i++) {

                            if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number4) {
                                rowsWithValues.clear();
                                numberAlreadySolved = true;
                                break;
                            }
                            if (fields[i][column].getPossibleValues().size() < 5 && fields[i][column].getPossibleValues().size() > 1 && numbers.containsAll(fields[i][column].getPossibleValues())) {
                                rowsWithValues.add(i);
                            }
                        }
                        if (numberAlreadySolved) {
                            continue;
                        }
                        if (rowsWithValues.size() == 4) {

                            for (int i = 0; i < 9; i++) {

                                if (!rowsWithValues.contains(i) && !fields[i][column].isSolved() && !Collections.disjoint(fields[i][column].getPossibleValues(), numbers)) {
                                    isChanged = true;
                                    fields[i][column].getPossibleValues().removeAll(numbers);
                                }
                            }
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    public boolean findNakedQuadsBox(int box) {

        boolean isChanged = false;
        boolean numberAlreadySolved = false;
        List<Integer> fieldsWithValues = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        for (int number1 = 1; number1 < 7; number1++) {

            numberAlreadySolved = false;
            for (int i = 0; i < 9; i++) {

                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            for (int number2 = number1 + 1; number2 < 8; number2++) {

                numberAlreadySolved = false;
                for (int i = 0; i < 9; i++) {

                    if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number2) {
                        numberAlreadySolved = true;
                        break;
                    }
                }
                if (numberAlreadySolved) {
                    continue;
                }


                for (int number3 = number2 + 1; number3 < 9; number3++) {

                    numberAlreadySolved = false;
                    for (int i = 0; i < 9; i++) {

                        if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number3) {
                            numberAlreadySolved = true;
                            break;
                        }
                    }
                    if (numberAlreadySolved) {
                        continue;
                    }

                    for (int number4 = number3 + 1; number4 <= 9; number4++) {

                        fieldsWithValues.clear();
                        numbers.clear();
                        numbers.add(number1);
                        numbers.add(number2);
                        numbers.add(number3);
                        numbers.add(number4);
                        numberAlreadySolved = false;

                        for (int i = 0; i < 9; i++) {

                            if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number4) {
                                fieldsWithValues.clear();
                                numberAlreadySolved = true;
                                break;
                            }
                            if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().size() < 5 && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().size() > 1 && numbers.containsAll(fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues())) {
                                fieldsWithValues.add(i);
                            }
                        }
                        if (numberAlreadySolved) {
                            continue;
                        }
                        if (fieldsWithValues.size() == 4) {

                            for (int i = 0; i < 9; i++) {

                                if (!fieldsWithValues.contains(i) && !fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && !Collections.disjoint(fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues(), numbers)) {
                                    isChanged = true;
                                    fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().removeAll(numbers);
                                }
                            }
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    public boolean findHiddenTriplesRow(int row) {

        boolean isChanged = false;
        boolean numberAlreadySolved = false;
        List<Integer> columnsWithValue1 = new ArrayList<>();
        List<Integer> columnsWithValue2 = new ArrayList<>();
        List<Integer> columnsWithValue3 = new ArrayList<>();
        Set<Integer> tempSet = new HashSet<>();
        List<Integer> numbers = new ArrayList<>();

        for (int number1 = 1; number1 < 8; number1++) {

            columnsWithValue1.clear();
            numberAlreadySolved = false;

            for (int i = 0; i < 9; i++) {

                if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
                if (fields[row][i].getPossibleValues().contains(number1)) {
                    columnsWithValue1.add(i);
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            if (columnsWithValue1.size() > 1 && columnsWithValue1.size() < 4) {

                for (int number2 = number1 + 1; number2 < 9; number2++) {

                    columnsWithValue2.clear();
                    numberAlreadySolved = false;

                    for (int i = 0; i < 9; i++) {

                        if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number2) {
                            numberAlreadySolved = true;
                            break;
                        }
                        if (fields[row][i].getPossibleValues().contains(number2)) {
                            columnsWithValue2.add(i);
                        }

                    }
                    if (numberAlreadySolved) {
                        continue;
                    }

                    if (columnsWithValue2.size() > 1 && columnsWithValue2.size() < 4) {

                        for (int number3 = number2 + 1; number3 <= 9; number3++) {

                            columnsWithValue3.clear();
                            numberAlreadySolved = false;
                            for (int i = 0; i < 9; i++) {

                                if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number3) {
                                    numberAlreadySolved = true;
                                    break;
                                }
                                if (fields[row][i].getPossibleValues().contains(number3)) {
                                    columnsWithValue3.add(i);
                                }

                            }
                            if (numberAlreadySolved) {
                                continue;
                            }
                            if (columnsWithValue3.size() > 1 && columnsWithValue3.size() < 4) {

                                tempSet.clear();
                                tempSet.addAll(columnsWithValue1);
                                tempSet.addAll(columnsWithValue2);
                                tempSet.addAll(columnsWithValue3);
                                numbers.clear();
                                numbers.add(number1);
                                numbers.add(number2);
                                numbers.add(number3);

                                if (tempSet.size() == 3) {

                                    for (Integer col : tempSet) {

                                        if (!numbers.containsAll(fields[row][col].getPossibleValues())) {

                                            isChanged = true;
                                            fields[row][col].getPossibleValues().clear();
                                            if (columnsWithValue1.contains(col)) {
                                                fields[row][col].getPossibleValues().add(Integer.valueOf(number1));
                                            }

                                            if (columnsWithValue2.contains(col)) {
                                                fields[row][col].getPossibleValues().add(Integer.valueOf(number2));
                                            }

                                            if (columnsWithValue3.contains(col)) {
                                                fields[row][col].getPossibleValues().add(Integer.valueOf(number3));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return isChanged;
    }


    public boolean findHiddenTriplesColumn(int column) {

        boolean isChanged = false;
        boolean numberAlreadySolved = false;
        List<Integer> rowsWithValue1 = new ArrayList<>();
        List<Integer> rowsWithValue2 = new ArrayList<>();
        List<Integer> rowsWithValue3 = new ArrayList<>();
        Set<Integer> tempSet = new HashSet<>();
        List<Integer> numbers = new ArrayList<>();

        for (int number1 = 1; number1 < 8; number1++) {

            rowsWithValue1.clear();
            numberAlreadySolved = false;

            for (int i = 0; i < 9; i++) {

                if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
                if (fields[i][column].getPossibleValues().contains(number1)) {
                    rowsWithValue1.add(i);
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            if (rowsWithValue1.size() > 1 && rowsWithValue1.size() < 4) {

                for (int number2 = number1 + 1; number2 < 9; number2++) {

                    rowsWithValue2.clear();
                    numberAlreadySolved = false;

                    for (int i = 0; i < 9; i++) {

                        if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number2) {
                            numberAlreadySolved = true;
                            break;
                        }
                        if (fields[i][column].getPossibleValues().contains(number2)) {
                            rowsWithValue2.add(i);
                        }

                    }
                    if (numberAlreadySolved) {
                        continue;
                    }

                    if (rowsWithValue2.size() > 1 && rowsWithValue2.size() < 4) {

                        for (int number3 = number2 + 1; number3 <= 9; number3++) {

                            rowsWithValue3.clear();
                            numberAlreadySolved = false;
                            for (int i = 0; i < 9; i++) {

                                if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number3) {
                                    numberAlreadySolved = true;
                                    break;
                                }
                                if (fields[i][column].getPossibleValues().contains(number3)) {
                                    rowsWithValue3.add(i);
                                }

                            }
                            if (numberAlreadySolved) {
                                continue;
                            }
                            if (rowsWithValue3.size() > 1 && rowsWithValue3.size() < 4) {

                                tempSet.clear();
                                tempSet.addAll(rowsWithValue1);
                                tempSet.addAll(rowsWithValue2);
                                tempSet.addAll(rowsWithValue3);
                                numbers.clear();
                                numbers.add(number1);
                                numbers.add(number2);
                                numbers.add(number3);

                                if (tempSet.size() == 3) {

                                    for (Integer row : tempSet) {

                                        if (!numbers.containsAll(fields[row][column].getPossibleValues())) {

                                            isChanged = true;
                                            fields[row][column].getPossibleValues().clear();
                                            if (rowsWithValue1.contains(row)) {
                                                fields[row][column].getPossibleValues().add(Integer.valueOf(number1));
                                            }

                                            if (rowsWithValue2.contains(row)) {
                                                fields[row][column].getPossibleValues().add(Integer.valueOf(number2));
                                            }

                                            if (rowsWithValue3.contains(row)) {
                                                fields[row][column].getPossibleValues().add(Integer.valueOf(number3));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return isChanged;
    }


    public boolean findHiddenTriplesBox(int box) {

        boolean isChanged = false;
        boolean numberAlreadySolved = false;
        List<Integer> fieldsWithValue1 = new ArrayList<>();
        List<Integer> fieldsWithValue2 = new ArrayList<>();
        List<Integer> fieldsWithValue3 = new ArrayList<>();
        Set<Integer> tempSet = new HashSet<>();
        List<Integer> numbers = new ArrayList<>();

        for (int number1 = 1; number1 < 8; number1++) {

            fieldsWithValue1.clear();
            numberAlreadySolved = false;

            for (int i = 0; i < 9; i++) {

                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number1)) {
                    fieldsWithValue1.add(i);
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            if (fieldsWithValue1.size() > 1 && fieldsWithValue1.size() < 4) {

                for (int number2 = number1 + 1; number2 < 9; number2++) {

                    fieldsWithValue2.clear();
                    numberAlreadySolved = false;

                    for (int i = 0; i < 9; i++) {

                        if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number2) {
                            numberAlreadySolved = true;
                            break;
                        }
                        if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number2)) {
                            fieldsWithValue2.add(i);
                        }

                    }
                    if (numberAlreadySolved) {
                        continue;
                    }

                    if (fieldsWithValue2.size() > 1 && fieldsWithValue2.size() < 4) {

                        for (int number3 = number2 + 1; number3 <= 9; number3++) {

                            fieldsWithValue3.clear();
                            numberAlreadySolved = false;
                            for (int i = 0; i < 9; i++) {

                                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number3) {
                                    numberAlreadySolved = true;
                                    break;
                                }
                                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number3)) {
                                    fieldsWithValue3.add(i);
                                }

                            }
                            if (numberAlreadySolved) {
                                continue;
                            }
                            if (fieldsWithValue3.size() > 1 && fieldsWithValue3.size() < 4) {

                                tempSet.clear();
                                tempSet.addAll(fieldsWithValue1);
                                tempSet.addAll(fieldsWithValue2);
                                tempSet.addAll(fieldsWithValue3);
                                numbers.clear();
                                numbers.add(number1);
                                numbers.add(number2);
                                numbers.add(number3);

                                if (tempSet.size() == 3) {

                                    for (Integer field : tempSet) {

                                        if (!numbers.containsAll(fields[box / 3 * 3 + field / 3][box % 3 * 3 + field % 3].getPossibleValues())) {

                                            isChanged = true;
                                            fields[box / 3 * 3 + field / 3][box % 3 * 3 + field % 3].getPossibleValues().clear();
                                            if (fieldsWithValue1.contains(field)) {
                                                fields[box / 3 * 3 + field / 3][box % 3 * 3 + field % 3].getPossibleValues().add(Integer.valueOf(number1));
                                            }

                                            if (fieldsWithValue2.contains(field)) {
                                                fields[box / 3 * 3 + field / 3][box % 3 * 3 + field % 3].getPossibleValues().add(Integer.valueOf(number2));
                                            }

                                            if (fieldsWithValue3.contains(field)) {
                                                fields[box / 3 * 3 + field / 3][box % 3 * 3 + field % 3].getPossibleValues().add(Integer.valueOf(number3));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return isChanged;
    }


    public boolean findNakedTriplesRow(int row) {

        boolean isChanged = false;
        boolean numberAlreadySolved = false;
        List<Integer> columnsWithValues = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        for (int number1 = 1; number1 < 8; number1++) {

            numberAlreadySolved = false;
            for (int i = 0; i < 9; i++) {

                if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            for (int number2 = number1 + 1; number2 < 9; number2++) {

                numberAlreadySolved = false;
                for (int i = 0; i < 9; i++) {

                    if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number2) {
                        numberAlreadySolved = true;
                        break;
                    }
                }
                if (numberAlreadySolved) {
                    continue;
                }


                for (int number3 = number2 + 1; number3 <= 9; number3++) {

                    columnsWithValues.clear();
                    numbers.clear();
                    numbers.add(number1);
                    numbers.add(number2);
                    numbers.add(number3);
                    numberAlreadySolved = false;

                    for (int i = 0; i < 9; i++) {

                        if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number3) {
                            columnsWithValues.clear();
                            numberAlreadySolved = true;
                            break;
                        }
                        if (fields[row][i].getPossibleValues().size() < 4 && fields[row][i].getPossibleValues().size() > 1 && numbers.containsAll(fields[row][i].getPossibleValues())) {
                            columnsWithValues.add(i);
                        }
                    }
                    if (numberAlreadySolved) {
                        continue;
                    }
                    if (columnsWithValues.size() == 3) {

                        for (int i = 0; i < 9; i++) {

                            if (!columnsWithValues.contains(i) && !fields[row][i].isSolved() && !Collections.disjoint(fields[row][i].getPossibleValues(), numbers)) {
                                isChanged = true;
                                fields[row][i].getPossibleValues().removeAll(numbers);
                            }
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    public boolean findNakedTriplesColumn(int column) {

        boolean isChanged = false;
        boolean numberAlreadySolved = false;
        List<Integer> rowsWithValues = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        for (int number1 = 1; number1 < 8; number1++) {

            numberAlreadySolved = false;
            for (int i = 0; i < 9; i++) {

                if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            for (int number2 = number1 + 1; number2 < 9; number2++) {

                numberAlreadySolved = false;
                for (int i = 0; i < 9; i++) {

                    if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number2) {
                        numberAlreadySolved = true;
                        break;
                    }
                }
                if (numberAlreadySolved) {
                    continue;
                }


                for (int number3 = number2 + 1; number3 <= 9; number3++) {

                    rowsWithValues.clear();
                    numbers.clear();
                    numbers.add(number1);
                    numbers.add(number2);
                    numbers.add(number3);
                    numberAlreadySolved = false;

                    for (int i = 0; i < 9; i++) {

                        if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number3) {
                            rowsWithValues.clear();
                            numberAlreadySolved = true;
                            break;
                        }
                        if (fields[i][column].getPossibleValues().size() < 4 && fields[i][column].getPossibleValues().size() > 1 && numbers.containsAll(fields[i][column].getPossibleValues())) {
                            rowsWithValues.add(i);
                        }
                    }
                    if (numberAlreadySolved) {
                        continue;
                    }
                    if (rowsWithValues.size() == 3) {

                        for (int i = 0; i < 9; i++) {

                            if (!rowsWithValues.contains(i) && !fields[i][column].isSolved() && !Collections.disjoint(fields[i][column].getPossibleValues(), numbers)) {
                                isChanged = true;
                                fields[i][column].getPossibleValues().removeAll(numbers);
                            }
                        }
                    }
                }
            }
        }
        return isChanged;
    }


    public boolean findNakedTriplesBox(int box) {

        boolean isChanged = false;
        boolean numberAlreadySolved = false;
        List<Integer> fieldsWithValues = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        for (int number1 = 1; number1 < 8; number1++) {

            numberAlreadySolved = false;
            for (int i = 0; i < 9; i++) {

                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            for (int number2 = number1 + 1; number2 < 9; number2++) {

                numberAlreadySolved = false;
                for (int i = 0; i < 9; i++) {

                    if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number2) {
                        numberAlreadySolved = true;
                        break;
                    }
                }
                if (numberAlreadySolved) {
                    continue;
                }

                for (int number3 = number2 + 1; number3 <= 9; number3++) {

                    fieldsWithValues.clear();
                    numbers.clear();
                    numbers.add(number1);
                    numbers.add(number2);
                    numbers.add(number3);
                    numberAlreadySolved = false;

                    for (int i = 0; i < 9; i++) {

                        if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number3) {
                            fieldsWithValues.clear();
                            numberAlreadySolved = true;
                            break;
                        }
                        if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().size() < 4 && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().size() > 1 && numbers.containsAll(fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues())) {
                            fieldsWithValues.add(i);
                        }
                    }
                    if (numberAlreadySolved) {
                        continue;
                    }
                    if (fieldsWithValues.size() == 3) {

                        for (int i = 0; i < 9; i++) {

                            if (!fieldsWithValues.contains(i) && !fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && !Collections.disjoint(fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues(), numbers)) {
                                isChanged = true;
                                fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().removeAll(numbers);
                            }
                        }
                    }
                }
            }
        }
        return isChanged;
    }

    public boolean findHiddenPairsRow(int row) {

        List<Integer> columnsWithValue1 = new ArrayList<>();
        List<Integer> columnsWithValue2 = new ArrayList<>();
        boolean isChanged = false;
        boolean numberAlreadySolved = false;

        for (int number1 = 1; number1 < 9; number1++) {

            columnsWithValue1 = new ArrayList<>();
            numberAlreadySolved = false;
            for (int i = 0; i < 9; i++) {

                if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
                if (fields[row][i].getPossibleValues().contains(number1)) {
                    columnsWithValue1.add(i);
                }
            }
            if (numberAlreadySolved) {
                continue;
            }
            if (columnsWithValue1.size() == 2) {

                for (int number2 = number1 + 1; number2 <= 9; number2++) {

                    columnsWithValue2 = new ArrayList<>();
                    numberAlreadySolved = false;
                    for (int i = 0; i < 9; i++) {

                        if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number2) {
                            numberAlreadySolved = true;
                            break;
                        }
                        if (fields[row][i].getPossibleValues().contains(number2)) {
                            columnsWithValue2.add(i);
                        }
                    }
                    if (numberAlreadySolved) {
                        continue;
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
        boolean numberAlreadySolved = false;

        for (int number1 = 1; number1 < 9; number1++) {

            rowsWithValue1 = new ArrayList<>();
            numberAlreadySolved = false;
            for (int i = 0; i < 9; i++) {
                if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
                if (fields[i][column].getPossibleValues().contains(number1)) {
                    rowsWithValue1.add(i);
                }
            }
            if (numberAlreadySolved) {
                continue;
            }
            if (rowsWithValue1.size() == 2) {

                for (int number2 = number1 + 1; number2 <= 9; number2++) {

                    rowsWithValue2 = new ArrayList<>();
                    numberAlreadySolved = false;
                    for (int i = 0; i < 9; i++) {
                        if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number2) {
                            numberAlreadySolved = true;
                            break;
                        }
                        if (fields[i][column].getPossibleValues().contains(number2)) {
                            rowsWithValue2.add(i);
                        }
                    }
                    if (numberAlreadySolved) {
                        continue;
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
        boolean numberAlreadySolved = false;

        for (int number1 = 1; number1 < 9; number1++) {

            fieldsWithValue1 = new ArrayList<>();
            numberAlreadySolved = false;
            for (int i = 0; i < 9; i++) {

                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number1)) {
                    fieldsWithValue1.add(i);
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            if (fieldsWithValue1.size() == 2) {

                for (int number2 = number1 + 1; number2 <= 9; number2++) {

                    fieldsWithValue2 = new ArrayList<>();
                    numberAlreadySolved = false;
                    for (int i = 0; i < 9; i++) {

                        if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number2) {
                            numberAlreadySolved = true;
                            break;
                        }
                        if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number2)) {
                            fieldsWithValue2.add(i);
                        }
                    }
                    if (numberAlreadySolved) {
                        continue;
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
        boolean numberAlreadySolved = false;
        List<Integer> fieldsWithBothValues = new ArrayList<>();

        for (int number1 = 1; number1 < 9; number1++) {

            numberAlreadySolved = false;
            for (int i = 0; i < 9; i++) {
                if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
            }
            if (numberAlreadySolved) {
                continue;
            }

            for (int number2 = number1 + 1; number2 <= 9; number2++) {

                fieldsWithBothValues = new ArrayList<>();
                numberAlreadySolved = false;
                for (int i = 0; i < 9; i++) {
                    if (fields[row][i].isSolved() && fields[row][i].getPossibleValues().get(0) == number2) {
                        numberAlreadySolved = true;
                        break;
                    }

                    if (fields[row][i].getPossibleValues().size() == 2 && fields[row][i].getPossibleValues().contains(number1) && fields[row][i].getPossibleValues().contains(number2)) {
                        fieldsWithBothValues.add(i);
                    }
                }
                if (numberAlreadySolved) {
                    continue;
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
        boolean numberAlreadySolved = false;
        List<Integer> fieldsWithBothValues = new ArrayList<>();

        for (int number1 = 1; number1 < 9; number1++) {

            numberAlreadySolved = false;
            for (int i = 0; i < 9; i++) {
                if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
            }
            if (numberAlreadySolved) {
                continue;
            }
            for (int number2 = number1 + 1; number2 <= 9; number2++) {

                fieldsWithBothValues = new ArrayList<>();
                numberAlreadySolved = false;
                for (int i = 0; i < 9; i++) {
                    if (fields[i][column].isSolved() && fields[i][column].getPossibleValues().get(0) == number2) {
                        numberAlreadySolved = true;
                        break;
                    }
                    if (fields[i][column].getPossibleValues().size() == 2 && fields[i][column].getPossibleValues().contains(number1) && fields[i][column].getPossibleValues().contains(number2)) {
                        fieldsWithBothValues.add(i);
                    }
                }
                if (numberAlreadySolved) {
                    continue;
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
        boolean numberAlreadySolved = false;
        List<Integer> fieldsWithBothValues = new ArrayList<>();

        for (int number1 = 1; number1 < 9; number1++) {

            numberAlreadySolved = false;
            for (int i = 0; i < 9; i++) {
                if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number1) {
                    numberAlreadySolved = true;
                    break;
                }
            }
            if (numberAlreadySolved) {
                continue;
            }
            for (int number2 = number1 + 1; number2 <= 9; number2++) {

                fieldsWithBothValues = new ArrayList<>();
                numberAlreadySolved = false;
                for (int i = 0; i < 9; i++) {

                    if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].isSolved() && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().get(0) == number2) {
                        numberAlreadySolved = true;
                        break;
                    }
                    if (fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().size() == 2 && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number1) && fields[box / 3 * 3 + i / 3][box % 3 * 3 + i % 3].getPossibleValues().contains(number2)) {
                        fieldsWithBothValues.add(i);
                    }
                }
                if (numberAlreadySolved) {
                    continue;
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
                    for (int i = 0; i < 9; i++) {

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
                result += (fields[i][j].isSolved() ? fields[i][j].getPossibleValues().get(0) : "?") + " ";
            }
            result += "\n";
        }
        return result;
    }
}
