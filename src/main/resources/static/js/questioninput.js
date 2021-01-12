window.onload = function WindowLoad(event) {
    addEventListeners();
}

function addEventListeners() {
    let trueFalseButton = document.getElementById("true/false");
    let scaleButton = document.getElementById("scale");
    let textButton = document.getElementById("text");
    let checkBoxButton = document.getElementById("checkbox");

    trueFalseButton.addEventListener("click", () => renderElements('true/false'), false);
    scaleButton.addEventListener("click",() => renderElements('scale'), false);
    textButton.addEventListener("click", () => renderElements('text'), false);
    checkBoxButton.addEventListener("click", () => renderElements('checkbox'), false);
}


function renderElements(buttonId) {
    let subdivAlreadyRendered = document.getElementById("subdivexample");

    disableSubmitButton(false);
    if (subdivAlreadyRendered != null){
        alert("you already rendered an example, press Reset to choose again");
        return;
    }
    let container = document.getElementById("typediv");
    let subdiv = document.createElement("DIV");
    subdiv.id = "subdiv";
    if (buttonId === "true/false" || buttonId === "checkbox") {
        subdiv.appendChild(createInputTrueFalseOrCheckboxNode(buttonId));
    } else if ("scale")  {
        subdiv.appendChild(createInputScaleTextNode(buttonId));
    }else{
        subdiv.appendChild(createInputTextNode(buttonId))
    }
    container.appendChild(subdiv);
    }


function createInputTextNode(){
    let container = document.createElement("DIV");
    let explanationText = createTextNode("You have choosen text answer possibility", "P");
    let resetbutton = createResetButton();
    let textInput = document.createElement("INPUT");

    textInput.value = buttonId;
    textInput.type = "hidden"
    textInput.readonly = "readonly"
    textInput.name = "type";

    container.appendChild(explanationText);
    container.appendChild(textInput);
    let subdivExample = createSubDivExample(buttonId);
    subdivExample.style.border = "thick solid #0000FF";

    container.appendChild(subdivExample);
    container.appendChild(resetbutton);

    return container;
}
function createInputTrueFalseOrCheckboxNode(buttonId) {
    let container = document.createElement("DIV");
    let explanationText = createTextNode("You have choosen" + buttonId +  " answer possibility", "P");
    let resetbutton = createResetButton();

    let textInput = document.createElement("INPUT");

    textInput.value = buttonId;
    textInput.type = "hidden"
    textInput.readonly = "readonly"
    textInput.name = "type";

    container.appendChild(explanationText);
    container.appendChild(textInput);
    let subdivExample = createSubDivExample(buttonId);
    subdivExample.style.border = "thick solid #0000FF";

    container.appendChild(subdivExample);
    container.appendChild(resetbutton);

    return container;
}

function createInputScaleTextNode(buttonId){
    let container = document.createElement("DIV");
    let explanationText = document.createElement("P");
    explanationText.textContent = "You have choosen" + buttonId +  " answer possibility";
    let resetbutton = createResetButton();
    container.id = "subdivexample";

    container.style.border = "thick solid #0000FF";
    let textInput = document.createElement("INPUT");
    textInput.value = buttonId;
    textInput.name = "type";
    textInput.type="range";
    textInput.min=3;
    textInput.max = 100;
    textInput.className="slider";
    textInput.id = "scaleRange";


    let output = document.createElement("INPUT");
    output.id = "rangeoutput";
    output.value="";



    explanationText.textContent = "You have choosen" + buttonId +  " answer possibility. ";

    let exampleText = createTextNode("Minimum number textInput is " + textInput.min
        +  '/n' + " Set the maximum textInput in the field below", "P")

    container.appendChild(explanationText);
    container.appendChild(exampleText);
    container.appendChild(textInput);
    container.appendChild(output);
    container.appendChild(resetbutton);

    return container;


}

function createSubDivExample(buttonId){
    let container = document.createElement("DIV");
    container.id = "subdivexample";
    let exampleText = createTextNode("An example for the choose question type", "P");

    container.appendChild(exampleText);
    if (buttonId === "true/false"){
        container.appendChild(createSubDivExampleTrueFalse());
    }else if (buttonId === "text"){
        container.appendChild(createSubDivExampleText())
    }else{
        container.appendChild(createSubDivExampleScale());
    }

    return container;
}

function createSubDivExampleTrueFalse() {
    let container = document.createElement("DIV");
    let questionText = createTextNode("Do you like Harry Potter?", "P");
    container.appendChild(questionText);
    for (i = 0; i < 2; i++) {
        let radioButton = document.createElement("INPUT");
        radioButton.type = "radio";
        radioButton.id = "radionbutton" + i;
        let radioButtonLabel = document.createElement("LABEL");
        radioButtonLabel.for = "radiobutton" + i;
        if (i === 0) {
            radioButtonLabel.textContent = "yes"
        } else {
            radioButtonLabel.textContent = "no"
        }
        container.appendChild(radioButtonLabel);
        container.appendChild(radioButton);
    }

    return container;
}

function createSubDivExampleText(){
    let container = createContainerSubDivExampleScaleAndText("Why do you like Harry Potter?");
    return container;

}

function createSubDivExampleScale(){
    let container = createContainerSubDivExampleScaleAndText("On a  scale from 1 to 10, how much do you like Harry Potter?");
    container.appendChild(document.createElement("BR"));
    let subDivExampleScaleOutput = document.createElement("OUTPUT");
    container.appendChild(subDivExampleScaleOutput);

    return container;

}

function createContainerSubDivExampleScaleAndText(labelText){
    let container = document.createElement("DIV");
    let textInput = document.createElement("INPUT");
    container.appendChild(document.createElement("BR"));
    textInput.placeholder = "Enter you answer here";
    textInput.id = "subdivtextinput";
    let labelSubDivInput = document.createElement("LABEL");
    labelSubDivInput.for = "subdivtextinput";
    labelSubDivInput.id = "subdivexamplelabel";
    labelSubDivInput.textContent = labelText;
    container.appendChild(labelSubDivInput);
    container.appendChild(textInput);
    container.appendChild(document.createElement("BR"));

    return container;
}


function createTextNode(questionText, tagType){
    let text = document.createElement(tagType);
    text.textContent = questionText;
    return text;
}
function createResetButton(){
    let resetbutton = document.createElement("button");

    resetbutton.id = "resetbutton";
    resetbutton.type = "button";
    resetbutton.textContent = "Reset to choose again"
    resetbutton.addEventListener("click", () => resetGeneratedSubDiv());

    return resetbutton;

}

function resetGeneratedSubDiv(){
    let container = document.getElementById("typediv");
    let subdiv = document.getElementById("subdiv");
    container.removeChild(subdiv);
    disableSubmitButton(true);
}

function disableSubmitButton(enable){
    if (enable){
        document.getElementById("questionsubmit").disabled = true;
    } else{
        document.getElementById("questionsubmit").disabled = false;
    }
    }

