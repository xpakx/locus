import { InternalMessage } from "../dto/internal-message";
import { AnnotationService } from "./annotation-service";
import { BookmarkService } from "./bookmark-service";

export class BackgroundService {
    bookmarkService: BookmarkService;
    annotationService: AnnotationService;
    storage;
    token: string | undefined;

    constructor(bookmarkService: BookmarkService, annotationService: AnnotationService) {
        this.bookmarkService = bookmarkService;
        this.annotationService = annotationService;
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
        } else if (message.action == "delete_bookmark" && message.id) {
            console.log("Deleting bookmark");
            return await this.bookmarkService.deleteBookmark(message.id, this.token);
        } else if (message.action == "add_annotation" && message.annotation && message.url) {
            console.log("Adding annotation");
            return await this.annotationService.addAnnotation(
               message.url,
               message.annotation.pageAnnotation,
               message.annotation.text,
               message.annotation.startElement,
               message.annotation.selectionStart,
               message.annotation.endElement,
               message.annotation.selectionEnd,
               this.token);
        }  else if (message.action == "check_bookmark" && message.url) {
            console.log("Checking if page is bookmarked");
            return await this.bookmarkService.checkBookmark(message.url, this.token);
        }  else if (message.action == "fetch_annotations" && message.url) {
            console.log("Checking if page is bookmarked")
            return await this.annotationService.fetchAllAnnotations(message.url, this.token);
        } else {
            return Promise.reject(new Error("No such action!"));
        }
    }
}