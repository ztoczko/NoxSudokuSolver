console.log("test");

const xx = '${page}';
console.log(xx);
console.log(page);
let timer;

function isEmpty(strIn) {
    if (strIn === undefined) {
        console.log("undefined");
        return true;
    } else if (strIn == null) {
        console.log("null");
        return true;
    } else if (strIn == "") {
        console.log("emoty");
        return true;
    } else {
        return false;
    }
}

function clearConflict(baseElement, secondElement) {

    let baseConflicts = baseElement.dataset.conflict.split(",");
    let secondConflicts = secondElement.dataset.conflict.split(",");
    if (baseConflicts.length == 1) {
        baseElement.classList.remove("bg-danger");
        baseElement.removeAttribute("data-conflict");
    } else {
        baseConflicts = baseConflicts.filter((item) => {
            return item != (secondElement.dataset.row.toString() + secondElement.dataset.column.toString());
        });
        baseElement.dataset.conflict = baseConflicts.join(",");
    }
    if (secondConflicts.length == 1) {
        secondElement.classList.remove("bg-danger");
        secondElement.removeAttribute("data-conflict");
    } else {
        secondConflicts = secondConflicts.filter((item) => {
            return item != (baseElement.dataset.row.toString() + baseElement.dataset.column.toString());
        });
        secondElement.dataset.conflict = secondConflicts.join(",");
    }
    if (!checkIfConflictExists()) {
        if (document.getElementById("save") != null) {
            document.getElementById("save").disabled = false;
        }
        if (document.getElementById("solve") != null) {
            document.getElementById("solve").disabled = false;
        }
    }

}

function createConflict(baseElement, secondElement) {

    if (document.getElementById("save") != null) {
        document.getElementById("save").disabled = true;
    }
    if (document.getElementById("solve") != null) {
        document.getElementById("solve").disabled = true;
    }

    if (!baseElement.classList.contains("bg-danger")) {
        baseElement.classList.add("bg-danger")
    }
    if (!secondElement.classList.contains("bg-danger")) {
        secondElement.classList.add("bg-danger")
    }
    if (baseElement.dataset.conflict == null) {
        const newConflictList = secondElement.dataset.row.toString() + secondElement.dataset.column.toString();
        baseElement.dataset.conflict = newConflictList;
    } else if (!baseElement.dataset.conflict.match(secondElement.dataset.row.toString() + secondElement.dataset.column.toString())) {
        baseElement.dataset.conflict += "," + secondElement.dataset.row.toString() + secondElement.dataset.column.toString();
    }
    if (secondElement.dataset.conflict == null) {
        const newConflictList = baseElement.dataset.row.toString() + baseElement.dataset.column.toString();
        secondElement.dataset.conflict = newConflictList;
    } else if (!secondElement.dataset.conflict.match(baseElement.dataset.row.toString() + baseElement.dataset.column.toString())) {
        secondElement.dataset.conflict += "," + baseElement.dataset.row.toString() + baseElement.dataset.column.toString();
    }
}

function checkIfConflictExists() {
    return !document.querySelector(".sudokuTable input[data-conflict]") == null;
}

function runCheck() {
    console.log("run");
    let number = this.value;

// find conflicts
    if (!isEmpty(number) && !number.match("^[1-9]$")) {
        this.value = "";
        console.log(this.value);
        number = undefined;
        console.log("wrong no");
    }
    if (!isEmpty(number)) {
        const table = document.querySelector(".sudokuTable");
        const value = this.value;
        const row = this.dataset.row;
        const column = this.dataset.column;
        const box = this.dataset.box;
        const rowElements = table.querySelectorAll("input[data-row=\"" + row + "\"]");
        rowElements.forEach((item) => {
            if (value == item.value && column != item.dataset.column) {
                createConflict(this, item);
            }
        });
        const columnElements = table.querySelectorAll("input[data-column=\"" + column + "\"]");
        columnElements.forEach((item) => {
            if (value == item.value && row != item.dataset.row) {
                createConflict(this, item);
            }
        });
        const boxElements = table.querySelectorAll("input[data-box=\"" + box + "\"]");
        boxElements.forEach((item) => {
            if (value == item.value && (row != item.dataset.row || column != item.dataset.column)) {
                createConflict(this, item);
            }
        });
    }
    // clear conflicts
    if (!isEmpty(this.dataset.conflict)) {
        const conflicts = this.dataset.conflict.split(",");
        console.log(conflicts);
        conflicts.forEach((item) => {
            console.log(document.querySelector("input[data-row=\"" + item.substring(0, 1) + "\"][data-column=\"" + item.substring(1, 2) + "\"]"));
            console.log("input[data-row=\"" + item.substring(0, 1) + "\"][data-column=\"" + item.substring(1, 2) + "\"]");
            if (number != document.querySelector("input[data-row=\"" + item.substring(0, 1) + "\"][data-column=\"" + item.substring(1, 2) + "\"]").value) {
                clearConflict(this, document.querySelector("input[data-row=\"" + item.substring(0, 1) + "\"][data-column=\"" + item.substring(1, 2) + "\"]"));
            }
        });

    }

    checkCompleteCondition();

}

function checkCompleteCondition() {

    if (document.querySelector("[data-conflict]") == null) {
        let complete = true;
        document.querySelectorAll(".sudokuTable input").forEach((item) => {
           if (!item.value.match("^[1-9]$")) {
               complete = false;
           }
        });
        if (complete) {
            let i = 0;
            const fields = document.querySelectorAll(".sudokuTable input");
            fields.forEach((item) => {
               item.readOnly = true;
            });
            if (page == 1) {
                document.getElementById("save").disabled = true;
                document.getElementById("solve").disabled = true;
            }
            clearInterval(timer);

            const victory = setInterval(() => {
                if (i < fields.length) {
                    fields[i].setAttribute("style", "background-color: #32CD32; transition: background-color 0.5s");
                }
                if (i > 12) {
                    fields[i - 13].setAttribute("style", "background-color: #f2f2f2; transition: background-color 0.5s");
                }
                if (i - 12 == fields.length) {
                    const victoryModal = new bootstrap.Modal(document.getElementById("victoryModal"));
                    document.getElementById("victoryModalText").innerText = page == 1 ? "Twój czas to " + document.getElementById("hour").innerText + ":" + document.getElementById("minutes").innerText + ":" + document.getElementById("seconds").innerText : "Chyba przypadkiem rozwiązałeś sudoku, które miał rozwiązać algorytm?";
                        victoryModal.toggle();
                    clearInterval(victory);
                }
                i++
            }, 50);
        }
    }
}

function resetGame() {

    document.querySelectorAll(".sudokuTable input").forEach((item, index) => {
       item.removeAttribute("data-conflict");
       if (baseSeed.substring(15 + index, 16 + index) == "0") {
           item.removeAttribute("readonly");
       }
       if (!item.hasAttribute("readonly")) {
           item.value = "";
       }
    });
    document.getElementById("solve").disabled = false;
    if (document.cookie.match("cookiePermission=yes")) {
        document.getElementById("save").disabled = false;
    }
}

function saveGame() {

    let saveSeed = baseSeed.substring(0, 15);
    document.querySelectorAll(".sudokuTable input").forEach((item) => {
       saveSeed += item.value.match("^[1-9]$") ? item.value.toString() : "0";
    });
    saveSeed += document.getElementById("hour").innerText + document.getElementById("minutes").innerText + document.getElementById("seconds").innerText;

    const date = new Date();
    date.setTime(date.getTime());
    // let formatter = new Intl.DateTimeFormat('en', { year: 'numeric', month: 'numeric', day: 'numeric'}).format(date);
    let dateStr = date.getFullYear() + "-" + (date.getMonth() < 9 ? "0" : "") + (date.getMonth() + 1) + "-" + (date.getDate() < 10 ? "0" : "") + date.getDate() + "_" + (date.getHours() < 10 ? "0" : "") + date.getHours() + "h" + (date.getMinutes() < 10 ? "0" : "") + date.getMinutes() + "m" + (date.getSeconds() < 10 ? "0" : "") + date.getSeconds() +"s";

    // dateStr = dateStr.substring(dateStr.indexOf(",") + 1);
    // const dateArray = dateStr.split(" ");
    // dateStr = dateArray
    setCookie("save" + dateStr + "-seed-" + saveSeed.substring(0, 15), saveSeed, 365);
}

function setCookie(name, value, expiry) {

    const date = new Date();
    date.setTime(date.getTime() + (expiry * 24 * 60 * 60 * 1000));
    const expireString = expiry == 0 ? "" : "expires=" + date.toUTCString();
    document.cookie = name + "=" + value + ";" + expireString + ";path=/";
}

function getCookieValue(name) {

    const cookies = document.cookie.split(";");

    for (let i = 0; i < cookies.length; i++) {
        while (cookies[i].charAt(0) == " ") {
            cookies[i] = cookies[i].substring(1);
        }
        if (cookies[i].indexOf(name + "=") == 0) {
            return cookies[i].substring(name.length +1);
        }
    }
    return null;
}

// timer
if (page == 1 && gamePlayed != null) {
    timer = setInterval(() => {
        let seconds = parseInt(document.getElementById("seconds").innerText);
        seconds++;
        if (seconds >= 60) {
            seconds = 0;
            let minutes = parseInt(document.getElementById("minutes").innerText);
            minutes++;
            if (minutes >= 60) {
                minutes = 0;
                let hour = parseInt(document.getElementById("hour").innerText);
                hour++;
                document.getElementById("hour").innerText = hour < 10 ? "0" + hour.toString() : hour.toString();
            }
            document.getElementById("minutes").innerText = minutes < 10 ? "0" + minutes.toString() : minutes.toString();
        }
        document.getElementById("seconds").innerText = seconds < 10 ? "0" + seconds.toString() : seconds.toString();
    }, 1000);
}

// czy wszystko dać w window load??
//porównywanie stringów

//save i reset
if (page == 1) {
    document.getElementById("save").addEventListener("click", saveGame);
    document.getElementById("sudokuReset").addEventListener("click", resetGame);

}

//check cookie permission
window.addEventListener("load", () => {

    let cookies = document.cookie;
    if (!cookies.match("cookiePermission")) {
        const cookieModal = new bootstrap.Modal(document.getElementById("checkCookiePermission"), {
            backdrop: "static",
            keyboard: false
        });
        document.getElementById("cookiesYes").addEventListener("click", () => {
            setCookie("cookiePermission", "yes", 365);
        });
        document.getElementById("cookiesNo").addEventListener("click", () => {
            setCookie("cookiePermission", "no", 0);
        });
        cookieModal.toggle();
    }

    if ("no" == getCookieValue("cookiePermission") && page == 1) {
            document.getElementById("save").disabled = true;
            document.getElementById("load").disabled = true;
    };
});

// document.getElementById("save").style.color = "pink";
// document.getElementById("save").style.backgroundColor = "white";

//numbers validation
document.querySelectorAll(".sudokuTable input").forEach((item) => {
    item.addEventListener("change", runCheck);
});