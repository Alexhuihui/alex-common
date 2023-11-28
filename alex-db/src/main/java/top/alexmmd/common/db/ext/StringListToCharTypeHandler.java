package top.alexmmd.common.db.ext;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.CHAR})
public class StringListToCharTypeHandler implements TypeHandler<List<String>> {

    private List<String> stringToList(String str) {
        if (StrUtil.isEmpty(str)) {
            return new ArrayList<>();
        }
        return Arrays.stream(str.split(",")).collect(Collectors.toList());
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, Joiner.on(",").join(parameter));
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        return stringToList(rs.getString(columnName));
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return stringToList(rs.getString(columnIndex));
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return stringToList(cs.getString(columnIndex));
    }
}
