package top.alexmmd.common.db.config;


import cn.hutool.core.util.StrUtil;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@Slf4j
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
class IntegerListToVarcharTypeHandler implements TypeHandler<List<Integer>> {

    private List<Integer> stringToList(String str) {
        if (StrUtil.isEmpty(str)) {
            return new ArrayList<>();
        }
        return Arrays.stream(str.split(",")).filter(this::canParseInt).map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, List<Integer> parameter,
            JdbcType jdbcType) throws SQLException {
        ps.setString(i, dealListToOneStr(parameter));
    }

    /**
     * 集合拼接字符串
     *
     * @param parameter
     * @return
     */
    private String dealListToOneStr(List<Integer> parameter) {
        if (parameter == null || parameter.size() <= 0) {
            return null;
        }
        String res = "";
        for (int i = 0; i < parameter.size(); i++) {
            if (i == parameter.size() - 1) {
                res += parameter.get(i);
                return res;
            }
            res += parameter.get(i) + ",";
        }
        return null;
    }


    @Override
    public List<Integer> getResult(ResultSet rs, String columnName) throws SQLException {
        return stringToList(rs.getString(columnName));
    }

    @Override
    public List<Integer> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return stringToList(rs.getString(columnIndex));
    }

    @Override
    public List<Integer> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return stringToList(cs.getString(columnIndex));
    }

    /**
     * 判断是否可以转换成Integer
     */
    private boolean canParseInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
