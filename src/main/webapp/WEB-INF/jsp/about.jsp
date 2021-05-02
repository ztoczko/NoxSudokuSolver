<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<jsp:include page="../includes/header.jsp"/>

<div class="row mainbody m-0">

    <div class="col-3 m-0 px-2 py-3 d-flex flex-column justify-content-center align-items-center"
         style="height: 100%;">

        <div class="m-0 px-2 py-3 d-flex flex-column align-items-center"
             style="height: 80%; width: 100%; overflow: auto; min-height: 0;">

            <button type="button" id="save" ${baseSeed == null ? "disabled" : ""} class="button red my-3"
                    style="width: 80%;">Zapisz grę
            </button>


            <form method="post"
                  class="m-0, p-0 d-flex flex-column justify-content-center align-items-center"
                  style="width: 100%; margin-bottom: 0;">
<%--                <button type="submit" id="load" ${baseSeed == null ? "disabled" : ""} class="button red my-3"--%>
<%--                        style="width: 80%;">Rozwiąż--%>
<%--                </button>--%>
                            </form>
                <button type="submit" class="button red my-3" style="width: 80%;">Rozwiąż</button>
                <button type="submit" name="bruteForce" value=""bruteForce class="button red my-3" style="width: 80%;">Rozwiąż poprzez backtracking</button>

                <button type="button" class="button red my-3" style="width: 80%;">Zresetuj</button>
        </div>

    </div>
    <div class="col-9 col-lg-8 p-4 d-flex flex-column justify-content-center align-items-center"
         style="height: 100%; overflow: auto">

        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam eu neque urna. Nam nec turpis vel elit elementum sollicitudin. Morbi sed bibendum velit, egestas sagittis lorem. Duis aliquam, sem eget vulputate hendrerit, lacus nunc malesuada neque, at mattis ipsum diam sit amet elit. Suspendisse viverra tempus tortor eget placerat. Praesent ut ex lacus. Fusce vel ex vitae dui aliquet auctor. Ut efficitur sapien finibus purus facilisis viverra. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed porttitor volutpat malesuada. Aenean condimentum efficitur felis eu pretium. Ut egestas ipsum sit amet quam dictum, ac porttitor justo tristique. Cras tristique, nunc ac finibus posuere, quam turpis consequat metus, ut cursus justo tellus id sem. Donec leo ipsum, efficitur nec accumsan in, volutpat ut mauris. </p>



    </div>
    <div class="col-lg-1"></div>

</div>


<jsp:include page="../includes/footer.jsp"/>
</html>
