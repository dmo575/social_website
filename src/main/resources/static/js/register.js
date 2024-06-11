import { validateUsername, validatePassword } from "./tools/clientvalidation.js";
import { errorMessage } from "./elements/popupmessage.js";


init();
console.log("first");
document.querySelector("#body-container").addEventListener("tab-register", (event) => {
    init();
    console.log("second");

});

function init() {
    const registrationForm = document.querySelector("#registration-form");
    const messageContainer = document.querySelector("#message-container");
    registerFormInit(registrationForm, messageContainer);
}


function registerFormInit(registrationForm, messageContainer) {
    // on registration form submit event
    registrationForm.addEventListener("submit", function (event) {

        // get fields
        const username = event.target.elements["username"].value;
        const password = event.target.elements["password"].value;
        let passCheck = validatePassword(password);
        let usernameCheck = validateUsername(username);

        event.preventDefault();

        // client-side data validation
        if(!passCheck || !usernameCheck) {

            

            // if password and username failed validation
            if(!passCheck && !usernameCheck) {

                errorMessage("Password and username too short, min 5 characters each", messageContainer);
                return;
            }
            // else if username failed validation
            else if (!usernameCheck) {
                errorMessage("Username too short, min 5 characters", messageContainer);
                return;
            }
            // if password failed validation
            errorMessage("Password too short, min 5 characters", messageContainer);
            return;
        }

        // POST to /register. Pass in JSON with username and password
        fetch("/register",{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                username: username,
                password: password
            })
        })
        // analyze response
        .then(async response => {
            
            // if 201 CREATED, follow Location
            if (response.ok) window.location.href = response.headers.get("Location");

            // if not 201 CREATED, log error:
            let msg = await response.text();
            errorMessage(msg, messageContainer);
        });
    });
}
