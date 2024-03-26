package ndt.project.authservice.repository;

import ndt.project.authservice.domain.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemRoleRepository extends JpaRepository<SystemRole, Long> {
    SystemRole findFirstById(Long Id);
}
