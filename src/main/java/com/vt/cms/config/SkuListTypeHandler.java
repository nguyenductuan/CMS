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
    /**
     * Thiết lập hoặc cập nhật set non null parameter. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void setNonNullParameter(
            PreparedStatement ps,
            int i,
            List<SkuResponse> parameter,
            JdbcType jdbcType
    ) throws SQLException {
        try {
            ps.setString(i, objectMapper.writeValueAsString(parameter));
        } catch (Exception e) {
            /**
             * Thực hiện xử lý sqlexception. Tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new SQLException("Cannot convert SKU list to JSON", e);
        }
    }

    @Override
    /**
     * Lấy dữ liệu get nullable result. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public List<SkuResponse> getNullableResult(
            ResultSet rs,
            String columnName
    ) throws SQLException {
        /**
         * Phân tích dữ liệu parse. Tham số và kết quả tuân theo chữ ký phương thức.
         */
        return parse(rs.getString(columnName));
    }

    @Override
    /**
     * Lấy dữ liệu get nullable result. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public List<SkuResponse> getNullableResult(
            ResultSet rs,
            int columnIndex
    ) throws SQLException {
        /**
         * Phân tích dữ liệu parse. Tham số và kết quả tuân theo chữ ký phương thức.
         */
        return parse(rs.getString(columnIndex));
    }

    @Override
    /**
     * Lấy dữ liệu get nullable result. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public List<SkuResponse> getNullableResult(
            CallableStatement cs,
            int columnIndex
    ) throws SQLException {
        /**
         * Phân tích dữ liệu parse. Tham số và kết quả tuân theo chữ ký phương thức.
         */
        return parse(cs.getString(columnIndex));
    }

    /**
     * Phân tích dữ liệu parse. Tham số và kết quả tuân theo chữ ký phương thức.
     */
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
            /**
             * Thực hiện xử lý sqlexception. Tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new SQLException(
                    "Cannot parse SKU JSON: " + json,
                    e
            );
        }
    }
}