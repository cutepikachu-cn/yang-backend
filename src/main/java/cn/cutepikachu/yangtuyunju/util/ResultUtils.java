package cn.cutepikachu.yangtuyunju.util;

import cn.cutepikachu.yangtuyunju.common.BaseResponse;
import cn.cutepikachu.yangtuyunju.common.ResponseCode;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @return
     */
    public static BaseResponse<Boolean> success() {
        return new BaseResponse<>(ResponseCode.SUCCESS, true);
    }

    /**
     * 成功返回数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS, data);
    }

    /**
     * 成功，带信息
     *
     * @return
     */
    public static BaseResponse<Boolean> success(String message) {
        return new BaseResponse<>(ResponseCode.SUCCESS, true, message);
    }

    /**
     * 成功返回数据，带信息
     *
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS, data, message);
    }

    /**
     * 失败
     *
     * @param responseCode
     * @return
     */
    public static BaseResponse<Boolean> error(ResponseCode responseCode) {
        return new BaseResponse<>(responseCode, false);
    }

    /**
     * 失败，带信息
     *
     * @param responseCode
     * @param message
     * @return
     */
    public static BaseResponse<Boolean> error(ResponseCode responseCode, String message) {
        return new BaseResponse<>(responseCode, false, message);
    }

    /**
     * 失败，带自定响应码和信息
     *
     * @param responseCode
     * @param message
     * @return
     */
    public static BaseResponse<Boolean> error(Integer responseCode, String message) {
        return new BaseResponse<>(responseCode, false, message);
    }

}
