import { loadView } from "./tools.js";

/**
 * Returns false if the tab is already selected.
 * 
 * @param {HTMLElement} tab - the tab element
 * @returns true if the selection was possible, false otherwise
 */
export function selectTab(tab) {

    // if tab is already selected, return false
    if(tab.classList.contains("is-active"))
        return false;

    // remove is-active from all other tabs under the same parent
    for(let i = 0; i < tab.parentElement.children.length; i++) {
        tab.parentElement.children[i].classList.remove("is-active");
    }

    // add selection highlight to tab
    tab.classList.add("is-active");

    return true;
}

/**
 * Goes trough the motion of opening a tab:
 *  - selects it
 *  - fetches its content (view)
 *  - loads it into the provided tab container
 * 
 * @param {HTMLElement} targetTabElement - the tab element
 * @param {String} tabPath - the path to use to fetch the tab's content (view)
 * @param {HTMLElement} tabContainer - the container that is to receive the view content
 */
export async function openTab(targetTabElement, tabPath, tabContainer) {
    if(!selectTab(targetTabElement)) return;

    await fetch(tabPath)
    .then(response => {
        if (response.ok) return response.text();
    })
    .then(loginView => {
        loadView(tabContainer, loginView, tabPath);
    });
}

/**
 * TODO:
 * - Automate VIEW event management (Creation, dispatching) via naming convention
 * - Work on improving the createTab by getting rid of the allTabs parameter
 * - Improve the view init() setup by figuring out when a script is fully loaded, then calling
 *  the event
 */