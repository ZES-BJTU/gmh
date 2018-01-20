package com.zes.squad.gmh.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCodeEnum {

    BUSINESS_EXCEPTION(10000, "业务异常"),
    BUSINESS_EXCEPTION_INVALID_PARAMETER(10001, "参数无效"),
    BUSINESS_EXCEPTION_ENTITY_NOT_FOUND(10002, "未找到主体对象"),
    BUSINESS_EXCEPTION_EMPTY_COLLECTION(10003, "数据集合为空"),
    BUSINESS_EXCEPTION_ILLEGAL_STATE(10004, "状态非法"),
    BUSINESS_EXCEPTION_OPERATION_NOT_ALLOWED(10005, "操作不允许"),
    BUSINESS_EXCEPTION_OPERATION_NOT_SUPPORTED(10006, "操作不支持"),
    BUSINESS_EXCEPTION_OPERATION_FAILED(10007, "操作失败"),
    BUSINESS_EXCEPTION_CONDITION_NOT_SUPPORTED(10008, "条件不支持"),

    WEB_EXCEPTION(20000, "web异常"),
    WEB_EXCEPTION_AUTH_FAILED(20001, "web认证失败"),

    CACHE_EXCEPTION(30000, "缓存异常"),
    CACHE_EXCEPTION_SERIALIZE_FAILED(30001, "缓存序列化异常"),
    CACHE_EXCEPTION_DESERIALIZE_FAILED(30002, "缓存反序列化异常"),

    PERSIST_EXCEPTION(40000, "持久化异常"),
    PERSIST_EXCEPTION_INSERT_FAILED(40001, "持久化插入数据异常"),
    PERSIST_EXCEPTION_DELETE_FAILED(40002, "持久化删除数据异常"),
    PERSIST_EXCEPTION_UPDATE_FAILED(40003, "持久化更新数据异常"),
    PERSIST_EXCEPTION_SELECT_FAILED(40004, "持久化查询数据异常"),

    TASK_EXCEPTION(50000, "任务异常"),

    UNKNOWN_EXCEPTION(70000, "未知异常");

    private int    code;
    private String message;

}
