<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Title</title>

    <base href="http://${pageContext.request.serverName }:${pageContext.request.serverPort }${pageContext.request.contextPath }/"/>

    <script type="javascript" src="jquery/jquery-2.1.1.min.js"/>
    <script type="javascript" src="layer/layer.js"/>
    <script type="text/javascript">

        $(function () {

            $("#btn1").click(function () {

                //准备json对象
                var student = {
                    "stuId":5,
                    "stuName":"tom",
                    "address":{
                        "province":"广东",
                        "city":"深圳",
                        "street":"后瑞"
                    },
                    "subjectList":[
                        {
                            "subjectName":"JavaSE",
                            "subjectScore":100
                        },{
                            "subjectName":"SSM",
                            "subjectScore":99
                        }
                    ],
                    "map":{
                        "k1":"v1",
                        "k2":"v2"
                    }
                };

                //第二部，将json对象转换为json字符串
                var requestBody = JSON.stringify(student);

                //发送Ajax请求
                $.ajax({

                    "url":"send/compose/object.json",
                    "type":"post",
                    "data":requestBody,
                    "contentType":"application/json;charset=UTF-8",
                    "dataType":"json",
                    "success":function (lick){
                          alert(lick);
                    },
                    "error":function (response){
                        alert(response);
                    }
                });
            });
        });
    </script>
</head>


<body>
<button id="btn1">Send Compose Object</button>
</body>
</html>
