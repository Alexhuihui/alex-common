package top.alexmmd.common.base.web.application;


import jakarta.annotation.Resource;
import top.alexmmd.common.base.web.service.BaseBiz;

@SuppressWarnings("rawtypes")
public class BaseAppService<Biz extends BaseBiz, Entity> {

    @Resource
    protected Biz bizService;

}
