package Zabook.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Zabook.models.Video;
import Zabook.repository.VideoRepository;
import Zabook.services.IVideoService;
@Service
public class VideoService implements IVideoService {

	@Autowired
	VideoRepository videoRepo;
	@Override
	public Video addVideo(Video video) {
		// TODO Auto-generated method stub
		return videoRepo.save(video);
	}

}
