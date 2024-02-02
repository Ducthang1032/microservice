package tdt.intern.image.repository;

import tdt.intern.image.entity.ImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageInfoRepository extends JpaRepository<ImageInfo, Long> {
}
