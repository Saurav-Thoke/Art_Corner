package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public ApiResponse HomeControllerHandler()
    {
        ApiResponse res=new ApiResponse("Hello");
        return res;
    }
}
