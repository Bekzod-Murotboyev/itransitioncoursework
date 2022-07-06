package uz.itransition.collectin.entity.collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.itransition.collectin.entity.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class FieldValue extends BaseEntity {

    private String value;

    @ManyToOne(cascade = CascadeType.ALL)
    private Item item;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field;


}
