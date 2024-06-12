/**
 * Validates a username
 * @param {String} username - the username string
 * @returns true if it passes validation
 */
export function validateUsername(username) {
    const minLength = 5;

    return username.length >= 5;
}

/**
 * Validates a password
 * @param {String} password - the password string
 * @returns true if it passes validation
 */
export function validatePassword(password) {
    const minLength = 5;

    return password.length >= 5;
}

// TODO: add some more validation, maybe characters too, spaces, ...
// Figure out how to create a single source of truth for these validations
// since we also do this on the server side, maybe use the database ?