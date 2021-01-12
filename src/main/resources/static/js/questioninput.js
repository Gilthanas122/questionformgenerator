let inputIndex = 0;

window.onload = function WindowLoad(event) {
    addEventListeners();
}

function addEventListeners() {
    let trueFalseButton = document.getElementById("radio");
    let scaleButton = document.getElementById("scale");
    let textButton = document.getElementById("text");
    let checkBoxButton = document.getElementById("checkbox");

    trueFalseButton.addEventListener("click", () => renderElements('radio'), false);
    scaleButton.addEventListener("click", () => renderElements('scale'), false);
    textButton.addEventListener("click", () => renderElements('text'), false);
    checkBoxButton.addEventListener("click", () => renderElements('checkbox'), false);
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
    let explanationText = createTextNode("You have choosen " + buttonId + "  answer possibility", "P");

    form.appendChild(explanationText);

    form.id = "inputquestionform";
    form.method = "POST";
    form.action = "/question/create" +"/"+ buttonId + "/" +getQuestionFormId();
    form.appendChild(createQuestionTextInputAndLabel());
    if (buttonId === "radio" || buttonId === "checkbox") {
        form.appendChild(createInputTrueFalseOrCheckboxNode(buttonId));
    } else if ( buttonId === "text") {
        form.appendChild(createInputTextNode(buttonId));
    } else if (buttonId === "scale"){
        form.appendChild(createInputScaleTextNode(buttonId));
    } else{
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

function createInputTrueFalseOrCheckboxNode(buttonId) {
    let container = document.createElement("DIV");
    let resetbutton = createResetButton();

    let innerInputContainer = document.createElement("DIV");
    innerInputContainer.id = "innerinputcontainer";
    let textInput = createAnotherInputField();

    let anotherInputButton = document.createElement("BUTTON");
    anotherInputButton.textContent = "Create another input field";
    anotherInputButton.type = "button";
    anotherInputButton.addEventListener("click",() => createAnotherInputField());


    innerInputContainer.appendChild(textInput);
    innerInputContainer.appendChild(anotherInputButton);
    let subdivExample = createSubDivExample(buttonId);
    container.appendChild(innerInputContainer);
    container.appendChild(subdivExample);
    container.appendChild(resetbutton);

    return container;
}

function createQuestionTextInputAndLabel(){
    let container = document.createElement("DIV");
    let label = document.createElement("LABEL");
    label.textContent = "Enter your question text";
    label.for="questiontext";
    let input = document.createElement("INPUT");
    input.id = "questiontext";
    input.name = "questionText";
    input.placeholder="Enter you questiontext";
    container.appendChild(label);
    container.appendChild(input);
    return container;
}

function createAnotherInputField() {
    let form = document.getElementById("innerinputcontainer");
    let input = document.createElement("input");
    input.placeholder = "Enter your answer here";
    let label = createTextNode("Enter answer possibility  " + (inputIndex+1) + " : ", "LABEL");
    input.type = "text";
    input.id = "input" + inputIndex;
    input.name = "answers[" + inputIndex + "]";
    label.for = "input" + inputIndex;
    if (form != null){
        form.insertBefore(input, document.getElementById("input" + (inputIndex -1)).nextSibling);
        form.insertBefore(label, input);
        inputIndex++;
    }else{
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
    input.placeholder = "enter the max value of the scale";
    container.appendChild(label);
    container.appendChild(input);
    container.appendChild(createSubDivExampleScale(buttonId));

    container.appendChild(resetbutton);

    return container;
}

function createSubDivExample(buttonId) {
    let container = document.createElement("DIV");
    container.id = "subdivexample";
    let exampleText = createTextNode("An example for the choose question type", "P");

    container.appendChild(exampleText);
    if (buttonId === "radio" || buttonId === "checkbox") {
        container.appendChild(createSubDivExampleRadioOrCheckbox(buttonId));
    } else if (buttonId === "text") {
        container.appendChild(createSubDivExampleText())
    } else {
        container.appendChild(createSubDivExampleScale());
    }
    return container;
}

function createSubDivExampleRadioOrCheckbox(buttonId) {
    let container = document.createElement("DIV");
    if (buttonId == "checkbox"){
        container.appendChild(createInputs("checkbox", "Which character(s) do you like from the following?", "Ron", "Harry"));
    }else{
        container.appendChild(createInputs("radio", "Which one is your favourite character?", "Hermione", "Dumbledore"));
    }
    return container;
}

function createInputs(inputType, questionTextInput, input1, input2){
    let container = document.createElement("DIV");
    let questionText = createTextNode(questionTextInput, "P");
    container.appendChild(questionText);
    for (i = 0; i < 2; i++) {
        let radioButton = document.createElement("INPUT");
        radioButton.type = inputType;
        radioButton.id = inputType + i;
        let radioButtonLabel = document.createElement("LABEL");
        radioButtonLabel.for = inputType + i;
        radioButton.disabled=true;
        if (i === 0) {
            radioButtonLabel.textContent = input1
        } else {
            radioButtonLabel.textContent = input2
        }
        container.appendChild(radioButtonLabel);
        container.appendChild(radioButton);
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

function createSubDivExampleScale(buttonId) {
    let container = document.createElement("DIV");
    container.id = "subdivexample";
    let textInput = document.createElement("INPUT");
    textInput.value = buttonId;
    textInput.name = "type";
    textInput.type = "range";
    textInput.max = 5;
    textInput.className = "slider";
    textInput.id = "scalerange";

    let rangeOutput = document.createElement("p");
    rangeOutput.id = "rangeoutput";

    addEventListenerToScaleInput(textInput);



    let exampleText = createTextNode("How much do you like the book Harry Potter?"
        + "On a scale of 1 to " + textInput.max, "LABEL")



    container.appendChild(exampleText);
    container.appendChild(textInput);
    container.appendChild(rangeOutput);
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

function disableSubmitButton(enable) {
    let form = document.getElementById("inputquestionform");
    if (form != null && enable) {
        document.getElementById("questionsubmit").disabled = true;
    } else if (form != null) {
        document.getElementById("questionsubmit").disabled = false;
    }
}

function createFormResetAndSubmitButtons() {
    let container = document.createElement("DIV");
    let submitButton = document.createElement("BUTTON");
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

function addEventListenerToScaleInput(input){
    input.onchange = function(){
        document.getElementById("rangeoutput").textContent=input.value;
    }
}

function enableFinishButton(value){
    let finishButton = document.getElementById("finish");
    finishButton.hidden = value;
}

function getQuestionFormId() {
    let questionFormId = document.getElementById("questionFormId");
    return questionFormId.textContent;
}



