package uz.itransition.collectin.payload.response.tag;

import lombok.Getter;
import lombok.Setter;
import uz.itransition.collectin.payload.response.Response;
import uz.itransition.collectin.service.marker.Modifiable;

@Getter
@Setter
public class TagResponse implements Response, Modifiable {
    private long id;

    private String name;
}
