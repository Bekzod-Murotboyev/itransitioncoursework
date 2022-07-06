package uz.itransition.collectin.controller.file;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.itransition.collectin.service.collection.CollectionService;
import uz.itransition.collectin.util.TaskManager;

import static uz.itransition.collectin.controller.ControllerUtils.COLLECTION_URI;


@RestController
@RequestMapping(COLLECTION_URI + "/csv")
public class CSVController {

    private final static Logger log = LoggerFactory.getLogger(CSVController.class);

    private final TaskManager cleaner;

    private final CollectionService collectionService;


    public CSVController(TaskManager cleaner, CollectionService collectionService) {
        this.cleaner = cleaner;
        this.collectionService = collectionService;
    }

    @SneakyThrows
    @GetMapping()
    public ResponseEntity<?> createCSV(
            @RequestParam(name = "collection_id") Long collectionId,
            @RequestParam(name = "lang") String lang
    ) {
        Resource resource = collectionService.loadCSVFile(collectionId, lang);
        try {
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            cleaner.scheduleForDeletion(resource.getFile().toPath(), 10);
        }
        return ResponseEntity.notFound().build();
    }
}
