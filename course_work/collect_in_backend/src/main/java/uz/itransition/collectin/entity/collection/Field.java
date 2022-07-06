package uz.itransition.collectin.entity.collection;

import lombok.Getter;
import lombok.Setter;
import uz.itransition.collectin.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class Field extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private int type;

    @ManyToOne
    private Collection collection;

}
