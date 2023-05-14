import { APIMessage } from "../dto/api-message";

export class APIService {
    public onMessage(message: APIMessage) {
        if (!message) {
            return;
        }

        if (message.action === "open_search") {
            chrome.tabs.create({ url: "pages/search.html" });
        } else if (message.action == "close_toolbar") {
            browser.tabs.query({"currentWindow": true, "active": true})
                .then(tab => this.sendToActiveTab("close_toolbar", this.getActiveTabId(tab)))
        } else if (message.action == "open_toolbar") {
            browser.tabs.query({"currentWindow": true, "active": true})
                .then(tab => this.sendToActiveTab("open_toolbar", this.getActiveTabId(tab)))
        } else if (message.action == "toggle_toolbar") {
            console.log("External API call to toggle toolbar")
            browser.tabs.query({"currentWindow": true, "active": true})
                .then(tab => this.sendToActiveTab("toggle_toolbar", this.getActiveTabId(tab)))
        }
    }

    private getActiveTabId(tab: browser.tabs.Tab[]): number | undefined {
        return tab.length > 0 ? tab[0].id : undefined;
    }

    sendToActiveTab(message: string, id?: number) {
        console.log("Active tab: " + id)
        if (!id) {
            return;
        }
        browser.tabs.sendMessage(id, {action: message})
            .then(response => {
                console.log(response)
            });
    }
}