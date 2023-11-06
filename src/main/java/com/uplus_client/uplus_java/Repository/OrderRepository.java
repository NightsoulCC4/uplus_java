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

        String query = "SELECT " +
                            "format_hn(patient.hn) AS hn_number, " +
                            "bed_management.bed_number AS room_number, " +
                            "bed_management.base_service_point_id AS ward, " +
                            "nt_patient_nutrition.base_nt_type_id AS diet_type, " +
                            "nt_patient_nutrition.note AS processing_note, " +
                            "nt_patient_nutrition.extra_note AS remark, " +
                            "ipd_attending_physician.employee_id AS doctor_name, " +
                            "diagnosis_icd10.beginning_diagnosis AS diagnosis, " +
                            "nt_allergy.base_nt_allergy_id AS allergy, " +
                            "nt_order.base_nt_food_type_id AS foodType, " +
                            "nt_order.fix_order_status_id AS orderItemStatus, " +
                            "nt_order.dispense_date AS order_date, " +
                            "nt_order.dispense_time AS order_time " +
                        "FROM " +
                            "patient " +
                        "INNER JOIN public.admit admit ON " +
                            "admit.patient_id = patient.patient_id " +
                        "INNER JOIN public.bed_management bed_management ON " +
                            "bed_management.admit_id = admit.admit_id " +
                        "INNER JOIN public.nt_patient_nutrition nt_patient_nutrition ON " +
                            "nt_patient_nutrition.patient_id = patient.patient_id " +
                        "INNER JOIN public.ipd_attending_physician ipd_attending_physician ON " +
                            "ipd_attending_physician.admit_id = admit.admit_id " +
                        "INNER JOIN public.visit visit ON " +
                            "visit.visit_id = admit.visit_id " +
                        "INNER JOIN public.diagnosis_icd10 diagnosis_icd10 ON " +
                            "diagnosis_icd10.visit_id = visit.visit_id " +
                        "INNER JOIN public.nt_allergy nt_allergy ON " +
                            "nt_allergy.nt_patient_nutrition_id = nt_patient_nutrition.nt_patient_nutrition_id " +
                        "INNER JOIN public.nt_order nt_order ON " +
                            "nt_order.visit_id = visit.visit_id " +
                        "ORDER BY " +
                            "nt_order.dispense_date DESC " +
                        "LIMIT 100;";

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
                lnkMap.put("remark", rs.getString("remark"));
                lnkMap.put("doctor_name", rs.getString("doctor_name"));
                lnkMap.put("diagnosis", rs.getString("diagnosis"));
                lnkMap.put("allergy", rs.getString("allergy"));
                lnkMap.put("foodType", rs.getString("foodType"));
                lnkMap.put("orderItemStatus", rs.getString("orderItemStatus"));
                lnkMap.put("order_date", Utility.isDateNull(rs.getString("order_date")) ? "dd/mm/yyyy" : Utility.getTimezone(rs.getString("order_date"), rs.getString("order_time")));

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
