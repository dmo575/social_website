import {validatePassword, validateUsername} from "./tools/clientvalidation.js"
import { errorMessage } from "./elements/popupmessage.js";


init();
document.querySelector("#body-container").addEventListener("tab-login", (event) => {
    init();
});

function init () {
    
    const loginForm = document.querySelector("#login-form");
    const messageContainer = document.querySelector(`#message-container`);

    // on login form submit event
    loginForm.addEventListener("submit", function (event) {

        event.preventDefault();

        const username = event.target.elements["username"];
        const password = event.target.elements["password"];

        const passOK = validatePassword(password.value);
        const usernameOK = validateUsername(username.value);
        

        // validate the input
        if(!passOK || !usernameOK) {
            if(!usernameOK && !passOK) {
                errorMessage("Username and Password lengths are invalid", messageContainer);
                username.classList.add("is-danger");
                password.classList.add("is-danger");
                return;
            }
            else if(!usernameOK) {
                errorMessage("Username length is invalid", messageContainer);
                username.classList.add("is-danger");
                return;
            }
            errorMessage("Password lenght  is invalid", messageContainer);
            password.classList.add("is-danger");
            return;
        }

        // POST to /login. Pass in JSON with username and password
        fetch("/login",{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                username: username,
                password: password
            })
        })
        // analyze response
        .then(async response => {
            
            // if 200 OK, redirect to home
            if (response.ok) window.location.href = response.headers.get("Location");

            // if not 200 OK, inform user
            let msg = await response.text();
            errorMessage(msg, messageContainer);
        });
    });

    // add danger clearing visual to the username input
    loginForm.elements["username"].addEventListener("click", (event) => {
        clearDanger(event.target);
    });

    // add danger clearing visual to the password input
    loginForm.elements["password"].addEventListener("click", (event) => {
        clearDanger(event.target);
    });
}

// clears the danger visual from given element
function clearDanger(el) {
    el.classList.remove("is-danger");
}