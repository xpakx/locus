var bookmarkContainer: HTMLElement | null = null;
var bookmarkTemplate: string = "";

const testBookmarks = [
    {id: 1, name: "Bookmark 1", url: "https://example.com/1", date: ""},
    {id: 2, name: "Bookmark 2", url: "https://example.com/2", date: ""},
    {id: 3, name: "Bookmark 3", url: "https://example.com/3", date: ""},
    {id: 4, name: "Bookmark 4", url: "https://example.com/4", date: ""},
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
        var bookmarkName = bookmarkElem.querySelector(".bookmark-name");
        if(bookmarkName) {
            bookmarkName.innerHTML = bookmark.name;
        }
        var bookmarkDate = bookmarkElem.querySelector(".date");
        if(bookmarkDate) {
            bookmarkDate.innerHTML = bookmark.date;
        }
        var bookmarkLink = <HTMLAnchorElement> bookmarkElem.querySelector(".bookmark-link");
        if(bookmarkLink) {
            bookmarkLink.href = bookmark.url;
        }
        bookmarkContainer.insertBefore(bookmarkElem, bookmarkContainer.firstChild);
    }
}