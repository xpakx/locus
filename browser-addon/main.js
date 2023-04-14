const storage = typeof browser !== "undefined" ? browser.storage : chrome.storage;

document.addEventListener('DOMContentLoaded', function () {
  fetch(chrome.extension.getURL("main.html"))
    .then(response => response.text())
    .then(html => {
      var toolbar = document.createElement("div");
      toolbar.classList.add("locus-wzHfKco");
      toolbar.innerHTML = html;
      document.body.insertBefore(toolbar, document.body.firstChild);
    });

  fetch(chrome.extension.getURL("style.css"))
    .then(response => response.text())
    .then(css => {
      var style = document.createElement("style");
      style.innerHTML = css;
      document.head.insertAdjacentElement('beforeend', style);
    });
});

storage.onChanged.addListener(function (changes, areaName) {
  if (areaName === "local" && changes.token) {
    console.log(changes.token.newValue);
  }
});