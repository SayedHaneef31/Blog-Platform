package com.Sayed.Blog.Backend;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController
{
    @GetMapping("health")
    public String healthChecker()
    {
        return "Working Fine";
    }
}
