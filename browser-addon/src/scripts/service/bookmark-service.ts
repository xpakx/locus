import { BookmarkSummary } from "../dto/bookmark-summary";

const apiUri = "http://localhost:8000/api/v1";

export function fetchAllBookmarks(token?: string): Promise<BookmarkSummary[]> {
    return fetch(`${apiUri}/bookmarks/all`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        }
    })
        .then(response => response.json());
}

export function searchBookmarksByString(searchString: string, token?: string) {
    return fetch(`${apiUri}/bookmarks?` + new URLSearchParams({searchString: searchString}), {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        }
    })
        .then(response => response.json());
}