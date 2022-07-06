package uz.itransition.collectin.repository.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.itransition.collectin.entity.tag.Tag;

public interface TagRepository extends JpaRepository<Tag,Long> {

}
