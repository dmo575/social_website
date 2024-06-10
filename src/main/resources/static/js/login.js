import { popMessage } from "./elements/popupmessage.js";


var loginForm = document.querySelector("#login-form");
var messageContainer = document.querySelector("#message-container");

// on login form submit event
loginForm.addEventListener("submit", function (event) {

    
    // get fields
    const username = event.target.elements["username"].value;
    const password = event.target.elements["password"].value;
    
    event.preventDefault();

    console.log("sssssssss");
    return;

    // POST to /login. Pass in JSON with username and password
    fetch("/login",{
        method: "POST",
        headers: {"Content-Type": "Application/JSON"},
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
    // analyze response
    .then(async response => {
        
        // if 200 OK, redirect to home
        if (response.status == 200) window.location.href = response.headers.get("Location");

        // if not 200 OK, inform user
        let text = await response.text();
        let msg = `Error: ${text}`;
        popMessage(msg, messageContainer);
    });
});