

<%--modal cookie--%>
<div class="modal fade" id="checkCookiePermission" tabindex="-1" aria-labelledby="checkCookiePermission" aria-hidden="true">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">

            <div class="modal-body">

                <p class="modalText">Strona używa ciasteczek w celu przechowywania zapisanych gier oraz przechowywania ustawień użytkownika</p>
                <p class="modalText">Czy chcesz zezwolić na przechowywanie ciasteczek?</p>

                    <div class="d-flex flex-row justify-content-evenly">
                        <button type="button" id="cookiesYes" class="button red m-3" style="width: 35%;" data-bs-dismiss="modal">
                            Tak
                        </button>
                        <button type="button" id="cookiesNo" class="button red m-3" style="width: 35%;" data-bs-dismiss="modal">
                            Nie
                        </button>
<%--                        <button type="button" class="button red m-3" data-bs-dismiss="modal">Zamknij</button>--%>
                    </div>

            </div>
        </div>
    </div>
</div>

<%--modal cookie--%>
<div class="modal fade" id="victoryModal" tabindex="-1" aria-labelledby="victoryModal" aria-hidden="true">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">

            <div class="modal-body d-flex flex-column justify-content-center align-items-center" style="width: 100%;">

                <img src="../../img/steven.webp"/>
                <p class="modalText" id="victoryModalText">Lorem ipsum</p>

                <div class="d-flex flex-row justify-content-evenly" style="width: 100%;">
                    <button type="button" class="button red m-3" style="width: 35%;" data-bs-dismiss="modal">
                        Super
                    </button>
                </div>

            </div>
        </div>
    </div>
</div>

<div class="row p-0 m-0" style="height: 3vh; overflow: auto">
    <div class="col-12 p-0 m-0 d-flex justify-content-center align-items-center">
        <p class="p-0 m-0" style="font-size: 0.6rem;">&copy; Copyright 2021 Zbigniew Toczko</p>
    </div>
</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf"
        crossorigin="anonymous"></script>
</body>
<script src="js/app.js"></script>
