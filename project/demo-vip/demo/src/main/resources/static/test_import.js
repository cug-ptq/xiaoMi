viewModel.on('beforeSearch', function (args) {
    debugger
    let filterData = viewModel.getCache('FilterViewModel');

    // 如果 filterData 为 null 或 undefined，则直接将 niandu 和 zuzhibumen 设置为 null
    let niandu = null;
    let zuzhibumen = null;

    if (filterData) {
        // 使用可选链操作符（?.）来安全地访问属性
        niandu = filterData.getAllData()?.niandu?.value1 || null;
        zuzhibumen = filterData.getAllData()?.zuzhibumen?.value1 || null;
    }

    let ytenat_id = cb.context.getTenantId()
    let params = {
        niandu: niandu, // 直接使用数组
        zuzhibumen: zuzhibumen,
        ytenat_id: ytenat_id
    };

    // 创建动态代理
    var proxy1 = cb.rest.DynamicProxy.create({
        ensure: {
            url: '/api/payrollProcessing/status',
            method: 'POST',
            options: {
                "domainKey": 'sanxia',
            },
            headers: {
                'Content-Type': 'application/json'
            }
        }
    });

    // 发送请求
    proxy1.ensure(params, function (err, data) {
        if (err) {
            cb.utils.alert('获取报表状态失败：' + err.message, 'error');
        } else {
            console.log('获取报表状态成功', 'success');
        }
    });


});

viewModel.on('modeChange', function (args) {
    // 状态发生改变进行刷新
    if (args != 'edit') {
        viewModel.execute('refresh')
    } else {

    }

});

// 参照赋值
let gridModel = viewModel.getGridModel(); // 获取表格模型
gridModel.on('afterSetDataSource', (params) => {
    console.log("表格数据加载后执行----------------------");
    const rows = gridModel.getRows(); // 获取表格所有数据
    // 遍历表格数据，并获取行号（索引）
    rows.forEach((row, index) => {
        gridModel.setCellValue(index, 'zuzhibumen_refname', row.bumenmingchen);
        gridModel.setCellValue(index, 'zuzhibumen', row.zuzhibumenbianma);
    });
});


gridModel.on("afterCellValueChange", function (data) {//  监听单元格值改变
    const rowIndex = data.rowIndex;

    // 每次都重新获取最新行数据
    let row = viewModel.getGridModel().getRow(rowIndex);

    // -----------------------------
    // Step 1: 计算 jibennianxinxiaoji（基本年薪小计）
    // 小计=职位年薪基数*起始档职位年薪系数+职级年薪
    // -----------------------------
    if (row.zhiweinianxinjishu != null && row.qishidangzhiweinianxinxishu != null && row.zhijinianxin != null
    ) {
        const value =
            Number(row.zhiweinianxinjishu) *
            Number(row.qishidangzhiweinianxinxishu) +
            Number(row.zhijinianxin);
        gridModel.setCellValue(rowIndex, 'jibennianxinxiaoji', value);
    } else {
        gridModel.setCellValue(rowIndex, 'jibennianxinxiaoji', 0);
    }

    // -----------------------------
    // Step 2: 计算 jixiaonianxinbiaozhuna（绩效年薪标准 A）
    // 使用的是 gerenkaohedengjixishua（个人考核等级系数 A）
    // 绩效年薪标准（A）=绩效年薪基数*(单位考核系数*0.6+个人考核等级系数(A)*0.4)*发展系数*1
    // -----------------------------
    if (row.jixiaonianxinjishu != null && row.danweikaohexishu != null && row.gerenkaohedengjixishua != null && row.fazhanxishu != null) {
        const base = Number(row.jixiaonianxinjishu);
        const unitRate = Number(row.danweikaohexishu) * 0.6;
        const personalRate = Number(row.gerenkaohedengjixishua) * 0.4; // 使用 A 版本系数
        const devFactor = Number(row.fazhanxishu);

        const performanceValue = base * (unitRate + personalRate) * devFactor * 1;

        gridModel.setCellValue(rowIndex, 'jixiaonianxinbiaozhuna', performanceValue);
    } else {
        gridModel.setCellValue(rowIndex, 'jixiaonianxinbiaozhuna', 0);
    }

    // -----------------------------
    // Step 3: 计算 jixiaonianxinjine（绩效年薪金额）
    // 使用的是 gerenkaohedengjixu（个人考核等级系数）
    // 绩效年薪金额=绩效年薪基数*(单位考核系数*0.6+个人考核等级系数*0.4)*发展系数*1
    // -----------------------------
    if (
        row.jixiaonianxinjishu != null && row.danweikaohexishu != null && row.gerenkaohedengjixishu != null && row.fazhanxishu != null
    ) {
        const base = Number(row.jixiaonianxinjishu);
        const unitRate = Number(row.danweikaohexishu) * 0.6;
        const personalRate = Number(row.gerenkaohedengjixishu) * 0.4;
        const devFactor = Number(row.fazhanxishu);

        const performanceValue = base * (unitRate + personalRate) * devFactor * 1;

        gridModel.setCellValue(rowIndex, 'jixiaonianxinjine', performanceValue);
    } else {
        gridModel.setCellValue(rowIndex, 'jixiaonianxinjine', 0);
    }

    // 再次获取更新后的行数据，确保后续计算拿到最新值
    row = viewModel.getGridModel().getRow(rowIndex);

    // -----------------------------
    // Step 4: 计算 nianduxinchoubiaozhun（年度薪酬标准）
    // 年度薪酬标准=小计+绩效年薪标准（A）
    // -----------------------------
    if (row.jibennianxinxiaoji != 0 && row.jixiaonianxinbiaozhuna != 0) {
        const total =
            Number(row.jibennianxinxiaoji) +
            Number(row.jixiaonianxinbiaozhuna);
        gridModel.setCellValue(rowIndex, 'nianduxinchoubiaozhun', total);
    } else {
        gridModel.setCellValue(rowIndex, 'nianduxinchoubiaozhun', 0);
    }

    // -----------------------------
    // Step 5: 计算 nianduxinchoujine（年度薪酬金额）
    // 年度薪酬金额=小计+绩效年薪金额
    // -----------------------------
    if (row.jibennianxinxiaoji != 0 && row.jixiaonianxinjine != 0) {
        const total =
            Number(row.jibennianxinxiaoji) +
            Number(row.jixiaonianxinjine);
        gridModel.setCellValue(rowIndex, 'nianduxinchoujine', total);
    } else {
        gridModel.setCellValue(rowIndex, 'nianduxinchoujine', 0);
    }
});

// 监听表格模型的值改变事件

viewModel.get('btnAddRow') && viewModel.get('btnAddRow').on('click', function (data) {
    // 新增
    let gridModel = viewModel.getGridModel();
    // 获取当前表格的所有行数据
    let rows = gridModel.__data.rows;
    let rowCount = rows.length;

    // 遍历所有行，设置行状态
    for (let i = 0; i < rowCount; i++) {
        if (i != rowCount - 1) {
            gridModel.setRowState(i, 'readOnly', true);

        }
    }
    viewModel.get('button13nd').setVisible(false);
    viewModel.get('button20kb').setVisible(false);
    viewModel.get('btnExportDrop').setVisible(false);
    viewModel.get('btnExport').setVisible(false);
    viewModel.get('button32nd').setVisible(false);
    viewModel.get('btnAddRow').setVisible(false);

});


viewModel.get('button29tg') && viewModel.get('button29tg').on('click', function (data){
    viewModel.get('button13nd').setVisible(true);
    viewModel.get('button20kb').setVisible(true);
    viewModel.get('btnExportDrop').setVisible(true);
    viewModel.get('btnExport').setVisible(true);
    viewModel.get('button32nd').setVisible(true);
    viewModel.get('btnAddRow').setVisible(true);
});

viewModel.get('button30xc') && viewModel.get('button30xc').on('click', function (data){
    viewModel.get('button13nd').setVisible(true);
    viewModel.get('button20kb').setVisible(true);
    viewModel.get('btnExportDrop').setVisible(true);
    viewModel.get('btnExport').setVisible(true);
    viewModel.get('button32nd').setVisible(true);
    viewModel.get('btnAddRow').setVisible(true);
});


viewModel.get('button9xb') && viewModel.get('button9xb').on('click', function (data) {
    // 编辑
    debugger;
    let gridModel = viewModel.getGridModel();
    let rows = gridModel.__data.rows; // 获取所有行数据
    let rowCount = rows.length; // 获取总行数

    // 遍历所有行，设置行状态
    for (let i = 0; i < rowCount; i++) {
        if (i != data.index) {
            gridModel.setRowState(i, 'readOnly', true);
        }
    }
});




viewModel.get('button13nd') && viewModel.get('button13nd').on('click', function () {//标准下发
    // 获取选中的行数据
    let dataRows = viewModel.getGridModel().getSelectedRows();
    let ids = dataRows.map(item => item.id);

    // 下发函数
    function propagate() {
        // 构建请求参数对象
        let params = {
            ids: ids // 直接使用数组
        };

        // 创建动态代理
        var proxy1 = cb.rest.DynamicProxy.create({
            ensure: {
                url: '/api/standard/propagate',
                method: 'POST',
                options: {
                    "domainKey": 'sanxia',
                },
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        });

        // 发送请求
        proxy1.ensure(params, function (err, data) {
            if (err) {
                cb.utils.alert('下发失败：' + err.message, 'error');
            } else {
                cb.utils.alert('下发成功！');
            }
        });
    }

    // 判断是否有选中行
    if (ids.length === 0) {
        cb.utils.confirm(
            '确定全部下发吗?',
            () => { // 确定
                propagate();
                viewModel.execute('refresh')
            },
            () => { // 取消
                console.log('用户取消了操作');
            }
        );
    } else {
        propagate();
        viewModel.execute('refresh')
    }
});



let gridModel1 = viewModel.getGridModel();
gridModel1.on('beforeSetActionsState', (actionState) => {//获取状态隐藏按钮
    const rows = gridModel1.getRows(false);
    const actions = gridModel1.getCache('actions');
    debugger
    rows.forEach((data, i) => {
        actions.forEach((action) => {
            if (action.cItemName == 'button9xb') {//要隐藏的字段
                if (data.shifuxiafa == 1) {//要获取状态的字段
                    actionState[i][action.cItemName] = { visible: false }
                }
            }
        });
    });
});


viewModel.get('button20kb') && viewModel.get('button20kb').on('click', function (data) {
    debugger;

    // 获取当前选中的行数据
    let dataRows = viewModel.getGridModel().getSelectedRows();

    // 检查是否选择了数据
    if (!dataRows || dataRows.length === 0) {
        cb.utils.alert('请选择数据后再进行操作', 'warning');
        return;
    }

    // 遍历选中的数据，检查是否有未下发的数据
    for (let i = 0; i < dataRows.length; i++) {
        let row = dataRows[i];

        // 假设每条数据有一个字段 `shifuxiafa` 表示下发状态
        if (row.shifuxiafa == '2') {
            // 提示用户存在未下发的数据，并建议重新选择
            cb.utils.alert('选中的数据中存在未下发的状态，请查询后重新选择', 'warning');
            return; // 终止操作
        }
    }

    // 如果所有校验都通过，则执行导出操作
    let ids = dataRows.map(item => item.id);
    // 获取主域名地址
    const prefix = cb.utils.getServiceUrl(); // 固定前缀
    // const exportUrl = `${prefix}/uniformdata/sanxia-be/api/payrollProcessing/export`
    // const exportUrl = `${prefix}/sanxia-be/api/payrollProcessing/export`
    // const exportUrl = `${prefix}sanxia-be/api/payrollProcessing/export`
    // const exportUrl = `http://uptest.ctg.com.cn/sanxia-be/api/payrollProcessing/export`
    const exportUrl = 'https://uptest.ctg.com.cn/sanxia-be/api/payrollProcessing/export';
    // 显示加载提示
    // cb.utils.showLoading('正在下载数据...');

    // 构建请求参数
    let params = {
        ids: ids // 传递的参数是一个对象，包含 ids 数组
    };
    console.log(cb.rest.ajax);
    // 发送 POST 请求
    // cb.rest.ajax('https://uptest.ctg.com.cn/sanxia-be/api/payrollProcessing/export', { params, method: 'post', domainKey: 'sanxia', responseType: 'blob', callback:(err, res) => {
    cb.rest.ajax(exportUrl, {
        params, method: 'post', domainKey: 'sanxia', responseType: 'blob', callback: (err, res) => {
            if (err) {
                // 错误处理
                cb.utils.alert('下载失败：' + err.message, 'error');
                console.error('导出失败详情:', err);
            } else {
                try {
                    // 提取文件名（如果后端返回了 Content-Disposition 头）
                    let fileName = 'exported_files.zip'; // 默认文件名
                    const contentDisposition = res.headers['content-disposition']; // 修复变量名为 res
                    if (contentDisposition && contentDisposition.includes('filename=')) {
                        const fileNameEncode = contentDisposition.split('filename=')[1].split(';')[0];
                        fileName = decodeURI(fileNameEncode, 'utf-8').replaceAll('"', '');
                    }

                    // 使用 Blob 对象处理文件流
                    const blob = new Blob([res.data], { type: 'application/octet-stream' }); // 修复变量名为 res

                    // 创建临时下载链接
                    const downloadUrl = window.URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.href = downloadUrl;
                    a.download = fileName; // 设置下载文件名
                    a.style.display = 'none'; // 隐藏 <a> 标签
                    document.body.appendChild(a);
                    a.click();

                    // 清理临时资源
                    window.URL.revokeObjectURL(downloadUrl);
                    document.body.removeChild(a);

                    cb.utils.alert('下载成功', 'success');
                } catch (error) {
                    // 捕获异常并提示用户
                    cb.utils.alert('下载失败：文件处理出错', 'error');
                    console.error('文件处理失败:', error);
                }
            }
        }
    });
})





// 在视图模型初始化时执行自定义逻辑
viewModel.on('customInit', function (data) {
    debugger
    // 标记脚本是否已加载完成，用于防止重复加载
    let scriptLoaded = false;

    /**
     * 动态加载外部 JS 脚本的方法
     * @param {string} src - 要加载的脚本地址
     * @param {function} callback - 加载完成后执行的回调函数
     */
    const loadScript = (src, callback) => {
        const script = document.createElement('script'); // 创建 <script> 元素
        script.src = src; // 设置脚本路径
        script.onload = () => {
            // 当脚本加载完成后执行回调
            scriptLoaded = true;
            callback && callback();
        };
        document.body.appendChild(script); // 将脚本插入到页面中
    };

    loadScript('https://cdn.jsdelivr.net/npm/xlsx@0.18.5/dist/xlsx.full.min.js', () => {
        if (viewModel.get('button32nd')) {
            /**
             * 给按钮 button32nd 添加点击事件监听器
             */
            viewModel.get('button32nd').on('click', function () {
                /**
                 * 创建一个隐藏的文件输入框，用于选择Excel文件
                 */
                const fileInput = document.createElement('input');
                fileInput.type = 'file'; // 文件类型
                fileInput.accept = '.xlsx,.xls'; // 限制只能选择Excel格式文件
                fileInput.style.display = 'none'; // 隐藏该元素

                /**
                 * 监听文件选择变化事件
                 */
                fileInput.addEventListener('change', function (e) {
                    const file = e.target.files[0]; // 获取用户选择的第一个文件
                    if (!file) return; // 如果没有选择文件，直接返回

                    /**
                     * 使用 FileReader 读取文件内容
                     */
                    const reader = new FileReader();

                    /**
                     * 当文件读取完成后的处理
                     */
                    reader.onload = function (event) {
                        const data = event.target.result; // 获取读取结果
                        /**
                         * 使用 XLSX 库解析 Excel 文件数据
                         * type: 'array' 表示以ArrayBuffer格式读取
                         */
                        const workbook = XLSX.read(data, { type: 'array' });

                        /**
                         * 获取第一个工作表名称，并获取其工作表对象
                         */
                        const sheetName = workbook.SheetNames[0];
                        const worksheet = workbook.Sheets[sheetName];

                        /**
                         * 将工作表转换为 JSON 数组格式
                         */
                        const jsonData = XLSX.utils.sheet_to_json(worksheet);

                        /**
                         * 将解析后的 JSON 数据发送到后端
                         */
                        sendToBackend(jsonData);
                    };

                    /**
                     * 以 ArrayBuffer 形式读取文件内容
                     */
                    reader.readAsArrayBuffer(file);
                });

                /**
                 * 插入文件输入框到页面并触发点击
                 */
                document.body.appendChild(fileInput);
                fileInput.click(); // 模拟点击打开文件选择对话框
                document.body.removeChild(fileInput); // 用完后移除
            });
        }
    });

    /**
     * 发送数据到后端的方法
     * @param {Array} excelData - 解析后的 Excel 数据（JSON 数组）
     */

    function sendToBackend(excelData) {
        if (!excelData || excelData.length <= 1) {
            console.warn("没有可发送的有效数据");
            alert("文件中没有有效的数据内容");
            return;
        }

        // 跳过第 0 行（标题行），只保留从第 1 行开始的真实数据
        const validData = excelData.slice(1);

        /**
         * 定义后端接口地址
         */
        const url = "/api/payrollProcessing/import";

        /**
         * 创建 cb.rest.DynamicProxy 请求代理对象
         */
        const proxy = cb.rest.DynamicProxy.create({
            ensure: {
                url: url,
                method: 'POST',
                options: {
                    domainKey: 'sanxia' // 可能是环境标识符或其他用途
                }
            }
        });

        /**
         * 构造请求参数
         */
        const params = {
            data: validData, // 发送解析后的JSON数据
            create_name:cb.rest.AppContext.user,
            ytenat_id:cb.context.getTenantId()
            // loggedInUser: cb.rest.AppContext.user, // 获取当前登录用户的ID
            // org: viewModel.getCache('FilterViewModel')?.get('org')?.getFromModel()?.__data?.value || '' // 获取组织信息
        };

        /**
         * 发送请求到后端 result: code msg
         */
        proxy.ensure(params, function (result, res) {
            const codePrefix = res.code / 100;
            if (codePrefix!=2) {
                console.error("请求失败", res.msg);
                alert(res.msg + "数据导入失败，请查看控制台日志");
            } else {
                console.log("后端返回结果", res.msg);
                alert("数据导入成功！");
                viewModel.execute('refresh'); // 刷新视图模型
            }
        });
    }
});
viewModel.get('btnExport') && viewModel.get('btnExport').on('click', function(data) {
//Excel导出--onClick
    let dataRows = viewModel.getGridModel().getSelectedRows();
    if (dataRows.length === 0) {
        console.log("未选中");
        alert("未选中数据");
    }
    else{
        const ids = dataRows.map(item => item.id).join(',');
        const prefix = cb.utils.getServiceUrl();
        url = "https://uptest.ctg.com.cn/sanxia-be/api/payrollProcessing/exportExcel";

        const eleForm = document.createElement('form');
        eleForm.method = 'GET';
        eleForm.action = url;
        eleForm.target = '_blank';
        eleForm.style.display = 'none';

        // 添加 domainKey 参数
        const eleInput1 = document.createElement('input');
        eleInput1.name = 'domainKey';
        eleInput1.value = 'sanxia';
        eleForm.appendChild(eleInput1);

        // 添加 ids 参数
        const eleInput2 = document.createElement('input');
        eleInput2.name = 'ids'; // 后端接收参数名是 ids（你可根据实际改）
        eleInput2.value = ids;
        eleForm.appendChild(eleInput2);

        console.log(eleForm);
        // 添加表单到body
        document.body.appendChild(eleForm);
        // 提交表单
        eleForm.submit();
        // 移除表单
        document.body.removeChild(eleForm);
    }


    // cb.rest.ajax(url, { params, method: 'post', domainKey: 'sanxia', responseType: 'blob', callback:(err, res) => {
    //     if (err) {
    //             // 错误处理
    //             cb.utils.alert('下载失败：' + err.message, 'error');
    //             console.error('导出失败详情:', err);
    //         } else {
    //             try {
    //                 // 提取文件名（如果后端返回了 Content-Disposition 头）
    //                 let fileName = 'exported_file.xlsx'; // 默认 Excel 文件名
    //                 const contentDisposition = res.headers['content-disposition']; // 获取响应头中的 Content-Disposition

    //                 if (contentDisposition && contentDisposition.includes('filename=')) {
    //                     const fileNameEncode = contentDisposition.split('filename=')[1].split(';')[0];
    //                     fileName = decodeURI(fileNameEncode, 'utf-8').replaceAll('"', '');
    //                 }

    //                 // 创建 Blob 对象处理 Excel 文件流
    //                 const blob = new Blob([res.data], {
    //                     type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    //                 });

    //                 // 创建临时下载链接
    //                 const downloadUrl = window.URL.createObjectURL(blob);
    //                 const a = document.createElement('a');
    //                 a.href = downloadUrl;
    //                 a.download = fileName; // 设置下载文件名
    //                 a.style.display = 'none';
    //                 document.body.appendChild(a);
    //                 a.click();

    //                 // 清理临时资源
    //                 window.URL.revokeObjectURL(downloadUrl);
    //                 document.body.removeChild(a);


    //                 cb.utils.alert('下载成功', 'success');
    //             } catch (error) {
    //                 // 捕获异常并提示用户
    //                 cb.utils.alert('下载失败：文件处理出错', 'error');
    //                 console.error('文件处理失败:', error);
    //             }
    //         }
    //     }
    // });
});