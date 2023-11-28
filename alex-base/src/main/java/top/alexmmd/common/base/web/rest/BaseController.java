package top.alexmmd.common.base.web.rest;

import jakarta.annotation.Resource;
import top.alexmmd.common.base.web.application.BaseAppService;

@SuppressWarnings("rawtypes")
public class BaseController<AppService extends BaseAppService> {

    @Resource
    public AppService appService;

}
