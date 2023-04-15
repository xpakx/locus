const b = typeof browser !== "undefined" ? browser : chrome;

b.runtime.onMessage.addListener(function (message, sender, sendResponse) {
    if (message.action === "open_tab") {
        chrome.tabs.create({ url: message.url });
    }
});
