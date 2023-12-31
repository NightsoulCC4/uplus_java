package com.uplus_client.uplus_java.Repository.H2;

import java.sql.*;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.uplus_client.uplus_java.Utility.Utility;

@Repository
public class H2DischargeRepository {
    @Value("${spring.datasource.url}")
    private String h2_url;

    @Value("${spring.datasource.username}")
    private String h2_username;

    @Value("${spring.datasource.password}")
    private String h2_password;

    Logger log = LogManager.getLogger(H2DischargeRepository.class);

    H2DischargeRepository() {

    }

    // Get back up discharge data for comparing to the current data.
    public String getDischargeBackupData() {
        String list = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT " +
                "           discharge_dataset, " +
                "       FROM " +
                "           discharge_backup " +
                "       ORDER BY " +
                "           time DESC " +
                "       LIMIT 1;";

        try {
            con = DriverManager.getConnection(h2_url, h2_username, h2_password);
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            if (rs.next())
                list = rs.getString("discharge_dataset");

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

    // Insert discharge data if the data changed.
    public boolean putDischargeBackupData(String json) {
        Connection con = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO " +
                "           discharge_backup " +
                "       ( " +
                "           discharge_dataset, " +
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
