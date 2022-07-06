package uz.itransition.collectin.entity.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.itransition.collectin.entity.User;
import uz.itransition.collectin.entity.collection.Item;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Column(nullable = false)
    private Date creationDate = new Date();

    @ManyToOne
    private User user;

    @ManyToOne
    private Item item;

    public Comment(String text, User user, Item item) {
        this.text = text;
        this.user = user;
        this.item = item;
    }

    public static Comment of(String text, Long userId, Long itemId){
        return new Comment(text,new User(userId), new Item(itemId) );
    }
}
