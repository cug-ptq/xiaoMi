

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
viewModel.getGridModel().on('afterValueChange', function (value) {
    // 在这里处理多个字段值改变的逻辑
    // 可以通过 value 参数获取改变后的值
    // 可以通过 this 获取当前的字段对象
    // 可以通过 this.get('字段编码') 获取当前字段的值
    // 可以通过 this.get('字段编码').getFromModel() 获取当前字段的模型对象
    this.get('zhiweinianxinjishu').getFromModel().on('afterValueChange', function (value) { }) // 监听当前字段的值改变事件
});

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
    // const exportUrl = `http://ehr.ctg.com.cn/sanxia-be/api/payrollProcessing/export`
    const exportUrl = `https://ehr.ctg.com.cn/sanxia-be/api/payrollProcessing/export`
    // 显示加载提示
    // cb.utils.showLoading('正在下载数据...');

    // 构建请求参数
    let params = {
        ids: ids // 传递的参数是一个对象，包含 ids 数组
    };
    console.log(cb.rest.ajax);
    // 发送 POST 请求
    // cb.rest.ajax('https://ehr.ctg.com.cn/sanxia-be/api/payrollProcessing/export', { params, method: 'post', domainKey: 'sanxia', responseType: 'blob', callback:(err, res) => {
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
