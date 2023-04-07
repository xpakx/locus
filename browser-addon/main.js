document.addEventListener('DOMContentLoaded', function() {



  fetch(chrome.extension.getURL("main.html"))
  .then(response => response.text())
  .then(html => {
    var toolbar = document.createElement("div");
    toolbar.classList.add("locus");
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

