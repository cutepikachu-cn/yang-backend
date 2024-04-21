package cn.cutepikachu.yangtuyunju.model.dto.user;

import cn.cutepikachu.yangtuyunju.model.enums.UserRole;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class UserRegisterRequest implements Serializable {

    @NotBlank
    @Pattern(regexp = "^[\\w-]{4,16}$")
    private String userAccount;

    @NotBlank
    @Pattern(regexp = "^[\\w-]{6,20}$")
    private String userPassword;

    @NotBlank
    @Pattern(regexp = "^[\\w-]{6,20}$")
    private String checkPassword;

    @NotBlank
    @Length(min = 4, max = 20)
    private String userNickname;

    private String userRole = UserRole.USER.getValue();

    @Serial
    private static final long serialVersionUID = 1L;

    @AssertTrue
    public boolean isPasswordMatch() {
        return userPassword != null && userPassword.equals(checkPassword);
    }

    @AssertTrue
    boolean isValidUserRole() {
        return userRole == null || UserRole.getEnumByValue(userRole) != null;
    }
}
