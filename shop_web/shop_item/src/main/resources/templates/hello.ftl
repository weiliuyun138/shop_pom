<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    hello , ${key}
<br/>
    <#if age<18>
        未成年
    </#if>
    <#if (age>18)>
        成年
    </#if>
    <#if age1<19>
        未成年
        <#elseif age1<30>
        中年
        <#else >
        老年
    </#if>
    <br/>

    <#--遍历集合-->
    <#list goodlist as good >
        ${good.gname},
        ${good.ginfo}
    </#list>
<br/>
    <#--日期格式处理-->
    <#--2019-4-12-->
    <#--17:41:58-->
    <#--2019-4-12 17:41:58-->
    <#--2019年04月12日 17时41粉58秒485毫秒-->
    ${now?date}
    ${now?time}
    ${now?datetime}
    ${now?string('yyyy年MM月dd日 HH时mm粉ss秒SS毫秒')}

</body>
</html>