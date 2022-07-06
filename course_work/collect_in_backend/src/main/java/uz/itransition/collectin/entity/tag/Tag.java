package uz.itransition.collectin.entity.tag;

import lombok.Getter;
import lombok.Setter;
import uz.itransition.collectin.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class Tag extends BaseEntity {

    @Column(nullable = false)
    private String name;

}
