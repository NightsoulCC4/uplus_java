package com.uplus_client.uplus_java.Repository.IMED;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.uplus_client.uplus_java.Utility.Utility;

// Get discharge data from database.
@Repository
public class DischargeRepository {

    @Value("${his.datasource.jdbc-url}")
    private String hisDatasourceJdbcUrl;

    @Value("${his.datasource.username}")
    private String hisDatasourceUsername;

    @Value("${his.datasource.password}")
    private String hisDatasourcePassword;

    Logger log = LogManager.getLogger(DischargeRepository.class);

    public DischargeRepository(){
        
    }

    public List<LinkedHashMap<String, Object>> getDischargeDataFromDB(){
        List<LinkedHashMap<String, Object>> list = null;
        LinkedHashMap<String, Object> lnkMap = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT " +
                       "     format_hn(patient.hn) AS hn_number, " + 
                       "     bed_management.room_number AS room_number," +
                       "     base_service_point.description AS ward, " +
                       "     visit.financial_discharge AS discharge_notify, " +
                       "     visit.financial_discharge_date AS discharge_date, " +
                       "     visit.financial_discharge_time AS discharge_time" +
                       " FROM " +
                       "     patient " +
                       " INNER JOIN visit visit ON " +
                       "     patient.patient_id = visit.patient_id " +
                       " INNER JOIN admit admit ON " +
                       "     admit.visit_id = visit.visit_id " +
                       " INNER JOIN bed_management bed_management ON " +
                       "     bed_management.admit_id = admit.admit_id " +
                       " INNER JOIN base_service_point base_service_point ON " +
                       "     base_service_point.ward_code = bed_management.ward_code " +
                       " INNER JOIN base_room base_room ON " +
                       "     base_room.base_room_id = bed_management.base_room_id " +
                       " WHERE " +
                       "     visit.financial_discharge = '1' " +
                       " AND " +
                       "     bed_management.current_bed = '1' " +
                       " AND " +
                       "     base_service_point.active = '1' " +
                       " AND " +
                       "     admit.admit_date = current_date::TEXT" +
                       " ORDER BY " +
                       "     discharge_date DESC;"; 

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
