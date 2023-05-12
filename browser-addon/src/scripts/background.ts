import { APIService } from "./service/api-service";
import { BackgroundService } from "./service/background-service";

const b = typeof browser !== "undefined" ? browser : chrome;
const bgService = new BackgroundService();
const apiService = new APIService();

b.runtime.onMessage.addListener(bgService.onMessage);
b.runtime.onMessageExternal.addListener(apiService.onMessage);