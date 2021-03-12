let createdTextQuestionIndex = 0;

function updateTextInput(val, id) {
    let lastChar = id.substr(id.length - 1);
    document.getElementById('rangeOutPut' + lastChar).value = val;
}

