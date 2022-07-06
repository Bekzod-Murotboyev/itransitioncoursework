package uz.itransition.collectin.service.collection;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.itransition.collectin.entity.collection.Collection;
import uz.itransition.collectin.entity.collection.Field;
import uz.itransition.collectin.entity.collection.FieldValue;
import uz.itransition.collectin.entity.collection.Item;
import uz.itransition.collectin.entity.tag.Tag;
import uz.itransition.collectin.exception.DataNotFoundException;
import uz.itransition.collectin.payload.request.collection.ItemRequest;
import uz.itransition.collectin.payload.request.collection.ItemUpdateRequest;
import uz.itransition.collectin.payload.request.field.FieldValueRequest;
import uz.itransition.collectin.payload.response.APIResponse;
import uz.itransition.collectin.payload.response.collection.FieldResponse;
import uz.itransition.collectin.payload.response.comment.CommentResponse;
import uz.itransition.collectin.payload.response.field.FieldValueListResponse;
import uz.itransition.collectin.payload.response.field.FieldValueResponse;
import uz.itransition.collectin.payload.response.item.ItemResponse;
import uz.itransition.collectin.payload.response.item.ItemResponseByTag;
import uz.itransition.collectin.payload.response.tag.TagResponse;
import uz.itransition.collectin.repository.CommentRepository;
import uz.itransition.collectin.repository.collection.CollectionRepository;
import uz.itransition.collectin.repository.collection.FieldRepository;
import uz.itransition.collectin.repository.collection.FieldValueRepository;
import uz.itransition.collectin.repository.collection.ItemRepository;
import uz.itransition.collectin.repository.tag.TagRepository;
import uz.itransition.collectin.service.CRUDService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static uz.itransition.collectin.exception.ResourceConstants.*;

@Service
@RequiredArgsConstructor
public class ItemService implements CRUDService<Long, APIResponse, ItemRequest, ItemUpdateRequest> {

    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;
    private final FieldRepository fieldRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final FieldValueRepository fieldValueRepository;
    private final CollectionRepository collectionRepository;

    @Override
    public APIResponse create(ItemRequest itemRequest) {
        List<Long> tagIdList = itemRequest.getTagIdList();
        List<Tag> tagList = tagRepository.findAllById(tagIdList);
        Collection collection = collectionRepository
                .findById(itemRequest.getCollectionId())
                .orElseThrow(() -> {
                    throw DataNotFoundException.of(COLLECTION_ENG, COLLECTION_RUS, String.valueOf(itemRequest.getCollectionId()));
                });
        Item item = itemRepository.save(new Item(itemRequest.getName(), collection, tagList));
        return APIResponse.success(fieldValueRepository.saveAll(getFieldValues(item, itemRequest.getFieldValues())));
    }

    @Override
    public APIResponse get(Long id) {
        return APIResponse.success(
                itemRepository.findById(id).orElseThrow(() -> {
                    throw DataNotFoundException.of(ITEM_ENG, ITEM_RUS, String.valueOf(id));
                })
        );
    }


    public List<Item> fullTextSearch(String text) {
        return itemRepository.fullTextSearch(text);
    }

    public APIResponse getItemResponse(Long id, Long userId) {
        Item item = itemRepository.findById(id).orElseThrow(() -> {
                    throw DataNotFoundException.of(ITEM_ENG, ITEM_RUS, String.valueOf(id));
                }
        );

        return APIResponse.success(map(item, userId));
    }

    public ItemResponse map(Item item, Long userId) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                List.of(modelMapper.map(item.getTags(), TagResponse[].class)),
                getFieldValueListResponse(item.getCollection().getId()),
                item.getLikes(),
                List.of(modelMapper.map(commentRepository.findAllByItemId(item.getId()), CommentResponse[].class)),
                itemRepository.existsByIdAndLikedUsers_Id(item.getId(), userId));
    }

    @Override
    public APIResponse modify(Long id, ItemUpdateRequest itemRequest) {
        Item item = itemRepository.findById(id).orElseThrow(() -> DataNotFoundException.of(ITEM_ENG, ITEM_RUS, String.valueOf(id)));
        List<Tag> tags = tagRepository.findAllById(itemRequest.getTags());
        List<Long> filedIds = itemRequest.getFieldValues().stream().map(f -> {
            return f.getFieldId();
        }).collect(Collectors.toList());
        List<Field> fields = fieldRepository.findAllById(filedIds);
        List<FieldValue> fieldValues = itemRequest.getFieldValues().stream().map(fieldValue -> {
            return new FieldValue(fieldValue.getValue(), item,
                    fields.stream().filter(f -> f.getId() == fieldValue.getFieldId()).findFirst().orElseThrow(
                            () -> DataNotFoundException.of(FIELD_ENG, FIELD_RUS, String.valueOf(fieldValue.getFieldId())))
            );
        }).collect(Collectors.toList());

        item.setName(itemRequest.getName());
        item.setTags(tags);
        itemRepository.save(item);
        fieldValueRepository.saveAll(fieldValues);

        return getItemResponse(item.getId(), item.getCollection().getUser().getId());
    }

    @Override
    public APIResponse delete(Long aLong) {
        return null;
    }


    public APIResponse getItemsByTagId(Long tagId) {
        Map<Long, List<ItemResponseByTag>> collect = fieldValueRepository.findAllByItem_Tags_Id(tagId).stream()
                .map(fieldValue -> {
                    return new ItemResponseByTag(fieldValue.getItem().getId(), fieldValue.getField().getName(), fieldValue.getValue());
                })
                .collect(Collectors.groupingBy(ItemResponseByTag::getItemId));
        return APIResponse.success(collect);
    }

    public APIResponse getItemsByCollectionId(Long collectionId) {
        return APIResponse.success(getFieldValueListResponse(collectionId));
    }

    private List<FieldValue> getFieldValues(Item item, List<FieldValueRequest> fieldValues) {
        return fieldValues
                .stream()
                .map((fvr) -> new FieldValue(fvr.getValue(),
                        item,
                        fieldRepository
                                .findById(fvr.getFieldId())
                                .orElseThrow(() -> DataNotFoundException.of(FIELD_ENG, FIELD_RUS, String.valueOf(fvr.getFieldId())))))
                .collect(Collectors.toList());
    }

    public APIResponse updateItemLike(Long userId, Long itemId, boolean isLiked) {
        if (isLiked) {
            itemRepository.insertItemLike(itemId, userId);
        } else {
            itemRepository.deleteItemLike(itemId, userId);
        }
        return APIResponse.success(true);
    }

    private FieldValueListResponse getFieldValueListResponse(Long collectionId) {
        FieldValueListResponse fieldList = new FieldValueListResponse();

        fieldList.setTypes(fieldRepository.findAllByCollection_Id(collectionId)
                .stream().map(elm -> modelMapper.map(elm, FieldResponse.class)).collect(Collectors.toList()));
        fieldList.setValues(fieldValueRepository
                .findAllByItem_CollectionId(collectionId)
                .stream()
                .map(item -> new FieldValueResponse(item.getValue(), item.getItem().getId(), item.getField().getId()))
                .collect(Collectors.groupingBy(FieldValueResponse::getItemId)));

        return fieldList;
    }
}
