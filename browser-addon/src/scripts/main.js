import { AnnotationService } from "./service/annotation-service";
import { HighlightService } from "./service/highlight-service";

const storage = typeof browser !== "undefined" ? browser.storage : chrome.storage;
const runtime = typeof browser !== "undefined" ? browser.runtime : chrome.runtime;
const url = window.location.href;
var token = null;
var heartIcon = null;
var bookmarked = false;
var toolbarDiv = null;
var sidebarDiv = null;
var markdownInput = null;
var bookmarkId = null;
var annotationService = new AnnotationService();
var highlightService = new HighlightService();

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
        fetchAnnotations();
      }
      console.log(chrome);

      const heartButton = heartIcon.parentElement.parentElement;
      heartButton.addEventListener('click', function (event) {
        console.log('Bookmark button clicked');
        if (!bookmarked) {
          addBookmark();
        } else if (bookmarkId != null) {
          deleteBookmark(bookmarkId);
        }
      });

      const searchButton = toolbar.querySelector('.search').parentElement.parentElement;
      searchButton.addEventListener('click', function (event) {
        console.log('Search button clicked');
        runtime.sendMessage({ action: "open_tab", url: "pages/search.html" })
          .then((response) => console.log(response));
      });

      const closeButton = toolbar.querySelector('.close').parentElement.parentElement;
      closeButton.addEventListener('click', function (event) {
        console.log('Close button clicked');
        toolbar.classList.add('hidden');
      });

      const annotationsButton = toolbar.querySelector('.highlight').parentElement.parentElement;
      annotationsButton.addEventListener('click', function (event) {
        console.log('Open full sidebar button clicked');
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
          const range = selection.getRangeAt(0);
          const startContainer = getPathTo(range.startContainer);
          const endContainer = getPathTo(range.endContainer);
          const startOffset = range.startOffset;
          const endOffset = range.endOffset;
          range.collapse();
          highlightText(selection.toString(), startContainer, endContainer, startOffset, endOffset);
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
        content = content.replace(/(<([^>]+)>)/g, "");
        content = content.replace(/\*\*([^<].*?)\*\*/g, "<span class=\"md-char\">**</span><b>$1</b><span class=\"md-char\">**</span>");
        content = content.replace(/\_([^<].*?)\_/g, "<span class=\"md-char\">_</span><i>$1</i><span class=\"md-char\">_</span>");
        markdownInput.innerHTML = content;
        setCaretPosition(markdownInput, caretPosition);
      });

      markdownInput.addEventListener("focus", function () {
        markdownInput.parentElement.classList.add("active");
      });

      markdownInput.addEventListener("blur", function () {
        //markdownInput.parentElement.classList.remove("active");
      });

      let sendBtn = markdownInput.parentElement.querySelector(".send-btn");
      sendBtn.addEventListener('click', function (event) {
        console.log('Send annotation button clicked');
        var text = markdownInput.innerHTML;
        text = text.replace(/(<([^>]+)>)/g, "");
        addPageAnnotation(text);
      });

    });
});

function checkBookmark() {
  runtime.sendMessage({action: "check_bookmark", url: url})
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
  runtime.sendMessage({ action: "add_bookmark", url: url })
  .then(
    (data) => {
      console.log("Data: " + data);
      heartIcon.classList.toggle('fav', true);
      bookmarked = true;
      bookmarkId = data.id;
    }
  )
  .catch(error => console.log(error));
}

function deleteBookmark(id) {
  runtime.sendMessage({ action: "delete_bookmark", id: id})
    .then(data => {
      heartIcon.classList.toggle('fav', false);
      bookmarked = false;
      bookmarkId = null;
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

function highlightText(text, startContainer, endContainer, startOffset, endOffset) {
  highlightService.prepareHighlight(startContainer, endContainer, startOffset, endOffset);
  annotationService.addAnnotation(url, null, text, startContainer, startOffset, endContainer, endOffset, token)
    .then(data => {
      prepareHighlight(startContainer, endContainer, startOffset, endOffset);
    })
    .catch(error => {
      console.error('An error occurred:', error);
    });
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

function getPathTo(node) {
  const path = [];
  const nodeIndex = Array.from(node.parentElement.childNodes).indexOf(node);;
  path.unshift(`${nodeIndex}`);
  let currentElem = node.parentElement;
  while (currentElem !== document.body) {
    const tagName = currentElem.tagName.toLowerCase();
    const index = Array.from(currentElem.parentElement.children)
      .filter(node => node.nodeName === tagName.toUpperCase())
      .indexOf(currentElem);
    path.unshift(`${tagName}{${index}}`);
    currentElem = currentElem.parentElement;
  }
  return path.join('/');
}

function addPageAnnotation(pageAnnotation) {
  annotationService.addAnnotation(url, pageAnnotation, null, null, null, null, null, token)
    .then(data => {
      // TODO: show annotation in sidebar
    })
    .catch(error => {
      console.error('An error occurred:', error);
    });
}

function fetchAnnotations() {
  if (!url) {
    return;
  }
  annotationService.fetchAllAnnotations(url, token)
    .then(data => {
      for (let annotation of data) {
        prepareHighlight(data.startElement, data.endElement, data.selectionStart, data.selectionEnd)
      }
    })
    .catch(error => {
      console.error('An error occurred:', error);
    });
}