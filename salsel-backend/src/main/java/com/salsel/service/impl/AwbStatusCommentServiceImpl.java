package com.salsel.service.impl;

import com.salsel.dto.AwbStatusCommentDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Awb;
import com.salsel.model.AwbStatusComment;
import com.salsel.repository.AwbRepository;
import com.salsel.repository.AwbStatusCommentRepository;
import com.salsel.service.AwbStatusCommentService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwbStatusCommentServiceImpl implements AwbStatusCommentService {
    private final AwbStatusCommentRepository awbStatusCommentRepository;
    private final AwbRepository awbRepository;

    public AwbStatusCommentServiceImpl(AwbStatusCommentRepository awbStatusCommentRepository, AwbRepository awbRepository) {
        this.awbStatusCommentRepository = awbStatusCommentRepository;
        this.awbRepository = awbRepository;
    }

    @Override
    @Transactional
    public AwbStatusCommentDto addComment(AwbStatusCommentDto awbStatusCommentDto) {
        AwbStatusComment awbStatusComment = toEntity(awbStatusCommentDto);
        awbStatusComment.setStatus(true);

        Awb awb = awbRepository.findById(awbStatusComment.getAwb().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for id => %d", awbStatusComment.getAwb().getId())));

        awbStatusComment.setAwb(awb);
        AwbStatusComment createdAwbStatusComment = awbStatusCommentRepository.save((awbStatusComment));
        return toDto(createdAwbStatusComment);
    }

    @Override
    public List<AwbStatusCommentDto> findByAwbId(Long awbId) {
        List<AwbStatusComment> awbStatusCommentList = awbStatusCommentRepository.findByAwbId(awbId);
        return awbStatusCommentList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public AwbStatusCommentDto toDto(AwbStatusComment awbStatusComment) {
        return AwbStatusCommentDto.builder()
                .id(awbStatusComment.getId())
                .timestamp(awbStatusComment.getTimestamp())
                .status(awbStatusComment.getStatus())
                .comment(awbStatusComment.getComment())
                .commentBy(awbStatusComment.getCommentBy())
                .awbStatus(awbStatusComment.getAwbStatus())
                .awb(awbStatusComment.getAwb())
                .build();
    }

    public AwbStatusComment toEntity(AwbStatusCommentDto awbStatusCommentDto) {
        return AwbStatusComment.builder()
                .id(awbStatusCommentDto.getId())
                .timestamp(awbStatusCommentDto.getTimestamp())
                .status(awbStatusCommentDto.getStatus())
                .comment(awbStatusCommentDto.getComment())
                .commentBy(awbStatusCommentDto.getCommentBy())
                .awbStatus(awbStatusCommentDto.getAwbStatus())
                .awb(awbStatusCommentDto.getAwb())
                .build();
    }
}
