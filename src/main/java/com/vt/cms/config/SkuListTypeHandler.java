package com.vt.cms.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vt.cms.model.resp.SkuResponse;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.Collections;
import java.util.List;

public class SkuListTypeHandler extends BaseTypeHandler<List<SkuResponse>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(
            PreparedStatement ps,
            int i,
            List<SkuResponse> parameter,
            JdbcType jdbcType
    ) throws SQLException {
        try {
            ps.setString(i, objectMapper.writeValueAsString(parameter));
        } catch (Exception e) {
            throw new SQLException("Cannot convert SKU list to JSON", e);
        }
    }

    @Override
    public List<SkuResponse> getNullableResult(
            ResultSet rs,
            String columnName
    ) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public List<SkuResponse> getNullableResult(
            ResultSet rs,
            int columnIndex
    ) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public List<SkuResponse> getNullableResult(
            CallableStatement cs,
            int columnIndex
    ) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    private List<SkuResponse> parse(String json) throws SQLException {

        if (json == null || json.isBlank() || json.equals("[]")) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(
                    json,
                    new TypeReference<List<SkuResponse>>() {}
            );
        } catch (Exception e) {
            throw new SQLException(
                    "Cannot parse SKU JSON: " + json,
                    e
            );
        }
    }
}