package org.ak.project.blog.controllers;


import org.ak.project.blog.entities.Image;
import org.ak.project.blog.entities.Blog;
import org.ak.project.blog.model.BlogRequest;
import org.ak.project.blog.model.BlogResponse;
import org.ak.project.blog.services.ImageService;
import org.ak.project.blog.services.BlogService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@Api(value = "BlogService", tags = "BlogService")
@RequestMapping(path = "v1/")
@RequiredArgsConstructor
public class BlogController {


    private final BlogService blogService;
    private final ImageService imageService;


        @PostMapping(path = "", produces = "application/json")
        @ApiOperation(value = "Create a new blog.", response = BlogResponse.class, nickname = "createBlog")
        public ResponseEntity<BlogResponse> createBlog(@ApiParam(value = "Prebuild blog object - if omitted an empty blog will be created", required = true) @RequestBody final BlogRequest blogRequest) {


        final BlogResponse blogResponse = blogService.createBlog(blogRequest);

            if(blogRequest.getImages().size()!=0){
                blogRequest.getImages().forEach(img->imageService.addImage((Image.builder().imgId(UUID.randomUUID().toString()).path(img.getPath()).build()), blogResponse.getBlogId()));
            }


        log.info("<<< createdBlog");
        return ResponseEntity.ok(blogResponse);
    }

    @GetMapping(path = "/{blogId}", produces = "application/json")
    @ApiOperation(value = "Get a post from Blog", response = Blog.class, nickname = "getBlog")
    public ResponseEntity<BlogResponse> getBlog(@ApiParam(value = "The blogId to look for blog on.", required = true, example = "011ca096-1813-4521-b5e7-48d4d3fcc3e6")
                                                  @PathVariable final String blogId) {
        log.info(">>> getPost on {}", blogId);

        final BlogResponse blogResponse = blogService.getBlog(blogId);




        if(blogResponse.getResult().equals(BlogResponse.Result.SUCCESS)){
            blogResponse.setImages(imageService.getImages(blogId));
        }

        return  (blogResponse.getResult().equals(BlogResponse.Result.SUCCESS))  ? ResponseEntity.ok(blogResponse) : ResponseEntity.notFound().build();


    }



}
