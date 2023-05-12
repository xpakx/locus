import { APIMessage } from "../dto/api-message";

export class APIService {
    public onMessage(message: APIMessage) {
        if (!message) {
            return;
        }

        if (message.action === "open_search") {
            chrome.tabs.create({ url: "pages/search.html" });
        }
    }
}