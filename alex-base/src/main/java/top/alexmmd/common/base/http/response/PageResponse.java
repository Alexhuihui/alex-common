package top.alexmmd.common.base.http.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import top.alexmmd.common.base.constant.StatusCode;

/**
 * @author wangyonghui
 * @since 2022年12月28日 15:41:00
 */
@Getter
@Setter
public class PageResponse<T> extends BaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    TableData<T> data;

    public PageResponse() {
        this.data = new TableData<T>(0L, Collections.emptyList());
    }

    public PageResponse(List<T> records, long total) {
        this.data = new TableData<T>(total, records);
    }

    public PageResponse(long total, long page, long limit, List<T> records) {
        this.data = new TableData<T>(total, page, limit, records);
    }

    @Override
    public PageResponse<T> code(StatusCode code) {
        this.setCode(code.getCode());
        this.setMessage(code.getMessage());
        return this;
    }

    @Override
    public PageResponse<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    public PageResponse<T> total(int total) {
        data.setTotal(total);
        return this;
    }

    public PageResponse<T> current(int page) {
        data.setCurrent(page);
        return this;
    }

    public PageResponse<T> size(int limit) {
        data.setSize(limit);
        return this;
    }

    public PageResponse<T> totalPage(int totalPage) {
        data.setTotalPage(totalPage);
        return this;
    }

    public PageResponse<T> records(List<T> records) {
        data.setRecords(records);
        return this;
    }

    @Getter
    @Setter
    public static class TableData<T> {

        public TableData() {
        }

        private List<T> records = new ArrayList<>();
        private long current;
        private long size = 20;
        private long total;
        private long totalPage;

        public TableData(Long total, List<T> records) {
            this.total = total;
            this.records = records;
        }

        public TableData(long total, long current, long size, List<T> records) {
            this.records = records;
            this.current = current;
            this.size = size;
            this.total = total;
        }

        public long getTotalPage() {
            totalPage = 0;
            if (isNormal(size)) {
                if (total % size == 0) {
                    totalPage = total / size;
                } else {
                    totalPage = total / size + 1;
                }
            }
            return totalPage;

        }

        private boolean isNormal(Long value) {
            return !(value == null || value.equals(0L));
        }

    }

}
