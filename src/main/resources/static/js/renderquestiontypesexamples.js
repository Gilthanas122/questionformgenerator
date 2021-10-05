let inputIndex = 0;

window.onload = function WindowLoad(event) {
    addEventListeners();
}

function addEventListeners() {
    let trueFalseButton = document.getElementById("RadioButtonQuestion");
    let ScaleQuestionButton = document.getElementById("ScaleQuestion");
    let textButton = document.getElementById("TextQuestion");
    let checkBoxButton = document.getElementById("CheckBoxQuestion");

    trueFalseButton.addEventListener("click", () => renderElements('RadioButtonQuestion'), false);
    ScaleQuestionButton.addEventListener("click", () => renderElements('ScaleQuestion'), false);
    textButton.addEventListener("click", () => renderElements('TextQuestion'), false);
    checkBoxButton.addEventListener("click", () => renderElements('CheckBoxQuestion'), false);
}

function renderElements(buttonId) {
    let inputQuestionFormAlreadyRendered = document.getElementById("inputquestionform");

    if (inputQuestionFormAlreadyRendered != null) {
        alert("you already rendered an example, press Reset to choose again");
        return;
    }
    enableFinishButton(true);
    let container = document.getElementById("typediv");
    let form = document.createElement("FORM");
    let explanationText = createTextNode("You have choosen " + buttonId + " Answer Possibilities", "P");

    form.appendChild(explanationText);

    form.id = "inputquestionform";
    form.method = "POST";
    form.action = "/question/create" + "/" + buttonId + "/" + getQuestionFormId();
    form.appendChild(createQuestionTextInputAndLabel());


    if (buttonId === "RadioButtonQuestion" || buttonId === "CheckBoxQuestion") {
        form.appendChild(createInputRadioOrCheckboxNode(buttonId));
    } else if (buttonId === "TextQuestion") {
        form.appendChild(createInputTextNode(buttonId));
    } else if (buttonId === "ScaleQuestion") {
        form.appendChild(createInputScaleTextNode(buttonId));
    } else {
        alert("Invalid input")
    }
    form.appendChild(createFormResetAndSubmitButtons());
    container.appendChild(form);
}

function createInputTextNode(buttonId) {
    let container = document.createElement("DIV");
    let resetbutton = createResetButton();

    let subdivExample = createSubDivExample(buttonId);
    subdivExample.style.border = "thick solid #0000FF";

    container.appendChild(subdivExample);
    container.appendChild(resetbutton);

    return container;
}

function createInputRadioOrCheckboxNode(buttonId) {
    let container = document.createElement("DIV");
    let resetbutton = createResetButton();

    let innerInputContainer = document.createElement("DIV");
    innerInputContainer.id = "innerinputcontainer";
    let textInput = createAnotherInputField();

    let anotherInputButton = document.createElement("BUTTON");
    anotherInputButton.textContent = "Create another input field";
    anotherInputButton.type = "button";
    anotherInputButton.addEventListener("click", () => createAnotherInputField());


    innerInputContainer.appendChild(textInput);
    innerInputContainer.appendChild(anotherInputButton);
    let subdivExample = createSubDivExample(buttonId);
    container.appendChild(innerInputContainer);
    container.appendChild(subdivExample);
    container.appendChild(resetbutton);

    return container;
}

function createQuestionTextInputAndLabel() {
    let container = document.createElement("DIV");
    let label = document.createElement("LABEL");
    label.textContent = "Enter your question text";
    label.for = "questiontext";
    let input = document.createElement("INPUT");
    input.id = "questiontext";
    input.name = "questionText";
    input.placeholder = "Enter you questiontext";
    container.appendChild(label);
    container.appendChild(input);
    addEventlistenerToItem(input);
    return container;
}

function createAnotherInputField() {
    let form = document.getElementById("innerinputcontainer");
    let input = document.createElement("input");
    input.placeholder = "Enter your answer here";
    let label = createTextNode("Enter answer possibility  " + (inputIndex + 1) + " : ", "LABEL");
    input.type = "text";
    input.id = "input" + inputIndex;
    input.name = "answers[" + inputIndex + "]";
    label.for = "input" + inputIndex;
    addEventlistenerToItem(input);
    if (form != null) {
        form.insertBefore(input, document.getElementById("input" + (inputIndex - 1)).nextSibling);
        form.insertBefore(label, input);
        inputIndex++;
    } else {
        inputIndex++;
        return input;
    }
}

function createInputScaleTextNode(buttonId) {
    let container = document.createElement("DIV");
    let resetbutton = createResetButton();

    let label = createTextNode("Provide a max value for the input", "LABEL");
    let input = document.createElement("INPUT");
    input.name = "answers[0]";
    input.id = "input0";
    input.placeholder = "enter the max value of the ScaleQuestion";
    addEventlistenerToItem(input);
    container.appendChild(label);
    container.appendChild(input);
    container.appendChild(createSubDivExample("ScaleQuestion"));

    container.appendChild(resetbutton);

    return container;
}

function createSubDivExample(buttonId) {
    let container = document.createElement("DIV");
    container.id = "subdivexample";
    let exampleText = createTextNode("An example for the choose question type", "P");

    container.appendChild(exampleText);
    if (buttonId === "RadioButtonQuestion" || buttonId === "CheckBoxQuestion") {
        container.appendChild(createSubDivExampleRadioOrCheckboxOrScale(buttonId));
    } else if (buttonId === "text") {
        container.appendChild(createSubDivExampleText())
    } else {
        container.appendChild(createSubDivExampleRadioOrCheckboxOrScale("ScaleQuestion"));
    }
    return container;
}

function createSubDivExampleRadioOrCheckboxOrScale(buttonId) {
    let container = document.createElement("DIV");
    if (buttonId == "CheckBoxQuestion") {
        container.appendChild(createInputs("CheckBoxQuestion", "Which character(s) do you like from the following?", "Ron", "Harry"));
    } else if (buttonId == "RadioButtonQuestion") {
        container.appendChild(createInputs("RadioButtonQuestion", "Which one is your favourite character?", "Hermione", "Dumbledore"));
    }else{
        container.appendChild(createInputs("ScaleQuestion", "How much do you like Harry Potter?", null, null))
    }
    return container;
}

function createInputs(inputType, questionTextInput, input1, input2) {
    let container = document.createElement("DIV");
    let questionText = createTextNode(questionTextInput, "P");
    container.appendChild(questionText);
    if (inputType == "ScaleQuestion"){
        for (let i = 1; i <= 5; i++) {
            let radioButton = document.createElement("INPUT");
            radioButton.className += "check";
            radioButton.name = "radiobutton";
            radioButton.type = "radio";
            radioButton.id = inputType + i;
            let radioButtonLabel = document.createElement("LABEL");
            radioButtonLabel.for = inputType + i;
            radioButtonLabel.textContent = i;
            container.appendChild(radioButtonLabel);
            container.appendChild(radioButton);
        }
    }else{
        for (i = 0; i < 2; i++) {
            let radioButton = document.createElement("INPUT");
            radioButton.className += "check";
            radioButton.type = inputType;
            radioButton.id = inputType + i;
            radioButton.name = "CheckBoxQuestionradio";
            let radioButtonLabel = document.createElement("LABEL");
            radioButtonLabel.for = inputType + i;
            if (i === 0) {
                radioButtonLabel.textContent = input1;
                radioButton.checked=true;
            } else {
                if (inputType === "CheckBoxQuestion"){
                    radioButton.checked =true;
                }
                radioButtonLabel.textContent = input2;
            }
            container.appendChild(radioButtonLabel);
            container.appendChild(radioButton);
        }
    }
    return container;
}

function createSubDivExampleText() {
    let container = document.createElement("DIV");
    container.appendChild(createTextNode("Why do you like Harry Potter?", "LABEL"));
    let exampleInput = document.createElement("INPUT");
    exampleInput.readonly = true;
    exampleInput.disabled = true;
    container.appendChild(exampleInput);
    return container;
}


function createTextNode(questionText, tagType) {
    let text = document.createElement(tagType);
    text.textContent = questionText;
    return text;
}

function createResetButton() {
    let resetbutton = document.createElement("button");

    resetbutton.id = "resetbutton";
    resetbutton.type = "button";
    resetbutton.textContent = "Reset to choose again"
    resetbutton.addEventListener("click", () => resetGeneratedSubDiv());

    return resetbutton;

}

function resetGeneratedSubDiv() {
    let container = document.getElementById("typediv");
    let form = document.getElementById("inputquestionform");
    container.removeChild(form);
    inputIndex = 0;
    enableFinishButton(false);
}

function createFormResetAndSubmitButtons() {
    let container = document.createElement("DIV");
    let submitButton = document.createElement("BUTTON");
    submitButton.id = "formsubmit";
    submitButton.disabled = true;
    submitButton.type = "submit";
    submitButton.textContent = "Submit Question";
    submitButton.class = "btn" + " btn-primary";

    let resetButton = document.createElement("BUTTON");
    resetButton.type = "reset";
    resetButton.textContent = "Reset inputs";
    resetButton.class = "btn" + " btn-primary";
    container.appendChild(submitButton);
    container.appendChild(resetButton);
    return container;
}

function addEventListenerToScaleInput(input) {
    input.onchange = function () {
        document.getElementById("rangeoutput").textContent = input.value;
    }
}

function addEventlistenerToItem(input) {
    input.onchange = function () {
        validateEnablingSubmit();
    }
}

function validateEnablingSubmit(){
    let answers = document.getElementById("input0");
    if (answers !== null){
        console.log(answers.value);
    }
    let questionInput = document.getElementById("questiontext");
    console.log(questionInput.value);

    if ((answers === null && questionInput.value !== "") ||(answers !== null &&
        answers.value !== "" && questionInput.value !=="")) {
        document.getElementById("formsubmit").disabled = false;
    }else{
        document.getElementById("formsubmit").disabled = true;

    }
}

function enableFinishButton(value) {
    let finishButton = document.getElementById("finish");
    finishButton.hidden = value;
}

function getQuestionFormId() {
    let questionFormId = document.getElementById("questionFormId");
    return questionFormId.textContent;
}



