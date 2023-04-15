const apiUri = "http://localhost:8000/api/v1";
const storage = typeof browser !== "undefined" ? browser.storage : chrome.storage;
const url = window.location.href;
var token = null;
var heartIcon = null;
var bookmarked = false;

storage.local.get('token', function (result) {
  if (result.token) {
    token = result.token;
  }
});

document.addEventListener('DOMContentLoaded', function () {
  fetch(chrome.extension.getURL("main.html"))
    .then(response => response.text())
    .then(html => {
      var toolbar = document.createElement("div");
      toolbar.classList.add("locus-wzHfKco");
      toolbar.innerHTML = html;
      document.body.insertBefore(toolbar, document.body.firstChild);

      heartIcon = toolbar.querySelector('.heart');
      if (token) {
        checkBookmark();
      }

      const heartButton = heartIcon.parentElement.parentElement;
      heartButton.addEventListener('click', function (event) {
        console.log('Bookmark button clicked');
        if(!bookmarked) {
          addBookmark();
        }
      });

      const searchButton = toolbar.querySelector('.search').parentElement.parentElement;
      searchButton.addEventListener('click', function (event) {
        console.log('Search button clicked');
      });

      const closeButton = toolbar.querySelector('.close').parentElement.parentElement;
      closeButton.addEventListener('click', function (event) {
        console.log('Close button clicked');
        toolbar.classList.add('hidden');
      });
      
    });

  fetch(chrome.extension.getURL("style.css"))
    .then(response => response.text())
    .then(css => {
      var style = document.createElement("style");
      style.innerHTML = css;
      document.head.insertAdjacentElement('beforeend', style);
    });



  storage.onChanged.addListener(function (changes, areaName) {
    if (areaName === "local" && changes.token) {
      console.log(changes.token.newValue);
      if (changes.token.newValue) {
        token = changes.token.newValue;
        checkBookmark();
      }
    }
  });
});

function checkBookmark() {
  const headers = {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + token
  };
  const options = {
    method: 'GET',
    headers: headers
  };

  fetch(`${apiUri}/bookmarks/check?url=${encodeURIComponent(url)}`, options)
    .then(response => response.json())
    .then(data => {
      const heartIcon = document.querySelector('.heart')[0];
      console.log(heartIcon);
      heartIcon.classList.toggle('fav', data.value);
      bookmarked = data.value;
    })
    .catch(error => {
      console.error('An error occurred:', error);
    });
}

function addBookmark() {
  fetch(`${apiUri}/bookmarks`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    },
    body: JSON.stringify({
      url: url
  })
  })
    .then(response => response.json())
    .then(data => {
      heartIcon.classList.toggle('fav', true);
      bookmarked = true;
    })
    .catch(error => {
      console.error('An error occurred:', error);
    });
}