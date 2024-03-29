var actualAnswerIndexForTextQuestion = 0;
var buttonIndexToEnable = 0;
var currentTextAnswerIndex = 0;
var numberOfInputFieldsCreated = 0;
var hasInputfieldsCreated = false;

window.onload = function WindowLoad(event) {
    createValidateResetAndSubmitButtons();
}

function renderQuestionText(index) {
    if (!hasInputfieldsCreated) {
        let container = document.getElementById("textquestioncreate[" + index + "]");
        container.appendChild(createTextQuestionInput(index));
        hasInputfieldsCreated = true;
    }
}

function renderQuestionTextForCreate(index) {
    let container = document.getElementById("textquestioncreate[" + index + "]");
    container.appendChild(createTextQuestionInput(index));
    hasInputfieldsCreated = true;
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
    let numberOfInputFieldsInContainer = container.getElementsByTagName('INPUT').length
    let input = document.createElement("INPUT");
    numberOfInputFieldsCreated++;
    input.id = "textanswer" + currentTextAnswerIndex;
    actualAnswerIndexForTextQuestion++;
    input.name = "answers" + "[" + index + "].actualAnswerTexts[" + numberOfInputFieldsInContainer + "].answerText";
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

function resetActualAnswerIndexForTextQuestion() {
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

function createValidateResetAndSubmitButtons() {
    let form = document.getElementById("answerform");
    let submitButton = document.createElement("BUTTON");
    submitButton.type = "submit";
    submitButton.id = "submitbutton";
    submitButton.textContent = "SUBMIT";

    let resetButton = document.createElement("BUTTON");
    resetButton.type = "reset";
    resetButton.textContent = "RESET";
    resetButton.addEventListener('click', function () {
        submitButton.disabled = true;
    })

    form.appendChild(resetButton);
    form.appendChild(submitButton);
}


function resetForCreatingTextQuestion() {
    hasInputfieldsCreated = false;
}