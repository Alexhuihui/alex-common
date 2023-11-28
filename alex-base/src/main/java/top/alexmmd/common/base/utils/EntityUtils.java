package top.alexmmd.common.base.utils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import top.alexmmd.common.base.web.support.IDGenerator;
import top.alexmmd.common.base.web.support.ISecurityContext;

/**
 * 自动设置更新人创建人及时间信息
 */
@Slf4j
public class EntityUtils {

    /**
     * 数组中日期位置
     */
    private static final int POSITION_DATE = 2;

    /**
     * 设置创建和更新信息id,crtName, crtId, crtTime、updName, updId,updTime
     */
    public static <T> void setCreatAndUpdateInfo(ISecurityContext securityContext,
            IDGenerator idGenerator,
            T entity) {
        setEntityId(entity, idGenerator);
        setCreateInfo(securityContext, entity);
        setUpdatedInfo(securityContext, entity);
    }

    /**
     * 设置创建信息
     */
    public static <T> void setCreateInfo(ISecurityContext securityContext, T entity) {
        Long userId = securityContext.getId();
        String crtName = securityContext.getName();
        Date crtTime = new Date();
        LocalDateTime now = LocalDateTime.now();
        // 默认属性
        String[] fieldNames = {"crtName", "crtId", "crtTime"};
        Field field = ReflectUtil.getField(entity.getClass(), "crtTime");
        // 默认值
        Object[] value = new Object[]{crtName, userId, null};
        if (field != null) {
            try {
                Object val = field.get(entity);
                if (field.getType().equals(Date.class)) {
                    crtTime = val == null ? crtTime : (Date) val;
                    value[POSITION_DATE] = crtTime;
                } else if (field.getType().equals(LocalDateTime.class)) {
                    now = val == null ? now : (LocalDateTime) val;
                    value[POSITION_DATE] = now;
                }
            } catch (Exception e) {
            }
        }
        // 填充默认属性值
        setDefaultValues(entity, fieldNames, value);
    }

    /**
     * 设置更新信息
     */
    public static <T> void setUpdatedInfo(ISecurityContext securityContext, T entity) {
        Long userId = securityContext.getId();
        String updName = securityContext.getName();
        // 默认属性
        String[] fieldNames = {"updName", "updId", "updTime"};
        Field field = ReflectUtil.getField(entity.getClass(), "updTime");
        Object[] value = new Object[]{updName, userId, null};
        if (field != null) {
            if (field.getType().equals(Date.class)) {
                value[POSITION_DATE] = new Date();
            } else if (field.getType().equals(LocalDateTime.class)) {
                value[POSITION_DATE] = LocalDateTime.now();
            }
        }
        // 填充默认属性值
        setDefaultValues(entity, fieldNames, value);
    }

    /**
     * 设置对象ID
     */
    public static <T> void setEntityId(T entity, IDGenerator idGenerator) {

        if (isPKNotNull(entity, "id")) {
            String[] fieldNames = {"id"};
            Object[] value = new Object[]{null};
            Field field = ReflectUtil.getField(entity.getClass(), "id");
            if (field != null) {
                if (field.getType().equals(String.class)) {
                    value[0] = idGenerator.stringId();
                } else {
                    value[0] = idGenerator.longId();
                }
                setDefaultValues(entity, fieldNames, value);
            }
        }

    }

    /**
     * 设置创建时间
     */
    public static <T> void setCrtTime(T entity) {
        String[] fieldNames = {"crtTime"};
        Field field = ReflectUtil.getField(entity.getClass(), "crtTime");
        Object[] value = new Object[]{null};
        if (field != null) {
            if (field.getType().equals(Date.class)) {
                value[0] = new Date();
            } else if (field.getType().equals(LocalDateTime.class)) {
                value[0] = LocalDateTime.now();
            }
        }
        setDefaultValues(entity, fieldNames, value);
    }

    /**
     * 设置更新时间
     */
    public static <T> void setUpdTime(T entity) {
        String[] fieldNames = {"updTime"};
        Field field = ReflectUtil.getField(entity.getClass(), "updTime");
        Object[] value = new Object[]{null};
        if (field != null) {
            if (field.getType().equals(Date.class)) {
                value[0] = new Date();
            } else if (field.getType().equals(LocalDateTime.class)) {
                value[0] = LocalDateTime.now();
            }
        }
        setDefaultValues(entity, fieldNames, value);
    }

    /**
     * 设置对象属性
     */
    private static <T> void setDefaultValues(T entity, String[] fields, Object[] value) {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            try {
                if (ReflectUtil.hasField(entity.getClass(), field)) {
                    ReflectUtil.setFieldValue(entity, field, value[i]);
                }
            } catch (Exception e) {
                log.error("审计字段异常：{}", e);
                sb.append(field).append(" ");
            }
        }
        if (!sb.toString().isEmpty()) {
            log.error("{}部分字段审计失败: {}", entity.getClass().getName(), sb.toString());
        }
    }

    /**
     * 根据主键属性，判断主键是否值为空
     */
    public static <T> boolean isPKNotNull(T entity, String field) {
        if (!ReflectUtil.hasField(entity.getClass(), field)) {
            return false;
        }
        Object value = ReflectUtil.getFieldValue(entity, field);
        return ObjectUtils.isEmpty(value);
    }
}
