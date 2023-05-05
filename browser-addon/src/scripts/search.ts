import { BookmarkSummary } from "./dto/bookmark-summary"

var bookmarkContainer: HTMLElement | null = null;
var bookmarkTemplate: string = "";
const apiUri = "http://localhost:8000/api/v1";
var token = null;

chrome.storage.local.get('token', function (result) {
    if (result.token) {
        token = result.token;
    }
});

const testBookmarks: BookmarkSummary[] = [
    { id: 1, name: "Bookmark 1", url: "https://example.com/1", date: "" },
    { id: 2, name: "Bookmark 2", url: "https://example.com/2", date: "" },
    { id: 3, name: "Bookmark 3", url: "https://example.com/3", date: "" },
    { id: 4, name: "Bookmark 4", url: "https://example.com/4", date: "" },
]

document.addEventListener('DOMContentLoaded', function () {
    bookmarkContainer = document.querySelector(".bookmarks");
    fetch(chrome.runtime.getURL("pages/bookmark.html"))
        .then(response => response.text())
        .then(html => {
            bookmarkTemplate = html;
            showBookmarks(testBookmarks);
            getAllBookmarks();
        });
});

function showBookmarks(bookmarks: BookmarkSummary[]) {
    if (!bookmarkContainer) {
        return;
    }
    for (let bookmark of bookmarks) {
        var bookmarkElem = document.createElement("div");
        bookmarkElem.classList.add("bookmark");
        bookmarkElem.innerHTML = bookmarkTemplate;
        var bookmarkName = bookmarkElem.querySelector(".bookmark-name");
        if (bookmarkName) {
            bookmarkName.innerHTML = bookmark.name;
        }
        var bookmarkDate = bookmarkElem.querySelector(".date");
        if (bookmarkDate) {
            bookmarkDate.innerHTML = bookmark.date;
        }
        var bookmarkLink = <HTMLAnchorElement>bookmarkElem.querySelector(".bookmark-link");
        if (bookmarkLink) {
            bookmarkLink.href = bookmark.url;
        }
        bookmarkContainer.insertBefore(bookmarkElem, bookmarkContainer.firstChild);
    }
}

function getAllBookmarks() {
    fetch(`${apiUri}/bookmarks/all`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        }
    })
        .then(response => response.json())
        .then((data: BookmarkSummary[]) => {
            showBookmarks(data);
        })
        .catch(error => {
            console.error('An error occurred:', error);
        });
}

function searchBookmarks(searchString: string) {
    fetch(`${apiUri}/bookmarks?` + new URLSearchParams({searchString: searchString}), {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        }
    })
        .then(response => response.json())
        .then((data: BookmarkSummary[]) => {
            showBookmarks(data);
        })
        .catch(error => {
            console.error('An error occurred:', error);
        });
}