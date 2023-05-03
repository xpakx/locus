
document.addEventListener('DOMContentLoaded', function () {
    fetch(chrome.runtime.getURL("pages/youtube.html"))
      .then(response => response.text())
      .then(html => {
        const controls = document.querySelector(".ytp-left-controls")
        var ytButton = document.createElement("div");
        ytButton.classList.add("locus-yt");
        ytButton.innerHTML = html;
        controls.insertBefore(ytButton, controls.lastChild);  
  
  
  
      });
  });