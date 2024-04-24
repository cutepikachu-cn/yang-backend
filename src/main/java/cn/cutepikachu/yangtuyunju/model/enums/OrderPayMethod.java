package cn.cutepikachu.yangtuyunju.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum OrderPayMethod {
    ALIPAY("支付宝", "alipay"),
    WECHAT("微信", "wechat"),
    BANK_CARD("银行卡", "bank_card");

    private final String text;
    private final String value;

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static OrderPayMethod getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (OrderPayMethod anEnum : OrderPayMethod.values()) {
            if (anEnum.value.equals(value.toLowerCase())) {
                return anEnum;
            }
        }
        return null;
    }
}
