package ru.synthiq.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.synthiq.service.FileService;

@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;


    @GetMapping("/get-doc")
    public ResponseEntity<?> getDoc(@RequestParam("id") String id) {
        // TODO для формирования badRequest добавить ControllerAdvice
        var doc = fileService.getDocument(id);
        if (doc == null) {
            return ResponseEntity.badRequest().build();
        }
        var binaryContent = doc.getBinaryContent();

        var fileSystemResource = fileService.getFileSystemResource(binaryContent);

        if (fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType()))
                .header("Content-Disposition", "attachment; filename=" + doc.getDocName())
                .body(fileSystemResource);
    }

    @GetMapping("/get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
        // TODO для формирования badRequest добавить ControllerAdvice
        var photo = fileService.getPhoto(id);
        if (photo == null) {
            return ResponseEntity.badRequest().build();
        }
        var binaryContent = photo.getBinaryContent();

        var fileSystemResource = fileService.getFileSystemResource(binaryContent);

        if (fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header("Content-Disposition", "attachment;")
                .body(fileSystemResource);
    }
}
