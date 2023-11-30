package top.alexmmd.common.base.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import top.alexmmd.common.base.web.application.BaseAppService;

@SuppressWarnings("rawtypes")
public class BaseController<AppService extends BaseAppService> {

    @Autowired
    public AppService appService;

}
