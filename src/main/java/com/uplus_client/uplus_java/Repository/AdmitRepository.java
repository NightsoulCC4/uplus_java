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
public class AdmitRepository {
    @Value("${his.datasource.jdbc-url}")
    private String hisDatasourceJdbcUrl;
    @Value("${his.datasource.username}")
    private String hisDatasourceUsername;
    @Value("${his.datasource.password}")
    private String hisDatasourcePassword;
    public List<LinkedHashMap<String, Object>> getAdmitDataFromDB(){
        List<LinkedHashMap<String, Object>> list = null;
        LinkedHashMap<String, Object> lnkMap = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT format_hn(patient.hn) as hn_number, " +
                        "patient.prename, " +
                        "patient.firstname, " + 
                        "patient.lastname, " +
                        "patient.birthdate as date_of_birth, " +
                        "vso.height, " +
                        "vso.weight, " +
                        "patient.fix_nationality_id as nationality, " +
                        "patient.fix_race_id as citizenship, " +
                        "patient.religion, " +
                        "patient.pid as id_number, " +
                        "patient.fix_occupation_id as occupation, " +
                        "patient.mobile as tel_no, " +
                        "patient.blood_group as blood_type, " +
                        "na.base_nt_allergy_id as allergy, " +
                        "di.beginning_diagnosis as diagnosis, " +
                        "a.admit_date as date_of_admission, " +
                        "a.admit_time, " +
                        "bm.bed_number as room_number, " +
                        "bsp.base_site_branch_id as building, " +
                        "bm.base_service_point_id as ward, " +
                        "npn.extra_note as remark, " +
                        "npn.base_nt_type_id as diet_type, " +
                        "npn.base_nt_food_category_id as category_of_food, " +
                        "iap.employee_id as doctor_name " +
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
                        "INNER JOIN base_service_point bsp " + 
                        "ON bsp.base_department_id = a.base_department_id " +
                        "INNER JOIN ipd_attending_physician iap " +
                        "ON iap.admit_id = a.admit_id " +
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
                lnkMap.put("prefix_name", rs.getString("prename"));
                lnkMap.put("first_name", rs.getString("firstname"));
                lnkMap.put("last_name", rs.getString("lastname"));
                lnkMap.put("birth_day", Utility.getDayFromDate(rs.getString("date_of_birth")));
                lnkMap.put("birth_month", Utility.getMonthFromDate(rs.getString("date_of_birth")));
                lnkMap.put("birth_year", Utility.getYearFromDate(rs.getString("date_of_birth")));
                lnkMap.put("birth_date", rs.getString("date_of_birth"));
                lnkMap.put("height", rs.getString("height"));
                lnkMap.put("weight", rs.getString("weight"));
                lnkMap.put("nationality", rs.getString("nationality"));
                lnkMap.put("citizenship", rs.getString("citizenship"));
                lnkMap.put("religion", rs.getString("religion"));
                lnkMap.put("id_number", rs.getString("id_number"));
                lnkMap.put("occupation", rs.getString("occupation"));
                lnkMap.put("tel_no", rs.getString("tel_no"));
                lnkMap.put("blood_type", rs.getString("blood_type"));
                lnkMap.put("allergy", rs.getString("allergy"));
                lnkMap.put("diagnosis", rs.getString("diagnosis"));
                lnkMap.put("date_of_admission", Utility.getTimezone(rs.getString("date_of_admission"), rs.getString("admit_time")));
                lnkMap.put("room_number", rs.getString("room_number"));
                lnkMap.put("building", rs.getString("building"));
                lnkMap.put("ward", rs.getString("ward"));
                lnkMap.put("remark", rs.getString("remark"));
                lnkMap.put("diet_type", rs.getString("diet_type"));
                lnkMap.put("category_of_food", rs.getString("category_of_food"));
                lnkMap.put("doctor_name", rs.getString("doctor_name"));
                
                list.add(lnkMap);
            }

            rs.close();
            rs = null;
            ps.close();
            ps = null;
            con.close();
            con = null;
        } catch (SQLException e) {
            e.printStackTrace();
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
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

        return list;
    }

    public LinkedHashMap<String, String> getMonitorInterfaceDataFromDB(){
        LinkedHashMap<String, String> list = new LinkedHashMap<>();

        return list;
    }
    
    public LinkedHashMap<String, String> getOrderSummaryDataFromDB(){
        LinkedHashMap<String, String> list = new LinkedHashMap<>();

        return list;
    }
}
