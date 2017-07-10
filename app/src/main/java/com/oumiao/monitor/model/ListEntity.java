package com.oumiao.monitor.model;

import java.io.Serializable;
import java.util.List;

public interface ListEntity<T extends Entity> extends Serializable {

    public List<T> getList();
    public int getResultCode();

    /**
     * code == 1，代表成功，其他代表错误
     * @param code
     */
    public void setResultCode(int code);

}
