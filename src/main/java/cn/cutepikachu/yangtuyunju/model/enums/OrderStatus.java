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
public enum OrderStatus {
    UNPAID("未支付", "unpaid"),
    PAID("已支付", "paid"),
    SHIPPED("已发货", "shipped"),
    COMPLETED("已完成", "completed"),
    CANCELLED("已取消", "cancelled"),
    AFTER_SALE("售后中", "after_sale");

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
    public static OrderStatus getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (OrderStatus anEnum : OrderStatus.values()) {
            if (anEnum.value.equals(value.toLowerCase())) {
                return anEnum;
            }
        }
        return null;
    }
}
