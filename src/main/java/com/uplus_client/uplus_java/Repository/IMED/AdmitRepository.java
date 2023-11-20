package com.uplus_client.uplus_java.Repository.IMED;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.*;
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

    Logger log = LogManager.getLogger(AdmitRepository.class);

    public AdmitRepository() {

    }

    public List<LinkedHashMap<String, Object>> getAdmitDataFromDB() {
        List<LinkedHashMap<String, Object>> list = null;
        LinkedHashMap<String, Object> lnkMap = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT " +
                       "     format_hn(patient.hn) AS hn_number, " +
                       "     patient.prename, " +
                       "     patient.firstname, " +
                       "     patient.lastname, " +
                       "     patient.birthdate AS date_of_birth, " +
                       "     CASE " +
                       "         WHEN vital_sign_opd.height <> '' THEN vital_sign_opd.height || ' เซนติเมตร'" +
                       "         ELSE " +
                       "             'N/A' " +
                       "     END AS height, " +
                       "     CASE " +
                       "         WHEN vital_sign_opd.weight <> '' THEN vital_sign_opd.weight || ' กิโลกรัม'" +
                       "         ELSE " +
                       "             'N/A' " +
                       "     END AS weight, " +
                       "     '' AS nationality, " +
                       "     CASE " +
                       "         WHEN patient.fix_race_id <> '' THEN fix_race.description " +
                       "         ELSE '' " +
                       "     END AS citizenship, " +
                       "     '' AS religion, " +
                       "     patient.pid AS id_number, " +
                       "     CASE " +
                       "         WHEN patient.fix_occupation_id <> '' THEN fix_occupation.description " +
                       "         ELSE '' " +
                       "     END AS occupation, " +
                       "     patient.mobile AS tel_no, " +
                       "     patient.blood_group AS blood_type, " +
                       "     string_agg( " +
                       "             nt_allergy.base_nt_allergy_id::TEXT, " +
                       "             ', ' " +
                       " ORDER BY " +
                       "             nt_allergy.nt_allergy_id) AS allergy, " +
                       "     string_agg( " +
                       "             diagnosis_icd10.beginning_diagnosis::TEXT, " +
                       "             ', ' " +
                       " ORDER BY " +
                       "             diagnosis_icd10.diagnosis_icd10_id) AS diagnosis, " +
                       "     admit.admit_date AS date_of_admission, " +
                       "     admit.admit_time, " +
                       "     bed_management.bed_number AS room_number, " +
                       "     base_building.description AS building, " +
                       "     base_service_point.description AS ward, " +
                       "     nt_patient_nutrition.extra_note AS remark, " +
                       "     CASE " +
                       "         WHEN nt_patient_nutrition.base_nt_type_id <> '' THEN base_nt_type.description " +
                       "         ELSE " +
                       "             '' " +
                       "     END AS diet_type, " +
                       "     CASE " +
                       "         WHEN nt_patient_nutrition.base_nt_food_category_id <> '' THEN base_nt_food_category.description " +
                       "             ELSE '' " +
                       "         END AS category_of_food, " +
                       "         employee.prename || employee.firstname || ' ' || employee.lastname AS doctor_name " +
                       "     FROM " + 
                       "         patient " +
                       "     INNER JOIN visit visit ON " +
                       "         patient.patient_id = visit.patient_id " +
                       "     INNER JOIN public.nt_patient_nutrition nt_patient_nutrition ON " +
                       "         nt_patient_nutrition.patient_id = patient.patient_id " +
                       "     INNER JOIN admit admit ON " +
                       "         visit.visit_id = admit.visit_id " +
                       "     INNER JOIN bed_management bed_management ON " +
                       "         bed_management.admit_id = admit.admit_id " +
                       "     INNER JOIN base_service_point base_service_point ON " +
                       "         base_service_point.ward_code = bed_management.ward_code " +
                       "     INNER JOIN ipd_attending_physician ipd_attending_physician ON " +
                       "         ipd_attending_physician.admit_id = admit.admit_id " +
                       "     INNER JOIN public.base_building base_building ON " +
                       "         base_service_point.base_building_id = base_building.base_building_id " +
                       "     INNER JOIN public.employee employee ON " +
                       "         employee.employee_id = ipd_attending_physician.employee_id " +
                       "     LEFT JOIN public.fix_race fix_race ON " +
                       "         fix_race.fix_race_id = patient.fix_race_id " +
                       "     LEFT JOIN public.fix_occupation fix_occupation ON " +
                       "         fix_occupation.fix_occupation_id = patient.fix_occupation_id " +
                       "     LEFT JOIN public.base_nt_type base_nt_type ON " +
                       "         base_nt_type.base_nt_type_id = nt_patient_nutrition.base_nt_type_id " +
                       "     LEFT JOIN public.base_nt_food_category base_nt_food_category ON " +
                       "         base_nt_food_category.base_nt_food_category_id = nt_patient_nutrition.base_nt_food_category_id " +
                       "     LEFT JOIN diagnosis_icd10 diagnosis_icd10 ON " +
                       "         visit.visit_id = diagnosis_icd10.visit_id " +
                       "     LEFT JOIN public.nt_allergy nt_allergy ON " +
                       "         nt_patient_nutrition.nt_patient_nutrition_id = nt_allergy.nt_patient_nutrition_id " +
                       "     LEFT JOIN public.vital_sign_opd vital_sign_opd ON " +
                       "         visit.visit_id = vital_sign_opd.visit_id " +
                       "     WHERE " +
                       "         ( " +
                       "         vital_sign_opd.height, " +
                       "         vital_sign_opd.weight, " +
                       "         vital_sign_opd.measure_date, " +
                       "         vital_sign_opd.measure_time) " +
                       "         IN ( " +
                       "         SELECT " +
                       "             height, " +
                       "             weight, " +
                       "             max(measure_date), " +
                       "             max(measure_time) " +
                       "         FROM " +
                       "             public.vital_sign_opd " +
                       "         WHERE " +
                       "             visit_id = visit.visit_id " +
                       "         GROUP BY " +
                       "             visit_id, " +
                       "             height, " +
                       "             weight, " +
                       "             measure_date, " +
                       "             measure_time " +
                       "         ORDER BY " +
                       "             measure_date DESC, " +
                       "             measure_time DESC " +
                       "         LIMIT 1) " +
                       "         AND admit.active = '1' " +
                       "         AND bed_management.current_bed = '1' " +
                       "         AND base_service_point.active = '1' " +
                       "         AND ipd_attending_physician.priority = '1' " +
                       "         AND ipd_attending_physician.is_current = '1' " +
                       "         AND visit.financial_discharge = '0' " +
                       "         AND admit.admit_date = current_date::TEXT" +
                       "         AND (bed_management.arrival_date) IN ( " +
                       "         SELECT " +
                       "             max(bed_management.arrival_date) " +
                       "         FROM " +
                       "             public.bed_management " +
                       "         WHERE " +
                       "             admit_id = bed_management.admit_id " +
                       "         GROUP BY " +
                       "             arrival_date) " +
                       "     GROUP BY " + 
                       "         patient.hn, " +
                       "         patient.prename, " +
                       "         patient.firstname, " +
                       "         patient.lastname, " +
                       "         patient.birthdate, " +
                       "         vital_sign_opd.height, " +
                       "         vital_sign_opd.weight, " +
                       "         patient.fix_nationality_id, " +
                       "         patient.fix_race_id, " +
                       "         fix_race.description, " +
                       "         patient.religion, " +
                       "         patient.pid, " +
                       "         fix_occupation.description, " +
                       "         patient.fix_occupation_id, " +
                       "         patient.mobile, " +
                       "         patient.blood_group, " +
                       "         admit.admit_date, " +
                       "         admit.admit_time, " +
                       "         bed_management.bed_number, " +
                       "         base_building.description, " +
                       "         base_service_point.description, " +
                       "         nt_patient_nutrition.extra_note, " +
                       "         nt_patient_nutrition.base_nt_type_id, " +
                       "         base_nt_type.description, " +
                       "         nt_patient_nutrition.base_nt_food_category_id, " +
                       "         base_nt_food_category.description, " +
                       "         employee.prename, " +
                       "         employee.firstname, " +
                       "         employee.lastname " +
                       "     ORDER BY " +
                       "         admit.admit_date DESC;";

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
                lnkMap.put("birth_date", Utility.getBirthDateISO(rs.getString("date_of_birth")));
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
                lnkMap.put("date_of_admission",
                        Utility.getTimezone(rs.getString("date_of_admission"), rs.getString("admit_time")));
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

    public LinkedHashMap<String, String> getMonitorInterfaceDataFromDB() {
        LinkedHashMap<String, String> list = new LinkedHashMap<>();

        return list;
    }

    public LinkedHashMap<String, String> getOrderSummaryDataFromDB() {
        LinkedHashMap<String, String> list = new LinkedHashMap<>();

        return list;
    }
}
