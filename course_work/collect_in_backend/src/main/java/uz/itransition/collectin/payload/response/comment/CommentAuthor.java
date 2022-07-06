package uz.itransition.collectin.payload.response.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentAuthor {
    private long id;
    private String name;
    @JsonProperty("image_url")
    private String imageUrl;
}
