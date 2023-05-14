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
                .then(tab => this.sendToActiveTab("close_toolbar", tab?.id))
        } else if (message.action == "open_toolbar") {
            browser.tabs.getCurrent()
                .then(tab => this.sendToActiveTab("open_toolbar", tab?.id))
        } else if (message.action == "toggle_toolbar") {
            browser.tabs.getCurrent()
                .then(tab => this.sendToActiveTab("toggle_toolbar", tab?.id))
        }
    }

    sendToActiveTab(message: string, id?: number) {
        if (!id) {
            return;
        }
        browser.tabs.sendMessage(id, {action: message})
            .then(response => {
                console.log(response)
            });
    }
}