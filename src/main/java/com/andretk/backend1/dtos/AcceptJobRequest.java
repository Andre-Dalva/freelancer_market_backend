package com.andretk.backend1.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AcceptJobRequest {
    private String optionalLink1;
    private String optionalLink2;
    private String comments;
}
