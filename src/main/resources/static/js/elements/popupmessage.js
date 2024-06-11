// returns the message element
export function errorMessage(message, container) {
    
    container.innerHTML =
    `<div class="flex-container-center">
        <article class="message is-danger w50">
            <div class="message-body">
            ${message}
            </div>
        </article>
    </div>`;
}