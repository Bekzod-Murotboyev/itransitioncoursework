package uz.itransition.collectin.repository.collection;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.itransition.collectin.entity.collection.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
