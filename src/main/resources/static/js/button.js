export function createButton(name, icon) {
    var container = document.createElement('div');
    var textField = codument.createElement('p');

    textField.textContent = name;

    container.appendChild(textField);

    return container;
}