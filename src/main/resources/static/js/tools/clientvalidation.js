// client-side validation for username
export function validateUsername(username) {
    const minLength = 5;

    return username.length >= 5;
}

// client-side validation for password
export function validatePassword(password) {
    const minLength = 5;

    return password.length >= 5;
}