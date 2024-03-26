package ndt.project.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import ndt.project.common.dto.MetaDTO;
import ndt.project.common.dto.ResponseMetaData;

@NoArgsConstructor
public class ResMDLogin extends ResponseMetaData {

    @JsonProperty("data")
    private LoginResponseDTO data;

    public void setData(LoginResponseDTO loginResponseDTO) {
        this.data = loginResponseDTO;
    }

    public ResMDLogin(MetaDTO metaDTO, LoginResponseDTO data) {
        this.setMeta(metaDTO);
        this.data = data;
    }

    public ResMDLogin(MetaDTO metaDTO) {
        this.setMeta(metaDTO);
        this.data = null;
    }
}
