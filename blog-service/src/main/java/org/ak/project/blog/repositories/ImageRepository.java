package org.ak.project.blog.repositories;


import org.ak.project.blog.entities.Image;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends CassandraRepository<Image, String> {
    public List<Image> findByBlogId(String blogId);
}
