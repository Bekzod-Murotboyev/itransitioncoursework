package uz.itransition.collectin.controller.collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.itransition.collectin.controller.AbstractCRUDController;
import uz.itransition.collectin.payload.request.collection.CollectionRequest;
import uz.itransition.collectin.service.collection.CollectionService;

import static uz.itransition.collectin.controller.ControllerUtils.COLLECTION_URI;


@RestController
@RequestMapping(COLLECTION_URI)
public class CollectionController extends AbstractCRUDController<CollectionService, Long, CollectionRequest, CollectionRequest> {

    protected CollectionController(CollectionService service) {
        super(service);
    }

    @GetMapping
    public ResponseEntity<?> getList(
        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "20") Integer size,
        @RequestParam(name = "order", required = false, defaultValue = "DESC") String order,
        @RequestParam(name = "categories", required = false) String[] categories
    ){
        return ResponseEntity.ok(service.getList(page, size, order, categories));
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestCollections(){
        return ResponseEntity.ok(service.getLatestCollections());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserCollections(@PathVariable("userId") Long id){
        return ResponseEntity.ok(service.getUserCollections(id));
    }
}
