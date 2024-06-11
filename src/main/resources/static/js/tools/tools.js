export function loadView(viewParent, view, viewEvent, addToParent=false) {

    // adds the view HTML to the parent container
    viewParent.innerHTML = addToParent ? viewParent.innerHTML + view : view;

    // selects all view script tags
    const allScripts = document.querySelectorAll("script");
    const viewScripts = viewParent.querySelectorAll("script");

    // creates an element per script and adds it to the document
    viewScripts.forEach(script => {
        const scriptElement = document.createElement("script");
        scriptElement.type = script.type;
        scriptElement.src = script.src;

        document.body.appendChild(scriptElement);
    });

    // lets any viewEvent listeners know that the view and its scripts have been loaded
    viewParent.dispatchEvent(viewEvent);
}