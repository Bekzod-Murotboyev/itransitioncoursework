package uz.itransition.collectin.payload.response.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.itransition.collectin.payload.response.Response;
import uz.itransition.collectin.service.marker.Modifiable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseByTag implements Response, Modifiable {
    private long itemId;
    private String fieldKey;
    private String fieldValue;
}
