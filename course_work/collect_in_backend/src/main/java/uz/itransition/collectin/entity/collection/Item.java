package uz.itransition.collectin.entity.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.itransition.collectin.entity.BaseEntity;
import uz.itransition.collectin.entity.User;
import uz.itransition.collectin.entity.tag.Tag;
import uz.itransition.collectin.payload.response.Searchable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseEntity implements Searchable {
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "int default 0")
    private long likes;

    @ManyToOne
    private Collection collection;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Tag> tags;

    public Item(String name, Collection collection, List<Tag> tags) {
        this.name = name;
        this.collection = collection;
        this.tags = tags;
    }

    @JsonIgnore
    @ManyToMany
    private Set<User> likedUsers = new HashSet<>();

    public Item(Long id) {
        super(id);
    }

    public long getLikes() {
        return likedUsers.size();
    }

}
