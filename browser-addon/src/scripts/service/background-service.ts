import { InternalMessage } from "../dto/internal-message";
import { BookmarkService } from "./bookmark-service";

export class BackgroundService {
    bookmarkService: BookmarkService;
    storage;
    token: string | undefined;

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

    public async onMessage(message: InternalMessage): Promise<any> {
        console.log(message)
        if (message.action === "open_tab") {
            chrome.tabs.create({ url: message.url });
            return Promise.resolve("OK");
        } else if (message.action == "add_bookmark" && message.url) {
            console.log("Adding bookmark");
            return await this.bookmarkService.addBookmark(message.url, this.token);

        } else {
            return Promise.reject(new Error("No such action!"));
        }
    }
}