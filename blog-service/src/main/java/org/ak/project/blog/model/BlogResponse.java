package org.ak.project.blog.model;

import org.ak.project.blog.entities.Image;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse {


    public enum Result {
        SUCCESS, INPUT_ERROR, INTERNAL_ERROR, NOT_FOUND
    }

    @ApiModelProperty(value = "Internal tracking id that can be used to identify the request ")
    private String trackingId;

    @NonNull
    @ApiModelProperty(value = "If result is INPUT_ERROR or INTERNAL_ERROR the message property will contain details about the error.", required = true)
    private Result result;

    @NonNull
    @ApiModelProperty(value = "A more detailed error message if result is not SUCCESS", required = true)
    private String message;

    @ApiModelProperty(value = "The postId if the post was successfully created.")
    private String blogId;

    private String author;
    @ApiModelProperty(value = "Location.", example = "Copenhagen")
    private String location;
    @ApiModelProperty(value = "Sentiment of the blog.")
    private String sentiment;
    @ApiModelProperty(value = "Comments for the blog.")
    private String content;

    @ApiModelProperty(value = "A list of images in this blog.")
    private List<Image> images ;


}
