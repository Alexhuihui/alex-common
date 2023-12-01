package top.alexmmd.common.base.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.alexmmd.common.base.utils.EntityUtils;
import top.alexmmd.common.base.web.mapper.Mapper;
import top.alexmmd.common.base.web.support.IDGenerator;
import top.alexmmd.common.base.web.support.ISecurityContext;

@Slf4j
public abstract class BaseBiz<M extends Mapper<T>, T> {


    @Autowired
    protected M mapper;

    @Autowired
    protected IDGenerator idGenerator;

    @Autowired
    protected ISecurityContext securityContext;

    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public M getMapper() {
        return this.mapper;
    }

    public int insert(T entity) {
        try {
            EntityUtils.setCreatAndUpdateInfo(securityContext, idGenerator, entity);
        } catch (Exception e) {
            EntityUtils.setCrtTime(entity);
        }
        return mapper.insert(entity);
    }

    public int updateById(T entity) {
        try {
            EntityUtils.setUpdatedInfo(securityContext, entity);
        } catch (Exception e) {
            EntityUtils.setUpdTime(entity);
        }
        return mapper.updateById(entity);
    }

}
