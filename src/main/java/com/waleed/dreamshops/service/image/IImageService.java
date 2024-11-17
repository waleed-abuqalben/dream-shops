package com.waleed.dreamshops.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.waleed.dreamshops.dto.ImageDto;
import com.waleed.dreamshops.model.Image;

public interface IImageService {

	Image getImageById(Long id);
	void deleteImageById(Long id);
	List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
	void updateImage(MultipartFile file, Long imageId);
}
