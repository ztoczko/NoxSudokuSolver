package pl.nox.sudokusolver;

import java.util.Scanner;

public class Main01 {

    public static void main(String[] args) {

        int[] fields = {0, 0, 1, 9, 0, 6, 0, 0, 5,
                        0, 0, 0, 0, 7, 0, 0, 0, 1,
                        3, 0, 9, 8, 0, 0, 7, 0, 6,
                        0, 0, 2, 6, 8, 0, 4, 7, 0,
                        7, 0, 4, 2, 0, 0, 0, 3, 0,
                        5, 0, 8, 7, 3, 0, 1, 0, 2,
                        0, 2, 7, 0, 6, 8, 9, 1, 3,
                        9, 0, 5, 3, 4, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 5, 0};

        int[] fields2 = {0, 8, 0, 0, 3, 1, 7, 6, 0,
                        7, 5, 2, 0, 0, 9, 3, 0, 0,
                        0, 1, 6, 0, 2, 0, 0, 0, 0,
                        0, 0, 0, 0, 9, 0, 0, 1, 0,
                        0, 0, 0, 0, 1, 0, 0, 0, 6,
                        0, 9, 1, 0, 0, 0, 0, 2, 4,
                        8, 0, 3, 0, 6, 2, 0, 0, 0,
                        0, 6, 0, 0, 0, 4, 0, 0, 0,
                        0, 0, 4, 0, 0, 0, 0, 7, 3};

        int[] fields3 = {0, 0, 0, 0, 1, 5, 6, 0, 0,
                         0, 0, 0, 0, 6, 0, 8, 5, 0,
                         0, 0, 0, 0, 0, 0, 0, 0, 3,
                         2, 0, 8, 0, 0, 0, 0, 0, 4,
                         0, 4, 0, 3, 9, 0, 0, 0, 2,
                         6, 0, 1, 2, 0, 0, 0, 0, 0,
                         4, 8, 0, 6, 7, 0, 0, 0, 0,
                         0, 0, 0, 8, 0, 0, 0, 9, 0,
                         0, 0, 6, 0, 4, 0, 0, 0, 0};

        int[] fields4 = {0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0};

//        Scanner userInput = new Scanner(System.in);
//        String input = new String();
//
//        for (int i = 0; i < 81; i++){
//            System.out.println("Podaj wartość dla pola " + (i + 1));
//            input = userInput.nextLine();
//            fields[i] = Integer.parseInt(input);
//        }

        Sudoku sudoku = new Sudoku(fields4);

        System.out.println(sudoku.heuristicSolve());

        System.out.println(sudoku);

    }
}
