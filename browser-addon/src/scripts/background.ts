import { APIMessage } from "./dto/api-message";
import { BackgroundService } from "./service/background-service";

const b = typeof browser !== "undefined" ? browser : chrome;
const bgService = new BackgroundService();

b.runtime.onMessage.addListener(bgService.onMessage);

b.runtime.onMessageExternal.addListener(function (message: APIMessage, sender) {
    if (!message) {
        return;
    }

    if (message.action === "open_search") {
        chrome.tabs.create({ url: "pages/search.html" });
    }
});