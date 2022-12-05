package com.imnu.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author testjava
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysRole对象", description="")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "角色名字不能为空")
    private String name;

    @NotBlank(message = "角色编码不能为空")
    private String code;

    @ApiModelProperty(value = "备注")
    private String remark;

    private LocalDateTime created;

    private LocalDateTime updated;

    private Integer statu;

    @TableField(exist = false)
    private List<Long> menuIds = new ArrayList<>();
}
