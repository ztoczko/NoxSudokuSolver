package pl.nox.sudokusolver.controller;

import org.apache.commons.lang3.StringUtils;
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
        request.setAttribute("page", 2);
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/solve.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("page", 2);
        String[] numbers = request.getParameterValues("fieldValue");
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] != null && numbers[i].isEmpty()) {
                numbers[i] = "0";
            }
        }
        String bruteForce = request.getParameter("bruteForce");
        String baseSeed = request.getParameter("seed");
        String solveAttempt = "fail";
        boolean quickSolution = false;
        boolean isError = false;
        String solution = new String();
        String fieldSeed = new String();

        if (baseSeed != null && baseSeed.matches("\\d{3}[1-5][1-9]{9}[0-3]{2}\\d{81}")) {
            solution = SudokuSeedDAO.readSolutionById(Integer.parseInt(baseSeed.substring(0, 3)));
            if (solution != null) {
                solution = Sudoku.randomizeFieldsSeed(solution, baseSeed.substring(4, 15));
                quickSolution = true;
                solveAttempt = "success";
//                request.setAttribute("solution", solution);
            }
        }
        if (numbers.length != 81) {
            isError = true;
        }
        if (!isError) {
            for (String str : numbers) {
                fieldSeed += str;
                if (!str.matches("\\d")) {
                    isError = true;
                    break;
                }
            }
        }
        if (!quickSolution && !isError) {

            Sudoku sudoku = new Sudoku(fieldSeed);
            if (bruteForce == null) {
                sudoku.reasoningSolve();
            } else {
                request.setAttribute("bruteForce", "bruteForce");
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
        if (isError) {
            request.setAttribute("error", "error");
        } else {
            request.setAttribute("originalSeed", fieldSeed);
        }
        if (solveAttempt.equals("fail")) {
            request.setAttribute("numbersMissing", StringUtils.countMatches(solution, '0'));
        }
        request.setAttribute("solveAttempt", solveAttempt);
        request.setAttribute("solution", solution);
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/solve.jsp").forward(request, response);
    }
}
