package com.uplus_client.uplus_java.Repository.IMED;

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

    public DischargeRepository(){
        
    }

    public List<LinkedHashMap<String, Object>> getDischargeDataFromDB(){
        List<LinkedHashMap<String, Object>> list = null;
        LinkedHashMap<String, Object> lnkMap = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT " +
                            "DISTINCT " + 
                            "format_hn(patient.hn) AS hn_number, " + 
                            "bed_management.bed_number AS room_number, " +
                            "bed_management.base_service_point_id AS ward, " +
                            "visit.financial_discharge AS discharge_notify, " +
                            "visit.financial_discharge_date AS discharge_date, " +
                            "visit.financial_discharge_time AS discharge_time " +
                        "FROM " +
                            "patient " +
                        "INNER JOIN visit visit ON " +
                            "patient.patient_id = visit.patient_id " +
                        "INNER JOIN vital_sign_opd vital_sign_opd ON " +
                            "visit.visit_id = vital_sign_opd.visit_id " +
                        "INNER JOIN nt_patient_nutrition nt_patient_nutrition ON " +
                            "nt_patient_nutrition.patient_id = patient.patient_id " +
                        "INNER JOIN nt_allergy nt_allergy ON " +
                            "nt_allergy.nt_patient_nutrition_id = nt_patient_nutrition.nt_patient_nutrition_id " +
                        "INNER JOIN diagnosis_icd10 diagnosis_icd10 ON " +
                            "diagnosis_icd10.visit_id = visit.visit_id " +
                        "INNER JOIN admit admit ON " +
                            "admit.patient_id = patient.patient_id " +
                        "INNER JOIN bed_management bed_management ON " +
                            "bed_management.admit_id = admit.admit_id " +
                        "INNER JOIN base_service_point base_service_point ON " +
                            "base_service_point.base_department_id = admit.base_department_id " +
                        "INNER JOIN ipd_attending_physician ipd_attending_physician ON " +
                            "ipd_attending_physician.admit_id = admit.admit_id " +
                        "WHERE " +
                            "visit.financial_discharge = '1' " +
                        "ORDER BY " +
                            "discharge_date DESC ";

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
