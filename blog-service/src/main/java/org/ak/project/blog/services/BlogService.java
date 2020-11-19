package org.ak.project.blog.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.ak.project.blog.entities.Blog;
import org.ak.project.blog.model.BlogRequest;
import org.ak.project.blog.model.BlogResponse;
import org.ak.project.blog.repositories.BlogRepository;
import io.jaegertracing.internal.JaegerSpanContext;
import io.micrometer.core.annotation.Timed;
import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RefreshScope
@RequiredArgsConstructor
public class BlogService {

    private static final int ORDER_QUARANTINE_MINUTES = 5;

    @Value("${osm.disabled:false}")
    private boolean osmDisabled;

    private final Tracer tracer;
    private final ObjectMapper mapper;

    private final BlogRepository blogRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Timed("services.blog.create.blog")
    public BlogResponse createBlog(final BlogRequest blogRequest) {
        String getBlogId = UUID.randomUUID().toString();

        Blog blog=modelMapper.map(blogRequest,Blog.class);



        blog.setBlogId(getBlogId);
        blogRepository.save(blog);


        return BlogResponse.builder().blogId(blog.getBlogId()).message("Blog Created").result(BlogResponse.Result.SUCCESS).build();
    }

    @Timed("services.blog.get.post")
    public BlogResponse getBlog(final String blogId) {

        Optional<Blog> blog= blogRepository.findById(blogId);



        if(blog.isPresent()){

            BlogResponse blogResponse =modelMapper.map(blog.get(),BlogResponse.class);
            blogResponse.setResult(BlogResponse.Result.SUCCESS);
            return blogResponse;
        }

        return BlogResponse.builder().result(BlogResponse.Result.NOT_FOUND).message("Blog not Found").build();



    }



    private String getTrackingId() {
        if (tracer != null && tracer.activeSpan() != null) {
            final Span span = tracer.activeSpan();
            JaegerSpanContext spanContext = (JaegerSpanContext) span.context();
            return spanContext.getTraceId();
        }
        return null;
    }

}
