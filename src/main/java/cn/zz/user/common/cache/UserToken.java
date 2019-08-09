package cn.zz.user.common.cache;

import lombok.Data;

/**
 * @author : zhourui
 * @version: 2019-08-09 17:03
 **/
@Data
public class UserToken {
    private String token;
    private Integer userId;
    private String userName;
    private String role;
}
