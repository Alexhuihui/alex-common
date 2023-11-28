package top.alexmmd.common.base.web.mapper;

public interface Mapper<T> {

    public int insert(T entity);

    public int updateById(T entity);
}
