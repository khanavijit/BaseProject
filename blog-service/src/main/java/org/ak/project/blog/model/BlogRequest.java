package org.ak.project.blog.model;

import org.ak.project.blog.entities.Image;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@ApiModel(value = "Blog Request", description = "The blog with information and images.")
@AllArgsConstructor
public class BlogRequest {


    private String blogId;
    @ApiModelProperty(value = "Author of The blog.", example = "Avijit")
    private String author;
    @ApiModelProperty(value = "Location.", example = "Copenhagen")
    private String location;
    @ApiModelProperty(value = "Sentiment of the blog.", example = "Happy")
    private String sentiment;
    @ApiModelProperty(value = "Comments for the blog.", example = "This is an blog content..")
    private String content;

    @ApiModelProperty(
            value = "A list of images in this blog.",
            example = "[{\"imgId\": \"e0015c94-234e-4210-90fd-ed83250aea9d\", \"path\": \"/img/1.jpg\"},{\"imgId\": \"e0015c94-234e-4210-90fd-ed83250aea9e\", \"path\": \"/img/2.jpg\"}]"
    )
    private List<Image> images ;
}
