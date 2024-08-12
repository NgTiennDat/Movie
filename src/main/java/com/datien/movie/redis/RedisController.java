package com.datien.movie.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v4/redis")
@RequiredArgsConstructor
public class RedisController {
    private final BaseRedisService baseRedisService;

    @PostMapping
    public void set() {
        baseRedisService.set("hihi", "haha");
    }
}
