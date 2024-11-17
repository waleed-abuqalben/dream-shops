package com.waleed.dreamshops.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.waleed.dreamshops.dto.ImageDto;
import com.waleed.dreamshops.exceptions.ResourceNotFoundException;
import com.waleed.dreamshops.model.Image;
import com.waleed.dreamshops.model.Product;
import com.waleed.dreamshops.repository.ImageRepository;
import com.waleed.dreamshops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class ImageService implements IImageService{

	private final ImageRepository imageRepository;
	private final IProductService productService;
	
	@Override
	public Image getImageById(Long id) {
		return imageRepository.findById(id)
			.orElseThrow(() -> 
				new ResourceNotFoundException("No image found with id: "+id));
	}

	@Override
	public void deleteImageById(Long id) {
		imageRepository.findById(id).ifPresentOrElse(imageRepository :: delete,
				() -> {
				   throw new ResourceNotFoundException("No image found with id: "+id);
				});
	}

	@Override
	public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
		Product product = productService.getProductById(productId);
		List<ImageDto> savedImageDtos = new ArrayList<>();
		for(MultipartFile file : files) {
			try {
				Image image = new Image();
				image.setFileName(file.getOriginalFilename());
				image.setFileType(file.getContentType());
				image.setImage(new SerialBlob(file.getBytes()));
				image.setProduct(product);
				
				//Build download URL
				String baseUrl = "/api/v1/images/image/download/";
				image.setDownloadUrl(baseUrl);
				Image savedImage = imageRepository.save(image);//To get correct id
				savedImage.setDownloadUrl(baseUrl+savedImage.getId());
				imageRepository.save(savedImage);
				
				//information to be returned
				ImageDto imageDto = new ImageDto();
				imageDto.setId(savedImage.getId());
				imageDto.setFileName(savedImage.getFileName());
				imageDto.setDownloadUrl(savedImage.getDownloadUrl());
				savedImageDtos.add(imageDto);
				 
			}catch (IOException | SQLException e) {
				new RuntimeException(e.getMessage());
			}
		}
		return savedImageDtos;
	}

	@Override
	public void updateImage(MultipartFile file, Long imageId) {
		Image image = getImageById(imageId);
		try {//TODO: fileType???
			image.setFileName(file.getOriginalFilename());
			image.setImage(new SerialBlob(file.getBytes()));
			imageRepository.save(image);
		}catch (IOException | SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}

}
