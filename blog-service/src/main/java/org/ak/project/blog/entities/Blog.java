package org.ak.project.blog.entities;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;




@Data
@Builder(toBuilder = true)
@Table("blog_post")
@NoArgsConstructor
@AllArgsConstructor
public class Blog {

    @PrimaryKey
    private String blogId;
    private String author;
    private String location;
    private String sentiment;
    private String content;





}
