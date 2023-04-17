package kopo.poly.mybatis;

import java.util.List;
import java.util.Map;

import kopo.poly.dto.BookingDTO;
import kopo.poly.dto.BusinessDTO;
import kopo.poly.dto.ReviewDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface AdminMapper {

    // 미승인 사업자 목록
    @Select("	SELECT * "
            + "	FROM (SELECT rownum AS num, business.* FROM business where approval='0') "
            + "	WHERE num between #{startPage} and #{endPage} "
            + "	ORDER BY pic_num ")
    List<BusinessDTO> notApprovalBuList(Map map);

    @Select("SELECT count(*) FROM business WHERE approval='0' ")
    int notApprovalBuCount();

    @Update("UPDATE business SET approval ='1' WHERE bu_email = #{bu_email} ")
    int businessApproval(String bu_email);

    @Delete("DELETE FROM business WHERE bu_email = #{bu_email} ")
    int deleteBusiness(String bu_email);

    //	사업자 월별 매출
    @Select("select sum(bo.price) price from booking bo where checkin like '____'||#{mon}||'%' ")
    BookingDTO selectSales(Map<String, Object> map);


    //	지역별 월별 매출
    @Select("select sum(bo.price) price "
            + " from business bu, booking bo "
            + " where bu.bu_title = bo.bu_title and bu.bu_address like #{area}||'%' and bo.checkin like '____'||#{month}||'%' ")
    BookingDTO selectAreaSales(Map<String, Object> map);

    //	카테고리별 월별 매출
    @Select("select sum(bo.price) price, bu.bu_id "
            + " from business bu, booking bo "
            + " where bu.bu_title = bo.bu_title and bu.bu_id like #{bu_id}||'%' and bo.checkin like '____'||#{month}||'%' "
            + " group by bu.bu_id order by bu.bu_id ")
    BookingDTO categorySales(Map<String, Object> map);

    @Select("SELECT r.*, b.bu_title, b.ro_name FROM review r, booking b WHERE r.report='1' and r.bo_num = b.bo_num")
    List<ReviewDTO> reportedReview();
}
