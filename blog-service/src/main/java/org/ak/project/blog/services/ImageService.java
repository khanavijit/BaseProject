package org.ak.project.blog.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ak.project.blog.entities.Image;
import org.ak.project.blog.model.BlogResponse;
import org.ak.project.blog.repositories.ImageRepository;
import io.jaegertracing.internal.JaegerSpanContext;
import io.micrometer.core.annotation.Timed;
import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RefreshScope
@RequiredArgsConstructor
public class ImageService {

    private static final int ORDER_QUARANTINE_MINUTES = 5;



    private final Tracer tracer;
    private final ObjectMapper mapper;

    private final ImageRepository imageRepository;

    @Timed("services.blog.add.image")
    public BlogResponse addImage(final Image img, String blogId) {
        String getImageId = UUID.randomUUID().toString();
        img.setBlogId(blogId);
        img.setImgId(getImageId);
        imageRepository.save(img);


        return BlogResponse.builder().blogId(img.getImgId()).message("Image Added").result(BlogResponse.Result.SUCCESS).build();
    }

    @Timed("services.blog.get.Image")
    public List<Image> getImages(final String blogId) {

        return imageRepository.findByBlogId(blogId);



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
