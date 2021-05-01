package pl.nox.sudokusolver.controller;

import pl.nox.sudokusolver.model.Sudoku;
import pl.nox.sudokusolver.model.SudokuSeedDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "Solve", value = "/solve")
public class Solve extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/solve.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String[] numbers = request.getParameterValues("fieldValue");
        String bruteForce = request.getParameter("bruteForce");
        String solveAttempt = "fail";
        boolean quickSolution = false;
        boolean isError = false;
        String solution = new String();
        if (numbers.length == 1 && numbers[0].matches("\\d{3}[1-5][1-9]{9}[0-3]{2}\\d{81}")) {
            solution = SudokuSeedDAO.readSolutionById(Integer.parseInt(numbers[0].substring(0, 3)));
            if (solution != null) {
                solution = Sudoku.randomizeFieldsSeed(solution, numbers[0].substring(4, 15));
                quickSolution = true;
                solveAttempt = "success";
                request.setAttribute("solution", solution);
            } else {
                String temp = numbers[0];
                numbers = new String[81];
                for (int i = 0; i < 81; i++) {
                    numbers[i] = temp.substring(i, i + 1);
                }
            }
        }
        if (!quickSolution && numbers.length != 81) {
            isError = true;
        }
        if (!quickSolution && !isError) {
            for (String str : numbers) {
                if (!str.matches("\\d")) {
                    isError = true;
                    break;
                }
            }
        }
        if (!quickSolution && !isError) {

            String fieldSeed = new String();
            for (String str : numbers) {
                fieldSeed += str;
            }
            Sudoku sudoku = new Sudoku(fieldSeed);
            if (bruteForce == null) {
                sudoku.reasoningSolve();
            } else {
                sudoku.bruteForceSolve();
            }
            if (sudoku.getIsSolved()) {
                solveAttempt = "success";
            }
            if (sudoku.isInvalid()) {
                solveAttempt = "invalid";
            }
            solution = sudoku.getSeed();
        }
        request.setAttribute("solveAttempt", solveAttempt);
        request.setAttribute("solution", solution);
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/solve.jsp").forward(request, response);
    }
}
