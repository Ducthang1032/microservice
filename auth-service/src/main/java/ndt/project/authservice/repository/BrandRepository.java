package ndt.project.authservice.repository;

import ndt.project.authservice.domain.BrandSQL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<BrandSQL, Long> {
}