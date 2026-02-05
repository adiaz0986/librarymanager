function getToken() {
    return localStorage.getItem("token");
}

function authHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + getToken()
    };
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "/login";
}

function requireAuth() {
    if (!getToken()) {
        window.location.href = "/login";
    }
}