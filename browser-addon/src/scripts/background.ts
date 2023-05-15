import { AnnotationService } from "./service/annotation-service";
import { APIService } from "./service/api-service";
import { BackgroundService } from "./service/background-service";
import { BookmarkService } from "./service/bookmark-service";

const b = typeof browser !== "undefined" ? browser : chrome;
const bookmarkService = new BookmarkService();
const annotationService = new AnnotationService();
const bgService = new BackgroundService(bookmarkService, annotationService);
const apiService = new APIService(bookmarkService);

b.runtime.onMessage.addListener((message, sender) => bgService.onMessage(message));
b.runtime.onMessageExternal.addListener((message) => apiService.onMessage(message));