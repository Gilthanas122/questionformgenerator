var actualAnswerIndexForTextQuestion = 0;
var buttonIndexToEnable = 0;
var currentTextAnswerIndex = 0;
var numberOfInputFieldsCreated = 0;

window.onload = function WindowLoad(event) {
    createValidateResetAndSubmitButtons();
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
    container.id = "textanswers" + index;
    let input = document.createElement("INPUT");
    numberOfInputFieldsCreated++;
    input.name = idName + "[" + index + "].actualAnswerTexts[" + actualAnswerIndexForTextQuestion + "].answerText";
    input.id= "textanswer"+currentTextAnswerIndex;
    let anotherInputButton = createAnotherInputButton(index, idName);
    anotherInputButton.disabled = true;
    anotherInputButton.id = "textanswerbutton" + currentTextAnswerIndex;
    let finishButton = createFinishButtonForTextAnswer(currentTextAnswerIndex);

    let reEnableButton = createReenableButton(currentTextAnswerIndex);



    container.appendChild(input);
    container.appendChild(anotherInputButton);
    container.appendChild(finishButton);
    container.appendChild(reEnableButton);
    enableDisableFinishAndAddAnotherTextFieldButtons();
    currentTextAnswerIndex++;
    return container;
}

function createScaleInput(index, scale, idName) {
    let container = document.createElement("DIV");
    let input = document.createElement("INPUT");
    numberOfInputFieldsCreated++;
    input.name = idName + "[" + index + "].actualAnswerTexts[0].answerText" ;
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
        numberOfInputFieldsCreated++;
        input.id = type + index;
        input.className = "radiocheckbox";
        if (type === "checkbox"){
            input.name = idName + "[" + index + "].actualAnswerTexts[" + i + "].answerText"
        }else{
            input.name = idName + "[" + index + "].actualAnswerTexts[0].answerText"
        }

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
    let container = document.getElementById("textanswers" + index);
    actualAnswerIndexForTextQuestion++;
    let input = document.createElement("INPUT");
    numberOfInputFieldsCreated++;
    input.name = idName + "[" + index + "].actualAnswerTexts[" + actualAnswerIndexForTextQuestion + "].answerText" ;

    container.insertBefore(input, document.getElementById("textanswerbutton" + buttonIndexToEnable));

}

function createFinishButtonForTextAnswer(index){
    let finishButton = document.createElement("BUTTON");
    finishButton.textContent = "FINISH THIS QUESTION";
    finishButton.id = "finishbutton" + currentTextAnswerIndex;
    finishButton.type = "button";
    finishButton.className = "buttonsToDisableEnable";
    finishButton.disabled = true;
    finishButton.addEventListener("click", () => resetActualAnswerIndexForTextQuestion(currentTextAnswerIndex));
    return finishButton;
}

function resetActualAnswerIndexForTextQuestion(currentTextAnswerIndex){
    actualAnswerIndexForTextQuestion = 0;
    buttonIndexToEnable++;
    enableDisableFinishAndAddAnotherTextFieldButtons();
}

function enableDisableFinishAndAddAnotherTextFieldButtons(){

    let buttons = document.getElementsByClassName("buttonsToDisableEnable");
    for (let i = 0; i < buttons.length; i++) {
        if (buttons[i].id === "finishbutton" + buttonIndexToEnable
            || buttons[i].id === "textanswerbutton" + buttonIndexToEnable
            || (buttons[i].id.includes("reenablebutton") && parseInt(buttons[i].id.charAt(buttons[i].id.length -1)) !==  buttonIndexToEnable) ){
            buttons[i].disabled = false;
        }else{
            buttons[i].disabled = true;
        }
    }

}

function createReenableButton(currentIndex){
    let enableButton = document.createElement("BUTTON");
    enableButton.textContent = "Reenable to modify";
    enableButton.id = "reenablebutton" + currentIndex;
    if (currentTextAnswerIndex !== 0){
        enableButton.disabled = true;
    }
    enableButton.className = "buttonsToDisableEnable";
    enableButton.type = "button";
    enableButton.addEventListener("click", () => reEnableTextQuestion(currentIndex));
    console.log("reenable button id is " + enableButton.id);
    return enableButton;

}

function reEnableTextQuestion(currentIndex){
    buttonIndexToEnable = currentIndex;
    enableDisableFinishAndAddAnotherTextFieldButtons();
}

function createValidInputFieldsButton(){
    let validateEmptyFieldsButton = document.createElement("BUTTON");
    validateEmptyFieldsButton.textContent = "Validate Inputs";
    validateEmptyFieldsButton.type = "button";
    validateEmptyFieldsButton.addEventListener("click", () => checkEmptyInputFields());
    return validateEmptyFieldsButton;

}

function createValidateResetAndSubmitButtons(){
    let form = document.getElementById("answerform");
    let submitButton = document.createElement("BUTTON");
    submitButton.type= "submit";
    submitButton.id = "submitbutton";
    submitButton.textContent = "SUBMIT";
    submitButton.disabled = true;

    let resetButton = document.createElement("BUTTON");
    resetButton.type = "reset";
    resetButton.textContent = "RESET";
    resetButton.addEventListener('click', function (){
        submitButton.disabled = true;
    })

    let validateButton = createValidInputFieldsButton();

    form.appendChild(resetButton);
    form.appendChild(validateButton);
    form.appendChild(submitButton);
}

function checkEmptyInputFields(){
    let inputFields = document.getElementsByTagName("INPUT");
    let isThereAnEmptyField = false;
    for (let i = 0; i < inputFields.length; i++) {
        if (inputFields[i].value === "" || inputFields[i].value === null){
            isThereAnEmptyField = true;
        }
    }
    if (!isThereAnEmptyField){
        let submitButton = document.getElementById("submitbutton");
        submitButton.disabled = false;
    }
}

$(document).ready(function(){
    var checked = $(".radiocheckbox input:checked").length > 0;
    if (!checked){
        alert("check me")
    }
});


