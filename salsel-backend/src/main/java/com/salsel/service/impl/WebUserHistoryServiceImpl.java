package com.salsel.service.impl;

import com.salsel.dto.WebUserHistoryDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.exception.UserAlreadyExistAuthenticationException;
import com.salsel.model.User;
import com.salsel.model.WebUserHistory;
import com.salsel.repository.UserRepository;
import com.salsel.repository.WebUserHistoryRepository;
import com.salsel.service.WebUserHistoryService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class WebUserHistoryServiceImpl implements WebUserHistoryService {

    private final WebUserHistoryRepository webUserHistoryRepository;
    private final UserRepository userRepository;

    public WebUserHistoryServiceImpl(WebUserHistoryRepository webUserHistoryRepository, UserRepository userRepository) {
        this.webUserHistoryRepository = webUserHistoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public WebUserHistoryDto loginWebUser(WebUserHistoryDto webUserHistoryDto) {
        WebUserHistory webUserHistory = toEntity(webUserHistoryDto);

        User existingUser = userRepository.findByEmail("webuser@gmail.com")
                .orElseThrow(() -> new RecordNotFoundException("WebUser not found"));

        webUserHistory.setUserId(existingUser.getId());
        WebUserHistory createdWebUser = webUserHistoryRepository.save(webUserHistory);
        return toDto(createdWebUser);
    }


    public WebUserHistoryDto toDto(WebUserHistory webUser) {
        return WebUserHistoryDto.builder()
                .id(webUser.getId())
                .browser(webUser.getBrowser())
                .timeZone(webUser.getTimeZone())
                .location(webUser.getLocation())
                .userId(webUser.getUserId())
                .ip(webUser.getIp())
                .build();
    }

    public WebUserHistory toEntity(WebUserHistoryDto webUserDto) {
        return WebUserHistory.builder()
                .id(webUserDto.getId())
                .browser(webUserDto.getBrowser())
                .timeZone(webUserDto.getTimeZone())
                .location(webUserDto.getLocation())
                .userId(webUserDto.getUserId())
                .ip(webUserDto.getIp())
                .build();
    }

}
