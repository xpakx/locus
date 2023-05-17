import { APIMessage } from "../dto/api-message";
import { BookmarkService } from "./bookmark-service";

export class APIService {
    bookmarkService: BookmarkService;
    token?: string;
    storage;

    constructor(bookmarkService: BookmarkService) {
        this.bookmarkService = bookmarkService;
        this.storage = typeof browser !== "undefined" ? browser.storage : chrome.storage;
        this.storage.local.get('token', function (result) {
            if (result.token) {
                this.token = result.token;
            }
        });
        this.storage.onChanged.addListener(function (changes, areaName) {
            if (areaName === "local" && changes.token) {
                if (changes.token.newValue) {
                    this.token = changes.token.newValue;
                }
            }
        });
    }

    public onMessage(message: APIMessage) {
        if (!message) {
            return;
        }

        if (message.action === "open_search") {
            chrome.tabs.create({ url: "pages/search.html" });
        } else if (message.action == "close_toolbar") {
            this.getActiveTabId()
                .then(id => this.sendToTab("close_toolbar", id));
        } else if (message.action == "open_toolbar") {
            this.getActiveTabId()
                .then(id => this.sendToTab("open_toolbar", id));
        } else if (message.action == "toggle_toolbar") {
            console.log("External API call to toggle toolbar")
            this.getActiveTabId()
                .then(id => this.sendToTab("toggle_toolbar", id));
        } else if (message.action == "close_sidebar") {
            this.getActiveTabId()
                .then(id => this.sendToTab("close_sidebar", id));
        } else if (message.action == "open_sidebar") {
            this.getActiveTabId()
                .then(id => this.sendToTab("open_sidebar", id));
        } else if (message.action == "toggle_sidebar") {
            this.getActiveTabId()
                .then(id => this.sendToTab("toggle_sidebar", id));
        } else if (message.action == "add_bookmark" && message.url) {
            this.bookmarkService.addBookmark(message.url, this.token);
        } else if (message.action == "delete_bookmark" && message.id) {
            this.bookmarkService.deleteBookmark(message.id, this.token);
        } else if (message.action == "add_highlight") {
            this.getActiveTabId()
                .then(id => this.sendToTab("add_highlight", id));
        }

    }

    private getActiveTabId(): Promise<number | undefined> {
        return browser.tabs.query({ "currentWindow": true, "active": true })
            .then((tabs) => this.getTabId(tabs));
    }

    private getTabId(tab: browser.tabs.Tab[]): number | undefined {
        return tab.length > 0 ? tab[0].id : undefined;
    }

    sendToTab(message: string, id?: number) {
        console.log("Active tab: " + id)
        if (!id) {
            return;
        }
        browser.tabs.sendMessage(id, { action: message })
            .then(response => {
                console.log(response)
            });
    }
}