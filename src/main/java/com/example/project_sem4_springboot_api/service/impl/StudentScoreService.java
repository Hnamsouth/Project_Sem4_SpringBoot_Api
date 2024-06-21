package com.example.project_sem4_springboot_api.service.impl;


import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.EAchievement;
import com.example.project_sem4_springboot_api.entities.enums.EPointType;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.example.project_sem4_springboot_api.entities.request.ScoreDetail;
import com.example.project_sem4_springboot_api.entities.request.StudentScoreCreate;
import com.example.project_sem4_springboot_api.entities.request.StudentScoreCreateDetail;
import com.example.project_sem4_springboot_api.entities.response.ClassInfo;
import com.example.project_sem4_springboot_api.entities.response.InsertPoints;
import com.example.project_sem4_springboot_api.entities.response.StudentScoreSubjectRes;
import com.example.project_sem4_springboot_api.exception.ArgumentNotValidException;
import com.example.project_sem4_springboot_api.repositories.*;
import com.example.project_sem4_springboot_api.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentScoreService {


    private final TeacherSchoolYearRepository teacherSchoolYearRepository;
    private final TeacherSchoolYearClassSubjectRepository teacherSchoolYearClassSubjectRepository;
    private final SchoolYearSubjectRepository schoolYearSubjectRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final StudentScoreSubjectRepository studentScoreSubjectRepository;
    private final StudentYearInfoRepository studentYearInfoRepository;
    private final StudentScoresRepository studentScoresRepository;
    private final PointTypeRepository pointTypeRepository;

    /**
     * @method get
     * @description: lấy danh sách "lớp" gv đang dạy theo năm học và môn
     * @param schoolYearId
     * @return danh sách môn học và list lớp học và số lượng hs trong lớp , số lượng hs có điểm
     * */
    public ResponseEntity<?> getClassByTeacher(Long schoolYearId,ESem sem){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        var teacher = teacherSchoolYearRepository.findByTeacher_User_IdAndSchoolYear_Id(currentUser.getId(), schoolYearId);
        var subjects = schoolYearSubjectRepository.findAllBySchoolYear_Id(schoolYearId);

        if(teacher==null) {
           return ResponseEntity.ok(getInsertPoints(
                   teacherSchoolYearClassSubjectRepository.findAllByTeacherSchoolYear_SchoolYear_Id(schoolYearId),
                     subjects,sem
           ));
        }else { // for bgh
            return ResponseEntity.ok(getInsertPoints(
                    teacher.getTeacherSchoolYearClassSubjects(),
                    subjects,sem
            ));
        }

    }

    /**
     * @method get
     * @description lấy danh sách điểm của học sinh trong lop theo học kỳ, môn học và lớp học
     * @param sem
     * @param schoolYearSubjectId
     * @param schoolYearClassId
     * @return danh sách điểm của học sinh trong lop
     * */
    public ResponseEntity<?> getStudentScoreSubject(ESem sem, Long schoolYearSubjectId, Long schoolYearClassId){
        var classInfo = schoolYearClassRepository.findById(schoolYearClassId).orElseThrow();
        var subject = schoolYearSubjectRepository.findById(schoolYearSubjectId).orElseThrow();
        var listStd = classInfo.getStudentYearInfos();
        List<Map<String,Object>> resBody = new ArrayList<>();
        var stdSS = StudentScoreSubject.builder().build();
        if(!listStd.isEmpty()){
            stdSS = listStd.get(0).getStudentScoreSubjects().stream().filter(ss->ss.getSchoolYearSubject().equals(subject)).toList().get(0);
        }
        StudentScoreSubject finalStdSS = stdSS;
        listStd.forEach(s->{
            resBody.add(finalStdSS.toResE(sem));
        });

        Map<String,Object> res = new TreeMap<>();
        res.put("schoolYearSubject",stdSS.getSchoolYearSubject().toRes());
        res.put("teacherSchoolYear",stdSS.getTeacherSchoolYear().toRes());
        res.put("studentScoreSubject",resBody);

        return ResponseEntity.ok(res);
    }

    /**
     * @method get
     * @param studentYearInfoId
     * @param sem
     * @param schoolYearSubjectId
     * @description: lấy danh điểm các môn học hoặc 1 môn của học sinh theo id hs khi gv thêm điểm
     *
     *
     * */
    public ResponseEntity<?> getStudentScoreSubjectBy(ESem sem,Long studentYearInfoId, Long schoolYearSubjectId){
        // neu studentYearInfoId != null && sem,schoolYearSubjectId == null : return ds hs cuar hk1 hk2 va ca nam cua tat ca mon
        //  sem && studentYearInfoId != null : return ds diem cua hs theo ki cua tat ca mon
        // rt
        // lấy ds điểm của tất cả các môn theo học kỳ
        if(sem==null && schoolYearSubjectId == null){
            var std = studentScoreSubjectRepository.findAllByStudentYearInfo_Id(studentYearInfoId);
            var res = new HashMap<String,Object>();
            Arrays.stream(ESem.values()).toList().forEach(e->{
                res.put(
                        e.getSem()==3? "summary":"semester"+e.getSem(),
                        std.stream().map(s->s.toResForStudent(e)).toList());

            });
            return ResponseEntity.ok(res);
        }else{
            var std = studentScoreSubjectRepository.findByStudentYearInfo_IdAndSchoolYearSubject_Id(studentYearInfoId, schoolYearSubjectId);
            return ResponseEntity.ok(std.toResForStudent(sem));
        }
    }

    public ResponseEntity<?> getPointType(){
        return ResponseEntity.ok(pointTypeRepository.findAll());
    }

    /**
     * @method post
     * @description: thêm điểm cho học sinh
     * @return danh sách điểm của học sinh trong lop
     *
     * */
    public ResponseEntity<?> createOrUpdateStudentScore(StudentScoreCreate data){
        var subject = schoolYearSubjectRepository.findById(data.getSchoolYearSubjectId()).orElseThrow(
                ()->new NullPointerException("Không tìm thấy môn học")
        );
        var pointTypes = pointTypeRepository.findAll();
        var stdIds = data.getStudentScoreDetails().stream().map(StudentScoreCreateDetail::getStudentYearInfoId).toList();
        if(!studentYearInfoRepository.existsAllByIdInAndSchoolYearClass_Id(stdIds,data.getSchoolYearClassId())) throw new ArgumentNotValidException("Id học sinh không tồn tại trong lớp!!!","","");
        // ds điểm 1 môn học của tất cả học sinh lớp
        var stdSSListAll = studentScoreSubjectRepository.findAllByStudentYearInfo_IdInAndSchoolYearSubject_Id(stdIds,data.getSchoolYearSubjectId());
        if(stdSSListAll.isEmpty()) throw new ArgumentNotValidException("Id học sinh hoặc Id môn học không hợp lệ!!!","","");
        var uniquePointType = List.of(EPointType.DTB,EPointType.KT_CUOI_KY,EPointType.KT_GIUA_KY);
        // ds điểm "unique cua 1 ki"  môn học của tat ca hs trong lop
        var checkUniqueScoreStd = studentScoresRepository.findAllBySemesterNameAndStudentScoreSubject_StudentYearInfo_IdInAndStudentScoreSubject_SchoolYearSubject_IdAndPointType_PointTypeIn(
                data.getSem(),stdIds,subject.getId(),uniquePointType
        );
        try {
            var res = new LinkedList<StudentScores>();
            data.getStudentScoreDetails().forEach(e->{
                // ds điểm 1 môn học của 1 hs
                var studentSS = stdSSListAll.stream().filter(ss->ss.getStudentYearInfo().getId().equals(e.getStudentYearInfoId())).findAny().get();
                // check điểm: chỉ  điểm Kttt mới được tạo hơn 1 lần cho mỗi kì
                var checkPTdata = e.getScoreDetails().stream().filter(a->uniquePointType.contains(a.getPointType())).toList();
                // ds điểm "unique cua 1 ki"  môn học của 1 hs
                var stdUniquePoint =  checkUniqueScoreStd.stream().filter(s->s.getStudentScoreSubject().equals(studentSS)).toList();
                // check diem
                // db co id nhung data ko co : false thows exception("diem da ton tai")
                //  nguoc lai : false thows exception("id ko tồn tại")
                // ca 2 cung co id giong nhau: true update}{ khac id thows exception
                // ca 2 deu ko co id: true create
                uniquePointType.forEach(pt->{
                    var c1 = stdUniquePoint.stream().filter(s->s.getPointType().getPointType().equals(pt)).toList();
                    var c2 = checkPTdata.stream().filter(s->s.getPointType().equals(pt)).toList();
                    if(!c1.isEmpty() && !c2.isEmpty() && c2.get(0).getStudentScoreId()!=null && !c1.get(0).getId().equals(c2.get(0).getStudentScoreId())){
                        throw new ArgumentNotValidException("Id điểm "+pt.getPointType()+"không hợp lệ!!!","","");
                    }
                    if(c1.isEmpty() && !c2.isEmpty() && (c2.get(0).getStudentScoreId()!=null)) throw new ArgumentNotValidException("Id điểm "+pt.getPointType()+" không tồn tại!!!","","");
                    if(!c1.isEmpty()  && !c2.isEmpty() && c2.get(0).getStudentScoreId()==null) throw new ArgumentNotValidException("Điểm "+pt.getPointType()+" đã tồn tại!!!","","");
                });
                e.getScoreDetails().forEach(s->{
                    // check điểm: chỉ  điểm Kttt mới được tạo hơn 1 lần cho mỗi kì
                    // check loại điểm của môn học là điểm số hay chữ: subject.getSubject().isNumberType();
                    var checkScore = subject.getSubject().isNumberType() ? s.getScore().matches("^[0-9]*$") : Arrays.toString(EAchievement.values()).contains(s.getScore());
                    if(!checkScore) throw new ArgumentNotValidException("Môn "+subject.getSubject().getName()+" yêu cầu thang điểm "+
                            (subject.getSubject().isNumberType() ? "số":"chữ")+"!!!","","");
                    var pointType = pointTypes.stream().filter(p->p.getPointType().equals(s.getPointType())).findAny().get();
                    res.add(
                            StudentScores.builder()
                                    .id(s.getStudentScoreId()!=null ? s.getStudentScoreId():null)
                                    .score( s.getScore())
                                    .pointType(pointType)
                                    .studentScoreSubject(studentSS)
                                    .semester(data.getSem().getSem())
                                    .semesterName(data.getSem())
                                    .build());
                });
            });
            studentScoresRepository.saveAll(res);
            return ResponseEntity.ok("Thêm điểm thành công!!!");
        }catch (Exception e){
            throw new ArgumentNotValidException(e.getMessage(),"","");
        }

    }




    private List<InsertPoints> getInsertPoints(List<TeacherSchoolYearClassSubject> entrusted, List<SchoolYearSubject> subjects, ESem sem){
        List<InsertPoints> res = new LinkedList<>();
        subjects.forEach(e->{
            var classList = entrusted.stream()
                    .filter(s->s.getSchoolYearSubject().equals(e))
                    .map(TeacherSchoolYearClassSubject::getSchoolYearClass)
                    .map(c->{
                        var completedQuantity = studentScoresRepository.findAllBySemesterNameAndStudentScoreSubject_StudentYearInfo_IdIn(
                                sem,c.getStudentYearInfos().stream().map(StudentYearInfo::getId).toList()).size();
                        return ClassInfo.builder()
                                .numberOfStudent(c.getStudentYearInfos().size())
                                .completedQuantity(completedQuantity)
                                .classInfo(c.toRes())
                                .build();
                    })
                    .toList();
            res.add(InsertPoints.builder()
                    .schoolYearSubject(e.toRes())
                    .classList(classList)
                    .build());
        });
        return res;
    }

}
