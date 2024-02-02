package tdt.intern.image.service;

import tdt.intern.image.entity.ImageInfo;
import tdt.intern.image.repository.Custom.ImageRepositoryCustom;
import tdt.intern.image.repository.ImageInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageInfoService {
    private final ImageInfoRepository imageInfoRepository;
    private final ImageRepositoryCustom imageRepositoryCustom;

    public ImageInfo getImageById(Long id) {
        return imageInfoRepository.findById(id).orElse(null);
    }

    public List<ImageInfo> getAll() {
        return imageRepositoryCustom.findAllImage();
    }

    public ImageInfo getById(Long id) {
        return imageRepositoryCustom.findById(id);
    }
}
