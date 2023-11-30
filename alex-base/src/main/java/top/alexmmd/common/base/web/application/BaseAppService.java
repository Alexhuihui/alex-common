package top.alexmmd.common.base.web.application;


import org.springframework.beans.factory.annotation.Autowired;
import top.alexmmd.common.base.web.service.BaseBiz;

@SuppressWarnings("rawtypes")
public class BaseAppService<Biz extends BaseBiz, Entity> {

    @Autowired
    protected Biz bizService;

}
