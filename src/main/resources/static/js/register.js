import { createDashBoard } from "./dashboard.js";
import { validateUsername, validatePassword } from "./tools/clientvalidation.js";
import { createMessage, popMessage } from "./tools/popupmessage.js";


var registrationForm = null;
var messageContainer = null;

document.addEventListener("DOMContentLoaded", () => {

    registrationForm = document.querySelector("#registration-form");
    messageContainer = document.querySelector("#message-container");

    // promise that returns a dashboard
    createDashBoard(["general"])
    .then(dashboard => {
        // append it to the page
        document.body.appendChild(dashboard);
    })
    .catch(err => {
        console.error(`Error while creating dashboard -> ${err}`);
    });


    // initializes the registration form
    registerFormInit(registrationForm);

});


// defines the submition event of the form:
// - prevent default
// - validate username & password
// - send POST to /register
// - handle response:
// -- 200OK: means registration successful, go to suggested next location
// -- not 200OK: we pop an error message to the user
function registerFormInit(registerForm) {
    // on registration form submit event
    registerForm.addEventListener("submit", function (event) {

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

                popMessage("Password and username too short, min 5 characters each", messageContainer);
                return;
            }
            // else if username failed validation
            else if (!usernameCheck) {
                popMessage("Username too short, min 5 characters", messageContainer);
                return;
            }
            // if password failed validation
            popMessage("Password too short, min 5 characters", messageContainer);
            return;
        }

        // POST to /register. Pass in JSON with username and password
        fetch("/register",{
            method: "POST",
            headers: {"Content-Type": "Application/JSON"},
            body: JSON.stringify({
                username: username,
                password: password
            })
        })
        // analyze response
        .then(async response => {
            
            // if 201 CREATED, follow Location
            if (response.status == 201) window.location.href = response.headers.get("Location");

            // if not 201 CREATED, it means we got an error message, so we inform the user by printing it
            let text = await response.text();
            let msg = `${text}`;
            popMessage(msg, messageContainer);
        });
    });
}
