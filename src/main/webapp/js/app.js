console.log("test");

const xx = '${page}';
console.log(xx);
console.log(page);

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
}

function createConflict(baseElement, secondElement) {
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

}

document.querySelectorAll(".sudokuTable input").forEach((item) => {
    item.addEventListener("change", runCheck);
});