package org.ak.project.blog.repositories;

import org.ak.project.blog.entities.Blog;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends CassandraRepository<Blog, String> {
}
