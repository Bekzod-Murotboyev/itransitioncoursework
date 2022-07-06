package uz.itransition.collectin.service.collection;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.itransition.collectin.entity.collection.Topic;
import uz.itransition.collectin.exception.DataNotFoundException;
import uz.itransition.collectin.payload.request.collection.TopicRequest;
import uz.itransition.collectin.payload.response.APIResponse;
import uz.itransition.collectin.payload.response.collection.TopicResponse;
import uz.itransition.collectin.repository.collection.TopicRepository;
import uz.itransition.collectin.service.CRUDService;

import java.util.List;

import static uz.itransition.collectin.exception.ResourceConstants.TOPIC_ENG;
import static uz.itransition.collectin.exception.ResourceConstants.TOPIC_RUS;

@Service
public class TopicService implements CRUDService<Long, APIResponse, TopicRequest, TopicRequest> {

    private final ModelMapper modelMapper;
    private final TopicRepository topicRepository;

    public TopicService(ModelMapper modelMapper, TopicRepository topicRepository) {
        this.modelMapper = modelMapper;
        this.topicRepository = topicRepository;
    }

    @Override
    public APIResponse create(TopicRequest topicRequest) {
        //// TODO: 21.06.2022 topic name should be unique
        return APIResponse.success(topicRepository
                .save(modelMapper.map(topicRequest, Topic.class)));
    }

    @Override
    public APIResponse get(Long id) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> {
            throw DataNotFoundException.of(TOPIC_ENG,TOPIC_RUS, String.valueOf(id));
        });
        return APIResponse.success(topic);
    }

    @Override
    public APIResponse modify(Long id, TopicRequest topicRequest) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> {
            throw DataNotFoundException.of(TOPIC_ENG,TOPIC_RUS, String.valueOf(id));
        });
        modelMapper.map(topicRequest, topic);
        return APIResponse.success(topicRepository.save(topic));
    }

    @Override
    public APIResponse delete(Long id) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> {
            throw DataNotFoundException.of(TOPIC_ENG,TOPIC_RUS, String.valueOf(id));
        });
        topicRepository.delete(topic);
        return APIResponse.success(true);
    }

    public APIResponse getAll() {
        return APIResponse.success(List.of(modelMapper.map(topicRepository.findAll(), TopicResponse[].class)));
    }
}
