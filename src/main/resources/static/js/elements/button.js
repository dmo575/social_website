// given the button data, returns a button element
// defines the look of a dashboard button
export function createButton(name, icon) {
    var container = document.createElement('div');
    var textField = codument.createElement('p');

    textField.textContent = name;

    container.appendChild(textField);

    return container;
}