package ndt.project.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ndt.project.common.enums.MetaData;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaDTO {
    private Integer code;
    private String message;

    public MetaDTO(MetaData metaData) {
        this.code = metaData.getMetaCode();
        this.message = metaData.getMessage();
    }
}
