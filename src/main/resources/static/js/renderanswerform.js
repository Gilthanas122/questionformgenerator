function callAlert(input) {
    alert("This is the input " + input);
}


function renderQuestion(type, idName, scale, index) {
    let container = document.getElementById("questions[" + index + "]");

    if (type === "text") {
        container.appendChild(createTextQuestionInput(index, idName));
    } else if (type === "scale" && scale != null) {
        container.appendChild(createScaleInput(index, scale, idName));
    }
}


function createTextNode(questionText, tagType) {
    let text = document.createElement(tagType);
    text.textContent = questionText;

    return text;
}

function createTextQuestionInput(index, idName) {
    let container = document.createElement("DIV");
    let input = document.createElement("INPUT");
    input.name = idName + "[" + index + "]";
    container.appendChild(input);
    return container;
}

function createScaleInput(index, scale, idName) {
    let container = document.createElement("DIV");
    let input = document.createElement("INPUT");
    input.name = idName + "[" + index + "]";
    input.type = "range";
    input.max = scale;

    let rangeOutput = document.createElement("p");
    rangeOutput.id = "rangeoutput";

    addEventListenerToScaleInput(input);

    container.appendChild(input);
    container.appendChild(rangeOutput);

    return container;
}

function addEventListenerToScaleInput(input) {
    input.onchange = function () {
        document.getElementById("rangeoutput").textContent = input.value;
    }
}

function renderCheckBoxOrRadioButtonQuestion(type, idName, index, answerTexts) {
    let container = document.getElementById("questions[" + index + "]");

    for (let i = 0; i < answerTexts.length; i++) {
        let input = document.createElement("INPUT");
        input.id = type + index;
        input.name = idName + "[" + index + "]";
        input.type = type;
        input.value = answerTexts[i];
        let label = document.createElement("LABEL");
        label.for = input.id;
        label.textContent = answerTexts[i];
        container.appendChild(label);
        container.appendChild(input);
    }
    return container;
}

function createAnotherInputButton() {
    let anotherInputButton = document.createElement("BUTTON");
    anotherInputButton.textContent = "Create another input field";
    anotherInputButton.type = "button";
    anotherInputButton.addEventListener("click", () => createAnotherInputField());
    return anotherInputButton;
}

