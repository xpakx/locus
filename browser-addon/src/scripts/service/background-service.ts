import { InternalMessage } from "../dto/internal-message";

export class BackgroundService {
    public onMessage(message: InternalMessage) {
        if (message.action === "open_tab") {
            chrome.tabs.create({ url: message.url });
        }
    }
}