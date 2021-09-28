var currentInputIndex;

function createAnotherInputField(index) {
    let allContainers = document.getElementsByClassName("innerMultipleAnswer")
    let container = allContainers[0];
    let anotherInputButtons = document.getElementsByClassName("updateMultipleAnswerButton");
    let anotherInputButton = anotherInputButtons[0];
    let input = document.createElement("input");
    input.placeholder = "Enter your answer here";
    input.type = "text";
    input.id = "input" + currentInputIndex;
    input.name = "questionTextPossibilities[" + currentInputIndex + "].answerText";
    currentInputIndex = currentInputIndex + 1;
    container.insertBefore(input, anotherInputButton);
}

function increaseInputIndex(index){
  let anotherInputButtons = document.getElementsByClassName("updateMultipleAnswerButton");
  let anotherInputButton = anotherInputButtons[0];
  anotherInputButton.addEventListener("click", ()=> createAnotherInputField(index + 1), false);
  currentInputIndex = index + 1;
}
