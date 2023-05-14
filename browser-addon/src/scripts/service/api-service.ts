import { APIMessage } from "../dto/api-message";

export class APIService {
    public onMessage(message: APIMessage) {
        if (!message) {
            return;
        }

        if (message.action === "open_search") {
            chrome.tabs.create({ url: "pages/search.html" });
        } else if (message.action == "close_toolbar") {
            browser.tabs.getCurrent()
                .then(tab => this.closeToolbar(tab?.id))
        }
    }

    closeToolbar(id?: number) {
        if (!id) {
            return;
        }
        browser.tabs.sendMessage(id, {action: "close_toolbar"})
            .then(response => {
                console.log(response)
            });
    }
}