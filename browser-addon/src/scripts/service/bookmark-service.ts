import { BookmarkSummary } from "../dto/bookmark-summary";

export class BookmarkService {
    private apiUri = "http://localhost:8000/api/v1";

    constructor() {
    }

    async fetchAllBookmarks(token?: string): Promise<BookmarkSummary[]> {
        const response = await fetch(`${this.apiUri}/bookmarks/all`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        });
        return await response.json();
    }

    async searchBookmarksByString(searchString: string, token?: string) {
        const response = await fetch(`${this.apiUri}/bookmarks?` + new URLSearchParams({ searchString: searchString }), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        });
        return await response.json();
    }
}
