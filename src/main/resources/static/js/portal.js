import { openTab } from "./tools/tab.js";


document.addEventListener("DOMContentLoaded", () => {

    const bodyContainer = document.querySelector(`#body-container`);
    const messageContainer = document.querySelector(`#message-container`);
    const tabMe = document.querySelector(`#tab-me`);
    const tabPrivate = document.querySelector(`#tab-private`);
    const tabPublic = document.querySelector(`#tab-public`);
    const tabSubs = document.querySelector(`#tab-subs`);
    const tabSaved = document.querySelector(`#tab-saved`);


    tabMe.addEventListener(`click`, onClickTabMe);

    /**
     * triggers when clicking on the Me tab
     * @param {Event} event event
     */
    function onClickTabMe(event) {
        // open tab account, then load view posts
        openTab(tabMe, "/account", bodyContainer);
    };

    onClickTabMe();
});


