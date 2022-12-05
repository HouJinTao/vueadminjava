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

import javax.validation.constraints.Email;
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
@ApiModel(value="SysUser对象", description="")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;

    private String avatar;

    @NotBlank(message = "邮箱格式不正确")
    @Email(message = "格式不正确")
    private String email;

    private String city;

    private LocalDateTime created;

    private LocalDateTime updated;

    private Date lastLogin;

    private Integer statu;

    @TableField(exist = false)
    private List<SysRole> sysRoles = new ArrayList<>();
}
