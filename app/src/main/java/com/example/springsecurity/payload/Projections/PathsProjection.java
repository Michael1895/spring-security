package com.example.springsecurity.payload.Projections;

import com.example.springsecurity.models.Resources;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.UUID;

public interface PathsProjection {

    @Value("#{target.finish_message}")
    String getFinishMessage();

    @Value("#{target.name}")
    String getPathName();

    @Value("#{target.story_back_cover_colour}")
    String getStoryBackCoverColor();

    @Value("#{target.story_back_cover_text_colour}")
    String getStoryBackCoverTextColor();

    @Value("#{target.story_finish_message}")
    String getStoryFinishMessage();

    @Value("#{target.image_id}")
    Resources getImageId();

    @Value("#{target.story_image_id}")
    Resources getStoryImageId();

}
