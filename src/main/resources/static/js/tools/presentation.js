/**
 * is-danger if false, is-safe if true
 * @param {HTMLElement} inputElement - the element to color
 * @param {Boolean} isvalid - defines its validity
 */
export function validateInputVisual(inputElement, isvalid) {

    if(isvalid) {
        inputElement.classList.remove("is-danger");
        inputElement.classList.add("is-success");
    }
    else {
        inputElement.classList.remove("is-success");
        inputElement.classList.add("is-danger");
    }
}

/**
 * Removes the is-danger and is-success classes from the element
 * @param {HTMLElement} inputElement - the input element to clear
 */
export function clearInputValidationVisual(inputElement) {
    inputElement.classList.remove("is-danger");
    inputElement.classList.remove("is-success");
}