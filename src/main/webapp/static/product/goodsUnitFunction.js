	// 商品单位模块 
	
	//弹出一个窗口，是那个修改单位的总窗口
	function openChooseGoodsUnitDialog(){
		$("#unitDialog").dialog("open").dialog("setTitle","单位");
	}
	
	//添加单位信息
	function openGoodsUnitAddDialog(){
		$("#addUnitDialog").dialog("open").dialog("setTitle","添加单位信息");
	}
	
	//删除选中的单位
	function deleteGoodsUnit(){
		var selectedRows=$("#unitTable").datagrid("getSelections");
		if(selectedRows.length!=1){
			 $.messager.alert("系统提示","请选择要删除的数据！");
			 return;
		 }
		 var id=selectedRows[0].id;
		 $.messager.confirm("系统提示","您确定要删除这条数据吗？",function(r){
				if(r){
					$.post("/admin/goodsUnit/delete",{id:id},function(result){
						if(result.success){
							 $("#unitTable").datagrid("reload");
						}else{
							$.messager.alert("系统提示",result.errorInfo);
						}
					},"json");
				} 
	   });
	}
	
	//选择好单位时，点确定
	function chooseGoodsUnit(){
		var selectedRows=$("#unitTable").datagrid("getSelections");
		if(selectedRows.length!=1){
			 $.messager.alert("系统提示","请选择单位！");
			 return;
		}
		var name=selectedRows[0].name;
		$("#unit").combobox("reload");
		$("#unit").combobox("setValue",name);
		$("#unitDialog").dialog("close");
	}
	
	//关闭整个选择窗口
	function closeGoodsUnitDialog(){
		$("#unitDialog").dialog("close");
	}
	
	
	//保存添加的单位
	function saveGoodsUnit(){
		$("#addUnitText").form("submit",{
			url:"/admin/goodsUnit/save",
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","保存成功！");
					closeGoodsUnitAddDialog();
					$("#unitTable").datagrid("reload");
				}else{
					$.messager.alert("系统提示",result.errorInfo);
				}
			}
		 });
	}
	
	//关闭添加新单位的窗口，并且设置窗口单位信息为空
	function closeGoodsUnitAddDialog(){
		$("#addUnitDialog").dialog("close");
		$("#goodsUnitName").val("");
	}
	
	/*
	*$(function(){}); 是$(document).ready()的简写 
	*/
	$(function (){
		//window.alert("lalla");
		 //双击单位展示表，就是双击名称，关闭单位对话窗口，自动填充
		 $("#unitTable").datagrid({  
		        //双击事件  //双击选中单位
		        onDblClickRow: function (index, row) {  
		        	var selectedRows=$("#unitTable").datagrid("getSelections");
		    		var name=selectedRows[0].name;
		    		$("#unit").combobox("reload");
		    		$("#unit").combobox("setValue",name);
		    		$("#unitDialog").dialog("close");
		        }  
		    });  		 
	});