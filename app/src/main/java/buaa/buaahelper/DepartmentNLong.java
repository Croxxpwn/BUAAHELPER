package buaa.buaahelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Croxx on 2017/2/9.
 */

public class DepartmentNLong {
    private static Map<Long, String> department_Long2String = new HashMap<>();

    static {

        department_Long2String.put((long) 1, "材料科学与工程学院");
        department_Long2String.put((long) 2, "电子信息工程学院");
        department_Long2String.put((long) 3, "自动化科学与电气工程学院");
        department_Long2String.put((long) 4, "能源与动力工程学院");
        department_Long2String.put((long) 5, "航空科学与工程学院");
        department_Long2String.put((long) 6, "计算机学院");
        department_Long2String.put((long) 7, "机械工程及自动化学院");
        department_Long2String.put((long) 8, "经济管理学院");
        department_Long2String.put((long) 9, "数学与系统科学学院");
        department_Long2String.put((long) 10, "生物与医学工程学院");
        department_Long2String.put((long) 11, "人文社会科学学院");
        department_Long2String.put((long) 12, "外国语学院");
        department_Long2String.put((long) 13, "交通科学与工程学院");
        department_Long2String.put((long) 14, "可靠性与系统工程学院");
        department_Long2String.put((long) 15, "宇航学院");
        department_Long2String.put((long) 16, "飞行学院");
        department_Long2String.put((long) 17, "仪器科学与光电工程学院");
        department_Long2String.put((long) 18, "北京学院");
        department_Long2String.put((long) 19, "物理科学与核能工程学院");
        department_Long2String.put((long) 20, "法学院");
        department_Long2String.put((long) 21, "软件学院");
        department_Long2String.put((long) 22, "现代远程教育学院");
        department_Long2String.put((long) 23, "高等理工学院");
        department_Long2String.put((long) 24, "中法工程师学院");
        department_Long2String.put((long) 25, "国际学院");
        department_Long2String.put((long) 26, "新媒体艺术与设计学院");
        department_Long2String.put((long) 27, "化学与环境学院");
        department_Long2String.put((long) 28, "马克思主义学院");
        department_Long2String.put((long) 29, "人文与社会科学高等研究院");
        department_Long2String.put((long) 30, "空间与环境学院");
        department_Long2String.put((long) 71, "启明书院");
        department_Long2String.put((long) 72, "冯如书院");
        department_Long2String.put((long) 79, "知行书院");
        department_Long2String.put((long) 101, "学生处");
        department_Long2String.put((long) 102, "安全保卫处");
        department_Long2String.put((long) 103, "教务处");
        department_Long2String.put((long)104, "后勤保障处");
        department_Long2String.put((long) 105, "国际交流合作处");
        department_Long2String.put((long) 106, "沙河校区");
        department_Long2String.put((long) 107, "财务处");
        department_Long2String.put((long) 108, "招生就业处");
        department_Long2String.put((long) 109, "网络信息中心");
        department_Long2String.put((long) 110, "校团委");
        department_Long2String.put((long) 111, "档案馆");
        department_Long2String.put((long) 112, "图书馆");
        department_Long2String.put((long) 113, "校医院");
    }

    public static String Department_Long2String(long department){

        return (String)department_Long2String.get(department);

    }

}

/*
1 材料科学与工程学院
2 电子信息工程学院
3 自动化科学与电气工程学院
4 能源与动力工程学院
5 航空科学与工程学院
6 计算机学院
7 机械工程及自动化学院
8 经济管理学院
9 数学与系统科学学院
10 生物与医学工程学院
11 人文社会科学学院
12 外国语学院
13 交通科学与工程学院
14 可靠性与系统工程学院
15 宇航学院
16 飞行学院
17 仪器科学与光电工程学院
18 北京学院
19 物理科学与核能工程学院
20 法学院
21 软件学院
22 现代远程教育学院
23 高等理工学院
24 中法工程师学院
25 国际学院
26 新媒体艺术与设计学院
27 化学与环境学院
28 马克思主义学院
29 人文与社会科学高等研究院
30 空间与环境学院
71 启明书院
72 冯如书院
79 知行书院
101 学生处
102 安全保卫处
103 教务处
104 后勤保障处
105 国际交流合作处
106 沙河校区
107 财务处
108 招生就业处
109 网络信息中心
110 校团委
111 档案馆
112 图书馆
113 校医院
 */