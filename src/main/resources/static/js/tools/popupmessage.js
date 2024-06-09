// returns the message element
export function createMessage(msg) {
    
    let message = `<h3>${msg}</h3>`;
    
    return message;
}

// creates and adds the message element to given container, clears previous content
export function popMessage(msg, container) {

    let message = createMessage(msg);

    container.innerHTML = message;
}