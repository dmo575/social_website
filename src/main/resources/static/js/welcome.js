import { openTab } from "./tools/tab.js";
import { loadView } from "./tools/tools.js";


document.addEventListener("DOMContentLoaded", () => {

    const bodyContainer = document.querySelector(`#body-container`);
    const messageContainer = document.querySelector(`#message-container`);
    const tabWelcome = document.querySelector(`#tab-welcome`);
    const tabLogin = document.querySelector(`#tab-login`);
    const tabRegister = document.querySelector(`#tab-register`);
    const tabContact = document.querySelector(`#tab-contact`);

    tabWelcome.addEventListener("click", onClickTabWelcome);
    tabLogin.addEventListener("click", onClickTabLogin);
    tabRegister.addEventListener("click", onClickTabRegister);
    tabContact.addEventListener("click", onClickTabContact);
    
    function onClickTabWelcome(event) {
        
        console.log("tab welcome");
    }

    function onClickTabLogin(event) {

        messageContainer.innerHTML = "";
        openTab(event.target.parentNode, "/login", bodyContainer);
        // handle tab not selected error
        // handle tab not oppened error

    }

    function onClickTabRegister(event) {
        messageContainer.innerHTML = "";
        openTab(event.target.parentNode, "/register", bodyContainer);
        // handle tab not selected error
        // handle tab not oppened error

    }

    function onClickTabContact(event) {

        console.log("tab contact");
    }

});

// TODO: We can have tab content transition animations, since we have the container for the tabs
// content, we can animate it going away, load the content and animate it comin in.
// we would have to make openTab an async function for that, so we can properly start the
// comin in animation after the data is all set.
// A fade out / fade in with some scale down / scale in, or a swipe right

// or maybe the fade in out combine it with a scale up for the fade out and another scale up
// from the fade in, the first one from normal size up, the second one from less than normal size
// to normal size, to give the impression of depth.