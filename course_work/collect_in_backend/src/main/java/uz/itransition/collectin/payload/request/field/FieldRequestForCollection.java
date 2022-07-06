package uz.itransition.collectin.payload.request.field;

import lombok.Getter;
import lombok.Setter;
import uz.itransition.collectin.payload.response.Response;
import uz.itransition.collectin.service.marker.Creationable;
import uz.itransition.collectin.service.marker.Modifiable;

@Getter
@Setter
public class FieldRequestForCollection implements Response, Creationable, Modifiable {
    private int id;
    private String name;
    private int type;
}
