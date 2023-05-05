import { BookmarkSummary } from "./dto/bookmark-summary"
import { fetchAllBookmarks, searchBookmarksByString } from "./service/bookmark-service";

var bookmarkContainer: HTMLElement | null = null;
var bookmarkTemplate: string = "";
var token = undefined;

chrome.storage.local.get('token', function (result) {
    if (result.token) {
        token = result.token;
    }
});

document.addEventListener('DOMContentLoaded', function () {
    bookmarkContainer = document.querySelector(".bookmarks");
    fetch(chrome.runtime.getURL("pages/bookmark.html"))
        .then(response => response.text())
        .then(html => {
            bookmarkTemplate = html;
            getAllBookmarks();
        });
});

function showBookmarks(bookmarks: BookmarkSummary[]) {
    if (!bookmarkContainer) {
        return;
    }
    if(bookmarks.length === 0) {
        bookmarkContainer.innerHTML = "No bookmarks!";
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
    fetchAllBookmarks(token)
        .then((data: BookmarkSummary[]) => {
            showBookmarks(data);
        })
        .catch(error => {
            console.error('An error occurred:', error);
            showBookmarkError();
        });
}

function searchBookmarks(searchString: string) {
    searchBookmarksByString(searchString, token)
        .then((data: BookmarkSummary[]) => {
            showBookmarks(data);
        })
        .catch(error => {
            console.error('An error occurred:', error);
            showBookmarkError();
        });
}

function showBookmarkError() {
    if (!bookmarkContainer) {
        return;
    }
    bookmarkContainer.innerHTML = "Couldn't fetch bookmarks!";
}