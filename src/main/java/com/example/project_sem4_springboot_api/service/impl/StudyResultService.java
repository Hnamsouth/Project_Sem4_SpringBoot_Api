package com.example.project_sem4_springboot_api.service.impl;


import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.EAchievement;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.example.project_sem4_springboot_api.entities.request.StudentStudyResult;
import com.example.project_sem4_springboot_api.entities.request.StudyResultCU;
import com.example.project_sem4_springboot_api.entities.request.StudyResultScores;
import com.example.project_sem4_springboot_api.entities.response.ClassInfo;
import com.example.project_sem4_springboot_api.entities.response.StudyResultRes;
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
public class StudyResultService {
    private final TeacherSchoolYearRepository teacherSchoolYearRepository;
    private final TeacherSchoolYearClassSubjectRepository teacherSchoolYearClassSubjectRepository;
    private final SchoolYearSubjectRepository schoolYearSubjectRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final StudyResultRepository studyResultRepository;
    private final StudyResultDetailRepository studyResultDetailRepository;
    private final StudentYearInfoRepository studentYearInfoRepository;
    private final SchoolYearSubjectGradeRepository schoolYearSubjectGradeRepository;


    /**
     * @param schoolYearId
     * @param sem
     * @return StudyResultRes
     * @apiNote get ds lop cua gv trong hoc ky
     * */
    public ResponseEntity<?> getStudyResultClass(Long schoolYearId, ESem sem,List<Long>  gradeIds){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        var teacher = teacherSchoolYearRepository.findByTeacher_User_IdAndSchoolYear_Id(currentUser.getId(), schoolYearId);
        if(teacher==null){
            var classList = schoolYearClassRepository.findAllBySchoolYear_Id(schoolYearId);
            return ResponseEntity.ok(
                    convertStudyResult(sem,
                            gradeIds.isEmpty()?
                                    classList:
                                    classList.stream().filter(e->gradeIds.contains(e.getGrade().getId())).toList()
                    )
            );
        }else{

            var classList = teacherSchoolYearClassSubjectRepository.getClassListIdByTeacherSchoolYear_Id(teacher.getId());
            return ResponseEntity.ok(
                    convertStudyResult(sem,
                            gradeIds.isEmpty()?
                                    classList:
                                    classList.stream().filter(e->gradeIds.contains(e.getGrade().getId())).toList()
                    )
            );
        }

    }

    public ResponseEntity<?> getStudyResult(ESem sem , Long schoolYearClassId,Long studentYearInfoId){
        if((schoolYearClassId!=null && studentYearInfoId!=null)||
                (schoolYearClassId==null && studentYearInfoId==null)
        ) throw new ArgumentNotValidException("Chỉ có thể lấy dữ liệu của lớp hoặc học sinh!!!","","");
        if (schoolYearClassId !=null){
            var std = studentYearInfoRepository.findAllBySchoolYearClass_Id(schoolYearClassId);
            std.stream().map(StudentYearInfo::getId).toList();
            return ResponseEntity.ok(std.get(0).getStudentStudyResults());
        }
        var res2= studyResultRepository.findAllBySemesterAndStudentYearInfo_Id(sem,studentYearInfoId);

        return ResponseEntity.ok( res2.stream().map(StudyResult::toRes).toList());
    }

    public ResponseEntity<?> createStudyResult(StudyResultCU data){

        var stdIds = data.getStudentStudyResults().stream().map(StudentStudyResult::getStudentYearInfoId).toList();
        var stdList = studentYearInfoRepository.findAllByIdInAndSchoolYearClass_Id(stdIds,data.getSchoolYearClassId());
        if(stdList.isEmpty()) throw new ArgumentNotValidException("Id học sinh không tồn tại trong lớp!!!","","");
        if(studyResultRepository.findAllByStudentYearInfo_IdInAndSemester(stdIds,data.getSemester()).size()>0)
            throw new ArgumentNotValidException(" Đã có Học sinh tổng kết kết quả học tập trong kì này!!!","","");
        var subjectClassList = teacherSchoolYearClassSubjectRepository.findAllBySchoolYearClass_Id(data.getSchoolYearClassId()).stream().map(TeacherSchoolYearClassSubject::getSchoolYearSubject).toList();
        var subjectClassIds = subjectClassList.stream().map(SchoolYearSubject::getId).toList();

        List<StudyResult> studyResults = new ArrayList<>();
        Map<Long, List<StudyResultDetail>> studyResultDetails = new HashMap<>();
        // U-Index or C-Index
        data.getStudentStudyResults().forEach(e->{
            // check passed
            if(!data.getSemester().equals(ESem.CA_NAM) && e.isPassed())
                throw new ArgumentNotValidException("Không thể đánh giá Kết quả lên lớp ở học kỳ "+data.getSemester().getSem()+"!!!","","");
            // completed khi đã có điểm của tất cả môn học trong học kỳ
            // check đã hs đã tổng kết kết quả của kì này chưa
            // check mon hoc cua hs co ton tai trong danh sach mon hoc cua lop
           var subjectIds =  e.getStudyResultScores().stream().map(StudyResultScores::getSchoolYearSubjectId).toList();
           if(!subjectClassIds.containsAll(subjectIds)) throw new ArgumentNotValidException("Điểm Môn học không tồn tại trong danh sách môn học của lớp!!!","","");
           var isCompleted = (subjectIds.size() == subjectClassIds.size());
            studyResults.add(
                    StudyResult.builder()
                    .id(e.getId())
                    .numOfDayOff(e.getNumOfDayOff())
                    .note(e.getNote())
                    .academicPerformance(e.getAcademicPerformance())
                    .conduct(e.getConduct())
                    .academicAchievement(e.getAcademicAchievement())
                    .isPassed(e.isPassed())
                    .semester(data.getSemester())
                    .isFinished(isCompleted)
                    .semesterName(data.getSemester().getSem()==3?"Cả năm":"Học kỳ "+data.getSemester().getSem())
                    .studentYearInfo(stdList.stream().filter(s->s.getId().equals(e.getStudentYearInfoId())).findFirst().orElseThrow())
                    .build()
            );

            List<StudyResultDetail> studyResultDetailList = new ArrayList<>();
            e.getStudyResultScores().forEach(s->{
                // check type of score
                var subject = subjectClassList.stream()
                        .filter(sub->sub.getId().equals(s.getSchoolYearSubjectId())).findFirst().orElseThrow();
                try {
                    var checkPoint = subject.getSubject().isNumberType() ?
                            Double.parseDouble(s.getScore()) >= 0 && Double.parseDouble(s.getScore()) <= 10:
                            Arrays.toString(EAchievement.values()).contains(s.getScore());
                    if(!checkPoint)
                        throw new ArgumentNotValidException("Môn "+subject.getSubject().getName()+" yêu cầu thang điểm "+
                                (subject.getSubject().isNumberType() ? "số":"chữ")+"!!!","","");
                }catch (NumberFormatException ignored){
                    throw new ArgumentNotValidException("Môn "+subject.getSubject().getName()+" yêu cầu thang điểm "+
                            (subject.getSubject().isNumberType() ? "số":"chữ")+"!!!","","");
                }

                studyResultDetailList.add(
                        StudyResultDetail.builder()
                                .id(s.getId())
                                .score(s.getScore())
                                .schoolYearSubject(subject)
                                .build()
                );
            });
            studyResultDetails.put(e.getStudentYearInfoId(),studyResultDetailList);
        });
        var studyResultRes = studyResultRepository.saveAll(studyResults);
        List<StudyResultDetail> studyResultDetailList = new ArrayList<>();
        studyResultRes.forEach(e->{
            studyResultDetailList.addAll(studyResultDetails.get(e.getStudentYearInfo().getId()).stream().map(s->{
                s.setStudyResult(e);
                return s;
            }).toList());
        });
        studyResultDetailRepository.saveAll(studyResultDetailList);
        return ResponseEntity.ok("Tạo kết quả học tập thành công!!!");
    }

    public ResponseEntity<?> updateStudyResult(StudyResultCU data) {
        var stdIds = data.getStudentStudyResults().stream().map(StudentStudyResult::getStudentYearInfoId).toList();
        var stdList = studentYearInfoRepository.findAllByIdInAndSchoolYearClass_Id(stdIds, data.getSchoolYearClassId());
        //
        return null;
    }



    private StudyResultRes convertStudyResult(ESem sem, List<SchoolYearClass> classList){
        var classInfos = new LinkedList<ClassInfo>();
        classList.forEach(e->{
            var stdIds=  e.getStudentYearInfos().stream().map(StudentYearInfo::getId).toList();
            var count = studyResultRepository.findAllByStudentYearInfo_IdInAndSemester(stdIds,sem);
            var checkCompleted = stdIds.size() == count.stream().filter(StudyResult::isFinished).toList().size();
            classInfos.add(ClassInfo.builder()
                    .classInfo(e.toRes())
                    .isCompleted(checkCompleted)
                    .completed(checkCompleted?"Đã hoàn thành":"Chưa hoàn thành")
                    .build());
        });
        return StudyResultRes.builder()
                .semester(sem)
                .semesterName(sem.getSem()==3?"Cả năm":"Học kỳ "+sem.getSem())
                .classList(classInfos)
                .build();
    }
}
