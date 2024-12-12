package Zabook.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Zabook.models.Image;
import Zabook.repository.ImageRepository;
import Zabook.services.IImageService;
@Service
public class ImageService implements IImageService {

	@Autowired
	ImageRepository imageRepo;

	@Override
	public Image addImage(Image image) {
		return imageRepo.save(image);
	}
	
	
}
