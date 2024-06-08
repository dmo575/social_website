import { createButton } from './button.js';


// returns promise that contains a dashboard
export async function createDashBoard(typesArr) {

    var dashboard = document.createElement('div');

    for(let t in typesArr) {
        
        // wait to finish fetching all the buttons
        let buttons = await fetchButtonsOfType(typesArr[t]);

        for(let b in buttons) {
            let button = document.createElement('button');
            button.textContent = buttons[b].name;
            dashboard.appendChild(button);
        }
    }

    return dashboard;
}


// returns promise that contains all buttons of type type
async function fetchButtonsOfType(type) {

    // fetch for buttons
    return fetch(`/ui/buttons/type/${type}`)
    .then(response => {

        // make the promise return an object/array containing all the buttons we fetched
        if(response.status == 200) return response.json().then((data) => data);
    })
    .catch(err => {
        console.error(`fetchButtonsOfType(): Cannot fetch buttons of type [${type}]\n -> ${err}`);
    });
}