package cn.cutepikachu.yangtuyunju.annotation;

import cn.cutepikachu.yangtuyunju.model.enums.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验注解
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 必须有某个角色
     *
     * @return
     */
    UserRole mustRole() default UserRole.USER;

}
