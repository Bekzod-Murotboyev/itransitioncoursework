package uz.itransition.collectin.service.comment;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.itransition.collectin.entity.comment.Comment;
import uz.itransition.collectin.payload.request.comment.CommentCreationRequest;
import uz.itransition.collectin.payload.request.comment.CommentUpdateRequest;
import uz.itransition.collectin.payload.response.APIResponse;
import uz.itransition.collectin.payload.response.comment.CommentResponse;
import uz.itransition.collectin.repository.CommentRepository;
import uz.itransition.collectin.service.AbstractPageable;
import uz.itransition.collectin.service.CRUDService;

import java.util.List;

@Service
public class CommentService extends AbstractPageable<CommentRepository> implements CRUDService<Long, APIResponse, CommentCreationRequest, CommentUpdateRequest> {

    private final ModelMapper modelMapper;

    private final CommentRepository repository;

    public CommentService(ModelMapper modelMapper, CommentRepository repository) {
        this.modelMapper = modelMapper;
        this.repository = repository;
    }

    @Override
    public APIResponse create(CommentCreationRequest commentCreationRequest) {
        final Comment comment =
                Comment.of(
                        commentCreationRequest.getText(),
                        commentCreationRequest.getUserId(),
                        commentCreationRequest.getItemId()
                );
        return APIResponse.success(modelMapper.map(repository.save(comment), CommentResponse.class));
    }

    @Override
    public APIResponse get(Long id) {
        return APIResponse.success(repository.findById(id).get());
    }

    @Override
    public APIResponse modify(Long id, CommentUpdateRequest commentUpdateRequest) {
        return null;
    }

    @Override
    public APIResponse delete(Long id) {
        return null;
    }

    public List<CommentResponse> commentResponseConverter(List<Comment> comments) {
        return List.of(modelMapper.map(comments, CommentResponse[].class));
    }

    public APIResponse getCommentsByItemId(Long itemId) {
        return APIResponse.success(commentResponseConverter(repository.findAllByItemId(itemId)));
    }
}
