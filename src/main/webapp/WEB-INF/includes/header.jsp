<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Nox Sudoku Solver</title>
    <link rel="stylesheet" href="css/bootstrap.min.css"/>
    <link rel="stylesheet" href="css/style.css"/>


</head>
<body>
<script type="text/javascript">
    var page = ${page};
    var gamePlayed = null;
    var baseSeed = null;
    var solveAttempt = null;
    var originalSeed = null;
    var solution = null;
    <c:if test="${baseSeed != null}">
    gamePlayed = "true";
    baseSeed = "${baseSeed}";
    solution = "${solution}";
    </c:if>
    <c:if test="${solveAttempt != null}">
        solveAttempt = "${solveAttempt}";
        originalSeed = "${originalSeed}";
        solution = "${solution}";
    </c:if>
</script>
<div class="container-fluid p-0 m-0" style="min-height: 100vh;">
    <div class="row p-0 m-0" style="height: 20vh; width: 100%;">
        <div class="col-12 topbar d-flex justify-content-center align-items-end"> <!--dodać buttony z językami!!-->
            <div class="row" style="width: 100%;">
                <div class="col-lg-3"></div>
                <div class="col-sm-12 col-lg-6 d-flex justify-content-between align-items-end btn-group"
                     style="max-height: 100%; overflow: auto;">
                    <a href="game" class="btn py-0 px-1 mx-3 ${page == 1 ? "selected" : ""}">Zagraj</a>
                    <a href="solve" class="btn py-0 px-1 mx-3 ${page == 2 ? "selected" : ""}">Znajdź rozwiązanie</a>
                    <a href="about" class="btn py-0 px-1 mx-3 ${page == 3 ? "selected" : ""}">O stronie</a>
                </div>
                <div class="col-lg-3"></div>
            </div>
        </div>
    </div>
