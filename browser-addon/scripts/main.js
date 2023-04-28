const apiUri = "http://localhost:8000/api/v1";
const storage = typeof browser !== "undefined" ? browser.storage : chrome.storage;
const runtime = typeof browser !== "undefined" ? browser.runtime : chrome.runtime;
const url = window.location.href;
var token = null;
var heartIcon = null;
var bookmarked = false;
var toolbarDiv = null;
var sidebarDiv = null;
var markdownInput = null;

storage.local.get('token', function (result) {
  if (result.token) {
    token = result.token;
  }
});

document.addEventListener('DOMContentLoaded', function () {
  fetch(chrome.runtime.getURL("pages/main.html"))
    .then(response => response.text())
    .then(html => {
      var toolbar = document.createElement("div");
      toolbar.classList.add("locus-wzHfKco");
      toolbar.innerHTML = html;
      document.body.insertBefore(toolbar, document.body.firstChild);
      toolbarDiv = toolbar;

      heartIcon = toolbar.querySelector('.heart');
      if (token) {
        checkBookmark();
      }
      console.log(chrome);

      const heartButton = heartIcon.parentElement.parentElement;
      heartButton.addEventListener('click', function (event) {
        console.log('Bookmark button clicked');
        if (!bookmarked) {
          addBookmark();
        }
      });

      const searchButton = toolbar.querySelector('.search').parentElement.parentElement;
      searchButton.addEventListener('click', function (event) {
        console.log('Search button clicked');
        runtime.sendMessage({ action: "open_tab", url: "pages/search.html" });
      });

      const closeButton = toolbar.querySelector('.close').parentElement.parentElement;
      closeButton.addEventListener('click', function (event) {
        console.log('Close button clicked');
        toolbar.classList.add('hidden');
      });

      const annotationsButton = toolbar.querySelector('.highlight').parentElement.parentElement;
      annotationsButton.addEventListener('click', function (event) {
        console.log('Open fulll sidebar button clicked');
        showFullsidebar();
      });

    });


  fetch(chrome.runtime.getURL("pages/highlighter.html"))
    .then(response => response.text())
    .then(html => {

      var hl = document.createElement("div");
      hl.classList.add("locus-highlighter");
      hl.innerHTML = html;
      document.body.insertBefore(hl, document.body.firstChild);

      document.addEventListener("mouseup", function () {
        var selection = window.getSelection();
        if (selection.toString().length > 0) {
          var range = selection.getRangeAt(0);
          var rect = range.getBoundingClientRect();
          hl.style.left = window.pageXOffset + ((rect.left + rect.right) / 2) + "px";
          hl.style.top = window.pageYOffset + (rect.bottom + 10) + "px";

          hl.style.display = "block";
        } else {
          hl.style.display = "none";
        }
      });

      const highlightButton = hl.querySelector('.highlight').parentElement.parentElement;
      highlightButton.addEventListener('mousedown', function (event) {
        console.log('Highlight button clicked');
        var selection = window.getSelection();
        if (selection && selection.toString().length > 0) {
          highlightText(selection.toString());
        }
        event.preventDefault();
      });
    });

  fetch(chrome.runtime.getURL("styles/style.css"))
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

  fetch(chrome.runtime.getURL("pages/main-full.html"))
    .then(response => response.text())
    .then(html => {
      var sidebar = document.createElement("div");
      sidebar.classList.add("locus-full");
      sidebar.innerHTML = html;
      sidebar.style.display = 'none';
      document.body.insertBefore(sidebar, document.body.firstChild);
      sidebarDiv = sidebar;

      const closeFullSidebarButton = sidebar.querySelector('.close').parentElement.parentElement;
      closeFullSidebarButton.addEventListener('click', function (event) {
        console.log('Close sidebar button clicked');
        hideFullsidebar();
      });

      markdownInput = sidebar.querySelector(".markdown-input");
      markdownInput.contentEditable = true;
      markdownInput.setAttribute("spellcheck", "false");

      markdownInput.addEventListener("input", function () {
        console.log("Input changed");
        var caretPosition = getCaretPosition(markdownInput);
        var content = markdownInput.innerHTML;
        content = content.replace(/\*\*([^<].*?)\*\*/g, "<span class=\"md-char\">**</span><b>$1</b><span class=\"md-char\">**</span>");
        content = content.replace(/\_([^<].*?)\_/g, "<span class=\"md-char\">_</span><i>$1</i><span class=\"md-char\">_</span>");
        markdownInput.innerHTML = content;
        setCaretPosition(markdownInput, caretPosition);
      });


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

document.addEventListener('keydown', function (event) {
  if (event.altKey && event.key === 's' && !bookmarked) {
    console.log('Bookmarking called with keyboard');
    addBookmark();
  } else if (event.altKey && event.key === 'f') {
    console.log('Search called with keyboard');
    event.preventDefault();
    runtime.sendMessage({ action: "open_tab", url: "pages/search.html" });
  } else if (event.altKey && event.key === 'x') {
    console.log('Closing toolbar called with keyboard');
    toolbarDiv.classList.toggle('hidden');
  }
});

function highlightText(text) {
  fetch(`${apiUri}/annotations`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    },
    body: JSON.stringify({
      url: url,
      highlightedText: text
    })
  })
    .then(response => response.json())
    .then(data => {
      heartIcon.classList.toggle('fav', true);
      bookmarked = true;
      applyHighlight(text);
    })
    .catch(error => {
      console.error('An error occurred:', error);
    });
}

function applyHighlight(highlightedText) {
  for (let i = 0; i < document.body.children.length; i++) {
    let element = document.body.children[i];
    let text = element.innerHTML;
    let regexString = highlightedText
      .split('')
      .map(char => char.replace(/[/\-\\^$*+?.()|[\]{}]/g, '\\$&'))
      .map(char => `(?:<[^>]*>)*${char}`)
      .join('')
      .replace(/ /g, '\\s+');
    let re = new RegExp(regexString, "gi");
    let newText = text.replace(re, "<mark>$&</mark>");
    if (newText !== text) {
      element.innerHTML = newText;
    }
  }
}

function showFullsidebar() {
  toolbarDiv.style.display = "none";
  sidebarDiv.style.display = "flex";
}

function hideFullsidebar() {
  sidebarDiv.style.display = "none";
  toolbarDiv.style.display = "block";
}

function getCaretPosition(element) {
  var range = window.getSelection().getRangeAt(0);
  var preCaretRange = range.cloneRange();
  preCaretRange.selectNodeContents(element);
  preCaretRange.setEnd(range.endContainer, range.endOffset);
  return preCaretRange.toString().length;
}


function setCaretPosition(element, position) {
  console.log(position);
  var range = document.createRange();
  var sel = window.getSelection();
  var currentNode = element;
  var totalLength = 0;
  for (var i = 0; i < currentNode.childNodes.length; i++) {
    var childNode = currentNode.childNodes[i];
    var childLength = childNode.textContent.length;
    if (totalLength + childLength >= position) {
      if (childNode.nodeType == Node.TEXT_NODE) {
        range.setStart(childNode, position - totalLength);
        range.collapse(true);
        sel.removeAllRanges();
        sel.addRange(range);
        return;
      } else {
        setCaretPosition(childNode, position - totalLength);
        return;
      }
    }
    totalLength += childLength;
  }
}
