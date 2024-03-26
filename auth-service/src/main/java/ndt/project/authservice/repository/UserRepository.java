package ndt.project.authservice.repository;


import ndt.project.authservice.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findOneByEmailIgnoreCase(String email);

    UserEntity findOneByPhone(String phone, Long susSystem);

    UserEntity getUserEntityById(Long id);

    UserEntity findDistinctFirstById(Long id);

    UserEntity findFirstByFacebookId(String facebookId);

    List<UserEntity> findAllByEmailIgnoreCase(String email);

    List<UserEntity> findAllByPhone(String phone);

    List<UserEntity> findAllByCategoryCd(String categoryCd);

    List<UserEntity> findAllByIdIn(List<Long> idList);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhone(String phone);

}
