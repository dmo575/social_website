import {validatePassword, validateUsername} from "./tools/clientvalidation.js"
import { errorMessage } from "./elements/popupmessage.js";
import { validateInputVisual, clearInputValidationVisual } from "./tools/presentation.js";

init();
document.addEventListener("/login-view-event", (event) => {
    init();
});

function init () {
    
    const loginForm = document.querySelector("#login-form");
    const messageContainer = document.querySelector(`#message-container`);

    // on login form submit event
    loginForm.addEventListener("submit", function (event) {

        event.preventDefault();

        // get input elements
        const usernameElement = event.target.elements["username"];
        const passwordElement = event.target.elements["password"];

        // check whether the input is valid or not
        const passOK = validatePassword(passwordElement.value);
        const usernameOK = validateUsername(usernameElement.value);
        
        // highlight the input elements according to their valid state
        validateInputVisual(usernameElement, usernameOK);
        validateInputVisual(passwordElement, passOK);

        // show a message to the user with information about what went wrong
        if(!passOK || !usernameOK) {

            if(!usernameOK && !passOK)
                errorMessage("Username and Password lengths are invalid", messageContainer);
            else if(!usernameOK)
                errorMessage("Username length is invalid", messageContainer);
            else
                errorMessage("Password lenght  is invalid", messageContainer);

            return;
        }



        // POST to /login. Pass in JSON with username and password
        fetch("/login",{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                username: usernameElement.value,
                password: passwordElement.value
            })
        })
        // analyze response
        .then(async response => {
            
            // if 200 OK, redirect to home
            if (response.ok) window.location.href = response.headers.get("Location");

            // if not 200 OK:
            
            // inform user
            let msg = await response.text();
            errorMessage(`${msg}`, messageContainer);

            // update input elements' visual
            validateInputVisual(usernameElement, false);
            clearInputValidationVisual(passwordElement);
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
}