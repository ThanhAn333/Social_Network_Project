package Zabook.services.impl;

import java.util.List;

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
    
}
