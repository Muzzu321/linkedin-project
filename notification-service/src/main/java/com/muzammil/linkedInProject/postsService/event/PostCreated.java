package com.muzammil.linkedInProject.postsService.event;

import lombok.Builder;
import lombok.Data;

@Data
public class PostCreated {
    private Long ownerUserId;
    private Long postId;
    private Long userId;
    private String content;
}
