import { BookmarkSummary } from "../dto/bookmark-summary";

export class BookmarkService {
    private apiUri = "http://localhost:8000/api/v1";

    constructor() {
    }

    async fetchAllBookmarks(token?: string): Promise<BookmarkSummary[]> {
        const response = await fetch(`${this.apiUri}/bookmarks/all`, {
            method: 'GET',
            headers: this.getHeaders(token)
        });
        return await response.json();
    }

    private getHeaders(token: string | undefined): HeadersInit | undefined {
        return {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        };
    }

    async searchBookmarksByString(searchString: string, token?: string) {
        const params = new URLSearchParams({ searchString: searchString });
        const response = await fetch(`${this.apiUri}/bookmarks?` + params, {
            method: 'GET',
            headers: this.getHeaders(token)
        });
        return await response.json();
    }

    async checkBookmark(url: string, token?: string) {
        const params = new URLSearchParams({ url: url });
        const response = await fetch(
            `${this.apiUri}/bookmarks/check?` + params,
            {
                method: 'GET',
                headers: this.getHeaders(token)
            }
        )
        return await response.json();
    }


    async addBookmark(url: string, token?: string) {
        const response = await fetch(`${this.apiUri}/bookmarks`, {
            method: 'POST',
            headers: this.getHeaders(token),
            body: JSON.stringify({
                url: url
            })
        })
        return await response.json();
    }

    async deleteBookmark(id: number, token?: string) {
        const response = await fetch(`${this.apiUri}/bookmarks/${id}`, {
            method: 'DELETE',
            headers: this.getHeaders(token)
        })
        return await response.json();
    }
}
