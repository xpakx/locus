var bookmarkContainer: HTMLElement | null = null;
var bookmarkTemplate: string = "";

const testBookmarks = [
    {id: 1, txt: "Bookmark 1"},
    {id: 2, txt: "Bookmark 2"},
    {id: 3, txt: "Bookmark 3"},
    {id: 4, txt: "Bookmark 4"},
]

document.addEventListener('DOMContentLoaded', function () {
    bookmarkContainer = document.querySelector(".bookmarks");
    fetch(chrome.runtime.getURL("pages/bookmark.html"))
      .then(response => response.text())
      .then(html => {
        bookmarkTemplate = html;
        showTestBookmarks();
    });
});
  
function showTestBookmarks() {
    if(!bookmarkContainer) {
        return;
    }
    for(let bookmark of testBookmarks) {
        var bookmarkElem = document.createElement("div");
        bookmarkElem.classList.add("bookmark");
        bookmarkElem.innerHTML = bookmarkTemplate;
        var bookmarkText = bookmarkElem.querySelector(".text");
        if(bookmarkText) {
            bookmarkText.innerHTML = bookmark.txt;
        }
        bookmarkContainer.insertBefore(bookmarkElem, bookmarkContainer.firstChild);
    }
}