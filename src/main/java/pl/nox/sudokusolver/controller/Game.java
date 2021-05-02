package pl.nox.sudokusolver.controller;

import pl.nox.sudokusolver.model.Sudoku;
import pl.nox.sudokusolver.model.SudokuSeedDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "Play", value = "/game")
public class Game extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("KURWA");
        request.setAttribute("page", 1);
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/game.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//parametry - type new i trudność lub type load i nazwa ciasteczka
        request.setAttribute("page", 1);
        boolean isParameterError = false;
        String type = request.getParameter("type");
        if (type == null || !(type.equals("load") || type.equals("new"))) {
            isParameterError = true;
        }
        if (!isParameterError && type.equals("new")) {
            String difficulty = request.getParameter("difficulty");
            if (difficulty == null || !difficulty.matches("[1-5]")) {
                isParameterError = true;
            } else {
                String seed = Sudoku.generateRandomSudokuSeed(Integer.parseInt(difficulty));
                if (seed == null) {
                    isParameterError = true;
                } else {
//                    Sudoku sudoku = new Sudoku(seed.substring(15));
                    request.setAttribute("baseSeed", seed);
                }
            }
        }
        if (!isParameterError && type.equals("load")) {
            String loadedGame = request.getParameter("gameToLoad");
            Cookie[] cookies = request.getCookies();
            boolean cookieFound = false;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(loadedGame)) {
                    cookieFound = true;
                    loadedGame = cookie.getValue();
                    System.out.println(loadedGame);
                    break;
                }
            }
            if (!cookieFound || !loadedGame.matches("\\d{3}[1-5][1-9]{9}[0-3]{2}\\d{87}") || loadedGame.substring(4, 13).matches(".*(\\d).*\\1.*")) {
                isParameterError = true;
            } else {
                String baseSeed = SudokuSeedDAO.readById(Integer.parseInt(loadedGame.substring(0, 3)));
                if (baseSeed == null) {
                    isParameterError = true;
                } else {
                    //         id +diff + randomizer
                    baseSeed = loadedGame.substring(0, 15) + Sudoku.randomizeFieldsSeed(baseSeed, loadedGame.substring(4, 15));
                    request.setAttribute("baseSeed", baseSeed); //format id + dif + randomizer + fieldvalues
                    request.setAttribute("loadedGame", loadedGame); //format id + dif + randomizer + fieldvalues + time
                }
            }
        }
        if (isParameterError) {
            request.setAttribute("error", "parameterError");
        }
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/game.jsp").forward(request, response);
    }
}
