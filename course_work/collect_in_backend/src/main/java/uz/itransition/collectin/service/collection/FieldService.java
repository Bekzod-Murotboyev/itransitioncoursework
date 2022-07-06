package uz.itransition.collectin.service.collection;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.itransition.collectin.entity.collection.Collection;
import uz.itransition.collectin.entity.collection.Field;
import uz.itransition.collectin.exception.DataNotFoundException;
import uz.itransition.collectin.payload.request.field.FieldRequest;
import uz.itransition.collectin.payload.response.APIResponse;
import uz.itransition.collectin.repository.collection.CollectionRepository;
import uz.itransition.collectin.repository.collection.FieldRepository;
import uz.itransition.collectin.service.CRUDService;

import static uz.itransition.collectin.exception.ResourceConstants.*;

@Service
@RequiredArgsConstructor
public class FieldService implements CRUDService<Long, APIResponse, FieldRequest, FieldRequest> {

    private final CollectionRepository collectionRepository;
    private final FieldRepository fieldRepository;
    private final ModelMapper modelMapper;

    @Override
    public APIResponse create(FieldRequest fieldRequest) {
        Collection collection = collectionRepository
                .findById(fieldRequest.getId())
                .orElseThrow(() -> {
                    throw DataNotFoundException.of(COLLECTION_ENG, COLLECTION_RUS, String.valueOf(fieldRequest.getId()));
                });
        Field field = modelMapper.map(fieldRequest, Field.class);
        field.setCollection(collection);
        fieldRepository.save(field);
        return APIResponse.success(true);
    }

    @Override
    public APIResponse get(Long id) {
        return APIResponse.success(
                fieldRepository
                        .findById(id)
                        .orElseThrow(() -> {
                            throw DataNotFoundException.of(FIELD_ENG, FIELD_RUS, String.valueOf(id));
                        })
        );
    }

    public APIResponse getAllFieldsByCollectionId(Long id) {
        Collection collection = collectionRepository
                .findById(id)
                .orElseThrow(() -> {
                    throw DataNotFoundException.of(COLLECTION_ENG, COLLECTION_RUS, String.valueOf(id));
                });
        return APIResponse.success(fieldRepository.findAllByCollection(collection));
    }

    @Override
    public APIResponse modify(Long id, FieldRequest fieldRequest) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> {
                    throw DataNotFoundException.of(FIELD_ENG, FIELD_RUS, String.valueOf(id));
                });
        modelMapper.map(fieldRequest, field);
        return APIResponse.success(field);
    }

    @Override
    public APIResponse delete(Long id) {
        fieldRepository.deleteById(id);
        return APIResponse.success(true);
    }
}
