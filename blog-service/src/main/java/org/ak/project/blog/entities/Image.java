package org.ak.project.blog.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@Data
@Builder
@Table("blog_image")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "An image in the blog")
public class Image {


    @PrimaryKey
    @ApiModelProperty(value = "The image id (a UUID).", example = "e0015c94-234e-4210-90fd-ed83250aea9c", readOnly = true)
    private String imgId;
    @ApiModelProperty(value = "The path of the image.", example = "/img/1.jpg")
    private String path;
    @ApiModelProperty(value = "BlogId of the Image.", example = "e0015c94-234e-4210-90fd-ed83250aea9c")
    private String blogId;
}
