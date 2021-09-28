var actualAnswerIndexForTextQuestion = 0;
var buttonIndexToEnable = 0;
var currentTextAnswerIndex = 0;
var numberOfInputFieldsCreated = 0;
var hasInputfieldsCreated = false;
const currentIndexesForTextInputUpdate = [];
var isItUpdatePage = true;

window.onload = function WindowLoad(event) {
    createValidateResetAndSubmitButtons();
}

function renderQuestionText(index) {
    isItUpdatePage = true;
    if (!hasInputfieldsCreated) {
        let container = document.getElementById("textquestioncreate[" + index + "]");
        container.appendChild(createTextQuestionInput(index));
        hasInputfieldsCreated = true;
    }
}

function renderQuestionTextForCreate(index) {
    isItUpdatePage = false;
    let container = document.getElementById("textquestioncreate[" + index + "]");
    container.appendChild(createTextQuestionInput(index));
    hasInputfieldsCreated = true;
}

function setCurrentIndexForAnotherInputField(listIndex, value) {
    console.log("listindex " + listIndex + " value " + value);
    currentIndexesForTextInputUpdate[listIndex] = value;
    console.log(currentIndexesForTextInputUpdate[listIndex]);
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
    if (isItUpdatePage) {
        input.name = "answers" + "[" + index + "].actualAnswerTexts[" + (currentIndexesForTextInputUpdate[index] + 1) + "].answerText";
    } else {
        input.name = "answers" + "[" + index + "].actualAnswerTexts[" + actualAnswerIndexForTextQuestion + "].answerText";
    }
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

function updateTextInput(val, id) {
    let lastChar = id.substr(id.length - 1);
    document.getElementById('rangeOutPut' + lastChar).value = val;
}

function resetForCreatingTextQuestion() {
    hasInputfieldsCreated = false;
}