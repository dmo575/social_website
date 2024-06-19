import {validatePassword, validateUsername} from "./tools/clientvalidation.js"
import { errorMessage } from "./elements/popupmessage.js";
import { validateInputVisual, clearInputValidationVisual } from "./tools/presentation.js";

init();
document.addEventListener("/register-view-event", (event) => {
    init();
});

function init () {
    
    const loginForm = document.querySelector("#registration-form");
    const messageContainer = document.querySelector(`#message-container`);

    // on login form submit event
    loginForm.addEventListener("submit", function (event) {

        event.preventDefault();

        // get input elements
        const usernameElement = event.target.elements["username"];
        const passwordElement = event.target.elements["password"];
        const passwordConfirmationElement = event.target.elements["password-confirmation"];


        // check whether the input is valid or not
        const passOK = validatePassword(passwordElement.value);
        const usernameOK = validateUsername(usernameElement.value);
        const passwordConfirmationOK = passwordElement.value === passwordConfirmationElement.value && passOK;
        
        // highlight the input elements according to their valid state
        validateInputVisual(usernameElement, usernameOK);
        validateInputVisual(passwordElement, passOK);
        validateInputVisual(passwordConfirmationElement, passwordConfirmationOK);


        // show a message to the user with information about what went wrong
        if(!passOK || !usernameOK || !passwordConfirmationOK) {

            if(!usernameOK && !passOK)
                errorMessage("Username and Password lengths are invalid", messageContainer);
            else if(!usernameOK)
                errorMessage("Username length is invalid", messageContainer);
            else if(!passwordConfirmationOK)
                errorMessage("Passwords do not match", messageContainer);
            else
                errorMessage("Password lenght  is invalid", messageContainer);

            return;
        }



        // POST to /register. Pass in JSON with username and password
        fetch("/register",{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                username: usernameElement.value,
                password: passwordElement.value
            })
        })
        // analyze response
        .then(async response => {
            
            // if 201, redirect
            if(response.status == 201)
                window.location.href = response.headers.get("Location");

            // else, log error
            let msg = await response.text();
            errorMessage(`${msg}`, messageContainer);
        });
    });

    loginForm.elements["username"].addEventListener("click", (event) => {
        // when clicking the input, clear it from any valid/invalid visual queue
        clearInputValidationVisual(event.target);
    });

    loginForm.elements["password"].addEventListener("click", (event) => {
        // when clicking the input, clear it from any valid/invalid visual queue
        clearInputValidationVisual(event.target);
    });

    loginForm.elements["password-confirmation"].addEventListener("click", (event) => {
        // when clicking the input, clear it from any valid/invalid visual queue
        clearInputValidationVisual(event.target);
    });
}