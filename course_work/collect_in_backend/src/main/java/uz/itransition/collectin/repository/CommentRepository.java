package uz.itransition.collectin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.itransition.collectin.entity.comment.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select c from Comment c where c.item.id = ?1")
    List<Comment> findAllByItemId(Long item_id);

    @Query(value = "select * from comment c where c.doc @@ plainto_tsquery(:text)", nativeQuery = true)
    List<Comment> fullTextSearch(String text);

}
