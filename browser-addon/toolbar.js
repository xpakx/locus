const apiUri = "http://localhost:8000/api/v1"
const storage = typeof browser !== "undefined" ? browser.storage : chrome.storage;

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById("login-form").addEventListener("submit", function (event) {
        event.preventDefault();

        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;

        fetch(`${apiUri}/authenticate`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.token && data.username) {
                    storage.local.set({
                        token: data.token,
                        username: data.username
                    });
                }
            })
            .catch(error => console.error(error));
    });
});