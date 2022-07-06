package uz.itransition.collectin.entity.collection;

import lombok.Getter;
import lombok.Setter;
import uz.itransition.collectin.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class Topic extends BaseEntity {

    @Column(nullable = false)
    private String name;

}
