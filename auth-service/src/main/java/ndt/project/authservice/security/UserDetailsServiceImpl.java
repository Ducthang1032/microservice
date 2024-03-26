package ndt.project.authservice.security;

import com.tdt.cqsta.authserver.domain.CQRole;
import com.tdt.cqsta.authserver.domain.UserEntity;
import com.tdt.cqsta.authserver.repository.CQRoleRepository;
import com.tdt.cqsta.authserver.repository.UserRepository;
import com.tdt.cqsta.common.constants.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final CQRoleRepository cqRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        //Checking in the db
        UserEntity user = userRepository.getUserEntityById(Long.valueOf(userId));
        if (Objects.isNull(user))
            return null;

        CQRole cqRole = cqRoleRepository.findFirstById(user.getRoleId());

        // Remember that Spring needs roles to be in this format: "ROLE_" + userRole (i.e. "ROLE_ADMIN")
        // So, we need to set it to that format, so we can verify and compare roles (i.e. hasRole("ADMIN")).
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(AuthoritiesConstants.ROLE_PREFIX.concat(cqRole.getRole()));

        // The "User" class is provided by Spring and represents a model class for user to be returned by UserDetailsService
        // And used by auth manager to verify and check user authentication.
        return new User(String.valueOf(user.getId()), user.getPassword(), grantedAuthorities);

    }
}