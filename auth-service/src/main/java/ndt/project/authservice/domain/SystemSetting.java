package ndt.project.authservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "system_setting")
public class SystemSetting extends AbstractAuditing implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "max_friend")
    private Long maxFriend = 0L; //Default is 0

    @Column(name = "max_follower")
    private Long maxFollower = 0L; //Default is 0

    @Column(name = "max_following")
    private Long maxFollowing = 0L; //Default is 0

    @Column(name = "term_of_use")
    private String termOfUse;

    @Column(name = "privacy_policy")
    private String privacyPolicy;

    @Column(name = "max_favorite")
    private Long maxFavorite = 0L; //Default is 0

    @Column(name = "max_report_number")
    private Long maxReportNumber = 0L; //Default is 0

    @Column(name = "max_login_fail")
    private Long maxLoginFail = 0L; //Default is 0

    @Column(name = "sub_system")
    private Long subSystem;

    @Column(name = "version_ios")
    private String versionIos;

    @Column(name = "version_android")
    private String versionAndroid;

    @Column(name = "version_web")
    private String versionWeb;
}