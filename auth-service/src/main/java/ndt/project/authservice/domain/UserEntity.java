package ndt.project.authservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import ndt.project.authservice.dto.UserRegisterDTO;
import ndt.project.common.constants.AuthoritiesConstants;
import ndt.project.common.enums.UserStatus;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class UserEntity extends AbstractAuditing implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name")
    private String displayName = StringUtils.EMPTY; //Default is empty

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "email")
    private String email = StringUtils.EMPTY; //Default is empty

    @Column(name = "password")
    private String password = StringUtils.EMPTY; //Default is empty

    @Column(name = "gender")
    private String gender = StringUtils.EMPTY; //Default is empty

    @Column(name = "lang")
    private String lang = AuthoritiesConstants.DEFAULT_LANG;

    @Column(name = "avatar")
    private String avatar = StringUtils.EMPTY; //Default is empty

    @Column(name = "address")
    private String address = StringUtils.EMPTY; //Default is empty

    @Column(name = "street")
    private String street = StringUtils.EMPTY; //Default is empty

    @Column(name = "pwd_change_flg")
    private Boolean pwdChangeFlg = false;

    @Column(name = "district")
    private String district = StringUtils.EMPTY; //Default is empty

    @Column(name = "city")
    private String city = StringUtils.EMPTY; //Default is empty

    @Column(name = "total_friend")
    private Long totalFriend = 0L; //Default is 0

    @Column(name = "total_follower")
    private Long totalFollower = 0L; //Default is 0

    @Column(name = "total_following")
    private Long totalFollowing = 0L; //Default is 0

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "status")
    private String status = UserStatus.ACTIVE.getValue(); //Default is active

    @Column(name = "pre_password_1")
    private String prePassword1 = StringUtils.EMPTY; //Default is empty

    @Column(name = "pre_password_2")
    private String prePassword2 = StringUtils.EMPTY; //Default is empty


    public UserEntity(UserRegisterDTO registerDTO, Long roleId) {
        this.email = StringUtils.lowerCase(registerDTO.getEmail());
        this.password = registerDTO.getPassword();
        this.roleId = roleId;
        this.pwdChangeFlg = false;  //Default is false
    }
}
