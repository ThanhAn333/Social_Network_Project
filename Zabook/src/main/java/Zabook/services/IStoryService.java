package Zabook.services;

import java.util.List;

import org.bson.types.ObjectId;

import Zabook.models.Story;

public interface IStoryService {

    public Story createStory(Story story);

    public List<Story> getActiveStories();

    public List<Story> getUserStories(ObjectId userId);
    
}
