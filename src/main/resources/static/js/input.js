var index = 1;

document.getElementById("inputbutton").addEventListener("click", function(){

    let container = document.getElementById("questiondiv");
    let input = document.createElement("input");
    let inputbutton = document.getElementById("inputbutton");
    let linebreak = document.createElement("br");
    input.type = "text";
    input.name = "names["+ index + "]";
    container.insertBefore(linebreak, inputbutton);
    container.insertBefore(input, inputbutton);
    index++;
});
