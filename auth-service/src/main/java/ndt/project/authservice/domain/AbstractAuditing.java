package ndt.project.authservice.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditing implements Serializable {
    @Column(name = "create_uid")
    private Long createUid = 1L; // default = 1

    @CreatedDate
    @Column(name = "create_date")
    private Instant createDate = Instant.now();

    @Column(name = "write_uid")
    private Long writeUid = 1L; // default = 1

    @LastModifiedDate
    @Column(name = "write_date")
    private Instant writeDate = Instant.now();
}