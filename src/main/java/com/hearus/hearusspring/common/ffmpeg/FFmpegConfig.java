package com.hearus.hearusspring.common.ffmpeg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FFmpegConfig {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    public String getFFmpegPath() {
        if ("production".equals(activeProfile)) {
            // Docker 환경에서는 시스템 경로의 FFmpeg 사용
            return "ffmpeg";
        } else {
            // 로컬 개발 환경에서는 프로젝트에 포함된 FFmpeg 사용
            return "src/main/resources/ffmpeg/bin/ffmpeg";
        }
    }
}