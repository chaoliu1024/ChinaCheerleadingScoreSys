ChinaCheerleadingScoreSys
=========================

《中国啦啦操竞赛评分系统》

该系统是读硕士阶段，与实验室另外两位同学一起开发

系统已在2013-2014赛季、2014-2015赛季全国啦啦操各站比赛中投入使用

系统由三个部分组成：

1. PC端：系统总服务端（对应工程名ccss_pc）

2. Android端：各打分终端，比赛成绩发送给PC端（对应工程名ccss_android）

3. Web端：提供Android连接PC端数据库的服务（对应工程名ccss_sqlservice）

数据库采用MicroSoft SQL Server，对应脚本文件为score_sys.sql
