var actualAnswerIndexForTextQuestion = 0;
var buttonIndexToEnable = 0;
var textQuestionIndex = 0;


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
    container.id = "textanswers";
    let input = document.createElement("INPUT");
    input.name = idName + "[" + index + "].actualAnswerTexts[" + actualAnswerIndexForTextQuestion + "]";
    let anotherInputButton = createAnotherInputButton(index, idName);
    anotherInputButton.disabled = true;
    anotherInputButton.id = "textanswerbutton" + textQuestionIndex;
    let finishButton = createFinishButtonForTextAnswer(index);

    container.appendChild(input);
    container.appendChild(anotherInputButton);
    container.appendChild(finishButton);
    enableDisableFinishAndAddAnotherTextFieldButtons();
    textQuestionIndex++;
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

function createAnotherInputButton(index, idName) {
    let anotherInputButton = document.createElement("BUTTON");
    anotherInputButton.id = "anotherinputbutton" + index;
    anotherInputButton.textContent = "Create another input field";
    anotherInputButton.className = "buttonsToDisableEnable";
    anotherInputButton.type = "button";
    anotherInputButton.addEventListener("click", () => createAnotherInputField(index, idName));
    return anotherInputButton;
}

function createAnotherInputField(index, idName){
    let container = document.getElementById("textanswers");
    actualAnswerIndexForTextQuestion++;
    let input = document.createElement("INPUT");
    input.name = idName + "[" + index + "].actualAnswerTexts[" + actualAnswerIndexForTextQuestion + "]";

    container.insertBefore(input, document.getElementById("textanswerbutton" + index));

}

function createFinishButtonForTextAnswer(index){
    let finishButton = document.createElement("BUTTON");
    finishButton.textContent = "FINISH THIS QUESTION";
    finishButton.id = "finishbutton" + textQuestionIndex;
    finishButton.type = "button";
    finishButton.className = "buttonsToDisableEnable";
    finishButton.disabled = true;
    finishButton.addEventListener("click", () => resetActualAnswerIndexForTextQuestion());
    return finishButton;
}

function resetActualAnswerIndexForTextQuestion(){
    actualAnswerIndexForTextQuestion = 0;
    buttonIndexToEnable++;
    console.log("actual answer index is " + actualAnswerIndexForTextQuestion);
    console.log("current text question index is " + buttonIndexToEnable);
    enableDisableFinishAndAddAnotherTextFieldButtons();
}

function  enableDisableFinishAndAddAnotherTextFieldButtons(){

    let buttons = document.getElementsByClassName("buttonsToDisableEnable");
    for (let i = 0; i < buttons.length; i++) {
        if (buttons[i].id === "finishbutton" + buttonIndexToEnable
            || buttons[i].id === "textanswerbutton" + buttonIndexToEnable){
            buttons[i].disabled = false;
        }else{
            buttons[i].disabled = true;
        }
    }
    console.log("button index to enable" + buttonIndexToEnable);
    console.log("text question index" + textQuestionIndex);

}


