import { createDashBoard } from './dashboard.js';


document.addEventListener("DOMContentLoaded", () => {

    // initiates an async operation that returns the dashboard
    createDashBoard(["guest"])
    .then(dashboard => {
        // append resulting dashboard to the page
        document.body.appendChild(dashboard);
    })
    .catch(err => {
        console.error(`Error while creating dashboard -> ${err}`);
    });
});