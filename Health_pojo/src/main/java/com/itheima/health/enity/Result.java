package com.itheima.health.enity;

import java.io.Serializable;

/**
 * @author ${dong}
 * @date 2019/12/31 23:17
 */
public class Result implements Serializable {
    private boolean flag;//执行结果,true为执行成功，false为执行 失败
    private String message;//返回结果信息
    private Object data;//返回数据

    public Result(boolean flag, String message) {//保存、修改时不需要数据，只需要传状态和信息
        super();
        this.flag = flag;
        this.message = message;
    }

    public Result(boolean flag, String message, Object data) {//查询回显的数据
        this.flag = flag;
        this.message = message;
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
