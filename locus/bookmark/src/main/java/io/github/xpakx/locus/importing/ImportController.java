package io.github.xpakx.locus.importing;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/bookmarks/import")
@RequiredArgsConstructor
public class ImportController {
    private final ImportService service;

    @PostMapping("/html")
    @ResponseBody
    @Secured("MODERATOR")
    public boolean uploadFiles(@RequestParam("file") MultipartFile file, Principal principal) {
        return service.saveBookmarksFromHtml(file, principal.getName());
    }
}
