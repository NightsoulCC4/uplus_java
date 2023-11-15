package com.uplus_client.uplus_java.Repository.H2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.uplus_client.uplus_java.Utility.Utility;

@Repository
public class H2Repository {

    @Value("${spring.datasource.url}")
    private String h2_url;

    @Value("${spring.datasource.username}")
    private String h2_username;

    @Value("${spring.datasource.password}")
    private String h2_password;

    Logger log = LogManager.getLogger(H2Repository.class);

    H2Repository() {

    }

    public String getBackupData() {
        String list = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        log.info("\nURL: " + h2_url);

        String query = "SELECT " +
                "           json_data, " +
                "           time " +
                "       FROM " +
                "           json_data_table " +
                "       ORDER BY " +
                "           time DESC " +
                "       LIMIT 1;";

        try {
            con = DriverManager.getConnection(h2_url, h2_username, h2_password);
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            if (rs.next())
                list = rs.getString("JSON_DATA");

            con.close();
            con = null;
            ps.close();
            ps = null;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                con = null;
            if (ps != null)
                ps = null;
        }

        return list;
    }

    public boolean putBackupData(String json) {
        Connection con = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO " +
                "           JSON_DATA_TABLE " +
                "       ( " +
                "           json_data, " +
                "           time " +
                "       ) " +
                "       VALUES " +
                "       ( " +
                "           ?, " +
                "           ? " +
                "       );";

        try {
            con = DriverManager.getConnection(h2_url, h2_username, h2_password);
            ps = con.prepareStatement(query);

            ps.setString(1, json);
            ps.setString(2, Utility.getCurrentTime());

            ps.executeUpdate();

            con.close();
            con = null;
            ps.close();
            ps = null;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                con = null;
            if (ps != null)
                ps = null;
        }

        return true;
    }
}
