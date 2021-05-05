let timer;

// create starting table for all number possibilities for each field
let possibilities = [];
for (let i = 0; i < 81; i++) {
    possibilities.push([1, 2, 3, 4, 5, 6, 7, 8, 9]);
}

// simple function for string verification - checks if it's null/undefined/empty'
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

// function for clearing conflict between two fields
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

// function for creating conflict between two fields
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

// function verifying conflict presence within sudoku
function checkIfConflictExists() {
    return document.querySelector(".sudokuTable input[data-conflict]") != null;
}

// simple function to forward display possibilities request from event to regular function
function displayPossibilitiesButtonsEvent() {
    displayPossibilitiesButtons(this.dataset.row, this.dataset.column);
}

// function displaying buttons which define which values are possible in predefined field - function run each time some field gains focus
function displayPossibilitiesButtons(row, column) {

    const possibilityArray = possibilities[parseInt(row) * 9 + parseInt(column)];
    document.getElementById("possibilitiesBox").classList.remove("hide");
    document.querySelectorAll(".possibleButton").forEach((item, index) => {
        item.dataset.field = (parseInt(row) * 9 + parseInt(column)).toString();
        item.classList.remove("bg-secondary");
        if (!possibilityArray.includes(parseInt(item.innerText))) {
            item.classList.add("bg-secondary");
        }
    });
    clearLines();
    const possibilitiesElementCoords = document.getElementById("possibilitiesBox").getBoundingClientRect();
    const thisCoords = document.querySelector("[data-row=\"" + row + "\"][data-column=\"" + column + "\"]").getBoundingClientRect();
    document.body.appendChild(createLine(thisCoords.right, thisCoords.top, possibilitiesElementCoords.left, possibilitiesElementCoords.top));
    document.body.appendChild(createLine(thisCoords.right, thisCoords.bottom, possibilitiesElementCoords.left, possibilitiesElementCoords.bottom));
}

// primary function validating values entered by user - run on value change of any input field
function runFieldCheckOnChange() {

    let number = this.value;

    // remove possibilities display
    if (page == 1) {
        if (!this.nextElementSibling.classList.contains("hide")) {
            this.nextElementSibling.classList.add("hide");
        }
        clearLines();
    }

// validating input format
    if (isEmpty(number) || !number.match("^[1-9]$")) {
        this.value = "";
        number = undefined;
        if (page == 1) {
            if (this.classList.contains("topElementSolid")) {
                this.classList.remove("topElementSolid");
                this.classList.add("topElement");
            }
            this.nextElementSibling.classList.remove("hide")
            displayPossibilitiesButtons(this.dataset.row, this.dataset.column);
        }
    }

    // displaying button group controlling field's possible values
    if (!isEmpty(number)) {
        if (page == 1) {
            document.getElementById("possibilitiesBox").classList.add("hide");
            if (this.classList.contains("topElement")) {
                this.classList.add("topElementSolid");
                this.classList.remove("topElement");
            }
        }
        const table = document.querySelector(".sudokuTable");
        const value = this.value;
        const row = this.dataset.row;
        const column = this.dataset.column;
        const box = this.dataset.box;

        // search for conflicts in a row
        const rowElements = table.querySelectorAll("input[data-row=\"" + row + "\"]");
        rowElements.forEach((item) => {
            if (value == item.value && column != item.dataset.column) {
                createConflict(this, item);
            }
        });

        // search for conflicts in a column
        const columnElements = table.querySelectorAll("input[data-column=\"" + column + "\"]");
        columnElements.forEach((item) => {
            if (value == item.value && row != item.dataset.row) {
                createConflict(this, item);
            }
        });

        // search for conflicts in a box
        const boxElements = table.querySelectorAll("input[data-box=\"" + box + "\"]");
        boxElements.forEach((item) => {
            if (value == item.value && (row != item.dataset.row || column != item.dataset.column)) {
                createConflict(this, item);
            }
        });
    }

    // clear conflicts if applicable
    if (!isEmpty(this.dataset.conflict)) {
        const conflicts = this.dataset.conflict.split(",");
        conflicts.forEach((item) => {

            if (number != document.querySelector("input[data-row=\"" + item.substring(0, 1) + "\"][data-column=\"" + item.substring(1, 2) + "\"]").value) {
                clearConflict(this, document.querySelector("input[data-row=\"" + item.substring(0, 1) + "\"][data-column=\"" + item.substring(1, 2) + "\"]"));
            }
        });
    }
    //check if sudoku is complete
    checkCompleteCondition();
}

//verifying sudoku completion
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

//reseting game to baseseed - clearing conflicts, entered values and possible values table - function bound to button through click event
function resetGame() {

    possibilities = [];
    for (let i = 0; i < 81; i++) {
        possibilities.push([1, 2, 3, 4, 5, 6, 7, 8, 9]);
    }
    document.querySelectorAll(".sudokuTable input").forEach((item, index) => {
        item.removeAttribute("data-conflict");
        item.classList.remove("bg-danger");

        if (baseSeed.substring(15 + index, 16 + index) == "0") {
            item.removeAttribute("readonly");
            if (item.classList.contains("topElementSolid")) {
                item.classList.remove("topElementSolid");
                item.classList.add("topElement");
                item.nextElementSibling.classList.remove("hide");
            }
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

//saving gameseed to cookie - function bound to button through click event
function saveGame() {

    let saveSeed = baseSeed.substring(0, 15);
    document.querySelectorAll(".sudokuTable input").forEach((item) => {
        saveSeed += item.value.match("^[1-9]$") ? item.value.toString() : "0";
    });
    saveSeed += document.getElementById("hour").innerText + document.getElementById("minutes").innerText + document.getElementById("seconds").innerText;

    const date = new Date();
    date.setTime(date.getTime());

    let dateStr = date.getFullYear() + "-" + (date.getMonth() < 9 ? "0" : "") + (date.getMonth() + 1) + "-" + (date.getDate() < 10 ? "0" : "") + date.getDate() + "_" + (date.getHours() < 10 ? "0" : "") + date.getHours() + "h" + (date.getMinutes() < 10 ? "0" : "") + date.getMinutes() + "m" + (date.getSeconds() < 10 ? "0" : "") + date.getSeconds() + "s";

    setCookie("save" + dateStr + "-seed-" + saveSeed.substring(0, 15), saveSeed, 365);

    const newLoad = document.createElement("OPTION");
    newLoad.setAttribute("value", "save" + dateStr + "-seed-" + saveSeed.substring(0, 15));
    newLoad.innerText = dateStr + "-seed-" + saveSeed.substring(0, 15);
    document.getElementById("loadMenu").appendChild(newLoad);
}

// creating new cookie
function setCookie(name, value, expiry) {

    const date = new Date();
    date.setTime(date.getTime() + (expiry * 24 * 60 * 60 * 1000));
    const expireString = expiry == 0 ? "" : "expires=" + date.toUTCString();
    document.cookie = name + "=" + value + ";" + expireString + ";path=/";
}

// getting cookie value for predetermined cookie key
function getCookieValue(name) {

    const cookies = document.cookie.split(";");

    for (let i = 0; i < cookies.length; i++) {
        while (cookies[i].charAt(0) == " ") {
            cookies[i] = cookies[i].substring(1);
        }
        if (cookies[i].indexOf(name + "=") == 0) {
            return cookies[i].substring(name.length + 1);
        }
    }
    return null;
}

//function showing value of random empty or invalid field - function bound to button through click event
function displayHint() {

    const emptyFields = [];
    document.querySelectorAll(".sudokuTable input").forEach((item) => {
        if (!item.value.match("^[1-9]$") || item.value != solution.substring(parseInt(item.dataset.row) * 9 + parseInt(item.dataset.column), parseInt(item.dataset.row) * 9 + parseInt(item.dataset.column) + 1)) {
            emptyFields.push({row: item.dataset.row, column: item.dataset.column});
        }
    });
    const chosenField = Math.floor(Math.random() * emptyFields.length);
    const chosenElement = document.querySelector("[data-row=\"" + emptyFields[chosenField].row + "\"][data-column=\"" + emptyFields[chosenField].column + "\"]");
    chosenElement.value = solution.substring(parseInt(emptyFields[chosenField].row) * 9 + parseInt(emptyFields[chosenField].column), parseInt(emptyFields[chosenField].row) * 9 + parseInt(emptyFields[chosenField].column) + 1);
    chosenElement.classList.add("bg-primary");
    chosenElement.dispatchEvent(new Event("change"));
    const bgDelay = setTimeout(() => {
        chosenElement.classList.remove("bg-primary");
    }, 1000);
    const hintButton = document.getElementById("sudokuHint");
    hintButton.disabled = true;
    let delayCounter = 30;
    let buttonText = hintButton.innerText + " " + (delayCounter < 10 ? "0" : "") + delayCounter + "s";
    hintButton.innerText = buttonText;
    const hintDelay = setInterval(() => {
        delayCounter--;
        if (delayCounter > 0) {
            buttonText = buttonText.substring(0, buttonText.length - 3) + (delayCounter < 10 ? "0" : "") + delayCounter + "s";
        } else {
            buttonText = buttonText.substring(0, buttonText.length - 4);
        }
        hintButton.innerText = buttonText;
        if (delayCounter == 0) {
            hintButton.disabled = false;
            clearInterval(hintDelay);
        }
    }, 1000);

}

//clearing all lines present in document
function clearLines() {
    if (document.querySelector("[data-line]") != null) {
        document.querySelectorAll("[data-line]").forEach((item) => {
            item.parentElement.removeChild(item);
        });
    }
}

// drawing line - function called by create line which calculates its parameters
function createLineElement(x, y, length, angle) {
    let line = document.createElement("div");
    let styles = 'border: 1px dashed orange; '
        + 'width: ' + length + 'px; '
        + 'height: 0px; '
        + '-moz-transform: rotate(' + angle + 'rad); '
        + '-webkit-transform: rotate(' + angle + 'rad); '
        + '-o-transform: rotate(' + angle + 'rad); '
        + '-ms-transform: rotate(' + angle + 'rad); '
        + 'position: absolute; '
        + 'top: ' + y + 'px; '
        + 'left: ' + x + 'px; '
        + 'opacity: 0.5;'
        + 'z-index: 9;';
    line.setAttribute('style', styles);
    line.setAttribute("data-line", "1");
    return line;
}

//function calculating parameters for line element - needed for drawing lines indicating which field's possible values are being edited
function createLine(x1, y1, x2, y2) {
    let a = x1 - x2,
        b = y1 - y2,
        c = Math.sqrt(a * a + b * b);
    let sx = (x1 + x2) / 2,
        sy = (y1 + y2) / 2;
    let x = sx - c / 2,
        y = sy;
    let alpha = Math.PI - Math.atan2(-b, a);
    return createLineElement(x, y, c, alpha);
}

// toggling possible value for predetermined field  - function bound to button through click event
function verifyButtonPossiblity() {

    if (this.classList.contains("bg-secondary")) {
        this.classList.remove("bg-secondary");
        possibilities[parseInt(this.dataset.field)].push(this.innerText);
        document.querySelector(".col-4[data-field=\"" + this.dataset.field + "\"][data-number=\"" + this.innerText + "\"]").innerText = this.innerText;

    } else {
        this.classList.add("bg-secondary");
        document.querySelector(".col-4[data-field=\"" + this.dataset.field + "\"][data-number=\"" + this.innerText + "\"]").innerText = " ";
        possibilities[parseInt(this.dataset.field)] = possibilities[parseInt(this.dataset.field)].filter((item) => {
            return item != this.innerText;
        });
    }
}

//solve animation for auto-solve
if (page == 2 && solveAttempt != null) {
    window.addEventListener("load", () => {

        const color = solveAttempt == "success" ? "#32CD32" : "#f0ad4e";
        const fields = document.querySelectorAll(".sudokuTable input");
        let i = 0;
        let solutionDisplay = setInterval(() => {
            if (i < fields.length) {
                fields[i].setAttribute("style", "background-color: " + color + "; transition: background-color 0.5s");
                if (originalSeed.substring(i, i + 1) == "0" && solution.substring(i, i + 1) != "0") {
                    fields[i].value = solution.substring(i, i + 1);
                }
            }
            if (i > 12) {
                fields[i - 13].setAttribute("style", "background-color: f2f2f2; transition: background-color 0.5s");
            }
            if (i - 12 == fields.length) {
                clearInterval(solutionDisplay);
            }
            i++;
        }, 50);
    });
}


// sudoku solve timer
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

//porównywanie stringów???

//boundind functions to save, hint and reset buttons
if (page == 1) {
    document.getElementById("save").addEventListener("click", saveGame);
    document.getElementById("sudokuReset").addEventListener("click", resetGame);
    document.getElementById("sudokuHint").addEventListener("click", displayHint);

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
    }
    ;
});

// remove possibilities view from filled fields on load
if (page == 1) {
    window.addEventListener("load", () => {
        document.querySelectorAll(".possibilitiesTable").forEach((item) => {
            if (item.previousElementSibling.hasAttribute("readonly") || item.previousElementSibling.value.match("^[1-9]$")) {
                item.classList.add("hide");
                item.previousElementSibling.classList.remove("topElement");
                item.previousElementSibling.classList.add("topElementSolid");
            }
        });
    });
}

//primary numbers validation in sudoku field/form
document.querySelectorAll(".sudokuTable input").forEach((item) => {
    item.addEventListener("change", runFieldCheckOnChange);
});

// function createLinesToPossibilities() {
//     clearLines();
//     const possibilitiesElementCoords = document.getElementById("possibilitiesBox").getBoundingClientRect();
//     const thisCoords = this.getBoundingClientRect();
//     console.log(thisCoords.right, thisCoords.top, possibilitiesElementCoords.left, possibilitiesElementCoords.top);
//     document.body.appendChild(createLine(thisCoords.right, thisCoords.top, possibilitiesElementCoords.left, possibilitiesElementCoords.top));
//     document.body.appendChild(createLine(thisCoords.right, thisCoords.bottom, possibilitiesElementCoords.left, possibilitiesElementCoords.bottom));
//     displayPossibilitiesButtons(this.dataset.row, this.dataset.column);
// }


// handling displaying field's possible values through event
if (page == 1) {
    document.querySelectorAll(".sudokuTable input").forEach((item) => {
        if (!item.hasAttribute("readonly")) {
            item.addEventListener("focus", displayPossibilitiesButtonsEvent);
        }
    });
}


// event handling for possibilities buttons

if (page == 1) {
    document.querySelectorAll(".possibleButton").forEach((item) => {
        item.addEventListener("click", verifyButtonPossiblity);
    });

}

// removing possibility for accidental submitting form when pressing enter key
window.addEventListener('keydown', function (e) {
    if (e.keyIdentifier == 'U+000A' || e.keyIdentifier == 'Enter' || e.keyCode == 13) {
        e.preventDefault();
        return false;
    }
}, true);