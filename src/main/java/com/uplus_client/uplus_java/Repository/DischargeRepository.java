package com.uplus_client.uplus_java.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.uplus_client.uplus_java.Utility.Utility;

@Repository
public class DischargeRepository {
    @Value("${his.datasource.jdbc-url}")
    private String hisDatasourceJdbcUrl;
    @Value("${his.datasource.username}")
    private String hisDatasourceUsername;
    @Value("${his.datasource.password}")
    private String hisDatasourcePassword;
    public List<LinkedHashMap<String, Object>> getDischargeDataFromDB(){
        List<LinkedHashMap<String, Object>> list = null;
        LinkedHashMap<String, Object> lnkMap = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT format_hn(patient.hn) as hn_number, " + 
                        "bm.bed_number as room_number, " +
                        "bm.base_service_point_id as ward, " +
                        "v.financial_discharge as discharge_notify, " +
                        "v.financial_discharge_date as discharge_date, " +
                        "v.financial_discharge_time as discharge_time " +
                        "FROM patient " +
                        "INNER JOIN visit v " +
                        "ON patient.patient_id = v.patient_id " +
                        "INNER JOIN vital_sign_opd vso " +
                        "ON v.visit_id  = vso.visit_id " +
                        "INNER JOIN admit a " +
                        "ON a.patient_id = patient.patient_id " +
                        "INNER JOIN bed_management bm " +
                        "ON bm.admit_id = a.admit_id " +
                        "WHERE v.financial_discharge = '1'";
        try {
            con = DriverManager.getConnection(hisDatasourceJdbcUrl, hisDatasourceUsername, hisDatasourcePassword);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            for (; rs.next();) {

                if (list == null)
                    list = new ArrayList<>();

                lnkMap = new LinkedHashMap<>();

                lnkMap.put("hn_number", rs.getString("hn_number"));
                lnkMap.put("room_number", rs.getString("room_number"));
                lnkMap.put("ward", rs.getString("ward"));
                lnkMap.put("discharge_notify", rs.getString("discharge_notify"));
                lnkMap.put("discharge_date", Utility.getTimezone(rs.getString("discharge_date"), rs.getString("discharge_time")));

                list.add(lnkMap);
            }

            rs.close();
            rs = null;
            ps.close();
            ps = null;
            con.close();
            con = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }

        return list;
    }
}
