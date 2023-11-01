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

@Repository
public class OrderRepository {
    @Value("${his.datasource.jdbc-url}")
    private String hisDatasourceJdbcUrl;
    @Value("${his.datasource.username}")
    private String hisDatasourceUsername;
    @Value("${his.datasource.password}")
    private String hisDatasourcePassword;
    public List<LinkedHashMap<String, Object>> getOrderDataFromDB(){
        List<LinkedHashMap<String, Object>> list = null;
        LinkedHashMap<String, Object> lnkMap = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT format_hn(patient.hn) as hn_number, " +
                        "bm.bed_number as room_number, " +
                        "bm.base_service_point_id as ward, " +
                        "npn.base_nt_type_id as diet_type, " +
                        "npn.note as processing_note, " +
                        "npn.extra_note as remark, " +
                        "iap.employee_id as doctor_name, " +
                        "di.beginning_diagnosis as diagnosis, " +
                        "na.base_nt_allergy_id as allegy, " +
                        "no2.base_nt_food_type_id as foodType, " +
                        "no2.fix_order_status_id as orderItemStatus, " +
                        "no2.dispense_date as order_date, " +
                        "no2.dispense_time as order_time " +
                        "FROM patient " +
                        "INNER JOIN visit v " + 
                        "ON patient.patient_id = v.patient_id " + 
                        "INNER JOIN vital_sign_opd vso " + 
                        "ON v.visit_id  = vso.visit_id " + 
                        "INNER JOIN nt_patient_nutrition npn " +
                        "ON npn.patient_id = patient.patient_id " +
                        "INNER JOIN nt_allergy na " + 
                        "ON na.nt_patient_nutrition_id = npn.nt_patient_nutrition_id " +
                        "INNER JOIN diagnosis_icd10 di " +
                        "ON di.visit_id = v.visit_id " +
                        "INNER JOIN admit a " +
                        "ON a.patient_id = patient.patient_id " +
                        "INNER JOIN bed_management bm " +
                        "ON bm.admit_id = a.admit_id " +
                        "INNER JOIN ipd_attending_physician iap " +
                        "ON iap.admit_id = a.admit_id " +
                        "INNER JOIN nt_order no2 " +
                        "ON no2.visit_id = v.visit_id " +
                        "WHERE v.financial_discharge = '1' " +
                        "LIMIT 100";

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
                lnkMap.put("diet_type", rs.getString("diet_type"));
                lnkMap.put("processing_note", rs.getString("processing_note"));

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
