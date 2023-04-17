package kopo.poly.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kopo.poly.dto.BookingDTO;
import kopo.poly.dto.BusinessDTO;
import kopo.poly.dto.ReviewDTO;
import kopo.poly.mybatis.AdminMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao {
    private Map map = new HashMap();
    private final SqlSession sqlSession; // mybatis 연동

    @Autowired
    public AdminDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        System.out.println("AdminDao SqlSession On -> " + this.sqlSession);
    }

    public List<BusinessDTO> notApprovalBuList(int startPage, int endPage) throws Exception {
        map.clear();
        map.put("startPage", startPage);
        map.put("endPage", endPage);
        return sqlSession.getMapper(AdminMapper.class).notApprovalBuList(map);
        // sqlSession.getMapper(AdminMapper.class).notApprovalBuList(map);
    }

    public BookingDTO categorySales(Map<String, Object> map) {
        return sqlSession.getMapper(AdminMapper.class).categorySales(map);
    }

    public BookingDTO selectAreaSales(Map<String, Object> map) throws Exception {
        return sqlSession.getMapper(AdminMapper.class).selectAreaSales(map);
    }

    public int businessApproval(String bu_email) throws Exception {
        return sqlSession.getMapper(AdminMapper.class).businessApproval(bu_email);
    }

    public int businessApprovalCancel(String bu_email) throws Exception {
        return sqlSession.getMapper(AdminMapper.class).deleteBusiness(bu_email);
    }

    public int notApprovalBuCount() throws Exception {
        return sqlSession.getMapper(AdminMapper.class).notApprovalBuCount();
    }

    public BookingDTO selectSales(Map<String, Object> map) {
        return sqlSession.getMapper(AdminMapper.class).selectSales(map);
    }

    public List<ReviewDTO> reportedReview() throws Exception {
        return sqlSession.getMapper(AdminMapper.class).reportedReview();
    }

}
