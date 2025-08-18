package com.suke.czx.modules.tenancy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TenancyBase {

    @Schema(description = "渠道ID")
    @JsonProperty(value = "tenancyId")
    public Integer tenancyId;

    @TableField(exist = false)
    @Schema(description = "渠道名称")
    @JsonProperty(value = "tenancyName")
    public String tenancyName;

}
