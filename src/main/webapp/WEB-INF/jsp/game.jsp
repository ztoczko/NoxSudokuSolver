<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<jsp:include page="../includes/header.jsp"/>

<div class="row mainbody m-0">

    <div class="col-3 m-0 px-2 py-3 d-flex flex-column justify-content-center align-items-center"
         style="height: 100%;">

        <div class="d-flex flex-column justify-content-center align-items-center m-3 counter"
             style="overflow: auto; height: 20%; min-height: 0;">
            <%--solution timer with option of starting from timer saved in loaded game if such game is loaded--%>
            <span class="counter"><span
                    id="hour">${loadedGame != null ? loadedGame.substring(96, 98) : "00"}</span>:<span
                    id="minutes">${loadedGame != null ? loadedGame.substring(98, 100) : "00"}</span>:<span
                    id="seconds">${loadedGame != null ? loadedGame.substring(100, 102) : "00"}</span></span>
        </div>

        <%--        menu buttons--%>
        <div class="m-0 px-2 py-3 d-flex flex-column align-items-center"
             style="height: 80%; width: 100%; overflow: auto; min-height: 0;">
            <button type="button" class="button red my-3" data-bs-toggle="modal" data-bs-target="#newGame"
                    style="width: 80%;">Nowa gra
            </button>

            <%--            disabled if no game in progress also disabled through Javascript if cookies are not allowed--%>
            <button type="button" id="save" ${baseSeed == null ? "disabled" : ""} class="button red my-3"
                    style="width: 80%;">Zapisz grę
            </button>

            <%--            disabled through Javascript if cookied are not allowed--%>
            <button type="button" id="load" class="button red my-3" data-bs-toggle="modal" data-bs-target="#loadGame"
                    style="width: 80%;">Wczytaj grę
            </button>

            <%--            disabled if no game in progress--%>
            <button type="button" id="sudokuHint" ${baseSeed == null ? "disabled" : ""} class="button red my-3"
                    style="width: 80%;"> Podpowiedź
            </button>

            <%--            disabled if no game in progress--%>
            <form action="/solve" method="post"
                  class="m-0, p-0 d-flex flex-column justify-content-center align-items-center"
                  style="width: 100%; margin-bottom: 0;">

                <button type="submit" name="seed" value="${baseSeed}" id="solve" ${baseSeed == null ? "disabled" : ""}
                        class="button red my-3"
                        style="width: 80%;">Rozwiąż
                </button>

                <%--            disabled if no game in progress--%>
                <button type="button" id="sudokuReset" ${baseSeed == null ? "disabled" : ""} class="button red my-3"
                        style="width: 80%;">Zresetuj obecną grę
                </button>

        </div>

    </div>
    <div class="col-6 p-4 d-flex flex-column justify-content-center align-items-center"
         style="height: 100%; overflow: auto">

        <div class="py-3">
            <%--            error message if error sent from backend--%>
            <c:choose>
                <c:when test="${error != null}">
                    <span class="text-danger fw-bold"> Przesyłane dane były niepoprawne</span>
                </c:when>
            </c:choose>
        </div>

        <table class="sudokuTable">
            <%--            sudoku table/form generation--%>
            <c:forEach begin="0" end="8" var="row">
                <tr>
                    <c:forEach begin="0" end="8" var="column">
                        <td>

                            <input class="topElement" type="text" inputmode="numeric" pattern="[0-9]*"
                                <%--                                  field is disabled if error was detected on backend or there is no game in progress (baseseed is null)--%>
                                   name="fieldValue" ${error != null || baseSeed == null || !baseSeed.substring(row * 9 + column + 15, row * 9 + column + 16).equals("0") ? "readonly" : ""}
                            <c:choose>
                                <%--                            value from base seed if there is any--%>
                            <c:when test="${error != null || baseSeed != null && !baseSeed.substring(row * 9 + column + 15, row * 9 + column + 16).equals(\"0\")}">
                                   value="${baseSeed.substring(row * 9 + column + 15, row * 9 + column + 16)}"
                            </c:when>
                                <%--                                    value from loaded game if there is any and is not filled by base seed--%>
                            <c:when test="${error != null || baseSeed != null && loadedGame != null && baseSeed.substring(row * 9 + column + 15, row * 9 + column + 16).equals(\"0\") && !loadedGame.substring(row * 9 + column + 15, row * 9 + column + 16).equals(\"0\")}">
                                   value="${loadedGame.substring(row * 9 + column + 15, row * 9 + column + 16)}"
                            </c:when>
                            </c:choose>
                                   data-row="${row}" data-column="${column}"
                                   data-box="${(row -  row % 3)/ 3 * 3 + (column - column % 3) / 3}">

                                <%--                            possibilities values - visible when field is empty, heavily modified by Javascript--%>
                            <div class="bottomElement container-fluid possibilitiesTable" id="possibilities"
                                 style="overflow: auto;">
                                <div class="row" style="height: calc(100% / 3)">
                                    <div class="col-4" data-field="${row * 9 + column}" data-number="1">1</div>
                                    <div class="col-4" data-field="${row * 9 + column}" data-number="2">2</div>
                                    <div class="col-4" data-field="${row * 9 + column}" data-number="3">3</div>
                                </div>
                                <div class="row" style="height: calc(100% / 3)">
                                    <div class="col-4" data-field="${row * 9 + column}" data-number="4">4</div>
                                    <div class="col-4" data-field="${row * 9 + column}" data-number="5">5</div>
                                    <div class="col-4" data-field="${row * 9 + column}" data-number="6">6</div>
                                </div>
                                <div class="row" style="height: calc(100% / 3)">
                                    <div class="col-4" data-field="${row * 9 + column}" data-number="7">7</div>
                                    <div class="col-4" data-field="${row * 9 + column}" data-number="8">8</div>
                                    <div class="col-4" data-field="${row * 9 + column}" data-number="9">9</div>
                                </div>
                            </div>

                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>

        </form>

    </div>
    <div class="col-3 d-flex justify-content-center align-items-center">
        <%--                            possibilities button group - hidden by default, heavily modified through Javascript--%>
        <div class="possibilities hide" id="possibilitiesBox">
            <c:forEach var="buttonNo" begin="1" end="9">
                <button class="button green possibleButton" id="${"possibility".concat(buttonNo)}">
                        ${buttonNo}
                </button>
            </c:forEach>
        </div>

    </div>

    <%--  modal for loading game - loads all cookies with names starting with "save"--%>
    <div class="modal fade" id="loadGame" tabindex="-1" aria-labelledby="loadGame" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Wybierz zapisaną grę</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form method="post" class="my-3 d-flex flex-column justify-content-center align-items-center"
                          style="width: 100%;">
                        <select id="loadMenu" class="form-select" name="gameToLoad">
                            <option value="" selected>...</option>
                            <c:forEach var="cookieItem" items="${cookie}">
                                <%--                                <p> ${cookieItem.key.concat(cookieItem.key.contains(\"save\"))} </p>--%>
                                <c:if test="${cookieItem.key.contains(\"save\")}">
                                    <%--                                    nazwa savów : save**--%>
                                    <option value=${cookieItem.key}>${cookieItem.key.substring(4)} </option>
                                </c:if>
                            </c:forEach>
                        </select>
                        <div class="d-flex flex-row justify-content-evenly">
                            <button type="submit" name="type" value="load" class="button red m-3" style="width: 80%;">
                                Wczytaj
                            </button>
                            <button type="button" class="button red m-3" data-bs-dismiss="modal">Zamknij</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <%--    modal for choosing difficulty level for new game--%>
    <div class="modal fade" id="newGame" tabindex="-1" aria-labelledby="newGame" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Wybierz poziom trudności</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form method="post" class="my-3 d-flex flex-column justify-content-center align-items-center"
                          style="width: 100%;">
                        <select class="form-select" name="difficulty">
                            <option selected value="1">łatwe</option>
                            <option value="2">średnie</option>
                            <option value="3">trudne</option>
                            <option value="4">bardzo trudne</option>
                            <option value="5">niemożliwe</option>
                        </select>
                        <div class="d-flex flex-row justify-content-evenly">
                            <button type="submit" name="type" value="new" class="button red m-3" style="width: 80%;">
                                Nowa gra
                            </button>
                            <button type="button" class="button red m-3" data-bs-dismiss="modal">Zamknij</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>


<jsp:include page="../includes/footer.jsp"/>
</html>
