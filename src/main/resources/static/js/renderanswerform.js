
function callAlert(input){
    alert("This is the input " + input);
}


function renderQuestion(questionText, questionType){
    let container = document.getElementById("questionsDiv");
    let question= createTextNode(questionText, "p");
    container.append(question);
}


function createTextNode(questionText, tagType) {
    let text = document.createElement(tagType);
    text.textContent = questionText;
    return text;
}