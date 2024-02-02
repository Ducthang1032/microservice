package tdt.intern.image.repository.Custom;

import tdt.intern.image.entity.ImageInfo;

import java.util.List;

public interface ImageRepositoryCustom {
    List<ImageInfo> findAllImage();

    ImageInfo findById(Long id);
}
