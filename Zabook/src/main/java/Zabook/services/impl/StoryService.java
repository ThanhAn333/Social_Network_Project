package Zabook.services.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.time.Duration;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Zabook.models.Story;
import Zabook.repository.StoryRepository;
import Zabook.services.IStoryService;

@Service
public class StoryService implements IStoryService {

    @Autowired
    private StoryRepository storyRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public Story createStory(Story story) {
        return storyRepository.save(story);
    }

    @Override
    public List<Story> getActiveStories() {
        return storyRepository.findByIsActiveTrue();
    }

    @Override
    public List<Story> getUserStories(ObjectId userId) {

        return storyRepository.findByUserId(userId);
    }

    @Override
   public void updateStoryStatusIfExpired() {
    List<Story> allStories = storyRepository.findAll();

    for (Story story : allStories) {
        try {
            LocalDateTime timestamp = parseTimestamp(story.getTimestamp());
            LocalDateTime now = LocalDateTime.now();
            long hoursDifference = Duration.between(timestamp, now).toHours();

            if (hoursDifference > 24) {
                story.setActive(false);
                storyRepository.save(story);
            }
        } catch (Exception e) {
            // Handle parsing errors or any other issues
            System.out.println("Error parsing timestamp: " + story.getTimestamp());
        }
    }
}

public LocalDateTime parseTimestamp(String timestamp) {
    String[] formats = {
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS",  // For timestamps like 2024-12-12T23:56:03.189242300
        "dd/MM/yyyy HH:mm",                 // For timestamps like 13/12/2024 11:47
        // Add more formats if necessary
    };

    for (String format : formats) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(timestamp, formatter);
        } catch (DateTimeParseException e) {
            // Log and try the next format
        }
    }

    throw new DateTimeParseException("Unable to parse timestamp: " + timestamp, timestamp, 0);
}
    
}
