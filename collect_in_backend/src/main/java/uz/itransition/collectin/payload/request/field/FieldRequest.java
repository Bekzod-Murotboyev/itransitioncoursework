package uz.itransition.collectin.payload.request.field;

import lombok.Getter;
import lombok.Setter;
import uz.itransition.collectin.payload.response.Response;
import uz.itransition.collectin.service.marker.Creationable;
import uz.itransition.collectin.service.marker.Modifiable;

@Getter
@Setter
public class FieldRequest implements Response, Creationable, Modifiable {
    private long id;
    private String name;
    private int type;
}
