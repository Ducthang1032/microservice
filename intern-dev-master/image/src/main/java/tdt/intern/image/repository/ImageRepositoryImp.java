package tdt.intern.image.repository;

import org.springframework.stereotype.Component;
import tdt.intern.image.entity.ImageInfo;
import tdt.intern.image.repository.Custom.ImageRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class ImageRepositoryImp implements ImageRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ImageInfo> findAllImage() {
        String nativeQuery = "SELECT * from image";
        Query query = entityManager.createNativeQuery(nativeQuery, ImageInfo.class);

        return (List<ImageInfo>) query.getResultList();
    }

    @Override
    public ImageInfo findById(Long id) {
        String nativeQuery = "SELECT * from image WHERE id = :id ";
        Query query = entityManager.createNativeQuery(nativeQuery, ImageInfo.class);
        query.setParameter("id", id);

        return (ImageInfo) query.getSingleResult();
    }
}
