package pl.nox.sudokusolver;

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

        int[] fields4 = {6, 0, 0, 0, 0, 0, 0, 0, 0,
                         0, 0, 7, 5, 0, 9, 0, 1, 0,
                         0, 0, 0, 0, 7, 2, 0, 0, 4,
                         0, 9, 6, 0, 5, 0, 0, 0, 2,
                         2, 0, 0, 3, 0, 0, 0, 0, 0,
                         7, 0, 0, 0, 0, 0, 1, 9, 6,
                         0, 0, 5, 7, 0, 0, 4, 0, 0,
                         0, 0, 0, 0, 0, 0, 3, 7, 8,
                         0, 0, 0, 0, 2, 0, 0, 0, 0};

        int[] fields5 = {0, 0, 0, 1, 0, 6, 2, 0, 4, //can;t solve!
                         0, 4, 0, 0, 0, 0, 0, 5, 0,
                         1, 0, 0, 7, 0, 0, 0, 0, 0,
                         0, 0, 0, 0, 5, 0, 0, 3, 0,
                         0, 0, 0, 0, 0, 3, 0, 0, 0,
                         0, 0, 1, 0, 0, 0, 6, 7, 2,
                         6, 9, 2, 0, 0, 0, 0, 0, 5,
                         0, 0, 0, 4, 0, 0, 0, 0, 0,
                         0, 3, 0, 0, 6, 0, 0, 0, 8};

        int[] fields6 = {0, 3, 0, 4, 8, 0, 6, 0, 9,
                         0, 0, 0, 0, 2, 7, 0, 0, 0,
                         8, 0, 0, 3, 0, 0, 0, 0, 0,
                         0, 1, 9, 0, 0, 0, 0, 0, 0,
                         7, 8, 0, 0, 0, 2, 0, 9, 3,
                         0, 0, 0, 0, 0, 4, 8, 7, 0,
                         0, 0, 0, 0, 0, 5, 0, 0, 6,
                         0, 0, 0, 1, 3, 0, 0, 0, 0,
                         9, 0, 2, 0, 4, 8, 0, 1, 0};
        

        int[] fields7 = {0, 0, 0, 0, 0, 0, 0, 0, 0,
                		 0, 0, 0, 1, 0, 7, 0, 0, 8,
                		 0, 7, 0, 3, 9, 2, 5, 4, 1,
                		 0, 0, 4, 0, 0, 0, 0, 9, 2,
                		 0, 0, 5, 0, 0, 0, 6, 0, 0,
                		 9, 3, 0, 0, 0, 0, 4, 0, 0,
                		 1, 9, 2, 7, 8, 5, 0, 6, 0,
                		 5, 0, 0, 4, 0, 3, 0, 0, 0,
                		 0, 0, 0, 0, 0, 0, 0, 0, 0};

        int[] fields8 = {0, 0, 0, 0, 0, 0, 0, 0, 0,
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

        Sudoku sudoku = new Sudoku(fields7);


//        sudoku.fields[0][2].getPossibleValues().remove(Integer.valueOf(1));
//        sudoku.fields[0][2].getPossibleValues().remove(Integer.valueOf(2));
//
//        sudoku.fields[1][0].getPossibleValues().remove(Integer.valueOf(1));
//        sudoku.fields[1][0].getPossibleValues().remove(Integer.valueOf(2));
//
//        sudoku.fields[1][1].getPossibleValues().remove(Integer.valueOf(1));
//        sudoku.fields[1][1].getPossibleValues().remove(Integer.valueOf(2));
//
//        sudoku.fields[1][2].getPossibleValues().remove(Integer.valueOf(1));
//        sudoku.fields[1][2].getPossibleValues().remove(Integer.valueOf(2));
//
//        sudoku.fields[2][0].getPossibleValues().remove(Integer.valueOf(1));
//        sudoku.fields[2][0].getPossibleValues().remove(Integer.valueOf(2));
//
//        sudoku.fields[2][1].getPossibleValues().remove(Integer.valueOf(1));
//        sudoku.fields[2][1].getPossibleValues().remove(Integer.valueOf(2));
//
//        sudoku.fields[2][2].getPossibleValues().remove(Integer.valueOf(1));
//        sudoku.fields[2][2].getPossibleValues().remove(Integer.valueOf(2));

//        sudoku.fields[0][0].getPossibleValues().remove(Integer.valueOf(1));
//        sudoku.fields[0][0].getPossibleValues().remove(Integer.valueOf(2));
//        sudoku.fields[0][0].getPossibleValues().remove(Integer.valueOf(3));
//        sudoku.fields[0][0].getPossibleValues().remove(Integer.valueOf(4));
//        sudoku.fields[0][0].getPossibleValues().remove(Integer.valueOf(5));
//        sudoku.fields[0][0].getPossibleValues().remove(Integer.valueOf(6));
//        sudoku.fields[0][0].getPossibleValues().remove(Integer.valueOf(7));
//
//        sudoku.fields[0][1].getPossibleValues().remove(Integer.valueOf(1));
//        sudoku.fields[0][1].getPossibleValues().remove(Integer.valueOf(2));
//        sudoku.fields[0][1].getPossibleValues().remove(Integer.valueOf(3));
//        sudoku.fields[0][1].getPossibleValues().remove(Integer.valueOf(4));
//        sudoku.fields[0][1].getPossibleValues().remove(Integer.valueOf(5));
//        sudoku.fields[0][1].getPossibleValues().remove(Integer.valueOf(6));
//        sudoku.fields[0][1].getPossibleValues().remove(Integer.valueOf(8));
//
//        sudoku.fields[0][2].getPossibleValues().remove(Integer.valueOf(1));
//        sudoku.fields[0][2].getPossibleValues().remove(Integer.valueOf(2));
//        sudoku.fields[0][2].getPossibleValues().remove(Integer.valueOf(3));
//        sudoku.fields[0][2].getPossibleValues().remove(Integer.valueOf(4));
//        sudoku.fields[0][2].getPossibleValues().remove(Integer.valueOf(5));
//        sudoku.fields[0][2].getPossibleValues().remove(Integer.valueOf(6));
//        sudoku.fields[0][2].getPossibleValues().remove(Integer.valueOf(9));
//
//        sudoku.fields[8][3].getPossibleValues().remove(Integer.valueOf(7));
//        sudoku.fields[8][3].getPossibleValues().remove(Integer.valueOf(8));
//        sudoku.fields[8][3].getPossibleValues().remove(Integer.valueOf(9));
//
//        sudoku.fields[8][4].getPossibleValues().remove(Integer.valueOf(7));
//        sudoku.fields[8][4].getPossibleValues().remove(Integer.valueOf(8));
//        sudoku.fields[8][4].getPossibleValues().remove(Integer.valueOf(9));
//
//        sudoku.fields[8][5].getPossibleValues().remove(Integer.valueOf(7));
//        sudoku.fields[8][5].getPossibleValues().remove(Integer.valueOf(8));
//        sudoku.fields[8][5].getPossibleValues().remove(Integer.valueOf(9));
//
//        sudoku.fields[8][6].getPossibleValues().remove(Integer.valueOf(7));
//        sudoku.fields[8][6].getPossibleValues().remove(Integer.valueOf(8));
//        sudoku.fields[8][6].getPossibleValues().remove(Integer.valueOf(9));
//
//        sudoku.fields[8][7].getPossibleValues().remove(Integer.valueOf(7));
//        sudoku.fields[8][7].getPossibleValues().remove(Integer.valueOf(8));
//        sudoku.fields[8][7].getPossibleValues().remove(Integer.valueOf(9));
//
//        sudoku.fields[8][8].getPossibleValues().remove(Integer.valueOf(7));
//        sudoku.fields[8][8].getPossibleValues().remove(Integer.valueOf(8));
//        sudoku.fields[8][8].getPossibleValues().remove(Integer.valueOf(9));
//
//        sudoku.findNakedTriplesBox(0);
//        sudoku.findNakedTriplesRow(0);
//        sudoku.findHiddenTriplesRow(8);
//        sudoku.findNakedTriplesBox(6);





//        System.out.println(sudoku.fields[1][1].getPossibleValues());

        System.out.println(sudoku.reasoningSolve());

//        System.out.println(sudoku.fields[1][1].getPossibleValues());
//
//        System.out.println(sudoku.fields[0][8].getPossibleValues());
//
//        System.out.println(sudoku.fields[0][1].getPossibleValues());

        System.out.println(sudoku);
        sudoku.checkIfInvalid();
        System.out.println(sudoku.isInvalid());
        
        char ch = 49;
        System.out.println(ch);

//        System.out.println(sudoku.fields[8][1].getPossibleValues());
//        System.out.println(sudoku.fields[7][1].getPossibleValues());

    }
}