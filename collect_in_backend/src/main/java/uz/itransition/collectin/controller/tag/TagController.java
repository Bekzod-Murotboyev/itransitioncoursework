package uz.itransition.collectin.controller.tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.itransition.collectin.controller.AbstractCRUDController;
import uz.itransition.collectin.payload.request.tag.TagRequest;
import uz.itransition.collectin.service.tag.TagService;

import static uz.itransition.collectin.controller.ControllerUtils.TAG_URI;

@RestController
@RequestMapping(TAG_URI)
public class TagController extends AbstractCRUDController<TagService, Long, TagRequest, TagRequest> {

    private final TagService tagService;

    protected TagController(TagService service) {
        super(service);
        this.tagService = service;
    }

    @GetMapping
    public ResponseEntity<?> getTagList() {
        return ResponseEntity.ok(tagService.getList());
    }
}
