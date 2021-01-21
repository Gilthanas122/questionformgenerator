let inputIndex = 0;

window.onload = function WindowLoad(event) {
    addEventListeners();
}

function addEventListeners() {
    let checkBoxButton = document.getElementById("updatecheckboxbutton");
    let radioButton = document.getElementById("updateradiobutton");

    radioButton.addEventListener("click", () => createAnotherInputField('radio'), false);
    checkBoxButton.addEventListener("click", () => createAnotherInputField('checkbox'), false);
}

function createAnotherInputField(buttonId) {
    let tablerow;
    let anotherInputButton;
    if (buttonId === "checkbox"){
        tablerow = document.getElementById("updatecheckbox");
        anotherInputButton = document.getElementById("updatecheckboxbutton");
    }else{
        tablerow = document.getElementById("updateradio");
        anotherInputButton = document.getElementById("updateradiobutton");
    }
    let input = document.createElement("input");
    input.placeholder = "Enter your answer here";
    input.type = "text";
    input.id = "input" + inputIndex;
    input.name = "answers[" + inputIndex + "]";
    tablerow.insertBefore(input, anotherInputButton);
}