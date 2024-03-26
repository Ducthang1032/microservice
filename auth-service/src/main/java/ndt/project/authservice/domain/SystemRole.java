package ndt.project.authservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "system_role")
public class SystemRole extends AbstractAuditing implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role")
    private String role = StringUtils.EMPTY; //Default is empty

    @Column(name = "role_level")
    private Long roleLevel;

    @Column(name = "role_description")
    private String roleDescription = StringUtils.EMPTY; //Default is empty

    @Column(name = "active_flg")
    private Boolean activeFlg = true; //Default is true
}
