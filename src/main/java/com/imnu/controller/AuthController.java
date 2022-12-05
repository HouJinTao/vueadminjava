package com.imnu.controller;

import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.imnu.common.lang.Const;
import com.imnu.common.lang.Result;
import com.imnu.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@RestController
@Slf4j
public class AuthController extends BaseController{

    //图片随机数
    @Autowired
    Producer producer;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        log.info("=======================");
        //生成随机三十位随机数
        String key = UUID.randomUUID().toString();
        //生成五位随机数
        String code = producer.createText();

        //测试
/*        key = "aaaaa";
        code = "111111";*/

        //生成图片
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encode(outputStream.toByteArray());

        redisUtil.hset(Const.CAPTCHA_KEY,key,code,120);

        return Result.succ(
                MapUtil.builder()
                    .put("token",key)
                    .put("captchaImg",base64Img)
                    .build()
        );
    }
    /**
     * 获取用户信息接口
     * @param principal
     * @return
     */
    @GetMapping("/sys/userInfo")
    public Result userInfo(Principal principal) {

        SysUser sysUser = sysUserService.getByUsername(principal.getName());

        return Result.succ(MapUtil.builder()
                .put("id", sysUser.getId())
                .put("username", sysUser.getUsername())
                .put("avatar", sysUser.getAvatar())
                .put("created", sysUser.getCreated())
                .map()
        );
    }

}
