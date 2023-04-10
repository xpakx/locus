package io.github.xpakx.locus.importing;

import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmarks/import")
@RequiredArgsConstructor
public class ImportController {
    private final ImportService service;

    @PostMapping("/html")
    @ResponseBody
    public boolean uploadFiles(@RequestParam("file") MultipartFile file, Principal principal) {
        return service.saveBookmarksFromHtml(file, principal.getName());
    }
    
    @PostMapping("/json")
    @ResponseBody
    public boolean uploadBookmarks(@RequestBody List<BookmarkRequest> request, Principal principal) {
        return service.saveBookmarksFromJson(request, principal.getName());
    }
}
