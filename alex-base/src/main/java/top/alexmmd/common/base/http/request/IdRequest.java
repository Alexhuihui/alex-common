package top.alexmmd.common.base.http.request;

import lombok.Data;

@Data
public class IdRequest<T> {

    //主键
    T id;

}