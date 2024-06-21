package com.example.project_sem4_springboot_api;

import com.example.project_sem4_springboot_api.constants.StatusData;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.*;
import com.example.project_sem4_springboot_api.repositories.*;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.sql.Date;
import static com.example.project_sem4_springboot_api.constants.dataSeeding.*;

@Component
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private  final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    // school
    private final SchoolYearRepository schoolYearRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final SchoolYearSubjectRepository schoolYearSubjectRepository;
    private final SchoolYearSubjectGradeRepository schoolYearSubjectGradeRepository;
    private final RoomRepository   roomRepository;
    private final GradeRepository   gradeRepository;
    private final TeacherSchoolYearRepository   teacherSchoolYearRepository;
    private final TeacherSchoolYearClassSubjectRepository   teacherSchoolYearClassSubjectRepository;
    private final UserDetailRepository userDetailRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final StudentYearInfoRepository studentYearInfoRepository;
    private final StudentScoreSubjectRepository studentScoreSubjectRepository;
    private final StudentScoresRepository studentScoresRepository;
    private final StudentStatusRepository studentStatusRepository;
    private final CalendarReleaseRepository calendarReleaseRepository;
    private final StatusRepository statusRepository;
    private final ScheduleRepository scheduleRepository;
    private final PointTypeRepository pointTypeRepository;


//    @Getter
    private final List<EStatus> studentStatus = Arrays.stream(EStatus.values()).toList();
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @PostConstruct
    public void initializeData()  {
        createRolePermission();
        createSchoolInfo();
        createSchedule();
        createStudents();
        createUser("bdht2207a",2);
        createFee();

//        var t2 = schoolYearSubjectGradeRepository.findAllBySchoolYearSubject_IdAndGrade_Id((long) 1,(long) 1);
//        Arrays.stream(EUnit.values()).forEach(e->System.out.println(e.name()+" : "+e.getUnit()));
//        studentStatus.forEach(e->System.out.println(e.name()+" : "+e.getName()));
    }
    private void createRolePermission(){
        // Create Permission
        if(permissionRepository.findAll().isEmpty()){
            permissionRepository.saveAll(
                Arrays.stream(EPermission.values()).map(Permission::new).toList()
            );
            System.out.println("Created permission");
        }
        // Create Role
        if(roleRepository.findAll().isEmpty()){
            roleRepository.saveAll(
                Arrays.stream(ERole.values()).map(Role::new).toList()
            );
            System.out.println("Created role");
        }
        /*
        * create role - permission
        * role:
        *   phu-huynh: read:student , read_Teacher, update:student(update student service), read:schedule
        *   teacher: read:student , update:student, read:schedule , read:teacher, update:teacher
        *   bgh: all
        *   nv-van-thu: all:student,
        *   nv-tai-chinh:
        *   nv-thu-vien:
        *
        * */
        roleRepository.findAll().forEach(role ->{
            switch (role.getName()){
                case ROLE_PH -> addPermission(role,permissionRepository.findAllByNameIn(
                    List.of(
                        EPermission.READ_STUDENT,
                        EPermission.UPDATE_STUDENT,
                        EPermission.READ_SCHEDULE
                    )
                ));
                case ROLE_GV -> addPermission(role,permissionRepository.findAllByNameIn(
                    List.of(
                        EPermission.READ_STUDENT,
                        EPermission.UPDATE_STUDENT,
                        EPermission.READ_SCHEDULE,
                        EPermission.READ_TEACHER,
                        EPermission.UPDATE_TEACHER
                    )
                ));
                case ROLE_NV_VT -> addPermission(role,permissionRepository.findAllByNameIn(
                    List.of(
                        EPermission.ALL_USER,
                        EPermission.ALL_SCHEDULE,
                        EPermission.ALL_TEACHER,
                        EPermission.ALL_STUDENT
                    )
                ));
                case ROLE_BGH -> addPermission(role,permissionRepository.findAllByNameIn(
                    List.of(EPermission.values())
                ));
                default ->{}
            }
        });
    }
    private void addPermission(Role role, List<Permission> permissions){
        if(role.getPermission().isEmpty()){
            role.setPermission(permissions);
            roleRepository.save(role);
        }
    }

    /**
     * create school info
     * 1. school year
     * 2. subject
     * 3. room
     * 4. grade
     * 5. teacher
     * 6. teacher-schoolyear
     * 7. school year class
     * 8. school year subject
     * 9. school year subject grade
     * 10. teacher-schoolyear-subject-class
     * */
    private void createSchoolInfo() {
        /**
         *  Tổ chức khai giảng vào ngày 05 tháng 9 năm 2022.
         *  Kết thúc học kỳ I trước ngày 15 tháng 01 năm 2023,
         *  hoàn thành kế hoạch giáo dục học kỳ II trước ngày 25 tháng 5 năm 2023
         *  kết thúc năm học trước ngày 31 tháng 5 năm 2023
         * có 35 tuần thực học (học kỳ I có 18 tuần, học kỳ II có 17 tuần).
         **/
        // create school year
        if(schoolYearRepository.findAll().isEmpty()){
            schoolYearRepository.saveAll(List.of(
                    SchoolYear.builder()
                            .startSem1(Date.valueOf("2023-09-05"))
                            .startSem2(Date.valueOf("2024-01-15"))
                            .end(Date.valueOf("2024-05-31"))
                            .build(),
                    SchoolYear.builder()
                            .startSem1(Date.valueOf("2025-09-05"))
                            .startSem2(Date.valueOf("2026-01-15"))
                            .end(Date.valueOf("2026-05-31"))
                            .build()
                    )
            );
            System.out.println("Created schoolYear data");
        }
        // create point type
        if(pointTypeRepository.findAll().isEmpty()){
            pointTypeRepository.saveAll(
                Arrays.stream(EPointType.values()).map((e)->{
                    var coefficient =  0;
                    switch (e){
                        case KTTX,DTB -> coefficient = 1;
                        case KT_GIUA_KY -> coefficient = 2;
                        case KT_CUOI_KY -> coefficient = 3;
                    }
                    return PointType.builder()
                            .name(e.getPointType())
                            .pointType(e)
                            .coefficient(coefficient)
                            .build();
                }).toList()
            );
            System.out.println("Created pointType data");
        }

        //  create subject
        if (subjectRepository.findAll().isEmpty()){
            List<Subject> subjects = new ArrayList<>();
            ListSubject.forEach((name,code)->{
                var diemChu = new String[]{"HDTT","GDTC","AN","MT","TNXH","NTTC","HDTN"};
                var pointType = Arrays.asList(diemChu).contains(code);
                subjects.add(
                    Subject.builder()
                        .code(code)
                        .name(name.substring(3))
                        .isNumberType(!pointType)
                        .subjectPointType(!pointType ? SubjectPointType.DIEM_SO : SubjectPointType.DIEM_CHU)
                        .type(name.substring(0,2).contains("BB") ? ESubjectType.BAT_BUOC: ESubjectType.TU_CHON)
                        .build()
                );
            });
            subjectRepository.saveAll(subjects);
            System.out.println("Created subject data");
        }
        // create room
        if(roomRepository.findAll().isEmpty()){
            List<Room> rooms = new ArrayList<>();
            for(int i=1; i<=20; i++){
                rooms.add(
                    Room.builder().name("Phòng "+i).build()
                );
            }
            roomRepository.saveAll(rooms);
            System.out.println("Created room data");
        }
        // create grade
        if(gradeRepository.findAll().isEmpty()){
            gradeRepository.saveAll(
                Arrays.stream(EGrade.values())
                    .map((grade)->Grade.builder().name(grade).build())
                    .toList()
            );
        }
        // create teacher
        if(teacherRepository.findAll().isEmpty()){
            createUser("teacher",20);
            int check = 1;
            for(int i=1;i<=20;i++){
                // create user
                User user = userRepository.findByUsername("teacher"+i).orElseThrow();
                // create teacher
                String sortName =user.getUserDetail().get(0).isGender() ? "Mr." +  user.getUserDetail().get(0).getLastname(): "Mrs." +  user.getUserDetail().get(0).getLastname();
                Teacher teacher = Teacher.builder()
                        .officerNumber("GV"+i)
                        .joiningDate(new Date(System.currentTimeMillis()))
                        .active(true)
                        .sortName(sortName)
                        .user(user)
                        .build();

                teacherRepository.save(teacher);
                check = i%5==0 ? 1 : check +1;
            }
            System.out.println("Created teacher data");
        }
        // teacher school year
        if(teacherSchoolYearRepository.findAll().isEmpty()){
            teacherSchoolYearRepository.saveAll(teacherRepository.findAll().stream().map((teacher)->TeacherSchoolYear.builder()
                    .schoolYear(schoolYearRepository.findAll().get(0))
                    .teacher(teacher)
                    .build()).toList());
        }
        /**
         *  create school year class
         *
         * @var classes: 15 lớp
         * @var grade: 1-5
         * @var charName: A-E
         */
        if(schoolYearClassRepository.findAll().isEmpty()){
            var teachers = teacherSchoolYearRepository.findAll();
            int classes = 15;
            int grade = 1;
            int charName = 0;
            String[] var = {"A","B","C","D","E"};
            var schoolYear = schoolYearRepository.findById((long) 1).orElseThrow();
            for(int i=1; i <= classes; i++){
                schoolYearClassRepository.save(
                        SchoolYearClass.builder()
                            .className("Lớp " + grade + var[charName])
                            .classCode("L"+grade + var[charName] )
                            .teacherSchoolYear(teachers.get(i-1))
                            .grade(gradeRepository.findByName(EGrade.values()[grade-1]))
                            .room(roomRepository.findById((long) i).orElseThrow())
                            .schoolYear(schoolYear)
                            .build()
                );
                if(i%3==0){
                    grade = grade + 1;
                    charName=0;
                }else{
                    charName=charName+1;
                }
            }
            System.out.println("Created schoolYearClass data");
        }
        // create schoolyear subject: môn học trong năm
        if(schoolYearSubjectRepository.findAll().isEmpty()){
            var schoolYear = schoolYearRepository.findById((long) 1).orElseThrow();
            var subjects =  subjectRepository.findAll().stream().filter((s)->!s.getName().contains("TC-Môn tc")).toList();
            schoolYearSubjectRepository.saveAll(
                subjects.stream().map((s)-> SchoolYearSubject.builder()
                        .subject(s)
                        .schoolYear(schoolYear)
                        .build()).toList()
            );
            System.out.println("Created schoolYear subject data");
        };
        /**
         * create schoolyear subject grade: phân phối chương trình học
         *
         * @var tuần học: 35 -- k1: 18 tuần, k2: 17 tuần
         * @var số tiết học trung bình/tuần: 27
         * @var số tiết cả năm : 1225
         */
        if(schoolYearSubjectGradeRepository.findAll().isEmpty()){
            ListSubjectGrade.forEach((subject,numberOfGrade)->{
                List<SchoolYearSubjectGrade> sysg= new ArrayList<>() ;
                numberOfGrade.forEach((grade,number)->{
                    if(number>0){
                        sysg.add(SchoolYearSubjectGrade.builder()
                            .schoolYearSubject(schoolYearSubjectRepository.findBySubject_Name(subject))
                            .grade(gradeRepository.findByName(grade))
                            .number(number)
                            .sem(ESem.CA_NAM)
                            .build());
                    }
                });
                schoolYearSubjectGradeRepository.saveAll(sysg);
            });
            System.out.println("Created schoolYear subject Grade data");
        }

        /**
         * create teacher-schoolyear-subject-class: phân công giảng dạy
         * 1 giáo viên chỉ dạy 1-2 môn học
         *
         **/


    }
    public void createUser (String role,int number) {
        ERole rolename = ERole.ROLE_DEV;
        int inituser = number;
        String pw ="123456";
        Status sts = statusRepository.findByCode(StatusData.CREATE_NEW_USER);
        switch (role){
            case "teacher" -> rolename = ERole.ROLE_GV;
            case "parent" -> rolename = ERole.ROLE_PH;
            case "bdht2207a" -> rolename = ERole.ROLE_BGH;
        }
        for(int i=1;i<=number;i++){
            if(userRepository.existsByUsername(role+i)){
                logger.warn("User "+role+i+" already exists");
                inituser--;
                continue;
            }
            User user = User.builder()
                    .username(role+i)
                    .password(passwordEncoder.encode(pw))
                    .realPassword(pw)
                    .roles(roleRepository.findAllByNameIn(List.of(rolename)))
                    .status(sts)
                    .createdAt(new Date(System.currentTimeMillis()))
                    .build();
            userRepository.save(user);
        }
        if(inituser >0){
            System.out.println("Created user "+role+" data");
            Faker faker = new Faker();
            for (int i=1;i<=inituser;i++) {
                Name teacher = faker.name();
                User user = userRepository.findByUsername(role + i).orElseThrow();
                var userDetail = UserDetail.builder()
                        .firstname(teacher.firstName())
                        .lastname(teacher.lastName())
                        .address(faker.address().fullAddress())
                        .phone(faker.phoneNumber().cellPhone())
                        .email(role+i+"@gmail.com")
                        .gender(i>10)
                        .birthday(faker.date().birthday())
                        .citizen_id(faker.address().countryCode())
                        .avatar(faker.avatar().image())
                        .user(user)
                        .build();
                userDetailRepository.save(userDetail);
            }
            System.out.println("Created user "+role+" detail data");
        }

    }


    /**
     *  create teacherSchoolYearClassSubjectRepository
     *  15 lop => 15 gv chu nhiem && 5 gv bo mon,
     *  moi khoi 3 lop,
     *  moi gv cn se giay 1/3 mon hoc bb cua khoi
     *  20 gv:
     *  gv chu nhiem se day cac mon chinh tru nhac, my thuat, anh, the duc
     *  gv bo mon se day cac mon con lai
     * khoi hoc : 5 , mon hojc cua khoi: ?, lop cua khoi: 3
     *
     * */
    private void createSchedule (){

        var sySubjectGrade = schoolYearSubjectGradeRepository.findAll();
        var teachers = teacherSchoolYearRepository.findAll();
        var classes = schoolYearClassRepository.findAll();

        if(teacherSchoolYearClassSubjectRepository.findAll().isEmpty()){
            // khoi hoc
            Random rand = new Random();
            for( int grade = 1 ; grade <= 5; grade++){
                final int finalGrade = grade;
                // lop cua khoi
                var classGrade = classes.stream().filter(c->c.getGrade().getId().intValue() == finalGrade).toList();
                for(int cgI = 1;cgI <= classGrade.size(); cgI++){
                    // mon hoc cua khoi
                    var subjectGrade =   sySubjectGrade.stream().filter(e->e.getGrade().getId().intValue() == finalGrade).toList();
                    // mon chinh
                    var mainSb = subjectGrade.stream().filter(msb->msb.getSchoolYearSubject().getSubject().getType().equals(ESubjectType.BAT_BUOC)).toList();
                    // mon phu
                    var electiveSb = subjectGrade.stream().filter(msb->msb.getSchoolYearSubject().getSubject().getType().equals(ESubjectType.TU_CHON)).toList();
                    List<TeacherSchoolYearClassSubject> mtsycs = new ArrayList<>();
                    int moc = (mainSb.size()+1)/3;
                    for(int mSgI = 1;mSgI <= mainSb.size(); mSgI++){
                        //  moi gv cn se giay 1/3 mon hoc bb cua khoi.
                        var gvcn =  classGrade.get(cgI-1).getTeacherSchoolYear();
                        if(mSgI%moc==0){
                            int t = rand.nextInt(0,18);
                            gvcn =  teachers.get(t).equals(gvcn) ? teachers.get(t+1) : teachers.get(t);
                        }
                        if(mSgI <= moc){
                            gvcn =  classGrade.get(cgI-1).getTeacherSchoolYear();
                        }
                        mtsycs.add(TeacherSchoolYearClassSubject.builder()
                                .schoolYearClass(classGrade.get(cgI-1))
                                .schoolYearSubject(mainSb.get(mSgI-1).getSchoolYearSubject())
                                .teacherSchoolYear(gvcn)
                                .build());
                    }
                    if(electiveSb.size()>0){
                        for(int eSgI = 1; eSgI <= electiveSb.size(); eSgI++){
                            mtsycs.add(TeacherSchoolYearClassSubject.builder()
                                    .schoolYearClass(classGrade.get(cgI-1))
                                    .schoolYearSubject(electiveSb.get(eSgI-1).getSchoolYearSubject())
                                    .teacherSchoolYear(teachers.get(15+eSgI))
                                    .build());
                        }
                    }
                    teacherSchoolYearClassSubjectRepository.saveAll(mtsycs);
                }
            }
            System.out.println("Created teacherSchoolYearClassSubject data");
        }

        if(calendarReleaseRepository.findAll().isEmpty()){
            java.util.Date date = new java.util.Date(System.currentTimeMillis());
            calendarReleaseRepository.save(CalendarRelease.builder()
                    .releaseAt(date)
                    .title("Tkb ap dung ngay "+date)
                    .schoolYear(teachers.get(0).getSchoolYear())
                    .build());
        }

        if(scheduleRepository.findAll().isEmpty()){
            // tao tkb
            // sang 20 tiet , chieu upto 20
            var teacherSchoolYearCLassSubject = teacherSchoolYearClassSubjectRepository.findAll();
            int tuanhoc = 35;
            int ngayhoc = 5;
            int tiethoc=8;
            var calendar = calendarReleaseRepository.findAll().get(0);
            var dows = Arrays.stream(DayOfWeek.values()).toList();
            List<Schedule> schedules = new ArrayList<>();
            for (int cl=1;cl<=classes.size();cl++){
                int gradeId = classes.get(cl-1).getGrade().getId().intValue();
                // mon hoc cua khoi
                var subjectGrade =   sySubjectGrade.stream().filter(e->e.getGrade().getId().intValue()==gradeId).toList();
                // trung binh tiet hoc / tuan cua mon hoc
                var subjectIndex = 0;
                var subjectAdded = 0;

                for(int tiet = 1;tiet <= tiethoc ;tiet++){
                    for(int nh = 1 ; nh <= ngayhoc ; nh++){
                        var absSubjectOfWeek = subjectGrade.get(subjectIndex).getNumber()/tuanhoc;
                        if(subjectAdded == absSubjectOfWeek){
                            subjectIndex++;
                            subjectAdded = 0;
                        }
                        if(subjectIndex == subjectGrade.size()) {
                            subjectIndex--;
                            break;
                        }
                        var subject = subjectGrade.get(subjectIndex).getSchoolYearSubject();
                        var classs = classes.get(cl-1);
                        var teacher = teacherSchoolYearCLassSubject.stream().filter(t->
                                t.getSchoolYearClass().equals(classs) && t.getSchoolYearSubject().equals(subject)).toList().get(0).getTeacherSchoolYear();
                        schedules.add(Schedule.builder()
                                .calendarRelease(calendar)
                                .studyTime(tiet>4?StudyTime.CHIEU:StudyTime.SANG)
                                .indexLesson(tiet)
                                .dayOfWeek(dows.get(nh-1))
                                .schoolYearClass(classs)
                                .teacherSchoolYear(teacher)
                                .schoolYearSubject(subject)
                                .build());
                        subjectAdded++;

                    }
                }
            }
            scheduleRepository.saveAll(schedules);
            System.out.println("Created schedule data");
        }

    }

    /**
     * creat student-schoolyear & parent
     *
     * @var initStudent: 15 học sinh/lớp
     * @var parent: 1 phụ huynh/2 học sinh
     */
    private void createStudents(){

        if(statusRepository.findAll().isEmpty()){
            var colorErr = List.of(EStatus.NGUNG_HOAT_DONG,EStatus.STUDENT_BO_HOC,EStatus.STUDENT_THOI_HOC,EStatus.STUDENT_CHUYEN_TRUONG);
            var colorWarm =  List.of(EStatus.NGHI_TAM_THOI,EStatus.STUDENT_BAO_LUU);
            var colorSuccess = List.of(EStatus.STUDENT_DANG_HOC,EStatus.HOAT_DONG);
            var colorPrimary = List.of(EStatus.STUDENT_CHUYEN_LOP,EStatus.STUDENT_TOT_NGHIEP);
            statusRepository.saveAll(
                    studentStatus.stream().map((status)->Status.builder()
                            .name(status.getName())
                            .code(status.name())
                            .build()).toList()
            );
        }
        int initStudent = 15;
        List<SchoolYearClass> classes = schoolYearClassRepository.findAll();
        if(userRepository.findAllByUsernameContains("parent").isEmpty()){
            createUser("parent",((classes.size()*initStudent)/2 + 2));
        }
        if(studentRepository.findAll().isEmpty()){
            List<User> userParents = userRepository.findAllByUsernameContains("parent");
            Faker faker = new Faker();
            int studentNum = 1;
            int parentUser = 0;
            var status = statusRepository.findByCode(EStatus.STUDENT_DANG_HOC.name());
            List<StudentStatus> studentStatuses = new ArrayList<>();
            var newDate = new java.util.Date(System.currentTimeMillis());
            for(int i=1 ; i <= classes.size();i++){
                Name student = faker.name();
                var classId = classes.get(i-1).getId();
                var teacherSchoolYearCLassSubject = teacherSchoolYearClassSubjectRepository
                        .findAllBySchoolYearClass_Id(classId);
                for(int J=1;J<=initStudent;J++){
                    if(studentNum%2!=0){
                        parentUser++;
                    }
                    Student std =  Student.builder()
                            .gender(J>(initStudent/2))
                            .firstName(student.firstName())
                            .lastName(student.lastName())
                            .birthday(faker.date().birthday())
                            .address(faker.address().fullAddress())
                            .studentCode("HS"+classes.get(i-1).getClassCode()+J)
                            .parents(List.of(userParents.get(parentUser)))
                            .build();
                    var newStudent = studentRepository.save(std);
                    // create StudentStatus
                    studentStatuses.add(StudentStatus.builder().student(newStudent).status(status).createdAt(newDate).description("Bắt đầu nhập học.").build());
                    // create student year info
                    var stdInfo = studentYearInfoRepository.save(StudentYearInfo.builder()
                            .students(newStudent)
                            .schoolYearClass(classes.get(i-1))
                            .createdAt(newDate)
                            .build());



                    studentNum++;
                }
            }
            studentStatusRepository.saveAll(studentStatuses);
        }

        if(studentScoreSubjectRepository.findAll().isEmpty()){
            var classList = schoolYearClassRepository.findAll();
            List<StudentScoreSubject> data = new ArrayList<>();
            var newDate = new java.util.Date(System.currentTimeMillis());
            classList.forEach(c->{
                c.getStudentYearInfos().forEach(s->{
                    c.getTeacherSchoolYearClassSubjects().forEach(t->
                        data.add(StudentScoreSubject.builder()
                                .studentYearInfo(s)
                                .schoolYearSubject(t.getSchoolYearSubject())
                                .teacherSchoolYear(t.getTeacherSchoolYear())
                                .createdAt(newDate)
                                .build()));
                });
            });
            var rs = studentScoreSubjectRepository.saveAll(data);
            System.out.println("Created studentScoreSubject data");

            var stdClassList = classList.get(0).getStudentYearInfos();
            var pointType = pointTypeRepository.findAll();
            // get studentScoreSubjects of class
            var sss=  rs.stream().filter(e->stdClassList.contains(e.getStudentYearInfo())).toList();
            List<StudentScores> stdSCores = new ArrayList<>();
            var scoreChar = new String[]{"T","K","TB","Y"};

            sss.forEach(s->{
                Arrays.asList(ESem.values()).forEach(sem->{
                    // tạo ds điểm theo hệ số của từng môn và kì
                    Arrays.asList(EPointType.values()).forEach(pt->{
                        var pointT = pointType.stream().filter(e->e.getPointType().equals(pt)).findFirst().orElseThrow();
                        Faker faker = new Faker();
                        var scoreNum = faker.number().numberBetween(6,10);
                        var scoreCharIndex = faker.number().numberBetween(0,3);
                        stdSCores.add(StudentScores.builder()
                                        .score(s.getSchoolYearSubject().getSubject().isNumberType()?String.valueOf(scoreNum):scoreChar[scoreCharIndex])
                                        .semesterName(sem)
                                        .semester(sem.getSem())
                                        .pointType(pointT)
                                        .studentScoreSubject(s)
                                        .createdAt(newDate)
                                .build());
                    });
                });
            });
            studentScoresRepository.saveAll(stdSCores);
            System.out.println("Created studentScore data");
        }
    }
    private final UnitRepository unitRepository;
    private final PaymentTimeRepository paymentTimeRepository;
    private final ScopeRepository scopeRepository;
    private void createFee(){
        if(unitRepository.findAll().isEmpty()){
            unitRepository.saveAll(
                Arrays.stream(EUnit.values()).map(Unit::new).toList()
            );
            System.out.println("Created unit data");
        }
        if(paymentTimeRepository.findAll().isEmpty()){
            paymentTimeRepository.saveAll(
                Arrays.stream(EPaymentTime.values()).map(PaymentTime::new).toList()
            );
            System.out.println("Created paymentTime data");
        }
        if(scopeRepository.findAll().isEmpty()){
            scopeRepository.saveAll(
                Arrays.stream(EScope.values()).map(Scope::new).toList()
            );
            System.out.println("Created scope data");
        }
    }

}