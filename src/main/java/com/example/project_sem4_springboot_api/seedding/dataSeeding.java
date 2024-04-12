package com.example.project_sem4_springboot_api.seedding;

import com.example.project_sem4_springboot_api.entities.enums.EGrade;

import java.util.List;
import java.util.Map;

public class dataSeeding {
    public static Map<String,String> ListSubject = Map.ofEntries(
            Map.entry("BB-Toán","TOAN"),
            Map.entry("BB-Tiếng việt","TV"),
            Map.entry("BB-Anh","ANH"),
            Map.entry("BB-Lịch sử và Địa lý","LS_DL"),
            Map.entry("BB-Đạo đức","DD"),
            Map.entry("BB-Giáo dục thể chất","GDTC"),
            Map.entry("BB-Âm nhạc","AN"),
            Map.entry("BB-Mỹ thuật","MT"),
            Map.entry("BB-Tin","TIN"),
            Map.entry("BB-Tự nhiên và Xã hội","TNXH"),
            Map.entry("BB-Hoạt động trải nghiệm","HDTN"),
            Map.entry("BB-Hoạt động tập thể","HDTT"),
            Map.entry("TC-Nghệ thuật tăng cường","NTTC"),
            Map.entry("TC-Tiếng dân tộc","TDT"),
            Map.entry("TC-Hướng dẫn học","HDH"),
            Map.entry("TC-Môn tc 2","MT2"),
            Map.entry("TC-Môn tc 3","MT3")
    );
   
    public static Map<String,Map<EGrade,Integer>> ListSubjectGrade = Map.ofEntries(
            Map.entry("Toán",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,105),
                    Map.entry(EGrade.KHOI_2,175),
                    Map.entry(EGrade.KHOI_3,175),
                    Map.entry(EGrade.KHOI_4,175),
                    Map.entry(EGrade.KHOI_5,175)
            )),
            Map.entry("Tiếng việt",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,420),
                    Map.entry(EGrade.KHOI_2,350),
                    Map.entry(EGrade.KHOI_3,245),
                    Map.entry(EGrade.KHOI_4,245),
                    Map.entry(EGrade.KHOI_5,245)
            )),
            Map.entry("Anh",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,70),
                    Map.entry(EGrade.KHOI_2,70),
                    Map.entry(EGrade.KHOI_3,210),
                    Map.entry(EGrade.KHOI_4,210),
                    Map.entry(EGrade.KHOI_5,210)
            )),
            Map.entry("Lịch sử và Địa lý",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,0),
                    Map.entry(EGrade.KHOI_2,0),
                    Map.entry(EGrade.KHOI_3,0),
                    Map.entry(EGrade.KHOI_4,70),
                    Map.entry(EGrade.KHOI_5,70)
            )),
            Map.entry("Đạo đức",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,35),
                    Map.entry(EGrade.KHOI_2,35),
                    Map.entry(EGrade.KHOI_3,35),
                    Map.entry(EGrade.KHOI_4,35),
                    Map.entry(EGrade.KHOI_5,35)
            )),
            Map.entry("Giáo dục thể chất",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,70),
                    Map.entry(EGrade.KHOI_2,70),
                    Map.entry(EGrade.KHOI_3,70),
                    Map.entry(EGrade.KHOI_4,70),
                    Map.entry(EGrade.KHOI_5,70)
            )),
            Map.entry("Âm nhạc",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,35),
                    Map.entry(EGrade.KHOI_2,35),
                    Map.entry(EGrade.KHOI_3,35),
                    Map.entry(EGrade.KHOI_4,35),
                    Map.entry(EGrade.KHOI_5,35)
            )),
            Map.entry("Mỹ thuật",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,35),
                    Map.entry(EGrade.KHOI_2,35),
                    Map.entry(EGrade.KHOI_3,35),
                    Map.entry(EGrade.KHOI_4,35),
                    Map.entry(EGrade.KHOI_5,35)
            )),
            Map.entry("Tin",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,0),
                    Map.entry(EGrade.KHOI_2,0),
                    Map.entry(EGrade.KHOI_3,70),
                    Map.entry(EGrade.KHOI_4,70),
                    Map.entry(EGrade.KHOI_5,70)
            )),
            Map.entry("Tự nhiên và Xã hội",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,70),
                    Map.entry(EGrade.KHOI_2,70),
                    Map.entry(EGrade.KHOI_3,70),
                    Map.entry(EGrade.KHOI_4,0),
                    Map.entry(EGrade.KHOI_5,0)
            )),
            Map.entry("Hoạt động trải nghiệm",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,105),
                    Map.entry(EGrade.KHOI_2,105),
                    Map.entry(EGrade.KHOI_3,105),
                    Map.entry(EGrade.KHOI_4,105),
                    Map.entry(EGrade.KHOI_5,105)
            )),
            Map.entry("Hoạt động tập thể",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,35),
                    Map.entry(EGrade.KHOI_2,35),
                    Map.entry(EGrade.KHOI_3,35),
                    Map.entry(EGrade.KHOI_4,35),
                    Map.entry(EGrade.KHOI_5,35)
            )),
            Map.entry("Nghệ thuật tăng cường",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,35),
                    Map.entry(EGrade.KHOI_2,35),
                    Map.entry(EGrade.KHOI_3,35),
                    Map.entry(EGrade.KHOI_4,0),
                    Map.entry(EGrade.KHOI_5,0)
            ))
    );
}
