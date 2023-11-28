package top.alexmmd.common.db.assembler;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import top.alexmmd.common.base.constant.BaseStatusCode;
import top.alexmmd.common.base.exception.BaseException;
import top.alexmmd.common.base.http.response.PageResponse;

@Slf4j
public class PageResponseAssembler {

    public static <T> PageResponse<T> convert(IPage<?> page, Class<T> target) {
        List<T> resultList = BeanUtil.copyToList(page.getRecords(), target);
        return new PageResponse<>(page.getTotal(), page.getCurrent(), page.getSize(), resultList);
    }

    public static <T> IPage<T> convert(PageResponse<T> response) {
        if (!BaseStatusCode.SUCCESS.getCode().equals(response.getCode())) {
            String message = String.format("class:%s rpc happen Exception:%s",
                    Thread.currentThread().getStackTrace()[2].getClass().getName(),
                    response.getMessage());
            log.error("Exception:{}, Response:{}", message, response);
            throw new BaseException(message);
        }
        IPage<T> page = new Page<>();
        page.setRecords(response.getData().getRecords());
        page.setPages(response.getData().getTotalPage());
        page.setTotal(response.getData().getTotal());
        page.setCurrent(response.getData().getCurrent());
        page.setSize(response.getData().getSize());
        return page;
    }

}
