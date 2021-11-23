
function fillAuthTree(){

    // 1.发送 Ajax 请求查询 Auth 数据
    var ajaxReturn = $.ajax({
        "url":"assgin/get/all/auth.json",
        "type":"post",
        "dataType":"json",
        "async":false
    });
    if(ajaxReturn.status != 200) {
        layer.msg(" 请 求 处 理 出 错 ！ 响 应 状 态 码 是 ： "+ajaxReturn.status+" 说 明 是 ： "+ajaxReturn.statusText);
        return ;
}


    // 2.从响应结果中获取 Auth 的 JSON 数据
    // 从服务器端查询到的 list 不需要组装成树形结构，这里我们交给 zTree 去组装
    var authList = ajaxReturn.responseJSON.data;

    // 3.准备对 zTree 进行设置的 JSON 对象
    var setting = {
        "data": {
            "simpleData": {
                // 开启简单 JSON 功能
                "enable": true,
                // 使用 categoryId 属性关联父节点，不用默认的 pId 了
                "pIdKey": "categoryId"
            },
            "key": {
                // 使用 title 属性显示节点名称，不用默认的 name 作为属性名了
                "name": "title"
            }
            },
        "check": {
            "enable": true
        }
    };

    // 4.生成树形结构
    // <ul id="authTreeDemo" class="ztree"></ul>
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);
    // 获取 zTreeObj 对象
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
    // 调用 zTreeObj 对象的方法，把节点展开
    zTreeObj.expandAll(true);

    // 5.查询已分配的 Auth 的 id 组成的数组
    ajaxReturn = $.ajax({
        "url":"assign/get/assigned/auth/id/by/role/id.json",
        "type":"post",
        "data":{
            "roleId":window.roleId
        },
        "dataType":"json",
        "async":false
    });

    if(ajaxReturn.status != 200) {
        layer.msg(" 请 求 处 理 出 错 ！ 响 应 状 态 码 是 ： "+ajaxReturn.status+" 说 明 是 ： "+ajaxReturn.statusText);
        return ; 
    }
    // 从响应结果中获取 authIdArray
    var authIdArray = ajaxReturn.responseJSON.data;
    // 6.根据 authIdArray 把树形结构中对应的节点勾选上
    // ①遍历 authIdArray
    for(var i = 0; i < authIdArray.length; i++) {
        var authId = authIdArray[i];
        // ②根据 id 查询树形结构中对应的节点
        var treeNode = zTreeObj.getNodeByParam("id", authId);
        // ③将 treeNode 设置为被勾选
        // checked 设置为 true 表示节点勾选
        var checked = true;
        // checkTypeFlag 设置为 false，表示不“联动”，不联动是为了避免把不该勾选的勾 选上
        var checkTypeFlag = false;
        // 执行
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
    }
}





//声明专门的函数显示确认模态框
function showConfirmModal(roleArray) {
// 打开模态框
    $("#confirmModal").modal("show");
// 清除旧的数据
    $("#roleNameDiv").empty();
// 在全局变量范围创建数组用来存放角色 id
    window.roleIdArray = [];
// 遍历 roleArray 数组
    for (var i = 0; i < roleArray.length; i++) {
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName + "<br/>");
        var roleId = role.roleId;
// 调用数组对象的 push()方法存入新元素
        window.roleIdArray.push(roleId);
    }
}

/**
 * 外部函数一：generatePage()
 * 执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
 */
function generatePage() {
    // 1.获取分页数据
    var pageInfo = getPageInfoRemote();
    // 2.填充表格
    fillTableBody(pageInfo);
}



/**
 * 外部函数二：getPageInfoRemote()
 * @returns {pageInfo}
 * 远程访问服务器端程序获取pageInfo数据
 */
function getPageInfoRemote() {

// 调用$.ajax()函数发送请求并接受$.ajax()函数的返回值
    var ajaxResult = $.ajax({
        "url": "role/get/page/info.json",
        "type":"post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword },
            "async":false,
            "dataType":"json" });

         console.log(ajaxResult);
         // 判断当前响应状态码是否为 200
    var statusCode = ajaxResult.status;
    // 如果当前响应状态码不是 200，说明发生了错误或其他意外情况，显示提示消息，让 当前函数停止执行
    if(statusCode != 200) {
        layer.msg("失败！响应状态码="+statusCode+" 说明信息="+ajaxResult.statusText); return null; }
    // 如果响应状态码是 200，说明请求处理成功，获取 pageInfo
   var resultEntity = ajaxResult.responseJSON;
    // 从 resultEntity 中获取 result 属性
    var result = resultEntity.result;
    // 判断 result 是否成功
    if(result == "FAILED") {
        layer.msg(resultEntity.message); return null;
    }
    // 确认 result 为成功后获取 pageInfo
    var pageInfo = resultEntity.data;

    // 返回 pageInfo
    return pageInfo;
}


/**
 * 外部函数三：fillTableBody(pageInfo)
 * @param pageInfo
 * 填充表格
 */
function fillTableBody(pageInfo){

    // 清除 tbody 中的旧的内容
    $("#rolePageBody").empty();
    $("#Pagination").empty();

    // 这里清空是为了让没有搜索结果时不显示页码导航条
    $("#Pagination").empty();

    // 判断 pageInfo 对象是否有效
    if(pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0) {
        $("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉！没有查询到您搜 索的数据！</td></tr>");

        return;
    }

    // 使用 pageInfo 的 list 属性填充 tbody
    for(var i = 0; i < pageInfo.list.length; i++) {
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;

        var numberTd = "<td>" + (i + 1) + "</td>";
        var checkboxTd = "<td><input id='"+roleId+"' class='itemBox' type='checkbox'></td>";
        var roleNameTd = "<td>" + roleName + "</td>";

        var checkBtn = "<button id='"+roleId+"' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";

        var pencilBtn = "<button id='"+roleId+"' type='button' class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button id='"+roleId+"' type='button' class='btn btn-danger btn-xs removeBtn'><i class=' glyphicon glyphicon-remove'></i></button>";
        var buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";
        var tr = "<tr>" + numberTd + checkboxTd + roleNameTd + buttonTd + "</tr>";
        $("#rolePageBody").append(tr);
    }
        // 生成分页导航条
        generateNavigator(pageInfo);
}

/**
 * 外部函数四：generateNavigator(pageInfo)
 * @param pageInfo
 * 生成分页页码导航条
 */
function generateNavigator(pageInfo){

// 获取总记录数
var totalRecord = pageInfo.total;
// 声明相关属性
var properties = {
    "num_edge_entries": 3,
    "num_display_entries": 5,
    "callback": paginationCallBack,
    "items_per_page": pageInfo.pageSize,
    "current_page": pageInfo.pageNum - 1,
    "prev_text": "上一页",
    "next_text": "下一页"
}
    // 调用 pagination()函数
    $("#Pagination").pagination(totalRecord, properties);
}


/**
 * 外部函数五：paginationCallBack(pageIndex, jQuery)
 * @param pageIndex
 * @param jQuery
 * 翻页时的回调函数
 */
function paginationCallBack(pageIndex, jQuery){

    // 修改 window 对象的 pageNum 属性
    window.pageNum = pageIndex + 1;
    // 调用分页函数
    generatePage();
    // 取消页码超链接的默认行为
    return false;
}