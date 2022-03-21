package com.jobs.sitran;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        String text = "SEPT 2017 - PRESENT DANANG UNIVERSITY OF TECHNOLOGY\n" +
                "MAJOR: INFORMATION TECHNOLOGY\n" +
                "GPA: 3.43\\\\4\n" +
                "MAY 2021 - JUL 2021 SMARTDEV LLC\n" +
                "INTERN: FULLSTACK DEVELOPER (JAVA SPRING BOOT + REACT)\n" +
                "PROJECT: CHOTOT WEBSITE\n" +
                "DEC 2020 - MAR 2021 MCREWTECH COMPANY\n" +
                "PHP - LARAVEL DEVELOPER\n" +
                "PROJECT: JAPANESE STUDY WEBSITE\n" +
                "SEPT 2020 - NOV 2020 DANANG UNIVERSITY OF TECHNOLOGY\n" +
                "NODEJS DEVELOPER\n" +
                "PROJECT: CAFE WEBSITE\n" +
                "TRAN VAN SI\n" +
                "FULLSTACK DEVELOPER\n" +
                "DATE OF BIRTH:  17\\\\04\\\\1999\n" +
                "GENDER:  MALE\n" +
                "PHONE:  0796236387\n" +
                "EMAIL:  SITRANHUE@GMAIL.COM\n" +
                "ADDRESS:  DANANG, VIETNAM\n" +
                "WEBSITE:  GITHUB.COM\\\\SITRANV\n" +
                "OBJECTIVE\n" +
                "\"TO BE ONE OF THE BEST SOFTWARE ENGINEER BY GAINING SENIOR EXPERTISE AND SIGNIFICANT EXPERIENCE IN SOFTWARE\n" +
                "DEVELOPMENT AS WELL AS TO CONTRIBUTE GREAT IMPACT TO THE PROJECTS I LOVE WORKING WITH.\"\n" +
                "EDUCATION\n" +
                "WORK EXPERIENCE\n" +
                "SKILLS\n" +
                "                                      PHP                            OOP                   JAVA CORE\n" +
                "                      \n" +
                "                                      LARAVEL                   JAVASCIPT   SPRING BOOT \n" +
                "                                      MYSQL                      REACT               SPRING DATA\n" +
                "                                      \n" +
                "                                      POSTGRESQL          JQUERY        \n" +
                " \n" +
                "                                      MONGODB                BOOTSTRAP       \n" +
                "                                      GIT                             HTML\\\\CSS\n" +
                "                      \n" +
                "INTERESTS\n" +
                "PROGRAMING, CREATING USEFUL SOFTWARE AND BEING HELPFUL TO OTHERS.\n" +
                "ON MY FREE TIME, I ENJOY MUSIC, GAMES AND OUT DOOR ACTIVITIES.\n" +
                "© TOPCV.VN\n";


        String text1 =  "                                      PHP                            OOP                   JAVA, CORE\n" +
                "                      \n" +
                "                                      LARAVEL                   JAVASCRIPT   SPRING BOOT \n" +
                "                                      MYSQL                      REACT               SPRING DATA\n" +
                "                                      \n" +
                "                                      POSTGRESQL          JQUERY        \n" +
                " \n" +
                "                                      MONGODB                BOOTSTRAP       \n" +
                "                                      GIT                             HTML\\\\CSS\n" +
                "                      JAVA    SYSTEM ADMIN" ;

        String patternString1 = "(DESIGNER)[,. ]+|(DESIGNER)$|(TESTER)[,. ]+|(TESTER)$|(AWS)[,. ]+|(AWS)$|(CLOUD)[,. ]+|(CLOUD)$|(DEVOPS)[,. ]+|(DEVOPS)$|(LINUX)[,. ]+|(LINUX)$|(AGILE)[,. ]+|(AGILE)$|(BUSINESS ANALYST)[,. ]+|(BUSINESS ANALYST)$|(ERP)[,. ]+|(ERP)$|(SQL)[,. ]+|(SQL)$|(SENIOR)[,. ]+|(SENIOR)$|(.NET)[,. ]+|(.NET)$|(NESTJS)[,. ]+|(NESTJS)$|(NEXTJS)[,. ]+|(NEXTJS)$|(C#)[,. ]+|(C#)$|(CSS)[,. ]+|(CSS)$|(DATABASE)[,. ]+|(DATABASE)$|(JAVA)[,. ]+|(JAVA)$|(JAVASCRIPT)[,. ]+|(JAVASCRIPT)$|(SCRUM)[,. ]+|(SCRUM)$|(PHP)[,. ]+|(PHP)$|(BLOCKCHAIN)[,. ]+|(BLOCKCHAIN)$|(ENGLISH)[,. ]+|(ENGLISH)$|(NODEJS)[,. ]+|(NODEJS)$|(OOP)[,. ]+|(OOP)$|(DRUPAL)[,. ]+|(DRUPAL)$|(WORDPRESS)[,. ]+|(WORDPRESS)$|(ANDROID)[,. ]+|(ANDROID)$|(KOTLIN)[,. ]+|(KOTLIN)$|(FLUTTER)[,. ]+|(FLUTTER)$|(FLASK)[,. ]+|(FLASK)$|(VB.NET)[,. ]+|(VB.NET)$|(UNITY)[,. ]+|(UNITY)$|(FRESHER)[,. ]+|(FRESHER)$|(JUNIOR)[,. ]+|(JUNIOR)$|(C++)[,. ]+|(C++)$|(PYTHON)[,. ]+|(PYTHON)$|(ASP.NET)[,. ]+|(ASP.NET)$|(IOS)[,. ]+|(IOS)$|(MVC)[,. ]+|(MVC)$|(INTERN)[,. ]+|(INTERN)$|(MYSQL)[,. ]+|(MYSQL)$|(SPRING)[,. ]+|(SPRING)$|(LARAVEL)[,. ]+|(LARAVEL)$|(JQUERY)[,. ]+|(JQUERY)$|(SCALA)[,. ]+|(SCALA)$|(ANGULAR)[,. ]+|(ANGULAR)$|(REACTJS)[,. ]+|(REACTJS)$|(DJANGO)[,. ]+|(DJANGO)$|(MAGENTO)[,. ]+|(MAGENTO)$|(HYBRID)[,. ]+|(HYBRID)$|(NETWORKING)[,. ]+|(NETWORKING)$|(REACT NATIVE)[,. ]+|(REACT NATIVE)$|(VUEJS)[,. ]+|(VUEJS)$|(MANAGER)[,. ]+|(MANAGER)$|(JAPANESE)[,. ]+|(JAPANESE)$|(APACHE)[,. ]+|(APACHE)$|(ORACLE)[,. ]+|(ORACLE)$|(ANGULARJS)[,. ]+|(ANGULARJS)$|(GOLANG)[,. ]+|(GOLANG)$|(NOSQL)[,. ]+|(NOSQL)$|(RUBY)[,. ]+|(RUBY)$|(AJAX)[,. ]+|(AJAX)$|(HTML5)[,. ]+|(HTML5)$|(UNITY)[,. ]+|(UNITY)$|(PROJECT MANAGER)[,. ]+|(PROJECT MANAGER)$|(IT SUPPORT)[,. ]+|(IT SUPPORT)$|(SWIFT)[,. ]+|(SWIFT)$|(OBJECTIVE C)[,. ]+|(OBJECTIVE C)$|(TEAM LEADER)[,. ]+|(TEAM LEADER)$|(JSON)[,. ]+|(JSON)$|(PRODUCT MANAGER)[,. ]+|(PRODUCT MANAGER)$|(PRODUCT OWNER)[,. ]+|(PRODUCT OWNER)$|(SYSTEM ADMIN)[,. ]+|(SYSTEM ADMIN)$|(RUBY ON RAILS)[,. ]+|(RUBY ON RAILS)$|(MANUAL TESTER)[,. ]+|(MANUAL TESTER)$|(IT COMTOR)[,. ]+|(IT COMTOR)$|(REDIS)[,. ]+|(REDIS)$|(POSTGRESQL)[,. ]+|(POSTGRESQL)$|(GAMES)[,. ]+|(GAMES)$|(SAP)[,. ]+|(SAP)$|(UI\\\\UX)[,. ]+|(UI\\\\UX)$|(J2EE)[,. ]+|(J2EE)$|(SHAREPOINT)[,. ]+|(SHAREPOINT)$|(EMBEDDED)[,. ]+|(EMBEDDED)$|(QA QC)[,. ]+|(QA QC)$|(SYSTEM ENGINEER)[,. ]+|(SYSTEM ENGINEER)$|(SOFTWARE ARCHITECT)[,. ]+|(SOFTWARE ARCHITECT)$|(XAMARIN)[,. ]+|(XAMARIN)$|(CODEIGNITER)[,. ]+|(CODEIGNITER)$|(FASTAPI)[,. ]+|(FASTAPI)$|(EXPRESSJS)[,. ]+|(EXPRESSJS)$|(MANUAL TESTER)[,. ]+|(MANUAL TESTER)$|(BRIDGE ENGINEER)[,. ]+|(BRIDGE ENGINEER)$|(C LANGUAGE)[,. ]+|(C LANGUAGE)$|(ZEND)[,. ]+|(ZEND)$|(JSP)[,. ]+|(JSP)$|(MYBATIS)[,. ]+|(MYBATIS)$|(MESSAGE QUEUE)[,. ]+|(MESSAGE QUEUE)$|(YII)[,. ]+|(YII)$|(JOOMLA)[,. ]+|(JOOMLA)$|(ML)[,. ]+|(ML)$|(CAKEPHP)[,. ]+|(CAKEPHP)$|(SYMFONY)[,. ]+|(SYMFONY)$|(METEORJS)[,. ]+|(METEORJS)$|(MONGODB)[,. ]+|(MONGODB)$|(BOOTSTRAP)[,. ]+|(BOOTSTRAP)$|(ALGORITHM)[,. ]+|(ALGORITHM)$|(PUPPET)[,. ]+|(PUPPET)$|(CHEF)[,. ]+|(CHEF)$|(SELENIUM)[,. ]+|(SELENIUM)$|(JQUERY)[,. ]+|(JQUERY)$|(HADOOP)[,. ]+|(HADOOP)$|(SPARK)[,. ]+|(SPARK)$|(MACHINE LEARNING)[,. ]+|(MACHINE LEARNING)$|(NLP)[,. ]+|(NLP)$|(AI)[,. ]+|(AI)$|(FULLSTACK)[,. ]+|(FULLSTACK)$|(FULLSTACK DEVELOPER)[,. ]+|(FULLSTACK DEVELOPER)$|(FRONTEND)[,. ]+|(FRONTEND)$|(BACKEND)[,. ]+|(BACKEND)$|(DEEP LEARNING)[,. ]+|(DEEP LEARNING)$|(MOBILE APPS)[,. ]+|(MOBILE APPS)$|(ODOO)[,. ]+|(ODOO)$|(TYPESCRIPT)[,. ]+|(TYPESCRIPT)$|(PL\\\\SQL)[,. ]+|(PL\\\\SQL)$|(DATA SCIENTIST)[,. ]+|(DATA SCIENTIST)$|(DATA ENGINEER)[,. ]+|(DATA ENGINEER)$|(AI ENGINEER)[,. ]+|(AI ENGINEER)$|(BIG DATA)[,. ]+|(BIG DATA)$|(BUSINESS INTELLIGENCE)[,. ]+|(BUSINESS INTELLIGENCE)$|(COCOS)[,. ]+|(COCOS)$|(DATA ANALYST)[,. ]+|(DATA ANALYST)$|(AUTOMATION TEST)[,. ]+|(AUTOMATION TEST)$|(IOT)[,. ]+|(IOT)$|(CYBER SECURITY)[,. ]+|(CYBER SECURITY)$|(DEVELOPER)[,. ]+|(DEVELOPER)$|(SENIOR FULL STACK DEVELOPER)[,. ]+|(SENIOR FULL STACK DEVELOPER)$|(PYTHON WEB DEVELOPER)[,. ]+|(PYTHON WEB DEVELOPER)$|(FRONT END DEVELOPER)[,. ]+|(FRONT END DEVELOPER)$|(C++ DEVELOPER)[,. ]+|(C++ DEVELOPER)$|(IOS DEVELOPER)[,. ]+|(IOS DEVELOPER)$|(JAVA DEVELOPER)[,. ]+|(JAVA DEVELOPER)$|(MOBILE APPS DEVELOPER)[,. ]+|(MOBILE APPS DEVELOPER)$|(PHP DEVELOPER)[,. ]+|(PHP DEVELOPER)$|(SOFTWARE DEVELOPER)[,. ]+|(SOFTWARE DEVELOPER)$|(SENIOR PRODUCT OWNER)[,. ]+|(SENIOR PRODUCT OWNER)$|(JAVA WEB DEVELOPER)[,. ]+|(JAVA WEB DEVELOPER)$|(SENIOR FRONT END DEVELOPER)[,. ]+|(SENIOR FRONT END DEVELOPER)$|(FULL STACK WEB DEVELOPER)[,. ]+|(FULL STACK WEB DEVELOPER)$|(BACK END WEB DEVELOPER)[,. ]+|(BACK END WEB DEVELOPER)$|(FRONT END WEB DEVELOPER)[,. ]+|(FRONT END WEB DEVELOPER)$|(BACK END DEVELOPER)[,. ]+|(BACK END DEVELOPER)$|(FULL STACK DEVELOPER)[,. ]+|(FULL STACK DEVELOPER)$|(ANDROID APP DEVELOPER)[,. ]+|(ANDROID APP DEVELOPER)$|(NODEJS DEVELOPER)[,. ]+|(NODEJS DEVELOPER)$|(.NET DEVELOPER)[,. ]+|(.NET DEVELOPER)$|(ANDROID DEVELOPER)[,. ]+|(ANDROID DEVELOPER)$|(PYTHON DEVELOPER)[,. ]+|(PYTHON DEVELOPER)$|(EMBEDDED ENGINEER)[,. ]+|(EMBEDDED ENGINEER)$|(UX UI DESIGNER)[,. ]+|(UX UI DESIGNER)$|(SENIOR JAVA DEVELOPER)[,. ]+|(SENIOR JAVA DEVELOPER)$|(SENIOR BACK END DEVELOPER)[,. ]+|(SENIOR BACK END DEVELOPER)$|(SYSTEM ADMINISTRATOR)[,. ]+|(SYSTEM ADMINISTRATOR)$|(GITHUB)[,. ]+|(GITHUB)$|(GITLAB)[,. ]+|(GITLAB)$|(UBUNTU)[,. ]+|(UBUNTU)$|(GIT)[,. ]+|(GIT)$|(SPRING BOOT)[,. ]+|(SPRING BOOT)$|(HTML\\\\CSS)[,. ]+|(HTML\\\\CSS)$";
        Pattern pattern = Pattern.compile(patternString1);
        Matcher matcher = pattern.matcher(text.replace("\n", " "));

        while(matcher.find()) {
            System.out.println("found: " + matcher.group());
        }
    }

}
