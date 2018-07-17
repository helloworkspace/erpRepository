	// 商品仓库模块 
	
	//弹出一个窗口，是那个修改仓库的总窗口
	function openChooseWarehouseDialog(){
		$("#warehouseDialog").dialog("open").dialog("setTitle","仓库");
	}
	
	//添加仓库信息
	function openWarehouseAddDialog(){
		$("#addWarehouseDialog").dialog("open").dialog("setTitle","添加仓库信息");
	}
	
	//删除选中的仓库
	function deleteWarehouse(){
		var selectedRows=$("#warehouseTable").datagrid("getSelections");
		if(selectedRows.length!=1){
			 $.messager.alert("系统提示","请选择要删除的数据！");
			 return;
		 }
		 var id=selectedRows[0].id;
		 $.messager.confirm("系统提示","您确定要删除这条数据吗？",function(r){
				if(r){
					$.post("/admin/warehouse/delete",{id:id},function(result){
						if(result.success){
							 $("#warehouseTable").datagrid("reload");
						}else{
							$.messager.alert("系统提示",result.errorInfo);
						}
					},"json");
				} 
	   });
	}
	
	//选择好仓库时，点确定
	function chooseWarehouse(){
		var selectedRows=$("#warehouseTable").datagrid("getSelections");
		if(selectedRows.length!=1){
			 $.messager.alert("系统提示","请选择仓库！");
			 return;
		}
		var name=selectedRows[0].name;
		$("#warehouse").combobox("reload");
		$("#warehouse").combobox("setValue",name);
		$("#warehouseDialog").dialog("close");
	}
	
	//关闭整个选择窗口
	function closeWarehouseDialog(){
		$("#warehouseDialog").dialog("close");
	}
	
	
	//保存添加的仓库
	function saveWarehouse(){
		$("#addWarehouseText").form("submit",{
			url:"/admin/warehouse/save",
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","保存成功！");
					closeWarehouseAddDialog();
					$("#warehouseTable").datagrid("reload");
				}else{
					$.messager.alert("系统提示",result.errorInfo);
				}
			}
		 });
	}
	
	//关闭添加新仓库的窗口，并且设置窗口仓库信息为空
	function closeWarehouseAddDialog(){
		$("#addWarehouseDialog").dialog("close");
		$("#warehouseName").val("");
	}
	
	/*
	*$(function(){}); 是$(document).ready()的简写 
	*/
	$(function (){
		//window.alert("lalla");
		 //双击仓库展示表，就是双击名称，关闭仓库对话窗口，自动填充
		 $("#warehouseTable").datagrid({  
		        //双击事件  //双击选中仓库
		        onDblClickRow: function (index, row) {  
		        	var selectedRows=$("#warehouseTable").datagrid("getSelections");
		    		var name=selectedRows[0].name;
		    		$("#warehouse").combobox("reload");
		    		$("#warehouse").combobox("setValue",name);
		    		$("#warehouseDialog").dialog("close");
		        }  
		    });  		 
	});