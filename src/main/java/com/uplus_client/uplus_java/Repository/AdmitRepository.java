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

        String query = "SELECT " +
                            "DISTINCT " +
                            "format_hn(patient.hn) AS hn_number, " +
                            "patient.prename, " +
                            "patient.firstname, " + 
                            "patient.lastname, " +
                            "patient.birthdate AS date_of_birth, " +
                            "vital_sign_opd.height, " +
                            "vital_sign_opd.weight, " +
                            "patient.fix_nationality_id AS nationality, " + 
                            "patient.fix_race_id AS citizenship, " +
                            "patient.religion, " +
                            "patient.pid AS id_number, " +
                            "patient.fix_occupation_id AS occupation, " +
                            "patient.mobile AS tel_no, " +
                            "patient.blood_group AS blood_type, " +
                            "nt_allergy.base_nt_allergy_id AS allergy, " +
                            "diagnosis_icd10.beginning_diagnosis AS diagnosis, " +
                            "admit.admit_date AS date_of_admission, " +
                            "admit.admit_time, " +
                            "bed_management.bed_number AS room_number, " +
                            "base_service_point.base_site_branch_id AS building, " +
                            "bed_management.base_service_point_id AS ward, " +
                            "nt_patient_nutrition.extra_note AS remark, " +
                            "nt_patient_nutrition.base_nt_type_id AS diet_type, " +
                            "nt_patient_nutrition.base_nt_food_category_id AS category_of_food, " +
                            "ipd_attending_physician.employee_id AS doctor_name " +
                        "FROM " +
                            "patient " +
                        "INNER JOIN visit visit ON " +
                            "patient.patient_id = visit.patient_id " +
                        "INNER JOIN public.vital_sign_opd vital_sign_opd ON " +
                            "visit.visit_id = vital_sign_opd.visit_id " +
                        "INNER JOIN public.nt_patient_nutrition nt_patient_nutrition ON " +
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
                            "(vital_sign_opd.height, " +
                            "vital_sign_opd.weight, " +
                            "vital_sign_opd.measure_date) " + 
                            "IN ( " +
                            "SELECT " +
                                "height, " +
                                "weight, " +
                                "max(measure_date) " +
                            "FROM " +
                                "public.vital_sign_opd " +
                            "WHERE " +
                                "height <> '' " +
                                "AND weight <> '' " +
                            "GROUP BY " +
                                "height, " +
                                "weight) " +
                        "ORDER BY " +
                            "admit.admit_date DESC; ";

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
