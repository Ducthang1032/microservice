package ndt.project.authservice.service;

import lombok.RequiredArgsConstructor;
import ndt.project.authservice.domain.BrandSQL;
import ndt.project.authservice.domain.SystemRole;
import ndt.project.authservice.repository.BrandRepository;
import ndt.project.authservice.repository.SystemRoleRepository;
import ndt.project.common.constants.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CacheableService {

    private final BrandRepository brandRepository;

    private final SystemRoleRepository systemRoleRepository;

    private final CQSystemSettingRepository cqSystemSettingRepository;

    @Cacheable(value = "Map<Long, BrandSQL>", key = "#mappingBrandKey")
    public Map<Long, BrandSQL> mappingBrand(String mappingTopicKey) {
        return brandRepository.findAll().stream().collect(Collectors.toMap(BrandSQL::getId, Function.identity()));
    }

    @Cacheable(value = "Map<Map<Long, SystemRole>>", key = "#mappingRoleKey")
    public Map<Long, SystemRole> mappingRole(String mappingRoleKey) {
        return systemRoleRepository.findAll().stream()
                .collect(Collectors.toMap(SystemRole::getId, Function.identity()));
    }

    public String getRoleNameById(Long roleId) {
        return mappingRole(CommonConstant.ROLE_KEY_NAME).values().stream()
                .filter(role -> Objects.equals(role.getId(), roleId))
                .map(SystemRole::getRole).findFirst().orElse(StringUtils.EMPTY);
    }

    @Cacheable(value = "CQSystemSetting", key = "#subSystem")
    public CQSystemSetting getSystemSetting(Long subSystem) {
        return cqSystemSettingRepository.findAll().stream()
                .filter(item -> Objects.equals(item.getSubSystem(), subSystem))
                .findFirst().orElse(new CQSystemSetting());
    }

    public String getUrlResourceWithAuthKey(String urlResource, String signingKey, String cdnURL) {
        if (StringUtils.isBlank(urlResource)) return StringUtils.EMPTY;
        String urlResourceFormat = StringUtils.containsIgnoreCase(urlResource, AUTH_KEY_CDN)
                ? urlResource.substring(NumberUtils.INTEGER_ZERO, StringUtils.indexOfIgnoreCase(urlResource, AUTH_KEY_CDN))
                : urlResource;
        String resourceKey = String.format(CACHE_RESOURCE_KEY, urlResourceFormat);
        return getUrlResourceWithAuthKeyFromCache(resourceKey, urlResourceFormat, signingKey, cdnURL);
    }

    @Cacheable(value = "String", key = "#resourceKey")
    public String getUrlResourceWithAuthKeyFromCache(String resourceKey, String urlResource, String signingKey, String cdnURL) {
        return SecurityUtil.getUrlResourceWithAuthKey(urlResource, signingKey, cdnURL);
    }

    public Long getRoleIdByRoleName(String roleName) {
        return mappingRole(CommonConstant.ROLE_KEY_NAME).values().stream()
                .filter(role -> StringUtils.equalsIgnoreCase(role.getRole(), roleName))
                .map(CQRole::getId).findFirst().orElse(null);
    }
}