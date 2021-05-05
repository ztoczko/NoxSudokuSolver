<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<jsp:include page="../includes/header.jsp"/>

<div class="row mainbody m-0">

    <div class="col-3 m-0 px-2 py-3 d-flex flex-column justify-content-center align-items-center"
         style="height: 100%;">

        <div class="m-0 px-2 py-3 d-flex flex-column align-items-center"
             style="height: 80%; width: 100%; overflow: auto; min-height: 0;">

            <%--            menu buttons for solve subpage - backtracking is only available when procedural solve fails--%>
            <form method="post"
                  class="m-0, p-0 d-flex flex-column justify-content-center align-items-center"
                  style="width: 100%; margin-bottom: 0;">

                <button type="submit" id="solve" class="button red my-3"
                        style="width: 80%; ${error != null || solveAttempt != null ? "display: none;" : ""} ">Rozwiąż
                </button>

                <button type="button" class="button red my-3" data-bs-toggle="modal" data-bs-target="#bruteForceModal"
                        style="width: 80%; ${error != null || !"fail".equals(solveAttempt) ? "display: none;" : ""}">
                    Rozwiąż poprzez backtracking
                </button>

                <button type="button" onclick="location.href='solve';" class="button red my-3" style="width: 80%;">
                    Zresetuj
                </button>
        </div>

    </div>
    <div class="col-9 col-lg-8 p-4 d-flex flex-column justify-content-center align-items-center"
         style="height: 100%; overflow: auto">

        <div class="py-3">

            <%--            displaying various messages sent from backend--%>
            <c:choose>
                <c:when test="${error != null}">
                    <span class="text-danger fw-bold"> Przesyłane dane były niepoprawne</span>
                </c:when>
                <c:when test="${error == null && \"invalid\".equals(solveAttempt)}">
                    <span class="text-danger fw-bold"> Sudoku nie ma rozwiązania</span>
                </c:when>
                <c:when test="${error == null && \"fail\".equals(solveAttempt)}">
                    <span class="text-danger fw-bold"> Nie udało się rozwiązać sudoku poprzez rozumowanie - możesz spróbować backtrackingu</span>
                </c:when>
                <c:when test="${error == null && \"success\".equals(solveAttempt) && bruteForce == null}">
                    <span class="text-success fw-bold">Znaleziono następujące jedyne rozwiązanie:</span>
                </c:when>
                <c:when test="${error == null && \"success\".equals(solveAttempt) && bruteForce != null}">
                    <span class="text-success fw-bold">Znaleziono następujące rozwiązanie - może nie być unikatowe:</span>
                </c:when>
            </c:choose>
        </div>

        <%--        sudoku table/input--%>
        <table class="sudokuTable">
            <c:forEach begin="0" end="8" var="row">
                <tr>
                    <c:forEach begin="0" end="8" var="column">

                        <td>
                            <input type="text"
                                   name="fieldValue" ${solveAttempt != null || error != null ? "readonly" : ""}

                                <%--                            value from base seed if there is any--%>
                            <c:if test="${solveAttempt != null && error == null && !originalSeed.substring(row * 9 + column, row * 9 + column + 1).equals(\"0\")}">
                                   value="${solution.substring(row * 9 + column, row * 9 + column + 1)}"
                            </c:if>

                                   data-row="${row}" data-column="${column}"
                                   data-box="${(row -  row % 3)/ 3 * 3 + (column - column % 3) / 3}">

                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>

        <%--       modal for confirming brute force solve - due to number of calculations needed brute force is disabled if over 20 fields are empty--%>
        <div class="modal fade" id="bruteForceModal" tabindex="-1" aria-labelledby="bruteForceModal" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">

                    <div class="modal-body">
                        <p>
                            Pola do rozwiązania w sudoku - ${numbersMissing} - backtracking zajmie
                            <c:choose>
                                <c:when test="${numbersMissing > 20}">
                                    niestety zbyt wiele czasu
                                </c:when>
                                <c:when test="${numbersMissing > 12}">
                                    bardzo długi okres czasu - czy jesteś pewien?
                                </c:when>
                                <c:when test="${numbersMissing > 9}">
                                    długi okres czasu - czy jesteś pewien?
                                </c:when>
                                <c:otherwise>
                                    chwilę - czy jesteś pewien?
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <div class="d-flex justify-content-around">
                            <button type="submit" name="bruteForce" value="bruteForce" class="button red m-3"
                                    style="width: 25%; ${numbersMissing > 20 ? "display: none;" : ""}">
                                Tak
                            </button>
                            <button type="button" class="button red m-3" data-bs-dismiss="modal" style="width: 25%">
                                Wróć
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        </form>
    </div>
    <div class="col-lg-1"></div>

</div>

<jsp:include page="../includes/footer.jsp"/>
</html>
