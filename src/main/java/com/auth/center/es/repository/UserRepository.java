package com.auth.center.es.repository;

import com.auth.center.es.index.UserIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/21 17:02
 **/
public interface UserRepository extends ElasticsearchRepository<UserIndex, String> {

    List<UserIndex> findByRealname(String realname, Sort sort);

    Page<UserIndex> findByRealname(String realname, Pageable pageable);
}
