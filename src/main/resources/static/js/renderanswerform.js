var actualAnswerIndexForTextQuestion = 0;
var buttonIndexToEnable = 0;
var currentTextAnswerIndex = 0;
var numberOfInputFieldsCreated = 0;
var hasInputfieldsCreated = false;

window.onload = function WindowLoad(event) {
    createValidateResetAndSubmitButtons();
}

function renderQuestionText(index) {
    if (!hasInputfieldsCreated){
        let container = document.getElementById("textquestioncreate[" + index + "]");
        container.appendChild(createTextQuestionInput(index));
        hasInputfieldsCreated = true;
    }
}

function createTextQuestionInput(index) {
    let container = document.getElementById("answers[" + index + "]");
    let anotherInputButton = createAnotherInputButton(index);
    anotherInputButton.disabled = true;
    anotherInputButton.id = "textanswerbutton" + currentTextAnswerIndex;
    let finishButton = createFinishButtonForTextAnswer(currentTextAnswerIndex);

    let reEnableButton = createReenableButton(currentTextAnswerIndex);

    container.appendChild(anotherInputButton);
    container.appendChild(finishButton);
    container.appendChild(reEnableButton);
    enableDisableFinishAndAddAnotherTextFieldButtons();
    currentTextAnswerIndex++;
    return container;
}

function createAnotherInputButton(index) {
    let anotherInputButton = document.createElement("BUTTON");
    anotherInputButton.id = "anotherinputbutton" + index;
    anotherInputButton.textContent = "Create another input field";
    anotherInputButton.className = "buttonsToDisableEnable";
    anotherInputButton.type = "button";
    anotherInputButton.addEventListener("click", () => createAnotherInputField(index));
    return anotherInputButton;
}

function createAnotherInputField(index) {
    let elementId = "answers[" + index + "]";
    let container = document.getElementById(elementId);
    let input = document.createElement("INPUT");
    numberOfInputFieldsCreated++;
    input.id = "textanswer" + currentTextAnswerIndex;
    actualAnswerIndexForTextQuestion++;
    input.name = "answers" + "[" + index + "].actualAnswerTexts[" + actualAnswerIndexForTextQuestion + "].answerText";
    console.log("textanswerbutton"+ buttonIndexToEnable);
    container.insertBefore(input, document.getElementById("textanswerbutton" + buttonIndexToEnable));
}

function createFinishButtonForTextAnswer(index) {
    let finishButton = document.createElement("BUTTON");
    finishButton.textContent = "FINISH THIS QUESTION";
    finishButton.id = "finishbutton" + currentTextAnswerIndex;
    finishButton.type = "button";
    finishButton.className = "buttonsToDisableEnable";
    finishButton.disabled = true;
    finishButton.addEventListener("click", () => resetActualAnswerIndexForTextQuestion(currentTextAnswerIndex));
    return finishButton;
}

function resetActualAnswerIndexForTextQuestion(currentTextAnswerIndex) {
    actualAnswerIndexForTextQuestion = 0;
    buttonIndexToEnable++;
    enableDisableFinishAndAddAnotherTextFieldButtons();
}

function enableDisableFinishAndAddAnotherTextFieldButtons() {

    let buttons = document.getElementsByClassName("buttonsToDisableEnable");
    for (let i = 0; i < buttons.length; i++) {
        if (buttons[i].id === "finishbutton" + buttonIndexToEnable
            || buttons[i].id === "textanswerbutton" + buttonIndexToEnable
            || (buttons[i].id.includes("reenablebutton") && parseInt(buttons[i].id.charAt(buttons[i].id.length - 1)) !== buttonIndexToEnable)) {
            buttons[i].disabled = false;
        } else {
            buttons[i].disabled = true;
        }
    }

}

function createReenableButton(currentIndex) {
    let enableButton = document.createElement("BUTTON");
    enableButton.textContent = "Reenable to modify";
    enableButton.id = "reenablebutton" + currentIndex;
    if (currentTextAnswerIndex !== 0) {
        enableButton.disabled = true;
    }
    enableButton.className = "buttonsToDisableEnable";
    enableButton.type = "button";
    enableButton.addEventListener("click", () => reEnableTextQuestion(currentIndex));
    return enableButton;

}

function reEnableTextQuestion(currentIndex) {
    buttonIndexToEnable = currentIndex;
    enableDisableFinishAndAddAnotherTextFieldButtons();
}

function createValidInputFieldsButton() {
    let validateEmptyFieldsButton = document.createElement("BUTTON");
    validateEmptyFieldsButton.textContent = "Validate Inputs";
    validateEmptyFieldsButton.type = "button";
    validateEmptyFieldsButton.addEventListener("click", () => checkEmptyInputFields());
    return validateEmptyFieldsButton;

}

function createValidateResetAndSubmitButtons() {
    let form = document.getElementById("answerform");
    let submitButton = document.createElement("BUTTON");
    submitButton.type = "submit";
    submitButton.id = "submitbutton";
    submitButton.textContent = "SUBMIT";
    submitButton.disabled = true;

    let resetButton = document.createElement("BUTTON");
    resetButton.type = "reset";
    resetButton.textContent = "RESET";
    resetButton.addEventListener('click', function () {
        submitButton.disabled = true;
    })

    let validateButton = createValidInputFieldsButton();

    form.appendChild(resetButton);
    form.appendChild(validateButton);
    form.appendChild(submitButton);
}

function checkEmptyInputFields() {
    let inputFields = document.getElementsByTagName("INPUT");
    let isThereAnEmptyField = false;
    for (let i = 0; i < inputFields.length; i++) {
        if (inputFields[i].value === "" || inputFields[i].value === null) {
            isThereAnEmptyField = true;
        }
    }
    if (!isThereAnEmptyField) {
        let submitButton = document.getElementById("submitbutton");
        submitButton.disabled = false;
    }
}

function updateTextInput(val, id) {
    let lastChar = id.substr(id.length - 1);
    document.getElementById('rangeOutPut' + lastChar).value = val;
}

function resetForCreatingTextQuestion(){
    hasInputfieldsCreated = false;
}


