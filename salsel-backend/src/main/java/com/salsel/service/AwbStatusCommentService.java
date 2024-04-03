package com.salsel.service;

import com.salsel.dto.AwbStatusCommentDto;

import java.util.List;

public interface AwbStatusCommentService {
    AwbStatusCommentDto addComment(AwbStatusCommentDto awbStatusCommentDto);
    List<AwbStatusCommentDto> findByAwbId(Long awbId);
}
