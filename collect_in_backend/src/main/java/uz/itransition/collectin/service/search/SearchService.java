package uz.itransition.collectin.service.search;


import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.itransition.collectin.entity.collection.Collection;
import uz.itransition.collectin.entity.collection.Item;
import uz.itransition.collectin.entity.comment.Comment;
import uz.itransition.collectin.exception.DataNotFoundException;
import uz.itransition.collectin.payload.response.APIResponse;
import uz.itransition.collectin.payload.response.ElementType;
import uz.itransition.collectin.payload.response.Searchable;
import uz.itransition.collectin.payload.response.collection.CollectionSearchResponse;
import uz.itransition.collectin.payload.response.comment.CommentSearchResponse;
import uz.itransition.collectin.payload.response.item.ItemResponse;
import uz.itransition.collectin.payload.response.item.ItemSearchResponse;
import uz.itransition.collectin.repository.CommentRepository;
import uz.itransition.collectin.repository.collection.CollectionRepository;
import uz.itransition.collectin.repository.collection.ItemRepository;
import uz.itransition.collectin.service.collection.ItemService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static uz.itransition.collectin.exception.ResourceConstants.ITEM_ENG;

@Service
public class SearchService {
    private final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final CollectionRepository collectionRepository;

    private final ItemRepository itemRepository;

    private final CommentRepository commentRepository;

    private final ModelMapper modelMapper;

    private final ItemService itemService;

    public SearchService(CollectionRepository collectionRepository, ItemRepository itemRepository, CommentRepository commentRepository, ModelMapper modelMapper, ItemService itemService) {
        this.collectionRepository = collectionRepository;
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.itemService = itemService;
    }

    public APIResponse search(String text) {
        final List<Searchable> res = new ArrayList<>();
        res.addAll(searchCollection(text));
        res.addAll(searchItem(text));
        res.addAll(searchComment(text));
        return APIResponse.success(res);
    }

    private List<CollectionSearchResponse> searchCollection(String text) {
        final List<Collection> collections = collectionRepository.fullTextSearch(text);
        return Arrays.stream(modelMapper.map(collections, CollectionSearchResponse[].class)).collect(Collectors.toList());
    }

    private List<ItemSearchResponse> searchItem(String text) {
        final List<Item> items = itemRepository.fullTextSearch(text);
        return Arrays.stream(modelMapper.map(items, ItemSearchResponse[].class)).collect(Collectors.toList());
    }

    private List<CommentSearchResponse> searchComment(String text) {
        final List<Comment> comments = commentRepository.fullTextSearch(text);
        return Arrays.stream(modelMapper.map(comments, CommentSearchResponse[].class)).collect(Collectors.toList());
    }

    public APIResponse getElement(@NonNull String type, @NonNull Long id, Long userId) {

        ElementType element = ElementType.valueOf(type);
        if (element == ElementType.COLLECTION) {
            return getCollection(id);
        }
        return APIResponse.success(getItem(id, userId));
    }

    private ItemResponse getItem(Long id, Long userId) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> DataNotFoundException.of(ITEM_ENG, ITEM_ENG, String.valueOf(id))
        );

        return itemService.map(item, userId);
    }

    private APIResponse getCollection(Long id) {
        return itemService.getItemsByCollectionId(id);
    }


}
