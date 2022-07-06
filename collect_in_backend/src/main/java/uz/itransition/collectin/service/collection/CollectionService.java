package uz.itransition.collectin.service.collection;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.itransition.collectin.entity.User;
import uz.itransition.collectin.entity.collection.Collection;
import uz.itransition.collectin.entity.collection.Field;
import uz.itransition.collectin.entity.collection.FieldValue;
import uz.itransition.collectin.entity.collection.Topic;
import uz.itransition.collectin.exception.DataNotFoundException;
import uz.itransition.collectin.exception.user.UserNotFoundException;
import uz.itransition.collectin.payload.request.collection.CollectionRequest;
import uz.itransition.collectin.payload.request.field.FieldRequest;
import uz.itransition.collectin.payload.response.APIResponse;
import uz.itransition.collectin.payload.response.collection.CollectionResponse;
import uz.itransition.collectin.payload.response.collection.FieldResponse;
import uz.itransition.collectin.repository.UserRepository;
import uz.itransition.collectin.repository.collection.*;
import uz.itransition.collectin.service.CRUDService;
import uz.itransition.collectin.util.CSVUtil;

import java.util.List;
import java.util.stream.Collectors;

import static uz.itransition.collectin.exception.ResourceConstants.*;

@Service
@RequiredArgsConstructor
public class CollectionService implements CRUDService<Long, APIResponse, CollectionRequest, CollectionRequest> {

    private final CollectionRepository collectionRepository;
    private final TopicRepository topicRepository;
    private final FieldRepository fieldRepository;

    private final FieldValueRepository fieldValueRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private final static String DEFAULT_SORT_FIELD = "creationDate";

    @Override
    public APIResponse create(CollectionRequest collectionRequest) {
        User user = userRepository.findById(collectionRequest.getUserId()).orElseThrow(() -> {
            throw UserNotFoundException.of(String.valueOf(collectionRequest.getUserId()));
        });
        Topic topic = topicRepository.findById(collectionRequest.getTopicId()).orElseThrow(() -> {
            throw DataNotFoundException.of(TOPIC_ENG,TOPIC_RUS, String.valueOf(collectionRequest.getTopicId()));
        });
        Collection collection = collectionRepository.save(
                new Collection()
                .setName(collectionRequest.getName())
                .setDescription(collectionRequest.getDescription())
                .setImageUrl(collectionRequest.getImageUrl())
                .setUser(user)
                .setTopic(topic));

        return APIResponse.success(getCollectionResponse(collection,
                collectionRequest.getCustomFields()));
    }

    @Override
    public APIResponse get(Long id) {
        final Collection collection = collectionRepository.findById(id).orElseThrow(() -> {
            throw DataNotFoundException.of(COLLECTION_ENG,COLLECTION_RUS, String.valueOf(id));
        });
        final FieldResponse[] fieldList = modelMapper.map(fieldRepository.findAllByCollection(collection), FieldResponse[].class);
        final CollectionResponse response = modelMapper.map(collection, CollectionResponse.class);

        response.setFields(fieldList);
        return APIResponse.success(response);
    }

    public APIResponse getList(int page, int size, String order, String[] categories) {
        categories = (categories == null || categories.length == 0) ? new String[]{DEFAULT_SORT_FIELD} : categories;
        Page<Collection> list = collectionRepository
                .findAll(PageRequest.of(page, size, Sort.Direction.valueOf(order), categories));
        return APIResponse.success(getCollectionResponseList(list.getContent()));
    }

    @Override
    public APIResponse modify(Long id, CollectionRequest collectionRequest) {
        User user = userRepository.findById(collectionRequest.getUserId()).orElseThrow(() -> {
            throw UserNotFoundException.of(String.valueOf(collectionRequest.getUserId()));
        });
        Topic topic = topicRepository.findById(collectionRequest.getTopicId()).orElseThrow(() -> {
            throw DataNotFoundException.of(TOPIC_ENG,TOPIC_RUS, String.valueOf(collectionRequest.getTopicId()));
        });
        Collection collection = collectionRepository.findById(id).orElseThrow(() -> {
            throw DataNotFoundException.of(COLLECTION_ENG,COLLECTION_RUS, String.valueOf(id));
        });
        collection.setImageUrl(collectionRequest.getImageUrl());
        collection.setDescription(collection.getDescription());
        collection.setName(collectionRequest.getName());
        collection.setTopic(topic);
        collection.setUser(user);
        collection = collectionRepository.save(collection);
        return APIResponse.success(getCollectionResponse(collection,
                collectionRequest.getCustomFields()));
    }

    @Override
    public APIResponse delete(Long id) {
        collectionRepository.deleteById(id);
        fieldRepository.deleteAllByCollection_Id(id);
        itemRepository.deleteAllByCollection_Id(id);
        return APIResponse.success(true);
    }

    public APIResponse getLatestCollections() {
        List<Long> collectionIds = fieldRepository.findTopCollectionIds();
        return APIResponse.success(getCollectionResponseList(collectionRepository.findAllById(collectionIds)));
    }

    public APIResponse getUserCollections(Long userId) {
        List<Collection> collectionList = collectionRepository.findAllByUser_Id(userId);
        return APIResponse.success(getCollectionResponseList(collectionList));
    }

    private CollectionResponse getCollectionResponse(Collection collection, FieldRequest[] fieldRequests) {
        CollectionResponse response = modelMapper.map(collection, CollectionResponse.class);
        response.setFields(saveCollectionFields(collection, fieldRequests));
        return response;
    }

    private FieldResponse[] saveCollectionFields(Collection collection, FieldRequest[] fieldRequests) {
        List<Field> fields = List.of(modelMapper.map(fieldRequests, Field[].class));
        fields.forEach(field -> field.setCollection(collection));
        fields = fieldRepository.saveAll(fields);
        return modelMapper.map(fields, FieldResponse[].class);
    }

    private List<CollectionResponse> getCollectionResponseList(List<Collection> collectionList) {
        return collectionList
                .stream()
                .map(collection -> {
                    CollectionResponse response = modelMapper.map(collection, CollectionResponse.class);
                    return response;
                }).collect(Collectors.toList());
    }

    public Resource loadCSVFile(long collectionId, String lang) {
        Collection collection = collectionRepository.findById(collectionId).orElseThrow(() -> {
            throw DataNotFoundException.of(COLLECTION_ENG,COLLECTION_RUS, String.valueOf(collectionId));
        });
        List<Field> fields = fieldRepository.findAllByCollectionIdOrderOrderByName(collectionId);
        List<FieldValue> fieldValues = fieldValueRepository.findAllByCollectionIdOrderItemIdAndByFieldName(collectionId);
        String filePath = CSVUtil.ofCollection(lang, collection, fields, fieldValues);
        return CSVUtil.load(filePath);
    }
}
