const b = typeof browser !== "undefined" ? browser : chrome;

b.runtime.onMessage.addListener(function (message, sender, sendResponse) {
    if (message.action === "open_tab") {
        chrome.tabs.create({ url: message.url });
    }
});

b.runtime.onMessageExternal.addListener(function (message, sender) {
    if (!message) {
        return;
    }

    if (message.action === "open_search") {
        chrome.tabs.create({ url: "pages/search.html" });
    }
});