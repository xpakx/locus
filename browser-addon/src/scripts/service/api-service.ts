import { APIMessage } from "../dto/api-message";

export class APIService {
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