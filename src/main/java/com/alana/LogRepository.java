package com.alana;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogRepository {
    public void inserir (Connection conn, LogEntry entry) throws SQLException {
        String sql = "INSERT INTO tb_log_entries (ip, data_hora, metodo, endpoint, status_code) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entry.getIp());
            ps.setString(2, entry.getTimestamp());
            ps.setString(3, entry.getMethod());
            ps.setString(4, entry.getEndpoint());
            ps.setInt(5, entry.getStatusCode());
            ps.executeUpdate();
        }
    }
}
