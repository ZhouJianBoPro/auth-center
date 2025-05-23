package com.auth.center.controller;

import com.auth.center.es.index.UserIndex;
import com.auth.center.es.repository.UserRepository;
import com.auth.center.rateLimit.RateLimit;
import com.auth.center.vo.ResultVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/21 17:12
 **/
@RequestMapping("/elasticsearch")
@RestController
public class ElasticsearchController {

    @Resource
    private UserRepository userRepository;

    @RateLimit(window = 10, limit = 2)
    @GetMapping("/queryByRealname")
    public ResultVO<List<UserIndex>> queryByRealname(@RequestParam String realname) {
        List<UserIndex> list = userRepository.findByRealname(realname, Sort.by(Sort.Direction.ASC, "id"));
        return ResultVO.ok(list);
    }

    @GetMapping("/pageByRealname")
    public ResultVO<Page<UserIndex>> pageByRealname(@RequestParam String realname, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserIndex> list = userRepository.findByRealname(realname, pageable);
        return ResultVO.ok(list);
    }
}
